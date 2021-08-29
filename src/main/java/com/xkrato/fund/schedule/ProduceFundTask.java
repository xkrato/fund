package com.xkrato.fund.schedule;

import com.xkrato.fund.domain.dto.Fund;
import com.xkrato.fund.domain.enumerate.SendStatusEnum;
import com.xkrato.fund.domain.queue.FundQueue;
import com.xkrato.fund.service.IFundService;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@EnableScheduling
public class ProduceFundTask {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProduceFundTask.class);
  private static final BlockingQueue<Fund> FUND_MQ = FundQueue.getFundQueue();

  @Resource private IFundService fundService;

  /** 将待处理记录推到队列 */
  @Scheduled(cron = "0 */1 * * * ?")
  public void putPendingRecordsToMQ() throws Exception {
    LOGGER.info("******************* start push pending record *******************");
    // push sendStatus 1 to mq
    // sendStatus processing. sendStatus From 1 To 2. (1 -> 2)
    putRecordsToMQ(SendStatusEnum.WAIT.val());
    LOGGER.info("******************* end push pending record *******************");
  }

  /** 将超时记录推到队列 */
  @Scheduled(cron = "0 */1 * * * ?")
  public void putTimeoutRecordsToMQ() throws Exception {
    LOGGER.info("******************* start push pending record *******************");
    // push sendStatus 4 to mq
    // sendStatus processing. sendStatus From 4 To 2. (4 -> 2)
    putRecordsToMQ(SendStatusEnum.TIMEOUT.val());
    LOGGER.info("******************* end push pending record *******************");
  }

  /** 将失败记录推到队列 */
  @Scheduled(cron = "0 */1 * * * ?")
  public void putFailureRecordsToMQ() throws Exception {
    LOGGER.info("******************* start push pending record *******************");
    // push sendStatus 9 to mq
    // sendStatus processing. sendStatus From 9 To 2. (9 -> 2)
    putRecordsToMQ(SendStatusEnum.FAILURE.val());
    LOGGER.info("******************* end push pending record *******************");
  }

  private void putRecordsToMQ(String sendStatus) throws InterruptedException {
    List<Fund> funds = fundService.queryFundsBySendStatus(sendStatus);
    putFunds(funds);
    fundService.updateSendStatusByUuid(funds, SendStatusEnum.PROCESSING.val());
  }

  private void putFunds(List<Fund> funds) throws InterruptedException {
    if (CollectionUtils.isEmpty(funds)) {
      return;
    }
    for (Fund fund : funds) {
      FUND_MQ.put(fund);
    }
  }
}
