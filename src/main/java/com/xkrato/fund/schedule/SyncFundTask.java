package com.xkrato.fund.schedule;

import com.xkrato.fund.service.IFundService;
import java.util.concurrent.BlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SyncFundTask {

  @Autowired private IFundService fundService;
  
  // TODO 临界资源后面用mq代替
  public static BlockingQueue FACK_MQ;

//  @Scheduled(cron = "0 0 0/1 * * ?")
  @Scheduled(cron = "0/10 * * * * ?")
  public void getFundInfo() throws InterruptedException {
    System.out.println("get fund manager info.");
    try {
      fundService.getFundInfo();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 将待处理记录推到队列（待发送和失败重派两种）
   * TODO 先用BlockingQueue，后面改MQ
   * 
   * @throws InterruptedException
   */
  @Scheduled(cron = "0/30 * * * * ?")
  public void pushPendingRecordToMQ() throws InterruptedException {
    System.out.println("push pending record.");
    // TODO push sendStatus 1/4/9 to mq
    
    // TODO sendStatus processing. sendStatus From 1/4/9 To 2. (1/4/9 -> 2)
  }

  /**
   * 处理超时记录
   * 
   * @throws InterruptedException
   */
  @Scheduled(cron = "0/30 * * * * ?")
  public void syncProcessingTask() throws InterruptedException {
    System.out.println("fix timeout record.");
    // TODO timeout 30min. sendStatus From 2 To 4. (2 -> 4)
  }
  
}
