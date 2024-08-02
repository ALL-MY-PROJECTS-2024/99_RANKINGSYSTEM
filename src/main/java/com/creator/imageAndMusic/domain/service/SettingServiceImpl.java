package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.domain.entity.ConnectionUser;
import com.creator.imageAndMusic.domain.repository.ConnectionUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingServiceImpl {


    @Autowired
    private ConnectionUserRepository connectionUserRepository;


    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> getAllConnectionList(){
        Map<String,Object> result = new HashMap<>();
        
        //전체 전달
        List<ConnectionUser> totalConnectionList = connectionUserRepository.findAll();
        result.put("totalConnectionList",totalConnectionList);

        return result;

    }
    @Transactional(rollbackFor = Exception.class)
    public Map<LocalDate,Object> getConnectionListPerWeekly(){
        Map<LocalDate,Object> result = new HashMap<>();

        //
        //{날짜KEY :{계정명 : user , 조회수 : 55}}
        List<LocalDate> weeklyDate = getWeekDates();
        for(int i=0;i<weeklyDate.size();i++){
            List<ConnectionUser> cu = connectionUserRepository.findAllByToday(weeklyDate.get(i));
            result.put(weeklyDate.get(i), cu);
        }
        return result;
    }

    public static List<LocalDate> getWeekDates() {
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 오늘 날짜의 요일
        DayOfWeek currentDayOfWeek = today.getDayOfWeek();

        // 이번 주 일요일 계산
        LocalDate sunday = today.minusDays(currentDayOfWeek.getValue() % 7);

        // List 생성
        List<LocalDate> weekDates = new ArrayList<>();

        // 일요일부터 토요일까지의 날짜를 List에 추가
        for (int i = 0; i < 7; i++) {
            LocalDate day = sunday.plusDays(i);
            weekDates.add(day);
        }

        return weekDates;
    }




}
