package com.serbin.javaxMail.domains;

import javax.activation.DataSource;
import java.io.InputStream;
import java.util.*;

public class EmailMessage {
    private int id;
    private String from;
    private String to;
    private String subject;
    private String text;
    private String HTMLContent;
    private Date date;
    private String dateDay;
    private String dateTime;
    private boolean flag;
    private List<String> attachmentNames;
    Map<String, InputStream> attachments;

    public EmailMessage() {
        attachmentNames = new ArrayList<>();
    }

    public void print() {
        var flagger = flag ? "read" : "unread";
        System.out.println("id: " + id +"\n");
        System.out.println("flag: " + flagger +"\n");
        System.out.println("from: " + from +"\n");
        System.out.println("to: " + to +"\n");
        System.out.println("date: " + date.toString() +"\n");
        System.out.println("subject: " + subject +"\n");
        System.out.println("text: " + text +"\n");
        System.out.println("html: " + HTMLContent +"\n");
        System.out.println("attachments: " + attachmentNames.size() +"\n");
        for(String attachment : attachmentNames) {
            System.out.println("attachment: " + attachment +"\n");
        }
    }

    public Map<String, InputStream> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, InputStream> attachments) {
        this.attachments = attachments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public String getHTMLcontent() {
        return HTMLContent;
    }

    public void setHTMLcontent(String HTMLcontent) {
        this.HTMLContent = HTMLcontent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAttachmentNames() {
        return attachmentNames;
    }

    public void setAttachmentNames(List<String> attachments) {
        this.attachmentNames = attachments;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.dateTime = cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE);
        this.dateDay = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        this.date = date;
    }
}
