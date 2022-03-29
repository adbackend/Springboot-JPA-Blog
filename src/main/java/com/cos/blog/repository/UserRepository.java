package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;


// JpaRepository는 findall()는 함수를 들고 있어, User 테이블이 들고 있는 모든 행을 리턴
// DAO 와 비슷
// 자동으로 bean 등록이 된다
// @Repository 생략 가능
public interface UserRepository extends JpaRepository<User, Integer>{

}
