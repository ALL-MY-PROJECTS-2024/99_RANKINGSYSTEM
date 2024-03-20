package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

public interface UserService {

    public boolean memberJoin(UserDto dto, Model model, HttpServletRequest request) throws Exception;
}
