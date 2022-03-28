package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//ORM - Object -> 테이블로 매핑해주는 기술
@Data
@NoArgsConstructor //빈생성자
@AllArgsConstructor //전체생성자
@Builder //빌더패턴
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob //대용량 데이터
	private String content; // 썸머노트 라이브러리 <html>태그가 섞여서 디자인 됨
	
	@ColumnDefault("0") // int값 이기때문에 ' '가 없어도 된다
	private int count; //조회수
	
	@ManyToOne(fetch = FetchType.EAGER)// (board=many, user=one) 여러개의 Board게시글은 한명의user에 의해 쓰인다
	@JoinColumn(name="userId")
	private User user;
	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER) //하나의 게시글은,여러개의 답변을 가지고 있다.  
	//mappedBy-연관관계 주인이아니다.(나는 fk가 아니에요) db에 컬럼을 만들지 마세요
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;
}
