package com.xkrato.fund.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FundManagerCrawler extends WebCrawler {

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

    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      String html = htmlParseData.getHtml();
      Set<WebURL> links = htmlParseData.getOutgoingUrls();

      System.out.println("Text length: " + text.length());
      System.out.println("Html length: " + html.length());
      System.out.println("Number of outgoing links: " + links.size());
    }
  }
}
