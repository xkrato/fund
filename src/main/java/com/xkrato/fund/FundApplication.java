package com.xkrato.fund;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.xkrato.fund.mapper")
@ComponentScan({
  "com.xkrato.fund.config.**",
  "com.xkrato.fund.controller.**",
  "com.xkrato.fund.domain.**",
  "com.xkrato.fund.service.**",
  "com.xkrato.fund.crawler.**",
  "com.xkrato.fund.schedule.**"
})
public class FundApplication {

  public static void main(String[] args) {
    SpringApplication.run(FundApplication.class, args);
  }
}
