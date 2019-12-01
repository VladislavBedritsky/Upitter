package com.example.twitter.service;

import com.example.twitter.dao.UserDAO;
import com.example.twitter.entity.Role;
import com.example.twitter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MailSender mailSender;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }

    public void findUserByUsername(String username) {
        userDAO.findByUsername(username);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public void save(User user) {
        userDAO.save(user);
    }

    public boolean addUser(User user) {

        User userFromDB = userDAO.findByUsername(user.getUsername());

        if(userFromDB != null) {
            return false;   // пользователь не добавлен
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        user.setActivationCode(UUID.randomUUID().toString());

        userDAO.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello,"+user.getUsername()+"\nWelcome to Upitter. Please, visit this link\n" +
                            "http://localhost:8080/activate/"+user.getActivationCode());

            mailSender.send(user.getEmail(),"Activation code",message);
        }
    }


    public boolean activateUser(String code) {

        User user = userDAO.findByActivationCode(code);

        if(user == null) {
            return false;
        }

        user.setActivationCode(null);

        userDAO.save(user);

        return true;
    }


    public void userRole(User user, Map<String, Object> model) {
        model.put("user",user);
        if(user.getRoles().contains(Role.ADMIN)){
            model.put("is",true);
        }
    }

    public void saveUserByHisRole (User user, Map<String, String> form, String username) {

        user.setUsername(username);

        //из Enum в список set (строковый вид)
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        save(user);
    }

    public void updateUserProfile(User user, String password,String password1, String email) {

        String userEmail = user.getEmail();

        boolean q = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email)) ;

        if(q) {
            user.setEmail(email);

            if(!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if(password.equals(password1) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(password1)) {
            user.setPassword(password);

            save(user);

            if(q) {
                sendMessage(user);
            }
        }
    }

    public void follow(User user, User u) {
        u.getFollowers().add(user);
        userDAO.save(u);
    }

    public void unFollow(User user, User u) {
        u.getFollowers().remove(user);
        userDAO.save(u);
    }
}
