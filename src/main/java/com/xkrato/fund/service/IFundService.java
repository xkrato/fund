package com.xkrato.fund.service;

import com.xkrato.fund.domain.dto.Fund;
import java.util.List;
import org.jsoup.nodes.Document;

public interface IFundService {
  public void crawlerFundInfo() throws Exception;

  public Fund parseFundInfo(String urlPath, Document document);

  public void saveFundInfo(Fund fund);

  public List<Fund> queryFundsById(String id);

  public List<Fund> queryFundsBySendStatus(String sendStatus);

  public int addFund(Fund latestFund);

  public int addFund(List<Fund> funds);

  public int updateSendStatusByUuid(String uuid, String sendStatus);

  public int updateSendStatusByUuid(Fund fund, String sendStatus);

  public int updateSendStatusByUuid(List<Fund> funds, String sendStatus);

  public int updateFundByUuid(String uuid, Fund latestFund);

  public int updateFundByUuid(List<String> uuid, Fund latestFund);

  public int fixTimeoutRecord(Fund record, long timeout);
}
