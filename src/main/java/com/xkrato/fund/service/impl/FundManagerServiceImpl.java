package com.xkrato.fund.service.impl;

import com.xkrato.fund.crawler.FundManagerCrawler;
import com.xkrato.fund.service.IFundManagerService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Service
public class FundManagerServiceImpl implements IFundManagerService {

  @Value("${fund.homepage}")
  private String FUND_HOMEPAGE;

  @Value("${fund.name1}")
  private String FUND_NAME1;

  @Value("${fund.name2}")
  private String FUND_NAME2;

  @Value("${fund.cache-dir}")
  private String FUND_CACHE_DIR;

  @Override
  public void getFundManagerInfo() throws Exception {
    // 爬虫状态存储文件夹，可以从这里边读取数据，以边恢复之前的爬取状态
    // 爬虫数量，也就是线程数，一般不超过CPU线程数
    int numberOfCrawlers = 1;
    // 爬虫配置
    CrawlConfig config = new CrawlConfig();
    config.setCrawlStorageFolder(FUND_CACHE_DIR);

    /*
     * Instantiate the controller for this crawl.
     */
    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    // 要爬取的起始地址
    controller.addSeed(FUND_HOMEPAGE + FUND_NAME1);
    controller.addSeed(FUND_HOMEPAGE + FUND_NAME2);
    // 启动
    controller.start(FundManagerCrawler.class, numberOfCrawlers);
  }
}
