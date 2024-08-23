package com.creator.imageAndMusic.controller;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.domain.dto.BoardDto;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.Reply;
import com.creator.imageAndMusic.domain.service.BoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
@Slf4j
@Tag(name = "자유게시판", description = "리스트,글쓰기,글읽기,글삭제,댓글추가,댓글삭제")
public class BoardController {

//    @Autowired
//    private BoardRepository boardRepository;



    @Autowired
    private BoardServiceImpl boardService;

    public static String READ_DIRECTORY_PATH ;





    //-------------------
    //-------------------
    @Operation(
            summary = "홈>자유게시판>리스트",
            description = "글목록을 10개씩 표시합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/list")
    public String list(
            @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(name = "type",defaultValue = "") String type,
            @RequestParam(name = "keyword",defaultValue = "") String keyword,
            Model model,
            HttpServletResponse response
    )
    {
        log.info("GET /board/list... " + pageNo + " " + type +" " + keyword);

        //----------------
        //PageDto  Start
        //----------------
        Criteria criteria = null;
        if(pageNo==null) {
            //최초 /board/list 접근
            pageNo=1;
            criteria = new Criteria();  //pageno=1 , amount=10
        }
        else {
            criteria = new Criteria(pageNo,10); //페이지이동 요청 했을때
        }
        //--------------------
        //Search
        //--------------------
        //criteria.setType(type);
        //criteria.setKeyword(keyword);


        //서비스 실행
        Map<String,Object> map = boardService.GetBoardList(criteria);

        PageDto pageDto = (PageDto) map.get("pageDto");
        List<Board> list = (List<Board>) map.get("list");


        //Entity -> Dto
        List<BoardDto>  boardList =  list.stream().map(board -> BoardDto.Of(board)).collect(Collectors.toList());
        System.out.println(boardList);

        //View 전달
        model.addAttribute("boardList",boardList);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageDto",pageDto);

        //--------------------------------
        //COUNT UP - //쿠키 생성(/board/read.do 새로고침시 조회수 반복증가를 막기위한용도)
        //--------------------------------
        Cookie init = new Cookie("reading","true");
        response.addCookie(init);
        //--------------------------------

        return "board/list";
    }


    //-------------------
    // POST
    //-------------------
    @Operation(
            summary = "홈>자유게시판>리스트>글쓰기",
            description = "글등록을 위한 페이지를 표시합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/post")
    public void get_addBoard(){
        log.info("GET /board/post");
    }

    @Operation(
            summary = "홈>자유게시판>리스트>글쓰기",
            description = "글등록 처리를 요청합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @PostMapping("/post")
    public String post_addBoard(@Valid BoardDto dto, BindingResult bindingResult, Model model) throws IOException {
        log.info("POST /board/post " + dto + " " + dto);

        //유효성 검사
        if(bindingResult.hasFieldErrors()) {
            for(FieldError error  : bindingResult.getFieldErrors()) {
                log.info(error.getField()+ " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/board/post";
        }

        //서비스 실행
        boolean isadd = boardService.addBoard(dto);

        if(isadd) {
            return "redirect:/board/list";
        }
        return "redirect:/board/post";


    }



    //-------------------
    // READ
    //-------------------
    @Operation(
            summary = "홈>자유게시판>리스트>읽기",
            description = "게시판 글읽기 페이지로 이동합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/read")
    public String read(@RequestParam(name = "no",defaultValue = "1") Long no,
                       @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.info("GET /board/read : " + no);

       //서비스 실행
       Board board =  boardService.getBoardOne(no);

       BoardDto dto = new BoardDto();
       dto.setNo(board.getNo());
       dto.setTitle(board.getTitle());
       dto.setContent(board.getContent());
       dto.setRegdate(board.getRegdate());
       dto.setUsername(board.getUsername());
       dto.setCount(board.getCount());

        List<Reply> replyList =  boardService.findByReply(board);

        int replyCount = replyList.size();

        //-------------------
        // COUNTUP
        //-------------------
        //쿠키 확인 후  CountUp(/board/read.do 새로고침시 조회수 반복증가를 막기위한용도)
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
        {
            for(Cookie cookie:cookies)
            {
                if(cookie.getName().equals("reading"))
                {
                    if(cookie.getValue().equals("true"))
                    {
                        //CountUp
                        System.out.println("COOKIE READING TRUE | COUNT UP");
                        boardService.count(board.getNo());
                        //쿠키 value 변경
                        cookie.setValue("false");
                        response.addCookie(cookie);
                    }
                }
            }
        }

        model.addAttribute("boardDto",dto);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("replyList",replyList);
        model.addAttribute("replyCount",replyCount);

        return "board/read";

    }
    @Operation(
            summary = "홈>자유게시판>리스트>읽기>수정",
            description = "게시판 글내용 수정 페이지로 이동합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/update")
    public String update(@RequestParam(name = "no") Long no, @RequestParam(name = "pageNo") Integer pageNo,@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        log.info("GET /board/update no " + no);

        Board board =  boardService.getBoardOne(no);

        if(!board.getUsername().equals(principalDetails.getUserDto().getUsername())){
            model.addAttribute("username","글수정은 글쓴이만 가능합니다.");
            return "redirect/board/read?no="+no+"&pageNo="+pageNo;
        }

        //서비스 실행
        BoardDto dto = new BoardDto();
        dto.setNo(board.getNo());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setRegdate(board.getRegdate());
        dto.setUsername(board.getUsername());
        dto.setCount(board.getCount());


        model.addAttribute("boardDto",dto);

        return "board/update";
    }

    @Operation(
            summary = "홈>자유게시판>리스트>읽기>수정",
            description = "게시판 글내용 수정 처리를 요청합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @PostMapping("/update")
    public String Post_update(@Valid BoardDto dto, BindingResult bindingResult, Model model) throws IOException {
        log.info("POST /board/update dto " + dto);

        if(bindingResult.hasFieldErrors()) {
            for( FieldError error  : bindingResult.getFieldErrors()) {
                log.info(error.getField()+ " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "board/read";
        }

        //서비스 실행
        boolean isadd = boardService.updateBoard(dto);

        if(isadd) {
            return "redirect:/board/read?no="+dto.getNo();
        }
        return "redirect:/board/update?no="+dto.getNo();

    }


    @Operation(
            summary = "홈>자유게시판>리스트>읽기>댓글추가",
            description = "게시판 글읽기페이지 내에서 댓글 추가 요청 처리입니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @GetMapping("/reply/add")
    public @ResponseBody ResponseEntity<String> reply_add(@RequestParam("bno")Long bno ,@RequestParam("content") String content, @AuthenticationPrincipal PrincipalDetails principalDetails){

        boolean iadded =boardService.addReply(bno,content,principalDetails.getUserDto());

        return new ResponseEntity("SUCCESS", HttpStatus.OK);
    }

    //--------------------------------
//    // /Board/reply/delete
//    //--------------------------------
//    @GetMapping("/reply/delete/{bno}/{rno}")
//    public String delete(@PathVariable Long bno, @PathVariable Long rno){
//        log.info("GET /board/reply/delete bno,rno " + rno + " " + rno);
//
//        boardService.deleteReply(rno);
//
//        return "redirect:/board/read?no="+bno;
//    }
//
//    //--------------------------------
//    // /board/reply/thumbsup
//    //--------------------------------
//    @GetMapping("/reply/thumbsup")
//    public String thumbsup(Long bno, Long rno)
//    {
//
//        boardService.thumbsUp(rno);
//        return "redirect:/board/read?no="+bno;
//    }
//    //--------------------------------
//    // /board/reply/thumbsdown
//    //--------------------------------
//    @GetMapping("/reply/thumbsdown")
//    public String thumbsudown(Long bno, Long rno)
//    {
//        boardService.thumbsDown(rno);
//        return "redirect:/board/read?no="+bno;
//    }




    @Operation(
            summary = "홈>자유게시판>리스트>읽기>댓글삭제",
            description = "등록된 댓글 삭제 처리입니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "없음"
                    )
            }
    )
    @DeleteMapping("/reply/delete")
    public @ResponseBody ResponseEntity<String> delete(
            @RequestParam("bno") Long bno,
            @RequestParam("rno") Long rno,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    )
    {
        Map<String,Object> result=boardService.deleteReply(bno,rno,principalDetails);

        String message =(String)result.get("message");
        boolean success = (boolean)result.get("success");
        if(!success)
            return new ResponseEntity<>(message,HttpStatus.BAD_GATEWAY);

        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
