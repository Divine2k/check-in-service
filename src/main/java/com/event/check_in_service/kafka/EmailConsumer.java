package com.event.check_in_service.kafka;

import com.event.check_in_service.utility.GlobalConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class EmailConsumer {

    @KafkaListener(topics = GlobalConstants.EMAIL_KAFKA_TOPIC, groupId = GlobalConstants.EMAIL_KAFKA_GROUP_ID,
            concurrency = GlobalConstants.EMAIL_KAFKA_CONCURRENCY)
    public void getMessageForEmailNotification(String data) {

        try {

            log.info("Attempting to received message for email notification: {}", data);
            sendMail(data);
            log.info("Successfully synced with email notification system");
        } catch (Exception e) {

            log.error("Mail notification system call failed: {}", e.getMessage());
            throw new RuntimeException("Retry in progress");
        }
    }

    private void sendMail(String data) throws MessagingException {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1025);
        mailSender.setUsername("");
        mailSender.setPassword("");

        Properties mailProp = mailSender.getJavaMailProperties();
        mailProp.put("mail.transport.protocol", "smtp");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("system@no-reply.com");
        mimeMessageHelper.setText(data);
        mimeMessageHelper.setTo(InternetAddress.parse("employeeId@email"));
        mimeMessageHelper.setSubject("Test");

        mailSender.send(mimeMessage);
    }
}
