package com.serbin.javaxMail.domains;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmailMessage {
    private String from;
    private String subject;
    private String text;
    private Date date;
    private String dateDay;
    private String dateTime;
    private boolean flag;
    private List<String> attachments;

    public EmailMessage() {
        attachments = new ArrayList<>();
    }

    public void print() {
        var flagger = flag ? "read" : "unread";
        System.out.println("flag: " + flagger +"\n");
        System.out.println("from: " + from +"\n");
        System.out.println("date: " + date.toString() +"\n");
        System.out.println("subject: " + subject +"\n");
        System.out.println("text: " + text +"\n");
        System.out.println("attachments: " + attachments.size() +"\n");
        for(String attachment : attachments) {
            System.out.println("attachment: " + attachment +"\n");
        }
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
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
