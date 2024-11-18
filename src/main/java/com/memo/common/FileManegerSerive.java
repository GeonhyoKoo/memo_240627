package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileManegerSerive {
	
	// 실제 업로드 된 이미지가 저장될 서버 경로  마지막에 꼭 /를 넣어야한다.
	public static final String FILE_UPLOAD_PATH = "/Users/geonhyo/koo/5_spring_project/workspace/images/";
	
	// input: MultipartFile , userLoginId
	// output:  String(이미지경로)
	public String uploadFile(MultipartFile file , String loginId) {
		// 폴더(디렉토리) 생성
		// 예 : aaaa_172391023/sun.png
		String directoryName =  loginId + "_" + System.currentTimeMillis(); 
		
		// /Users/geonhyo/koo/5_spring_project/workspace/images/aaaa_172391023/
		String filePath = FILE_UPLOAD_PATH + directoryName + "/"; 
		
		
		// 폴더 생성
		File directory = new File(filePath);
		if( directory.mkdir() == false) { // mkdir -> make directory
			return null; // 폴더 생성시 실패하면 경로를 null로 리턴하고 에러 아니게 처리를 한다. 사진 업로드가 안되었다고 bo에서 내용도 insert가 안되면 안되니까
		}
		
		
		// 파일 업로드
		try {
			byte[] bytes =  file.getBytes();
			// !!!!!!!!!!!!!! 파일명에 한글이 들어가면 안됨 !!!!!!!!!
			// 나중에 파일명을 영문자로 변경해야함. 한글명은 업로드가 불가하기 때문
			Path path = Paths.get(filePath + file.getOriginalFilename());
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null; // 이미지 업로드 시 실패하면 경로를 null로 리턴 에러 아니게 처리 , bo는 정상적으로 수행하기 위해
		}
		
		// 파일업로드가 성공하면 이미지 url path를 리턴
		// 주소는 이렇게 될 것이다. 예언
		// // 예 : /images/aaaa_172391023/sun.png
		
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
	}
	
	
	
	// 업로드 된 이미지를 서버(컴퓨터)에서 삭제
	// input : db에 저장된 imagePath
	// output : x
	public void deleteFile(String imagePath) {
		
		// /Users/geonhyo/koo/5_spring_project/workspace/images/aaaa_1730975988991/user_desc.png
		
		// /Users/geonhyo/koo/5_spring_project/workspace/images//images/aaaa_1730975988991/user_desc.png
		//   /images/ 가 겹치므로 제거해야한다.
		
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", "")); // replace로 /image/ 를 삭제해서 위치를 만든다.
		
		// 삭제할 이미지가 그 위치에 있는가?
		if (Files.exists(path)){
			// 이미지 삭제
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[파일매니저 파일삭제] imagePath:{}" , imagePath);
				return;
			}
			// 그 폴더(디렉토리) 삭제
			path = path.getParent();
			if (Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					log.info("[파일매니저 디렉토리삭제] imagePath:{}" , imagePath);
				}
			}
		}
		
		
		
	}
	
	
}
