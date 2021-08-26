package com.xkrato.fund.domain;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table fund_manager
 */
public class FundManager {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.uuid
     *
     * @mbg.generated
     */
    private String uuid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.fund_name
     *
     * @mbg.generated
     */
    private String fundName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.employment_date
     *
     * @mbg.generated
     */
    private String employmentDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.send_flag
     *
     * @mbg.generated
     */
    private String sendFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fund_manager.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.uuid
     *
     * @return the value of fund_manager.uuid
     *
     * @mbg.generated
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.uuid
     *
     * @param uuid the value for fund_manager.uuid
     *
     * @mbg.generated
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.name
     *
     * @return the value of fund_manager.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.name
     *
     * @param name the value for fund_manager.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.fund_name
     *
     * @return the value of fund_manager.fund_name
     *
     * @mbg.generated
     */
    public String getFundName() {
        return fundName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.fund_name
     *
     * @param fundName the value for fund_manager.fund_name
     *
     * @mbg.generated
     */
    public void setFundName(String fundName) {
        this.fundName = fundName == null ? null : fundName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.employment_date
     *
     * @return the value of fund_manager.employment_date
     *
     * @mbg.generated
     */
    public String getEmploymentDate() {
        return employmentDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.employment_date
     *
     * @param employmentDate the value for fund_manager.employment_date
     *
     * @mbg.generated
     */
    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate == null ? null : employmentDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.send_flag
     *
     * @return the value of fund_manager.send_flag
     *
     * @mbg.generated
     */
    public String getSendFlag() {
        return sendFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.send_flag
     *
     * @param sendFlag the value for fund_manager.send_flag
     *
     * @mbg.generated
     */
    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag == null ? null : sendFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.create_time
     *
     * @return the value of fund_manager.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.create_time
     *
     * @param createTime the value for fund_manager.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fund_manager.update_time
     *
     * @return the value of fund_manager.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fund_manager.update_time
     *
     * @param updateTime the value for fund_manager.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}