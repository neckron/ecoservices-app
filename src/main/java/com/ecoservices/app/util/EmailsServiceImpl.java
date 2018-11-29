package com.ecoservices.app.util;

import org.codehaus.groovy.antlr.java.Java2GroovyMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailsServiceImpl implements  EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email){
        mailSender.send(email);
    }
}
