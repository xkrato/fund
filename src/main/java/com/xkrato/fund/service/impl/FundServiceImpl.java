package com.xkrato.fund.service.impl;

import com.xkrato.fund.crawler.FundCrawler;
import com.xkrato.fund.domain.Fund;
import com.xkrato.fund.service.IFundService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

  @Override
  public void getFundInfo() throws Exception {
    // 爬虫配置
    CrawlConfig config = new CrawlConfig();
    // 爬虫状态存储文件夹，可以从这里边读取数据，以恢复之前的爬取状态
    config.setCrawlStorageFolder(FUND_CACHE_DIR);
    PageFetcher pageFetcher = new PageFetcher(config);
    CrawlController controller =
        new CrawlController(
            config, pageFetcher, new RobotstxtServer(new RobotstxtConfig(), pageFetcher));

    // 要爬取的起始地址
    controller.addSeed(FUND_HOMEPAGE + FUND_NAME1);
    controller.addSeed(FUND_HOMEPAGE + FUND_NAME2);
    // 启动
    // 1 爬虫数量，也就是线程数，一般不超过CPU线程数
    controller.start(FundCrawler.class, 1);
  }

  @Override
  public void parseFundInfo(String urlPath, Document document) {
    Fund fund = new Fund();

    List<String> urls = Arrays.asList(urlPath.split("/"));
    if (CollectionUtils.isEmpty(urls)) {
      return;
    }
    fund.setId(urls.get(urls.size() - 1).replaceAll(".html", ""));

    Elements elements = document.select("#fundManagerTab tr:eq(1) .td02");
    if (CollectionUtils.isEmpty(elements)) {
      return;
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

    this.saveFundInfo(fund);
  }

  @Override
  public void saveFundInfo(Fund fund) {
    // TODO fund not exist
    // TODO insert
    // TODO sendStatus From null To 0 (null -> 0)
    
    // TODO fund exist
    // TODO update sendStatus
    // TODO if fund managers changed. sendStatus From 0/4 To 1 (0/4 -> 1)
    // TODO if fund managers not changed. return
  }
}
