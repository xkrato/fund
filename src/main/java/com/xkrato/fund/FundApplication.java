package com.xkrato.fund;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan({
  "com.xkrato.fund.domain.**",
  "com.xkrato.fund.service.**",
  "com.xkrato.fund.factory.**",
  "com.xkrato.fund.crawler.**",
  "com.xkrato.fund.schedule.**"
})
@MapperScan("com.xkrato.fund.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class FundApplication {

  public static void main(String[] args) {
    SpringApplication.run(FundApplication.class, args);
  }
}
