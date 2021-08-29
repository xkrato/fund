package com.xkrato.fund.crawler;

import com.xkrato.fund.service.IFundService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FundCrawler extends WebCrawler {

  @Value("${fund.homepage}")
  private String FUND_HOMEPAGE;

  @Value("${fund.name1}")
  private String FUND_NAME1;

  @Value("${fund.name2}")
  private String FUND_NAME2;
  
  @Resource
  private IFundService fundService;
  
  private static final Pattern FILTERS =
      Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    //    return !FILTERS.matcher(href).matches() &&
    // href.startsWith("https://fund.eastmoney.com/420102.html?spm=aladin");
    return href.startsWith("https://fund.eastmoney.com/420102.html?spm=aladin")
        || href.startsWith("https://fund.eastmoney.com/001751.html?spm=aladin");
  }

  @Override
  public void visit(Page page) {
    String url = page.getWebURL().getURL();
    System.out.println("URL: " + url);
    if (!(page.getParseData() instanceof HtmlParseData)) {
      return;
    }

    Document document = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());
    fundService.saveFundInfo(fundService.parseFundInfo(page.getWebURL().getPath(), document));
  }
}
