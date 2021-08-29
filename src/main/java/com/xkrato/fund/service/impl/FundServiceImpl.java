package com.xkrato.fund.service.impl;

import com.xkrato.fund.domain.Fund;
import com.xkrato.fund.domain.FundExample;
import com.xkrato.fund.factory.FundCrawlerFactory;
import com.xkrato.fund.mapper.FundMapper;
import com.xkrato.fund.service.IFundService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Data
@Service
public class FundServiceImpl implements IFundService {

  @Value("${fund.homepage}")
  private String FUND_HOMEPAGE;

  @Value("${fund.name1}")
  private String FUND_NAME1;

  @Value("${fund.name2}")
  private String FUND_NAME2;

  @Value("${fund.cache-dir}")
  private String FUND_CACHE_DIR;

  @Resource private FundCrawlerFactory fundCrawlerFactory;

  @Resource private FundMapper fundMapper;

  @Override
  public void getFundInfo() throws Exception {
    CrawlConfig config = new CrawlConfig();
    // 爬虫状态存储文件夹，可以从这里边读取数据，以恢复之前的爬取状态
    config.setCrawlStorageFolder(FUND_CACHE_DIR);
    PageFetcher pageFetcher = new PageFetcher(config);
    CrawlController controller =
        new CrawlController(
            config, pageFetcher, new RobotstxtServer(new RobotstxtConfig(), pageFetcher));

    // 要爬取的地址
    controller.addSeed(FUND_HOMEPAGE + FUND_NAME1);
    controller.addSeed(FUND_HOMEPAGE + FUND_NAME2);

    // 1为爬虫数量，也就是线程数，一般不超过CPU线程数
    controller.start(fundCrawlerFactory, 1);
  }

  @Override
  public Fund parseFundInfo(String urlPath, Document document) {
    Fund fund = new Fund();

    List<String> urls = Arrays.asList(urlPath.split("/"));
    if (CollectionUtils.isEmpty(urls)) {
      return null;
    }
    fund.setId(urls.get(urls.size() - 1).replaceAll(".html", ""));

    Elements elements = document.select("#fundManagerTab tr:eq(1) .td02");
    if (CollectionUtils.isEmpty(elements)) {
      return null;
    }

    elements
        .get(0)
        .getElementsByTag("a")
        .forEach(
            element -> {
              fund.setManagerName(
                  StringUtils.isEmpty(fund.getManagerName())
                      ? element.text()
                      : (fund.getManagerName() + "-" + element.text()));

              List<String> strings = Arrays.asList(element.attr("href").split("/"));
              if (!CollectionUtils.isEmpty(strings)) {
                String managerId = strings.get(strings.size() - 1).replaceAll(".html", "");
                fund.setManagerId(
                    StringUtils.isEmpty(fund.getManagerId())
                        ? managerId
                        : (fund.getManagerId() + "-" + managerId));
              }
            });

    return fund;
  }

  @Override
  public void saveFundInfo(Fund latestFund) {
    List<Fund> funds = queryFundById(latestFund.getId());
    /* Ⅰ. If fund not exist */
    if (CollectionUtils.isEmpty(funds)) {
      // add new fund
      // sendStatus From null To 0 (null -> 0)
      latestFund.setSendStatus("0");
      addFund(latestFund);
      return;
    }

    /* Ⅱ. If fund exist */
    Fund lastFund = funds.get(0);
    // 1. If Managers have not changed, return
    if (Optional.ofNullable(lastFund.getManagerId())
        .orElse("")
        .equals(latestFund.getManagerId())) {
      return;
    }

    // 2. If Managers changed
    // update sendStatus
    // sendStatus From 0/4 To 1 (0/4 -> 1)
    latestFund.setSendStatus("1");
    updateFundByUuid(lastFund.getUuid(), latestFund);
  }

  @Override
  public List<Fund> queryFundById(String id) {
    FundExample example = new FundExample();
    FundExample.Criteria criteria = example.createCriteria();
    criteria.andIdEqualTo(id);

    return fundMapper.selectByExample(example);
  }

  @Override
  public int addFund(Fund latestFund) {
    return addFund(Arrays.asList(latestFund));
  }

  @Override
  @Transactional(
      noRollbackFor = {Exception.class},
      isolation = Isolation.READ_COMMITTED)
  public int addFund(List<Fund> funds) {
    BigInteger affectedRow = new BigInteger("0");
    int i = 0;
    funds.forEach(
        fund -> {
          affectedRow.add(new BigInteger("1"));
          fund.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
          fundMapper.insert(fund);
        });
    return affectedRow.intValue();
  }

  @Override
  public int updateFundByUuid(String uuid, Fund latestFund) {
    FundExample example = new FundExample();
    FundExample.Criteria criteria = example.createCriteria();
    criteria.andUuidEqualTo(uuid);

    latestFund.setUpdateTime(new Date());
    int affectRows = fundMapper.updateByExampleSelective(latestFund, example);
    if (affectRows == 0) {
      System.out.println("Zero row affected.");
    }
    return affectRows;
  }
}
