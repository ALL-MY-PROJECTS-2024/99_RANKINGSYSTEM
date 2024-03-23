package com.creator.imageAndMusic.domain.service;

import com.creator.imageAndMusic.domain.dto.BoardDto;
import com.creator.imageAndMusic.domain.dto.Criteria;
import com.creator.imageAndMusic.domain.entity.Board;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface BoardService {
    //모든 게시물 가져오기
    @Transactional(rollbackFor = Exception.class)
    Map<String, Object> GetBoardList(Criteria criteria);

    @Transactional(rollbackFor = Exception.class)
    boolean addBoard(BoardDto dto) throws IOException;

    @Transactional(rollbackFor = Exception.class)
    Board getBoardOne(Long no);

    //----------------------------------------------------------------
    //수정하기코드!
    //----------------------------------------------------------------
    @Transactional(rollbackFor = SQLException.class)
    boolean updateBoard(BoardDto dto) throws IOException;

    @Transactional(rollbackFor = SQLException.class)
    boolean removeFile(String no, String filename);

    @Transactional(rollbackFor = SQLException.class)
    boolean removeBoard(Long no);

    //----------------------------------------------------------------
    // COUNT
    //----------------------------------------------------------------
    @Transactional(rollbackFor = SQLException.class)
    void count(Long no);
}
