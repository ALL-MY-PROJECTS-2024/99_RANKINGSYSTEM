package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.EducationDto;
import com.creator.imageAndMusic.domain.entity.Education;
import com.creator.imageAndMusic.domain.service.EducationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/edu")
public class EducationController {

    @Autowired
    private EducationServiceImpl educationServiceImpl;

    @GetMapping("/stableDiffusion")
    public void t1(){
        log.info("/edu/stableDiffusion");
    }
    @GetMapping("/dalle")
    public void t2(Model model){

        log.info("/edu/dalle");
        List<Education> list =  educationServiceImpl.getEducations("dalle");
        model.addAttribute("list" , list);

    }
    @GetMapping("/mid")
    public void t3(){
        log.info("/edu/mid");
    }
    @GetMapping("/genAi")
    public void t4(){
        log.info("/edu/genAi");
    }
    @GetMapping("/lecture")
    public void t5(){
        log.info("/edu/lecture");
    }



    @PostMapping("/add")
    public String add(EducationDto dto, RedirectAttributes rttr) throws IOException {
        log.info("/edu/add...dto : " + dto);


        boolean isAdded = educationServiceImpl.addEducation(dto);

        if(isAdded) {
            rttr.addFlashAttribute("message","강의등록 성공!");
            return "redirect:/edu/"+dto.getCategory();
        }else{
            rttr.addFlashAttribute("message","강의등록 실패!");
            return "redirect:/edu/"+dto.getCategory();
        }


    }

    @DeleteMapping("/delete")
    public @ResponseBody void delete(EducationDto dto){
        log.info("/edu/add...dto : " + dto);
        boolean isDeleted =  educationServiceImpl.deleteEducation(dto);

    }

}
