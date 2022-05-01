package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;


// JpaRepository는 findall()는 함수를 들고 있어, User 테이블이 들고 있는 모든 행을 리턴
// DAO 와 비슷
// 자동으로 bean 등록이 된다
// @Repository 생략 가능
public interface UserRepository extends JpaRepository<User, Integer>{
	
	//JPA Naming 전략
//	 SELECT * FROM user WHERE username= ?1 AND password=?2;
	User findByUsernameAndPassword(String username, String password);
	
//	@Query(value="SELECT * FROM user WHERE username=?1 AND password =?2", nativeQuery = true)
//	User login(String username, String password);

}
