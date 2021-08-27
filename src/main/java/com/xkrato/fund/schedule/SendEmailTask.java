package com.xkrato.fund.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SendEmailTask {

  /**
   * 消费邮件队列数据
   *
   * @throws InterruptedException
   */
  @Scheduled(cron = "0/30 * * * * ?")
  public void offerRecordFromMQ() throws InterruptedException {
    System.out.println("send manager changed email.");
    this.sendEmail();
  }
  
  private void sendEmail() {
    // TODO send email error. sendStatus From 2 To 9. (2 -> 9)
    // TODO send email success. sendStatus From 2 To 3. (2 -> 3)
  }
}
