package com.creator.imageAndMusic.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/group")
@Tag(name = "그룹", description = "그룹조회")
public class GroupController {


    @Operation(
            summary = "홈>둘러보기>그룹",
            description = "그룹별 조회 페이지",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/main")
    public void main(){
        log.info("GET /group/main...");
    }


}
