//package com.creator.imageAndMusic.restcontroller;
//
//
//import com.creator.imageAndMusic.config.auth.PrincipalDetails;
//import com.creator.imageAndMusic.controller.BoardController;
//import com.creator.imageAndMusic.domain.entity.Board;
//import com.creator.imageAndMusic.domain.service.BoardServiceImpl;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import org.thymeleaf.util.StringUtils;
//
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/board")
//@Slf4j
//@Tag(name = "자유게시판", description = "리스트,글쓰기,글읽기,글삭제,댓글추가,댓글삭제")
//public class BoardRestController {
//
//    @Autowired
//    private BoardServiceImpl boardService;
//
//    //------------------
//    //FILEDOWNLOAD
//    //------------------
//
//    @GetMapping(value="/download" ,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<Resource> Download(@RequestParam(name = "filename",defaultValue = "") String filename) throws UnsupportedEncodingException
//    {
//        filename = filename.trim();
//        System.out.println("GET /board/download filename : " + filename);
//        String path  = BoardController.READ_DIRECTORY_PATH + File.separator + filename;
//        log.info("RestController_03Download's Download Call.." + path);
//        System.out.println("GET /board/download path : " + path);
//        //FileSystemResource : 파일시스템의 특정 파일로부터 정보를 가져오는데 사용
//        Resource resource = new FileSystemResource(path);
//        //파일명 추출
//        filename = resource.getFilename();
//        //헤더 정보 추가
//        HttpHeaders headers = new HttpHeaders();
//        //ISO-8859-1 : 라틴어(특수문자등 깨짐 방지)
//        headers.add("Content-Disposition","attachment; filename="+new String(filename.getBytes("UTF-8"),"ISO-8859-1"));
//        //리소스,파일정보가 포함된 헤더,상태정보를 전달
//        return new ResponseEntity<Resource>(resource,headers, HttpStatus.OK);
//
//    }
//
//
//
//    //-------------------
//    // 수정하기
//    //-------------------
////    @PutMapping("/put/{no}/{filename}")
////    public String put(@PathVariable String no, @PathVariable String filename)
////    {
////        log.info("PUT /board/put " + no + " " + filename);
////        boolean isremoved = boardService.removeFile(no,filename);
////        return "success";
////    }
//
//
//    //-------------------
//    // 삭제하기
//    //-------------------
//    @DeleteMapping("/delete")
//    public ResponseEntity<Map<String,Object>> delete(@RequestParam("no") Long no, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception{
//        log.info("DELETE /board/delete no " + no);
//
//
//        Map<String,Object> result = new HashMap<>();
//        Board board =  boardService.getBoardOne(no);
//        String loginedUsername = principalDetails.getUsername();
//        String writer = board.getUsername();
//        if(!StringUtils.equals(loginedUsername,writer)){
//            result.put("message","작성자만 삭제할 수 있습니다.");
//            return new ResponseEntity<>(result,HttpStatus.BAD_GATEWAY);
//        }
//
//        boolean isremoved =  boardService.removeBoard(no);
//        result.put("message","삭제완료.");
//        return new ResponseEntity<>(result,HttpStatus.OK);
//
//
//    }
//
//
//
//
////    //-------------------
////    //댓글추가
////    //-------------------
////    @GetMapping("/reply/add")
////    public void addReply(Long bno,String contents , String username){
////        log.info("GET /board/reply/add " + bno + " " + contents + " " + username);
////        boardService.addReply(bno,contents, username);
////    }
////    //-------------------
////    //댓글 조회
////    //-------------------
////    @GetMapping("/reply/list")
////    public List<ReplyDto> getListReply(Long bno){
////        log.info("GET /board/reply/list " + bno);
////        List<ReplyDto> list =  boardService.getReplyList(bno);
////        return list;
////    }
////    //-------------------
////    //댓글 카운트
////    //-------------------
////    @GetMapping("/reply/count")
////    public Long getCount(Long bno){
////        log.info("GET /board/reply/count " + bno);
////        Long cnt = boardService.getReplyCount(bno);
////
////        return cnt;
////    }
//
//
//
//
//
//    //-------------------
//    //댓글삭제
//    //-------------------
//
//    //-------------------
//    //댓글수정
//    //-------------------
//
//
//
//
//
//
//}
