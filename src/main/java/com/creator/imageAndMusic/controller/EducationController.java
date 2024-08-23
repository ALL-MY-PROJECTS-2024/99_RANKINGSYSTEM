package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.domain.dto.EducationDto;
import com.creator.imageAndMusic.domain.entity.Education;
import com.creator.imageAndMusic.domain.service.EducationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "교육", description = "Stable Diffusion,Dalle-E,Mid Journey,GenAI일반,LangChain,온라인강의")
public class EducationController {

    @Autowired
    private EducationServiceImpl educationServiceImpl;

    @Operation(
            summary = "홈>교육>Stable Diffusion",
            description = "Stable Diffusion 교육영상 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/stableDiffusion")
    public void t1(Model model){
        log.info("/edu/stableDiffusion");
        List<Education> list =  educationServiceImpl.getEducations("stableDiffusion");
        model.addAttribute("list" , list);
    }


    @Operation(
            summary = "홈>교육>Dalle-E",
            description = "Dalle-E 교육영상 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/dalle")
    public void t2(Model model){

        log.info("/edu/dalle");
        List<Education> list =  educationServiceImpl.getEducations("dalle");
        model.addAttribute("list" , list);

    }
    @Operation(
            summary = "홈>교육>Mid Journey",
            description = "Mid Journey 교육영상 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/mid")
    public void t3(Model model){
        log.info("/edu/mid");
        List<Education> list =  educationServiceImpl.getEducations("mid");
        model.addAttribute("list" , list);
    }

    @Operation(
            summary = "홈>교육>GenAI일반",
            description = "GenAI일반 교육영상 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/genAi")
    public void t4(Model model){
        log.info("/edu/genAi");
        List<Education> list =  educationServiceImpl.getEducations("genAi");
        model.addAttribute("list" , list);
    }

    @Operation(
            summary = "홈>교육>온라인강의",
            description = "온라인강의 교육영상 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/lecture")
    public void t5(Model model){
        log.info("/edu/lecture");
        List<Education> list =  educationServiceImpl.getEducations("lecture");
        model.addAttribute("list" , list);
    }

    @Operation(
            summary = "홈>교육>Lang Chain",
            description = "Lang Chain 교육영상 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/langChain")
    public void LangChain(Model model){
        log.info("/edu/langChain");
        List<Education> list =  educationServiceImpl.getEducations("LangChain");
        model.addAttribute("list" , list);
    }


    @Operation(
            summary = "홈>교육>영상 등록",
            description = "교육영상 등록",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
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
    @Operation(
            summary = "홈>교육>영상 삭제",
            description = "교육영상 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @DeleteMapping("/delete")
    public @ResponseBody void delete(EducationDto dto){
        log.info("/edu/add...dto : " + dto);
        boolean isDeleted =  educationServiceImpl.deleteEducation(dto);

    }

}
