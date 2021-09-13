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
  
  @Value("${send-grid.email.sender}")
  private String SEND_GRID_EMAIL_SENDER;
  
  @Value("${send-grid.email.receiver}")
  private String SEND_GRID_EMAIL_RECEIVER;
  
  @Value("${send-grid.subject.fund}")
  private String SEND_GRID_SUBJECT_FUND;

  @Value("${send-grid.content.fund}")
  private String SEND_GRID_CONTENT_FUND;

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
	// com.sendgrid:sendgrid-java:4.0.1
	Email from = new Email("xuchuang0426@outlook.com");
    String subject = "Sending with SendGrid is Fun";
    Email to = new Email("xuchuang0426@qq.com");
    Content content = new Content("text/plain", "测试邮件");
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid("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");
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      LOGGER.info(response.getStatusCode());
      LOGGER.info(response.getBody());
      LOGGER.info(response.getHeaders());
    } catch (Exception e) {
		// send email error. 
		// sendStatus From 2 To 9. (2 -> 9)
		fundService.updateSendStatusByUuid(fund.getUuid(), SendStatusEnum.FAILURE.val());
	  LOGGER.error("Something is wrong when sending email.", e);
	}
  
    // send email success. 
	// sendStatus From 2 To 3. (2 -> 3)
    fundService.updateSendStatusByUuid(fund.getUuid(), SendStatusEnum.SUCCESS.val());
  }
}
