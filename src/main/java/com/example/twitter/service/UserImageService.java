package com.example.twitter.service;

import com.example.twitter.dao.UserImageDAO;
import com.example.twitter.entity.User;
import com.example.twitter.entity.UserImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class UserImageService {

    @Autowired
    private UserImageDAO userImageDAO;

    @Value("${upload.path}")
    private String uploadPath;

    public void save (UserImage userImage) {
        userImageDAO.save(userImage);
    }

    public void uploadAvatar(MultipartFile file, UserImage userImage) {

        if(file != null && !file.getOriginalFilename().isEmpty()) {   // загрузка файла

            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File("D:/"+uploadPath+"/"+resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            userImage.setFileName(resultFileName);

        }
    }

    public UserImage findImageById (Integer id) {
        return userImageDAO.findById(id);
    }

    public void delete(UserImage userImage) {
        userImageDAO.delete(userImage);
    }

    public void userImage (User user, Map<String, Object> model) {
        Set<UserImage> userImages = user.getUserImages();
        model.put("userImages",userImages);
    }


}
