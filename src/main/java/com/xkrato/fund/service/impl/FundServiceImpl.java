package com.xkrato.fund.service.impl;

import com.xkrato.fund.domain.FundExample;
import com.xkrato.fund.domain.dto.Fund;
import com.xkrato.fund.domain.enumerate.SendStatusEnum;
import com.xkrato.fund.factory.FundCrawlerFactory;
import com.xkrato.fund.mapper.FundMapper;
import com.xkrato.fund.service.IFundService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Data
@Service
public class FundServiceImpl implements IFundService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FundServiceImpl.class);

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
  public void crawlerFundInfo() throws Exception {
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
    LOGGER.info("start parsing html info");
    Fund fund = new Fund();

    List<String> urls = Arrays.asList(urlPath.split("/"));
    if (CollectionUtils.isEmpty(urls)) {
      return null;
    }
    fund.setId(urls.get(urls.size() - 1).replaceAll(".html", ""));

    Elements elements = document.select("#fundManagerTab tr:eq(1) .td02");
    if (CollectionUtils.isEmpty(elements)) {
      LOGGER.warn("manager info is empty.");
      LOGGER.debug(document.toString());
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

    LOGGER.info("end parsing html info");
    return fund;
  }

  @Override
  public void saveFundInfo(Fund latestFund) {
    LOGGER.info(
        "start save fund info, fund id is: {}, fund name is: {}",
        latestFund.getId(),
        latestFund.getName());
    List<Fund> funds = queryFundsById(latestFund.getId());

    /* Ⅰ. If fund not exist */
    if (CollectionUtils.isEmpty(funds)) {
      // add new fund
      // sendStatus From null To 0 (null -> 0)
      latestFund.setSendStatus(SendStatusEnum.INITIAL.val());
      LOGGER.info("add new fund,  managerNames are: {}", latestFund.getManagerName());
      LOGGER.debug(latestFund.toString());
      LOGGER.info("Affected rows is {}", addFund(latestFund));
      return;
    }

    /* Ⅱ. If fund exist */
    Fund lastFund = funds.get(0);
    // 1. If Managers have not changed, return
    if (Optional.ofNullable(lastFund.getManagerId()).orElse("").equals(latestFund.getManagerId())) {
      return;
    }

    // 2. If Managers changed
    LOGGER.info(
        "fund managers have changed, the last managerNames are: {}, latest managerNames are: {}",
        lastFund.getManagerName(),
        latestFund.getManagerName());
    // sendStatus From 0/4 To 1 (0/4 -> 1)
    LOGGER.info("fund.send_status from {} to {}", lastFund.getSendStatus(), 1);
    latestFund.setSendStatus(SendStatusEnum.WAIT.val());
    // update sendStatus
    LOGGER.info("Affected rows is {}", updateFundByUuid(lastFund.getUuid(), latestFund));
  }

  @Override
  public List<Fund> queryFundsById(String id) {
    FundExample example = new FundExample();
    FundExample.Criteria criteria = example.createCriteria();
    criteria.andIdEqualTo(id);

    return fundMapper.selectByExample(example);
  }

  /**
   * TODO need limit 1000 once?
   *
   * @param sendStatus
   * @return
   */
  @Override
  public List<Fund> queryFundsBySendStatus(String sendStatus) {
    FundExample example = new FundExample();
    FundExample.Criteria criteria = example.createCriteria();
    criteria.andSendStatusEqualTo(sendStatus);

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
    AtomicInteger affectedRows = new AtomicInteger();
    funds.forEach(
        fund -> {
          fund.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
          affectedRows.getAndAdd(fundMapper.insert(fund));
        });
    return affectedRows.intValue();
  }

  @Override
  public int updateSendStatusByUuid(String uuid, String sendStatus) {
    Fund fund = new Fund();
    fund.setUuid(uuid);
    return updateSendStatusByUuid(fund, sendStatus);
  }

  @Override
  public int updateSendStatusByUuid(Fund fund, String sendStatus) {
    return updateSendStatusByUuid(Arrays.asList(fund), sendStatus);
  }

  @Override
  public int updateSendStatusByUuid(List<Fund> funds, String sendStatus) {
    if (CollectionUtils.isEmpty(funds)) {
      return 0;
    }

    Fund record = new Fund();
    record.setSendStatus(sendStatus);
    return updateFundByUuid(funds.stream().map(Fund::getUuid).collect(Collectors.toList()), record);
  }

  @Override
  public int updateFundByUuid(String uuid, Fund latestFund) {
    return updateFundByUuid(Arrays.asList(uuid), latestFund);
  }

  @Override
  public int updateFundByUuid(List<String> uuid, Fund latestFund) {
    FundExample example = new FundExample();
    FundExample.Criteria criteria = example.createCriteria();
    criteria.andUuidIn(uuid);

    latestFund.setUpdateTime(new Date());
    int affectRows = fundMapper.updateByExampleSelective(latestFund, example);
    if (affectRows == 0) {
      LOGGER.warn("updating affected rows is zero.");
    }
    return affectRows;
  }

  @Override
  public int fixTimeoutRecord(Fund record, long timeout) {
    FundExample example = new FundExample();
    FundExample.Criteria criteria = example.createCriteria();
    criteria.andUpdateTimeLessThan(new Date(new Date().getTime() - timeout));
    criteria.andSendStatusEqualTo(SendStatusEnum.PROCESSING.val());

    record.setUpdateTime(new Date());
    int affectRows = fundMapper.updateByExampleSelective(record, example);
    if (affectRows == 0) {
      LOGGER.warn("updating affected rows is zero.");
    }
    return affectRows;
  }

  //  @Override
  //  public List<Fund> queryFundsBySendStatus(String sendStatus, int limit) {
  //
  //  }
}
