package com.xkrato.fund.crawler;

import com.xkrato.fund.domain.dto.Fund;
import com.xkrato.fund.service.IFundService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FundCrawler extends WebCrawler {
  private static final Logger LOGGER = LoggerFactory.getLogger(FundCrawler.class);

  @Value("${fund.homepage}")
  private String FUND_HOMEPAGE;

  @Value("${fund.name1}")
  private String FUND_NAME1;

  @Value("${fund.name2}")
  private String FUND_NAME2;
  
  @Value("${fund.filter-types}")
  private String FILTERS_TYPE;

  @Resource private IFundService fundService;

  private static Pattern FILTERS;

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    if (FILTERS == null) {
      FILTERS = Pattern.compile(".*(\\.(" + FILTERS_TYPE +"))$");
    }
    
    String href = url.getURL().toLowerCase();
    //    return !FILTERS.matcher(href).matches() &&
    // href.startsWith("https://fund.eastmoney.com/420102.html?spm=aladin");
    return href.startsWith(FUND_HOMEPAGE + FUND_NAME1)
        || href.startsWith(FUND_HOMEPAGE + FUND_NAME2);
  }

  @Override
  public void visit(Page page) {
    String url = page.getWebURL().getURL();
    LOGGER.info("crawler start, Current crawler url is {}", url);
    if (!(page.getParseData() instanceof HtmlParseData)) {
      return;
    }

    Document document = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());
    Fund fund = fundService.parseFundInfo(page.getWebURL().getPath(), document);
    if (fund == null) {
      return;
    }
    
    fundService.saveFundInfo(fund);
    LOGGER.info("crawler end.");
  }
}
