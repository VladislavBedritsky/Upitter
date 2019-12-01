package com.example.twitter.controller;

import com.example.twitter.entity.Message;
import com.example.twitter.entity.User;
import com.example.twitter.entity.UserImage;
import com.example.twitter.service.MessageService;
import com.example.twitter.service.UserImageService;
import com.example.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("user-form")
public class UserFormController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserImageService userImageService;

    @GetMapping("{user}")
    public String getUserForm(
            @AuthenticationPrincipal User user,
            @PathVariable(name="user") User u,
            Map<String, Object> model) {

        boolean q = user.equals(u);
        model.put("isCurrentUser",q);
        model.put("isFollower",u.getFollowers().contains(user));

        messageService.userMessages(u,model);
        userImageService.userImage(u,model);

        userService.userRole(user,model);

        return "userForm";
    }

    @PostMapping("{user}")
    public String add(
            @AuthenticationPrincipal User user,
            @PathVariable(name="user") User u,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model,
            @RequestParam("file") MultipartFile file){

        boolean q = user.equals(u);
        model.put("isCurrentUser",q);

        Message message = new Message(text, tag, u,messageService.getCurrentTime());

        messageService.uploadFile(file,message);

        if(text != null && !text.isEmpty()) {
            messageService.save(message);
        }

        messageService.userMessages(u,model);
        userImageService.userImage(u,model);
        userService.userRole(user, model);

        return "userForm";
    }

    @GetMapping("remove/{id}")
    public String deleteMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id) {

        Message message = messageService.findById(id);
        messageService.delete(message);

        return "redirect:/user-form/"+user.getId();
    }

    @GetMapping("delete-image/{id}")
    public String deleteImage(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id){

        UserImage userImage = userImageService.findImageById(id);
        userImageService.delete(userImage);

        return "redirect:/user-form/"+user.getId();
    }

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

        return "redirect:/user-form/"+user.getId();
    }

    @GetMapping("like/{id}")
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

        return "redirect:/user-form/"+message.getUserId();
    }

    @GetMapping("like/{id}/list")
    public String userList(
            @AuthenticationPrincipal User u,
            @PathVariable Integer id,
            Map <String, Object> model) {

        Message message = messageService.findById(id);

        model.put("users",message.getLikes());

        return "likes";
    }
}
