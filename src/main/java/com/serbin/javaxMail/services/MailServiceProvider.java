package com.serbin.javaxMail.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import com.serbin.javaxMail.domains.FileMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MailServiceProvider
{
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender sender;

    public MailServiceProvider(JavaMailSender sender) {
        this.sender = sender;
    }

    public boolean sendMail(String subject, String text, String to, ArrayList<FileMap> fileMap)
    {
        boolean isMailsent;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            for (FileMap fileData : fileMap) {
                helper.addAttachment(fileData.getFilename(), fileData.getFile());
            }
            helper.setFrom(from);
            sender.send(message);
            isMailsent = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            isMailsent = false;
        }
        return isMailsent;
    }
}