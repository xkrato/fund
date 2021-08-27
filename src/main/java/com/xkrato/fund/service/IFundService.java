package com.xkrato.fund.service;

import com.xkrato.fund.domain.Fund;
import org.jsoup.nodes.Document;

public interface IFundService {
  public void getFundInfo() throws Exception;

  public void parseFundInfo(String urlPath, Document document) throws Exception;

  public void saveFundInfo(Fund fund);
}
