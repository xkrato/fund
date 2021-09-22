package com.xkrato.fund.schedule;

import com.sendgrid.*;
import com.xkrato.fund.util.DESedeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Test.class)
class ConsumeFundTaskTest {

    @Test
    public void sendEmailTest() {
        Email from = new Email("xuchuang0426@qq.com");
        String subject = "test";
        Email to = new Email("188798450@qq.com");
        Content content = new Content("text/plain", "test 1");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(DESedeUtil.decrypt("326a23480c26bc89246f85f3e8012345e782654bd8712509", "8496C18139090062C6F1BF505BC0F6C8D8A003B0FC4F0868EE17E706876BA5E03C3151EF09A2B29AFB5D6BFBC27EA91500CAACF8886659300A92EF7F23A211B18A2A4E1342A347EB"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}