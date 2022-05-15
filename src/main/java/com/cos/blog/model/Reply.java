package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.blog.dto.ReplySaveRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트 연결된 DB의 넘버링 전략을 따라간다
	private int id ;

	@Column(nullable = false, length = 200)
	private String content;

	@ManyToOne // 여러개의 답변이 있을수 있다, 하나의 게시글에
	@JoinColumn(name="boardId")
	private Board board;

	@ManyToOne // 여러개의 답변을, 하나의 유저가 쓸 수 있다
	@JoinColumn(name="userId")
	private User user;

	@CreationTimestamp
	private Timestamp createDate;
	
	public void update(User user, Board board, String content) {
		setUser(user);
		setBoard(board);
		setContent(content);
	}

	@Override
	public String toString() {
		return "Reply [id=" + id + ", content=" + content + ", board=" + board + ", user=" + user + ", createDate="
				+ createDate + "]";
	}
	
	
}
