package com.example.twitter.dao;

import com.example.twitter.entity.UserImage;
import org.springframework.data.repository.CrudRepository;

public interface UserImageDAO extends CrudRepository<UserImage,Long> {

    UserImage findById(Integer id);

}
