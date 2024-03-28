package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.Board;
import com.creator.imageAndMusic.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    //@Query(value = "SELECT * FROM CreatorDB.board b WHERE b.title LIKE %:keyWord%  ORDER BY b.no DESC LIMIT :amount OFFSET :offset", nativeQuery = true)
    User findUserByNicknameAndPhone(String nickname, String phone);
    User findUserByUsernameAndNicknameAndPhone(String username,String nickname, String phone);
}
