package com.alenut.mpi.service;

import com.alenut.mpi.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

    @Autowired
    public SecurityConfig securityConfig;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        JavaMailSender javaMailSender = securityConfig.getJavaMailSender();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("myprojectideaservice@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }
}
