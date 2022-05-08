package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로 /auth/** 허용
// 그냥 주소가 / 이면 index.jsp 허용
// static이하에 있는 /js/**, /css/**, /image/**
@Controller
public class UserController {

	@Value("${cos.key}")
	private String cosKey;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@GetMapping("/auth/joinForm")
	public String joinForm() {

		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {

		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}

	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { // @ResponseBody -> Data를 리턴해주는 컨트롤러함수

		// POST 방식으로 key=value 데이터를 요청(카카오쪽으로)
		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "765868ff055742f25202f6e5acdc34ee");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);

		// Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response = rt.exchange(
					"https://kauth.kakao.com/oauth/token",
					HttpMethod.POST,
					kakaoTokenRequest,
					String.class
				);

		// JSON, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		OAuthToken oauthToken = null;

		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("카카오 엑세스 토큰: " + oauthToken.getAccess_token());


		// POST 방식으로 key=value 데이터를 요청(카카오쪽으로)
		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization","Bearer "+oauthToken.getAccess_token()); // Bearer뒤 공백 필수!!!
		headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2);

		// Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response2 = rt2.exchange(
					"https://kapi.kakao.com/v2/user/me",
					HttpMethod.POST,
					kakaoProfileRequest2,
					String.class
				);

		ObjectMapper objectMapper2 = new ObjectMapper();
		objectMapper2.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		KakaoProfile kakaoProfile = null;

		System.out.println("으아아악!!!!"+response2.getBody());

		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// User오브젝트 : username, password, email
		System.out.println("카카오 아이디 " + kakaoProfile.getId());
		System.out.println("카카오 이메일 "+ kakaoProfile.getKakao_account().getEmail());

		System.out.println("블로그서버 - 카카오 유저네임: " + kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
		System.out.println("블로그서버 이메일: "+kakaoProfile.getKakao_account().getEmail());
//		UUID garbagePassword = UUID.randomUUID(); // 임시 패스워드- 쓰레기 패스워드
		// UUID란->중복되지 않는 어떤 특정값을 만들어 내는 알고리즘
		System.out.println("블로그서버 패스워드: " + cosKey);

		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();

		//가입자 혹은 비가입자 체크해서 처리
		User originUser = userService.회원찾기(kakaoUser.getUsername());

		if(originUser.getUsername() == null) {
			System.out.println("기존이 아닙니다------ 자동회원가입.");
			userService.회원가입(kakaoUser);
		}

		System.out.println("기존회원입니다....자동로그인 진행");
		//로그인 처리
		//세션등록
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/";
	}

}





