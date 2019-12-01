package com.example.twitter.controller;

import com.example.twitter.entity.Role;
import com.example.twitter.entity.User;
import com.example.twitter.entity.UserImage;
import com.example.twitter.service.UserImageService;
import com.example.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserImageService userImageService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String getListOfUsers(Model model) {
        model.addAttribute("users",userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String getFormUserEditRole(
            @PathVariable User user,
            Model model) {

        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String saveUserByHisRole(
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam("userId") User user) {

        userService.saveUserByHisRole(user,form,username);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(
            @AuthenticationPrincipal User user,
            Map <String, Object> model) {

        model.put("username",user.getUsername());
        model.put("email",user.getEmail());
        userService.userRole(user,model);

        return "profile";
    }

    @PostMapping("profile")
    public String editUserProfile (
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String password1,
            @RequestParam String email,
            Map<String, Object> model) {

        model.put("username",user.getUsername());

        userService.updateUserProfile(user,password,password1,email);

        if (!password.equals(password1)) {
            model.put("q2","Different passwords.");
            return "profile";
        }

        if (password.isEmpty() && email.isEmpty()) {
            model.put("q1","Fields can not be empty.");
            return "profile";
        }

     return "redirect:/login?logout";
    }


    @PostMapping("/profile/{user}")
    public String loadAvatar (
            @AuthenticationPrincipal User user,
            @PathVariable(name = "user") User u,
            Map<String, Object> model,
            @RequestParam("userImage") MultipartFile file){

        userService.userRole(user,model);

        UserImage userImage = new UserImage(user);

        userImageService.uploadAvatar(file,userImage);

        if(!file.isEmpty()) {

            userImageService.save(userImage);

        }

        Set<UserImage> userImages = u.getUserImages();

        if ( userImages.size() == 1 && !file.isEmpty()) {
            for (UserImage temp : userImages) {
                return "redirect:/user-form/delete-image/" + temp.getId();
            }
        }

        return "redirect:/user-form/"+u.getId();
    }

    @GetMapping("follow/{user}")
    public String follow (
            @AuthenticationPrincipal User user,
            @PathVariable(name = "user") User u) {

        userService.follow(user,u);

        return "redirect:/user-form/"+u.getId();
    }


    @GetMapping("unfollow/{user}")
    public String unFollow (
            @AuthenticationPrincipal User user,
            @PathVariable(name = "user") User u) {

        userService.unFollow(user,u);

        return "redirect:/user-form/"+u.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            @AuthenticationPrincipal User u,
            @PathVariable User user,
            @PathVariable String type,
            Map <String, Object> model) {

        model.put("userChannel",user);
        model.put("type",type);

        if("followings".equals(type)) {
            model.put("users",user.getFollowings());
        }else {
            model.put("users",user.getFollowers());
        }

        userService.userRole(u,model);

        return "followings";
    }


}
