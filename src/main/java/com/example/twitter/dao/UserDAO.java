package com.example.twitter.dao;

import com.example.twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User,Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

}
