package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {

	@Autowired
	private PostBO postBO;
	
	/**
	 * 글작성 API
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value= "file" , required=false) MultipartFile file,
			HttpSession session
			){
		
		// 글쓴이 번호 가져오기
		int userId =  (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId"); // 폴더명에 사용하기 위해
		
		
		// db insert
		int rowCount =  postBO.addPost(userId , userLoginId, subject, content, file);
		
		// 응답값
		Map<String , Object> result = new HashMap<>();
		if (rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "글을 저장하는데 실패했습니다. 관리자에게 문의해주세요");
		}
		return result;
		
	}
	
	
	/**
	 * 글 수정하기
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file" , required = false) MultipartFile file,
			HttpSession session
			){
		
		// 세션 -> userId(DB) userLoginId(파일업로드)
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// db 업데이트 + 파일 업로드
		postBO.updatePostByPostIdUserId(userLoginId, postId, userId, subject, content, file);
		
		// response
		Map<String , Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
	
	
	@DeleteMapping("/delete")
	public Map<String , Object> delete(
			@RequestParam("postId") int postId,
			HttpSession session
			){
		Map<String, Object> result = new HashMap<>();
		
		// 로그인 정보 확인
		Integer userId = (Integer)session.getAttribute("userId");
		
		if(userId == null) {
			result.put("code", 403);
			result.put("error_message", "로그인 후 사용 가능합니다.");
		}
		
		postBO.deletePostByPostIdUserId(postId, userId);
		
		
		result.put("code", 200);
		result.put("result", "성공");
		
		return result;
	}
	
	
	
}