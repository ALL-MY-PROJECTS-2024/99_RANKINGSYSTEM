package com.creator.imageAndMusic.domain.dto;


import com.creator.imageAndMusic.domain.entity.Board;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {



    private Long no;
    @NotBlank(message = "username을 입력하세요")
    private String username;
    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regdate;
    private Long count;

    public static BoardDto Of(Board board) {
        BoardDto dto = new BoardDto();
        dto.no = board.getNo();
        dto.title=board.getTitle();
        dto.content = board.getContent();
        dto.regdate = board.getRegdate();
        dto.count = board.getCount();
        dto.username  = board.getUsername();
        return dto;
    }
}
