package com.xkrato.fund.domain;

import java.util.Date;
import lombok.Data;

/** This class corresponds to the database table fund */
@Data
public class Fund {
  /** This field corresponds to the database column fund.uuid */
  private String uuid;

  /**
   * Database Column Remarks: 基金id
   *
   * <p>This field corresponds to the database column fund.id
   */
  private String id;

  /**
   * Database Column Remarks: 基金名称
   *
   * <p>This field corresponds to the database column fund.name
   */
  private String name;

  /**
   * Database Column Remarks: 基金经理id
   *
   * <p>This field corresponds to the database column fund.manager_id
   */
  private String managerId;

  /**
   * Database Column Remarks: 基金经理
   *
   * <p>This field corresponds to the database column fund.manager_name
   */
  private String managerName;

  /**
   * Database Column Remarks: 发送状态：0-初始状态，1-待发送，2-发送中，3-发送超时，4-发送成功，9-发送失败
   *
   * <p>This field corresponds to the database column fund.send_status
   */
  private String sendStatus;

  /**
   * Database Column Remarks: 创建时间
   *
   * <p>This field corresponds to the database column fund.create_time
   */
  private Date createTime;

  /**
   * Database Column Remarks: 更新时间
   *
   * <p>This field corresponds to the database column fund.update_time
   */
  private Date updateTime;
}
