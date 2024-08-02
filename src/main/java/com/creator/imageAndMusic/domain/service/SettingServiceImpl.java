package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.domain.entity.ConnectionUser;
import com.creator.imageAndMusic.domain.repository.ConnectionUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingServiceImpl {


    @Autowired
    private ConnectionUserRepository connectionUserRepository;


    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> getSettingInfo(){
        Map<String,Object> result = new HashMap<>();
        List<ConnectionUser> totalConnectionList = connectionUserRepository.findAll();

        result.put("totalConnectionList",totalConnectionList);
        return result;

    }



}
