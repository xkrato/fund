package com.xkrato.fund.schedule;

import com.xkrato.fund.service.IFundManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SyncFundManagerTask {

  @Autowired private IFundManagerService fundManagerService;

  @Scheduled(cron = "0 0 0/1 * * ?")
  public void getFundManagerInfo() throws InterruptedException {
    System.out.println("get fund manager info.");
  }

  @Scheduled(cron = "0/30 * * * * ?")
  public void sendManagerChangedEmail() throws InterruptedException {
    System.out.println("send manager changed email.");
    try {
      fundManagerService.getFundManagerInfo();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
