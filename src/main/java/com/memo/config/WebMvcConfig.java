package com.memo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManegerSerive;
import com.memo.common.ImageUpload;
import com.memo.interceptor.PermissionInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration // 설정 관련 스프링빈
public class WebMvcConfig implements WebMvcConfigurer{

	
	private final PermissionInterceptor interceptor;
	// 인터셉터 설정
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(interceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**" , "/img/**" , "/images/**" , "/user/sign-out"); // 제외시실것
	}
	
	
	
	
	
	// 예언된 이미지 path와  서버에 업로드 된 실제 이미지와 매핑 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/temp/**") // path  http://localhost/images/aaaa_42342432432/
		.addResourceLocations("file://" + ImageUpload.FILE_TEMP_UPLOAD_PATH);
		
		registry
		.addResourceHandler("/image/**") // path  http://localhost/images/aaaa_42342432432/
		.addResourceLocations("file://" + FileManegerSerive.FILE_UPLOAD_PATH);
		
	}
	
}

// file:// 맥은 두개 윈도우는 /// 세개