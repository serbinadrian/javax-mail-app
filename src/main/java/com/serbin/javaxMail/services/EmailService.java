package com.serbin.javaxMail.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;

import com.serbin.javaxMail.domains.EmailMessage;
import com.serbin.javaxMail.util.MessageParser;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    Session session;
    Folder emailFolder;
    Store store;
    Properties properties;
    Message[] messages;
    Folder[] availableFolders;

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
            System.out.println("Opening folder...\n");

            emailFolder.open(mode);

            System.out.println("Folder opened!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void closeFolder() {
        try {
            System.out.println("Closing folder...\n");

            emailFolder.close();

            System.out.println("Folder closed!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void getFolder(String folderName) { //"INBOX"
        try {
            System.out.println("Getting folder " + folderName + "...\n");
            emailFolder = store.getFolder(folderName);
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
            messages = emailFolder.getMessages();
            closeFolder();

            System.out.println("Messages acquired!\n");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public List<EmailMessage> exportMessages(){
        openFolder(Folder.READ_ONLY);
        List<EmailMessage> messagesToExport = new ArrayList<>();
        for(Message message : messages) {
            var exportMessage = new EmailMessage();
            try {
                exportMessage.setFrom(((InternetAddress)message.getFrom()[0]).getAddress());
                exportMessage.setSubject(message.getSubject());
                exportMessage.setDate(message.getReceivedDate());
                exportMessage.setText(MessageParser.getMessageBody(message));
                exportMessage.setFlag(message.getFlags().contains(Flags.Flag.SEEN));
                exportMessage.print();
                messagesToExport.add(exportMessage);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
        closeFolder();
        return messagesToExport;
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
}