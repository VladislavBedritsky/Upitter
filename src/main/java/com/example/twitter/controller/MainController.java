package com.example.twitter.controller;

import com.example.twitter.entity.Message;
import com.example.twitter.entity.User;
import com.example.twitter.service.MessageService;
import com.example.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String greeting(
            @RequestParam(name="name", required = false, defaultValue = "Guest") String name,
            Map<String, Object> model) {

        model.put("q",name);
        model.put("sss",true);
        model.put("AuthUsers",true);

        return "greeting";
    }

    @GetMapping("/main")
    public String findAllMessages(
            @AuthenticationPrincipal User user,
            Map <String, Object> model) {

        Iterable<Message> messages = messageService.findAll();

        model.put("messages",messages);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model,
            @RequestParam("file") MultipartFile file) {

        Message message = new Message(text, tag, user,messageService.getCurrentTime());

        messageService.uploadFile(file,message);

        if(text != null && !text.isEmpty()) {
            messageService.save(message);
        }

        Iterable<Message> messages = messageService.findAll();
        model.put("messages",messages);

        userService.userRole(user, model);

        return "main";
    }

    @PostMapping("filter")
    public String findMessageByTag(
            @AuthenticationPrincipal User user,
            @RequestParam String filter,
            Map<String,Object> model) {

        Iterable<Message> messages;

        if(filter != null && !filter.isEmpty()) {
            messages= messageService.findByTag(filter);
        }else messages= messageService.findAll();

        model.put("messages",messages);
        model.put("f",filter);

        userService.userRole(user, model);

        return "main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("remove/{id}")
    public String deleteMessage(
            @PathVariable Integer id,
            Map<String, Object> model) {

        Message message = messageService.findById(id);
        messageService.delete(message);

        return "redirect:/main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("update/{id}")
    public String pageUpdateMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id,
            Map <String, Object> model) {

        Message message = messageService.findById(id);

        model.put("message",message);

        userService.userRole(user, model);

        return "updateMessage";
    }



    @PostMapping("update/{id}")
    public String updateMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String,Object> model) {

        Message message = messageService.findById(id);

        if(!text.isEmpty()) {
            message.setText(text);
        }
        if(!tag.isEmpty()) {
            message.setTag(tag);
        }

        messageService.save(message);

        userService.userRole(user, model);

        return "redirect:/main";
    }


    @GetMapping("main/like/{id}/list")
    public String userList(
            @AuthenticationPrincipal User u,
            @PathVariable Integer id,
            Map <String, Object> model) {

        Message message = messageService.findById(id);

        model.put("users",message.getLikes());

        return "likes";
    }
    @GetMapping("main/like/{id}")
    public String likeMessage (
            @AuthenticationPrincipal User user,
            @PathVariable Integer id,
            Map<String,Object> model
    ){

        Message message = messageService.findById(id);

        if(message.getLikes().contains(user)) {
            messageService.disliked(user,message);
        }else {
            messageService.liked(user,message);
        }

        return "redirect:/main";
    }


}





//    @PostMapping("remove")
//    public String deleteMessage(@RequestParam String remove, Map<String, Object> model) {
//        Message message = messageRepository.findById(Integer.valueOf(remove));
//        messageRepository.delete(message);
//        Iterable<Message> messages = messageRepository.findAll();
//        model.put("messages",messages);
//        return "main";
//    }