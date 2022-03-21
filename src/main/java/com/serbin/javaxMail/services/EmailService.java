package com.serbin.javaxMail.services;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;

import com.serbin.javaxMail.domains.EmailMessage;
import com.serbin.javaxMail.util.MessageParser;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    Session session;
    Folder emailFolder;
    Folder emailSentFolder;
    Store store;
    Properties properties;
    List<Message> messages;
    List<Message> sentMessages;
    Folder[] availableFolders;
    Map<Integer, Message> map;
    Map<Integer, Message> sentMap;
    int paramID;
    int messagesCount;
    int sentMessagesCount;

    public EmailService() {
        System.out.println("Constructing Email Service...\n");
        defineProperties();
        startSession();
        getStore();
        getFolderNames();
        printFolderNames();
        getFolder("INBOX");
        getMessages();
    }

    private void defineProperties() {
        System.out.println("Defining options...\n");

        properties = System.getProperties();
        properties.setProperty("mail.store.protocol", "imaps");
        properties.put("mail.imaps.fetchsize","22020096");

        System.out.println("Properties defined!\n");
    }

    private void startSession() {
        System.out.println("Starting session...\n");

        session = Session.getDefaultInstance(properties, null);

        System.out.println("Session started!\n");
    }

    private void getStore() {
        try {
            System.out.println("Getting store...\n");

            store = session.getStore("imaps");
            store.connect("imap.gmail.com", "smokgjas@gmail.com", "xqgeohstohoadotm");

            System.out.println("Store acquired!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void openFolder(int mode) {
        try {
            System.out.println("Opening inbox folder...\n");

            emailFolder.open(mode);

            System.out.println("Folder inbox opened!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void openSentFolder(int mode) {
        try {
            System.out.println("Opening sent folder...\n");

            emailSentFolder.open(mode);

            System.out.println("Folder sent opened!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private void closeFolder() {
        try {
            System.out.println("Closing inbox folder...\n");

            emailFolder.close();

            System.out.println("Folder inbox closed!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void closeSentFolder() {
        try {
            System.out.println("Closing sent folder...\n");

            emailSentFolder.close();

            System.out.println("Folder sent closed!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void getFolder(String folderName) { //"INBOX"
        try {
            System.out.println("Getting folder " + folderName + "...\n");
            emailFolder = store.getFolder(folderName);
            System.out.println("Folder acquired!\n");
            System.out.println("Getting folder " + folderName + "...\n");
            emailSentFolder = store.getFolder("[Gmail]/Отправленные");
            System.out.println("Folder acquired!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void getFolderNames() {
        try {
            System.out.println("Getting available folders...\n");
            availableFolders = store.getDefaultFolder().list("*");
            System.out.println("Folders acquired!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void printFolderNames() {
        try {
            System.out.println("Available folders: \n");
            for (javax.mail.Folder folder : availableFolders) {
                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                    System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                }
            }
            System.out.println("\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void getMessages() {
        try {
            System.out.println("Getting messages...\n");

            openFolder(Folder.READ_ONLY);
            messages = sortMessages(emailFolder.getMessages());
            paramID = messages.size();
            messagesCount = messages.size();
            closeFolder();

            System.out.println("Messages acquired!\n");
            System.out.println("Getting messages...\n");

            openSentFolder(Folder.READ_ONLY);
            sentMessages = sortMessages(emailSentFolder.getMessages());
            sentMessagesCount = sentMessages.size();
            closeSentFolder();

            System.out.println("Messages acquired!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean inboxUpdated() {
        openFolder(Folder.READ_ONLY);
        try {
            messages = sortMessages(emailFolder.getMessages());
            if(messages.size() > messagesCount) {
                messagesCount = messages.size();
                paramID = messagesCount;
                closeFolder();
                return true;
            }
            closeFolder();
        } catch (MessagingException e) {
            e.printStackTrace();
            closeFolder();
            return false;
        }
        return false;
    }

    public boolean sentInboxUpdated() {
        openSentFolder(Folder.READ_ONLY);
        try {
            sentMessages = sortMessages(emailSentFolder.getMessages());
            if(sentMessages.size() > sentMessagesCount) {
                sentMessagesCount = sentMessages.size();
                closeSentFolder();
                return true;
            }
            closeSentFolder();
        } catch (MessagingException e) {
            e.printStackTrace();
            closeSentFolder();
            return false;
        }
        return false;
    }

    public EmailMessage getInboxMessage() {
        openFolder(Folder.READ_ONLY);
        System.out.println("NEW message");
        Message message = messages.get(0);
        int id = paramID + 1;
        map.put(id, message);
        MessageParser messageParser = new MessageParser();
        EmailMessage exportMessage = messageParser.preParse(message, id);
        System.out.println("end of NEW message");
        closeFolder();
        return exportMessage;
    }

    public EmailMessage getSentInboxMessage() {
        openSentFolder(Folder.READ_ONLY);
        System.out.println("NEW message");
        Message message = sentMessages.get(0);
        int id = sentMessagesCount * (-1) - 1;
        sentMap.put(id, message);
        MessageParser messageParser = new MessageParser();
        EmailMessage exportMessage = messageParser.preParse(message, id);
        System.out.println("end of NEW message");
        closeSentFolder();
        return exportMessage;
    }

    public List<EmailMessage> exportMessages() {
        openFolder(Folder.READ_ONLY);
        List<EmailMessage> messagesToExport = new ArrayList<>();

        map = new HashMap<>();
        int id = 0;
        for (Message message : messages) {
            System.out.println("NEW message");
            map.put(id, message);
            MessageParser messageParser = new MessageParser();
            messagesToExport.add(messageParser.preParse(message, id));
            System.out.println("end of NEW message");
            id++;
        }
        closeFolder();
        return messagesToExport;
    }

    public List<EmailMessage> exportSentMessages() {
        openSentFolder(Folder.READ_ONLY);
        List<EmailMessage> messagesToExport = new ArrayList<>();

        sentMap = new HashMap<>();
        int id = paramID * (-1) - 1;
        for (Message message : sentMessages) {
            System.out.println("NEW message");
            sentMap.put(id, message);
            MessageParser messageParser = new MessageParser();
            messagesToExport.add(messageParser.preParse(message, id));
            System.out.println("end of NEW message");
            id--;
        }
        closeSentFolder();
        return messagesToExport;
    }

    public EmailMessage getMessageById(int id) {
        openFolder(Folder.READ_ONLY);
        MessageParser messageParser = new MessageParser();
        EmailMessage emailMessage = messageParser.getContent(map.get(id), id);
        closeFolder();
        return emailMessage;
    }

    public EmailMessage getSentMessageById(int id) {
        openSentFolder(Folder.READ_ONLY);
        MessageParser messageParser = new MessageParser();
        EmailMessage emailMessage = messageParser.getContent(sentMap.get(id), id);
        closeSentFolder();
        return emailMessage;
    }

    public void saveAttachments() {
        String uploadDir = "C:/temp/";
        List<File> attachments = new ArrayList<File>();
        try {
            emailFolder.open(Folder.READ_ONLY);
            System.out.println("start");
            for (Message message : emailFolder.getMessages()) {
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) && bodyPart.getFileName() == null) {
                        continue;
                    }
                    InputStream is = bodyPart.getInputStream();
                    File f = new File(uploadDir + bodyPart.getFileName());
                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buf = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buf)) != -1) {
                        fos.write(buf, 0, bytesRead);
                    }
                    fos.close();
                    attachments.add(f);
                }
            }
            emailFolder.close();
            System.out.println("Attachments: " + attachments.size());
        } catch (MessagingException | IOException ex) {
            ex.printStackTrace();
        }
    }
    private List<Message> sortMessages(Message[] messages) {
        List<Message> messages1 = new ArrayList<>();
        for (int i = messages.length - 1; i > 0; i--) {
            messages1.add(messages[i]);
        }
        return messages1;
    }
}