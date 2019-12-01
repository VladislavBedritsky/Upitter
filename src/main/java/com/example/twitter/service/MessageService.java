package com.example.twitter.service;

import com.example.twitter.dao.MessageDAO;
import com.example.twitter.entity.Message;
import com.example.twitter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessageDAO messageDAO;

    @Value("${upload.path}")
    private String uploadPath;

    public Iterable<Message> findAll() {
        return messageDAO.findAll();
    }

    public void save(Message message) {
        messageDAO.save(message);
    }

    public Iterable<Message> findByTag(String filter) {
       return messageDAO.findByTag(filter);
    }

    public Message findById(Integer id) {
        return messageDAO.findById(id);
    }

    public void delete(Message message) {
        messageDAO.delete(message);
    }

    public void uploadFile(MultipartFile file, Message message) {

        if(file != null && !file.getOriginalFilename().isEmpty()) {   // загрузка файла

            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File("D:/"+uploadPath+"/"+resultFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }

            message.setFilename(resultFilename);
        }
    }

    public void userMessages (User user, Map<String, Object> model) {
        Set<Message> messages = user.getMessages();
        model.put("messages",messages);
    }

    public String getCurrentTime () {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd:MM:yy");
        return simpleDateFormat.format(date);
    }

    public void liked (User user, Message message) {
        message.getLikes().add(user);
        messageDAO.save(message);
    }

    public void disliked(User user, Message message) {
        message.getLikes().remove(user);
        messageDAO.save(message);
    }
}
