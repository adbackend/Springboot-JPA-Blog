package com.cos.blog.test;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;


// RestContoller : html 파일이 아니라 데이터를 리턴해주는 controller 
@RestController
public class DummyControllerTest {
	
	@Autowired //의존성 주입(DI)
	private UserRepository userRepository;
	
	// {id} 주소로 파라미터를 전달 받을 수 있다
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		// user/4을 찾으면 내가 데이터베이스에서 못찾아오게 되면 user가 null이 될 것 아냐?
		// 그럼 return 할때 null이 되잖아.. 그럼 프로그램에 문제가 잇지 않겠니?
		// 그래서 나는 Optional로 너의 User 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return 해!!
//		User user = userRepository.findById(id).get(); // .get() null 일리가 없어!

//      방법1)람다식
//		User user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당하숑자는 없습니다")
//		});

		//방법2
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		
		// 요청 : 웹브라우저
		// user객체 = 자바오브젝트
		// 변환 (웹 브라우저가 이해할 수 있는 데이터로 변환해야됨) -> json(Gson 라이브러리)
		// 스프링부트는 MessageConverter 라는 얘가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json이라는 변환해서 브라우저에게 던져준다
		return user;
	}
	
	@PostMapping("/dummy/join")
	public String join(User user) {
		
		System.out.println("id: "+user.getId());
		System.out.println("username: "+user.getUsername());
		System.out.println("email: "+user.getEmail());
		System.out.println("role: "+user.getRole());
		System.out.println("createDate: "+user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		
		return "회원가입이 완료되었습니다.";
	}
	
	
	
}
