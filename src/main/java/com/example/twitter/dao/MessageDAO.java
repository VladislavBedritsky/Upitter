package com.example.twitter.dao;

import com.example.twitter.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageDAO extends CrudRepository<Message, Long> {

    List<Message> findByTag (String tag);

    Message findById (Integer id);


}
