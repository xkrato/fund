package com.xkrato.fund.service;

import com.xkrato.fund.domain.Fund;
import java.util.List;
import org.jsoup.nodes.Document;

public interface IFundService {
  public void getFundInfo() throws Exception;

  public Fund parseFundInfo(String urlPath, Document document);

  public void saveFundInfo(Fund fund);

  public List<Fund> queryFundById(String id);
  
  public int addFund(Fund latestFund);
  
  public int addFund(List<Fund> funds);
  
  public int updateFundById(Fund latestFund);
  
}
