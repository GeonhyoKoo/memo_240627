package com.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.domain.Post;

@Mapper
public interface PostMapper {

	// input : x
	// output : List<Map<String, Object>>
	public  List<Map<String, Object>> selectPostList();
	
	
	// input : userId , 페이징정보
	// output : List<Post>
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId,
			@Param("standardId") Integer standardId,
			@Param("direction") String direction,
			@Param("limit") int limit
			);
	
	
	public int selectPostIdByUserIdAsSort(
			@Param("userId") int userId,
			@Param("sort") String sort);

	
	
	
	
	
	
	
	public int insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath
			);
	
	
	// input : postId, userId
	// output : Post or null
	public Post selectPostByPostIdAndUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId
			);
	
	
	
	// input : postId subject content imagePath
	// output : int or void
	public void updatePostByPostId(
			@Param("postId") int postId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath
			);	
	
	
	// delete
	public int deletePostByPostIdUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId
			);
	
	
}
