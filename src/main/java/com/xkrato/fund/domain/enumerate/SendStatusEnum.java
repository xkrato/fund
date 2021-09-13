package com.xkrato.fund.domain.enumerate;

public enum SendStatusEnum {
  INITIAL(0),
  WAIT(1),
  PROCESSING(2),
  SUCCESS(3),
  TIMEOUT(4),
  FAILURE(9);

  private int sendStatus;

  SendStatusEnum(int sendStatus) {
    this.sendStatus = sendStatus;
  }
  
  public String val() {
    return String.valueOf(sendStatus);
  }
}
