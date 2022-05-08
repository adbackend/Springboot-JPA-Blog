package com.cos.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;

@RestController
public class BoardApiController {

	@Autowired
	private BoardService boardService;

	//글쓰기
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {

		boardService.글쓰기(board, principal.getUser());
		return new ResponseDto<>(HttpStatus.OK.value(),1);
	}

	//글삭제
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id){

		System.out.println("id오냐...."+id);
		boardService.글삭제하기(id);

		return new ResponseDto<>(HttpStatus.OK.value(),1); // 1리턴하면 정상
	}

	//글수정
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board){

		System.out.println(id);
		System.out.println(board.getTitle());
		System.out.println(board.getContent());

		boardService.글수정하기(id, board);

		return new ResponseDto<>(HttpStatus.OK.value(),1);
	}


}
