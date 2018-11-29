package com.ecoservices.app.util;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    public void sendEmail(SimpleMailMessage mail);
}
