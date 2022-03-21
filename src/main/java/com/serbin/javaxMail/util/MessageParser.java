package com.serbin.javaxMail.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.serbin.javaxMail.domains.EmailMessage;
import org.apache.commons.mail.util.MimeMessageParser;

public class MessageParser {
    public EmailMessage preParse(Message message, int id) {
        var exportMessage = new EmailMessage();
        try {
            exportMessage.setId(id);
            exportMessage.setFrom(((InternetAddress) message.getFrom()[0]).getAddress());
            exportMessage.setTo(((InternetAddress)message.getRecipients(Message.RecipientType.TO)[0]).getAddress());
            exportMessage.setSubject(message.getSubject());
            exportMessage.setDate(message.getReceivedDate());
            exportMessage.setFlag(message.getFlags().contains(Flags.Flag.SEEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exportMessage;
    }

    public EmailMessage getContent(Message message, int id) {
        var exportMessage = preParse(message, id);
        MimeMessageParser parser;
        try {
            parser = new MimeMessageParser((MimeMessage) message).parse();
            if (parser.hasPlainContent()){
                System.out.println("has plain");
                exportMessage.setText(parser.getPlainContent());
            }
            if(parser.hasHtmlContent()) {
                System.out.println("has HTML");
                exportMessage.setHTMLcontent(parser.getHtmlContent());
            }
            if(parser.hasAttachments()) {
                System.out.println("has attachments");
                Map<String, InputStream> map = new HashMap<>();
                for(String cid : parser.getContentIds()){
                    map.put(cid, parser.findAttachmentByCid(cid).getInputStream());
                }
                exportMessage.setAttachments(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exportMessage;
    }
}