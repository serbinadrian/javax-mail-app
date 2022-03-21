package com.serbin.javaxMail.controllers;

import com.serbin.javaxMail.domains.EmailMessage;
import com.serbin.javaxMail.domains.FileMap;
import com.serbin.javaxMail.domains.User;
import com.serbin.javaxMail.services.EmailService;
import com.serbin.javaxMail.services.MailServiceProvider;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.ArrayList;
import java.util.Map;


@Controller
public class MVController {

    EmailService emailService = new EmailService();

    @Autowired
    MailServiceProvider mailServiceProvider;
    Map<String, InputStream> map;
    User user = new User("smokgjas@gmail.com", "user");

    @GetMapping("/")
    String getHomePage(Model model) {

        model.addAttribute("user", user);
        model.addAttribute("inboxMessages", emailService.exportMessages());
        model.addAttribute("sentMessages", emailService.exportSentMessages());
        return "index";
    }

    @GetMapping("/viewMessage/{messageId}")
    public String viewMessage(@PathVariable(value = "messageId") Integer messageId, Model model) {

        EmailMessage emailMessage = emailService.getMessageById(messageId);
        map = emailMessage.getAttachments();
        model.addAttribute("currentMessage", emailMessage);
        model.addAttribute("user", user);
        model.addAttribute("sent", false);

        return "message_in";
    }

    @GetMapping("/viewSentMessage/{messageId}")
    public String viewSentMessage(@PathVariable(value = "messageId") Integer messageId, Model model) {
        EmailMessage emailMessage = emailService.getSentMessageById(messageId);
        map = emailMessage.getAttachments();
        model.addAttribute("currentMessage", emailMessage);
        model.addAttribute("user", user);
        model.addAttribute("sent", true);

        return "message_in";
    }

    @RequestMapping(value = "/cid:{cid}", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response, @PathVariable(value = "cid") String cid) throws IOException {

        IOUtils.copy(map.get(cid), response.getOutputStream());
    }

    @GetMapping("/checkInbox")
    public String checkInbox(Model model, HttpServletResponse response) {
        if(emailService.inboxUpdated()){
            model.addAttribute("message", emailService.getInboxMessage());
            return "incoming_message";
        } else {
            return "blank_response";
        }
    }

    @GetMapping("/checkSentInbox")
    public String checkSentInbox(Model model, HttpServletResponse response) {
        if(emailService.sentInboxUpdated()){
            model.addAttribute("message", emailService.getSentInboxMessage());
            return "outcoming_message";
        } else {
            return "blank_response";
        }
    }

    @PostMapping("/sendCurrentMail")
    RedirectView sendMail(@RequestParam(value = "mailTo") String mailTo,
                          @RequestParam(value = "mailSubject") String mailSubject,
                          @RequestParam(value = "mailText") String mailText,
                          @RequestParam(value = "fileField") ArrayList<MultipartFile> files) throws IOException {

        var fileMap = new ArrayList<FileMap>();
        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                FileMap map = new FileMap();
                map.setFilename(multipartFile.getOriginalFilename());
                map.setFileStream(multipartFile);
                fileMap.add(map);
            }
        }

        System.out.println(mailTo);
        System.out.println(mailSubject);
        System.out.println(mailText);
        boolean isSent = mailServiceProvider.sendMail(mailSubject, mailText, mailTo, fileMap);
        System.out.println(isSent ? "SENT" : "NOT SENT");
        return new RedirectView("/");
    }
}
