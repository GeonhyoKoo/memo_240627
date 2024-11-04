package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.common.EncryptUtils;
import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;

@Service
public class UserBO {

	@Autowired
	private UserRepository userRepository;
	
	// 컨트롤러한테
	// input : loginId
	// output : UserEntity or null 단건일 땐 항상 있거나 널이거나
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	
	// input : 파라미터 4
	// output : UserEntity
	public UserEntity addUser(String loginId, String password , String name, String email) {
		
		return userRepository.save(
				UserEntity.builder().loginId(loginId).password(password).name(name).email(email).build()
				);
	}
	
	
	
	// input : loginId, password
	// output :  UserEntity
	public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
		
		// 비밀번호 해싱
		String hashedPassword =  EncryptUtils.md5(password);
		
		// 조회
		return userRepository.findByLoginIdAndPassword(loginId , hashedPassword);
	}
	
	
}
