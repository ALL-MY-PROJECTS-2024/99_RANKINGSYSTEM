package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.domain.dto.EducationDto;
import com.creator.imageAndMusic.domain.entity.Education;
import com.creator.imageAndMusic.domain.repository.EducationRepository;
import com.creator.imageAndMusic.properties.UPLOADPATH;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EducationServiceImpl {


    @Autowired
    private EducationRepository educationRepository;

    @Transactional(rollbackFor=Exception.class)
    public boolean addEducation(EducationDto dto) throws IOException {

        Education education = new Education();
        education.setTitle(dto.getTitle());
        education.setCategory(dto.getCategory());
        education.setType(dto.getType());
        education.setLink(dto.getLink());
        education.setRegdate(LocalDateTime.now());
        education.setFilepath(null);
        educationRepository.save(education);

        if(dto.getType().equals("file")){
            //디렉토리 만들기

            String uploadPath= UPLOADPATH.ROOTDIRPATH+ File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator + UPLOADPATH.EDUPATH+File.separator;
            uploadPath+=dto.getCategory() + File.separator+education.getId();

            File dir = new File(uploadPath);
            if(!dir.exists())
                dir.mkdirs();

            MultipartFile file = dto.getFile();
            File fileObj = new File(uploadPath,file.getOriginalFilename());
            file.transferTo(fileObj);


            String dirPath= File.separator+UPLOADPATH.UPPERDIRPATH+ File.separator + UPLOADPATH.EDUPATH+File.separator;
            dirPath+=dto.getCategory() + File.separator+education.getId();
            education.setFilepath(dirPath+File.separator+file.getOriginalFilename());
            educationRepository.save(education);
        }

        return true;
    }
    @Transactional(rollbackFor=Exception.class)
    public List<Education> getEducations(String dalle) {
        return  educationRepository.findAllByCategory(dalle);
    }
}
