package com.cos.blog.model;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//ORM - Object -> 테이블로 매핑해주는 기술
@Data
@NoArgsConstructor //빈생성자
@AllArgsConstructor //전체생성자
@Builder //빌더패턴
@Entity // User 클래스가 MySQL에 테이블이 생성된다
//@DynamicInsert  //insert시에 null이 빌드를 제외시켜준ㅌ다
public class User {
	
	@Id //primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에 연결된 db의 넘버링 전력을 따라간다. ex) 오라클-시퀀스, mysql-auto_increment
	private int id; //시퀀스, auto_increment
	
	@Column(nullable=false, length=30, unique=true)
	private String username; //아이디
	
	@Column(nullable=false, length=100) // 해쉬로 비밀번호를 데이터베이스에 넣을거기 때문에 넉넉하게 100자로 설정함
	private String password; 
	
	@Column(nullable=false, length=50)
	private String email;
	
//	@ColumnDefault(" 'user' ") //문자라는 것을 알려주기 위해 ' ' 
	@Enumerated(EnumType.STRING)// DB는 RoleType 이라는게 없다
	private RoleType role; // Enum을 쓰는게 좋다. // ADMIN, USER, MANAGER 등등..
	
	@CreationTimestamp //시간이 자동 입력
	private Timestamp createDate;

}
