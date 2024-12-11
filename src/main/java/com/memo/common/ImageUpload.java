package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/upload/*")
@RestController
public class ImageUpload {

	public static final String FILE_TEMP_UPLOAD_PATH = "/Users/geonhyo/koo/5_spring_project/workspace/temp/";
	
	
	@RequestMapping("/addImg")
	public Map<String , Object> uploadSummerNoteImage(
			@RequestParam("file") MultipartFile file,
			HttpSession session
			) {
		
		Map<String , Object> result = new HashMap<>();
		
		
		// session 에 저장된 로그인 아이디
		String loginId = (String)session.getAttribute("userLoginId");
		if (loginId == null) {
			result.put("code", 400);
			return result;
		}
		
		// 업로드된 파일의 원본 파일명과 확장자 추출
		String originalFileName = file.getOriginalFilename();
		// 확장자
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		// 확장자 체크
		if ( !(extension.equals(".jpg")) && !(extension.equals(".png")) && !(extension.equals(".gif"))) {
			result.put("code", 500);
			result.put("error_message", "확장자 오류");
			return result; 
		}
		
		// 폴더명
		String directoryName =  loginId + "_" + System.currentTimeMillis(); 
		// 경로
		String filePath = FILE_TEMP_UPLOAD_PATH + directoryName + "/"; 
		
		
		//폴더 생성
		File directory = new File(filePath);
		if( directory.mkdir() == false) {
			result.put("code", 500);
			result.put("error_message", "폴더 생성 실패");
			return result; 
		}
	    
	    
		// 파일 업로드
		try {
			byte[] bytes =  file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename());
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
			result.put("code", 500);
			result.put("error_message", "파일 업로드 실패");
			return null;
		}
		
		result.put("code", 200);
		String url = "/temp/" + directoryName + "/" + file.getOriginalFilename();
		result.put("url", url);
		return result;
	}
	
	
	
	
}
