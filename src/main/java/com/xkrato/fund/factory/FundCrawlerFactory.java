package com.xkrato.fund.factory;

import com.xkrato.fund.crawler.FundCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FundCrawlerFactory implements WebCrawlerFactory<FundCrawler>  {

  @Resource
  private ApplicationContext applicationContext;

  @Override
  public FundCrawler newInstance() throws BeansException {
    return applicationContext.getBean(FundCrawler.class);
  }
}
