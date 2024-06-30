package com.creator.imageAndMusic.domain.service;


import com.creator.imageAndMusic.controller.BoardController;
import com.creator.imageAndMusic.domain.dto.BoardDto;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.dto.PageDto;
import com.creator.imageAndMusic.domain.dto.UserDto;
import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.Reply;
import com.creator.imageAndMusic.domain.entity.User;
import com.creator.imageAndMusic.domain.repository.BoardRepository;
import com.creator.imageAndMusic.domain.repository.ReplyRepository;
import com.creator.imageAndMusic.domain.repository.UserRepository;
import com.creator.imageAndMusic.properties.UPLOADPATH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class BoardServiceImpl {

    private String uploadDir = UPLOADPATH.ROOTDIRPATH+File.separator+UPLOADPATH.UPPERDIRPATH+File.separator+UPLOADPATH.FILEPATH;

    @Autowired
    private BoardRepository boardRepository;


    @Autowired
    private UserRepository userRepository;;

    @Autowired
    private ReplyRepository replyRepository;

    //모든 게시물 가져오기
    
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> GetBoardList(Criteria criteria) {



        Map<String,Object> returns = new HashMap<String,Object>();


        //전체게시물 건수 받기(type,Keyword가 적용된 count로 변경
        //int totalcount = (int)boardRepository.count();
        //--------------------------------------------------------
        //SEARCH
        //--------------------------------------------------------
        int totalcount=0;
        if(criteria!=null&& criteria.getType()!=null) {
            if (criteria.getType().equals("title"))
                totalcount = boardRepository.countWhereTitleKeyword(criteria.getKeyword());
            else if (criteria.getType().equals("username"))
                totalcount = boardRepository.countWhereUsernameKeyword(criteria.getKeyword());
            else if (criteria.getType().equals("content"))
                totalcount = boardRepository.countWhereContentKeyword(criteria.getKeyword());
        }
        else
            totalcount = (int)boardRepository.count();


        System.out.println("COUNT  :" + totalcount);

        //PageDto 만들기
        PageDto pagedto = new PageDto(totalcount,criteria);

        //시작 게시물 번호 구하기(수정) - OFFSET
        int offset =(criteria.getPageno()-1) * criteria.getAmount();    //1page = 0, 2page = 10

        //--------------------------------------------------------
        //SEARCH
        //--------------------------------------------------------
        List<Board> list = null;
        if(criteria.getType() != null) {
            if (criteria.getType().equals("title")) {
                list = boardRepository.findBoardTitleAmountStart(criteria.getKeyword(), pagedto.getCriteria().getAmount(), offset);
                System.out.println("TITLE SEARCH!");
                System.out.println(list);
            } else if (criteria.getType().equals("username"))
                list = boardRepository.findBoardUsernameAmountStart(criteria.getKeyword(), pagedto.getCriteria().getAmount(), offset);
            else if (criteria.getType().equals("content"))
                list = boardRepository.findBoardContentsAmountStart(criteria.getKeyword(), pagedto.getCriteria().getAmount(), offset);
            else if (criteria.getType().equals("none"))
                list = boardRepository.findBoardAmountStart(pagedto.getCriteria().getAmount(), offset);
        }
        else
            list  =  boardRepository.findBoardAmountStart(pagedto.getCriteria().getAmount(),offset);


        returns.put("list",list);
        returns.put("pageDto",pagedto);

        return returns;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public boolean addBoard(BoardDto dto) throws IOException, java.io.IOException {

        //--------------------------------
        System.out.println("upload File Count : " + dto);

        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setRegdate(LocalDateTime.now());
        board.setUsername(dto.getUsername());
        board.setCount(0L);

        board =    boardRepository.save(board);

        return boardRepository.existsById(board.getNo());
    }


    
    @Transactional(rollbackFor = Exception.class)
    public Board getBoardOne(Long no) {
        Optional<Board> board =    boardRepository.findById(no);
        return board.orElse(null);
    }



    //----------------------------------------------------------------
    //수정하기코드!
    //----------------------------------------------------------------
    
    @Transactional(rollbackFor = SQLException.class)
    public boolean updateBoard(BoardDto dto) throws IOException, java.io.IOException {

        //--------------------------------
        System.out.println("upload File Count : " +dto);

        Board board = new Board();
        board.setNo(dto.getNo());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setRegdate(LocalDateTime.now());
        board.setUsername(dto.getUsername());
        board.setCount(dto.getCount());

        //기존 정보 가져오기
        board =  boardRepository.save(board);


        return board!=null;

    }


    
//

    
    @Transactional(rollbackFor = SQLException.class)
    public boolean removeBoard(Long no) {
        //DB 삭제
        boardRepository.deleteById(no);
        return true;
    }

    //----------------------------------------------------------------
    // COUNT
    //----------------------------------------------------------------
    
    @Transactional(rollbackFor = SQLException.class)
    public void count(Long no) {
        Board board =  boardRepository.findById(no).get();
        board.setCount(board.getCount()+1);
        boardRepository.save(board);
    }

    @Transactional(rollbackFor = SQLException.class)
    public boolean addReply(Long bno, String content, UserDto userDto) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setRegdate(LocalDateTime.now());
        reply.setLikecount(0L);

        Board board =  boardRepository.findById(bno).get();
        reply.setBoard(board);
        User user = userRepository.findById(userDto.getUsername()).get();
        reply.setUser(user);
        replyRepository.save(reply);
        return true;
    }
    @Transactional(rollbackFor = SQLException.class)
    public List<Reply> findByReply(Board board) {
        return  replyRepository.findAllByBoardOrderByRnoDesc(board);
    }
}
