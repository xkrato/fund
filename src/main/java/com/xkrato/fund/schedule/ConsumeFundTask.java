package com.xkrato.fund.schedule;

import com.xkrato.fund.domain.dto.Fund;
import com.xkrato.fund.domain.enumerate.SendStatusEnum;
import com.xkrato.fund.domain.queue.FundQueue;
import com.xkrato.fund.service.IFundService;
import java.util.concurrent.BlockingQueue;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ConsumeFundTask {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeFundTask.class);
  private static final BlockingQueue<Fund> FUND_MQ = FundQueue.getFundQueue();

  @Resource private IFundService fundService;

  /**
   * 消费邮件队列数据
   *
   * @throws InterruptedException
   */
  @Scheduled(cron = "* * * * * ?")
  public void takeRecordFromMQ() throws InterruptedException {
    LOGGER.info("******************* start send manager changed email *******************");
    // TODO 多线程？
    this.sendEmail(FUND_MQ.take());
    LOGGER.info("******************* end send manager changed email *******************");
  }

  private void sendEmail(Fund fund) {
    // TODO send email.
    
    // TODO send email error. sendStatus From 2 To 9. (2 -> 9)
    // TODO send email success. sendStatus From 2 To 3. (2 -> 3)
    fundService.updateSendStatusByUuid(fund.getUuid(), SendStatusEnum.SUCCESS.val());
  }
}
