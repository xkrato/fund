package com.xkrato.fund.schedule;

import com.xkrato.fund.domain.dto.Fund;
import com.xkrato.fund.domain.enumerate.SendStatusEnum;
import com.xkrato.fund.service.IFundService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SyncFundTask {
  private static final Logger LOGGER = LoggerFactory.getLogger(SyncFundTask.class);

  @Resource private IFundService fundService;

  @Scheduled(cron = "0 0/30 * * * ?")
  //  @Scheduled(cron = "0/30 * * * * ?")
  public void crawlerFundInfo() throws Exception {
    LOGGER.info("******************* start crawler fund info *******************");
    fundService.crawlerFundInfo();
    LOGGER.info("******************* end crawler fund info *******************");
  }

  /**
   * 处理超时记录
   *
   * @throws Exception
   */
  @Scheduled(cron = "0 0/1 * * * ?")
  public void fixTimeoutRecord() throws Exception {
    LOGGER.info("******************* start fix timeout record *******************");
    // timeout 1h
    // sendStatus From 2 To 4. (2 -> 4)
    Fund record = new Fund();
    record.setSendStatus(SendStatusEnum.TIMEOUT.val());
    // TODO timeout need to read from config
    fundService.fixTimeoutRecord(record, 60 * 60 * 1000);
    LOGGER.info("******************* end fix timeout record *******************");
  }
}
