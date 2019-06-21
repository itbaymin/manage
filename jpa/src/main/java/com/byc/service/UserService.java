package com.byc.service;

import com.byc.dao.entity.Users;
import com.byc.mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by baiyc
 * 2019/5/6/006 17:47
 * Description：
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(Users user){
        userRepository.save(user);
    }
}
