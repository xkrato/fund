package com.xkrato.fund.domain.queue;

import com.xkrato.fund.domain.dto.Fund;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FundQueue {
  private static final BlockingQueue<Fund> FAKE_FUNDS_MQ = new ArrayBlockingQueue<Fund>(1000);

  private FundQueue() {}

  public static BlockingQueue<Fund> getFundQueue() {
    return FAKE_FUNDS_MQ;
  }
}
