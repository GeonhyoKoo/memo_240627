package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManegerSerive;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j  // slf4j logger 아래 코드와 같음
@Service
public class PostBO {

	//private Logger log = LoggerFactory.getLogger(PostBO.class);
	//private Logger log = LoggerFactory.getLogger(this.getClass()); // 위와 같은 클래
	
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManegerSerive fileManeger;
	
	
	// 페이징 정보 필드 (limit)
	private static final int POST_MAX_SIZE = 3;
	
	
	public List<Post> getPostListByUserId(int userId , Integer prevId, Integer nextId){
		
		// 10 9 8 || 7 6 5 || 4 3 2 || 1
		// 만약 4 3 2 페이지에 있을 떄
		// 1) 다음 (nextId 가 있음) : 2보다 작은 3개 desc 정렬
		// 2) 이전 (prevId 가 있음) : 4보다 큰거 3개 asc로 가져온 5 6 7 을 다시 뒤집어서 7 6 5 로 리스트를 reverse해준다.
		// 3) 페이징 없음 (nextId , prevId 둘다 없음) : 최신순 3개 desc 가져오기
		
		
		// 3개의 쿼리를 만드는게 더 좋지만 하나로도 가능하긴함. 지금은 하나로 굳이하는것
		// xml 에서 하나의 쿼리로 만들기 위해 변수를 정제한다.
		Integer standardId = null; // 기준 id (prev or next)
		String direction = null; // 방향
		
		if (prevId != null) { // 2.이전
			standardId = prevId;
			direction = "prev";
			List<Post> postList = postMapper.selectPostListByUserId(userId , standardId, direction , POST_MAX_SIZE);
			
			// 5 6 7
			// 7 6 5 가 되도록
			Collections.reverse(postList); // 뒤집고 순서 저장 , void 타입이니까 자동 저장
			return postList;
			
		} else if (nextId != null) { // 1.다음
			standardId = nextId;
			direction = "next";
		}
		
		//3. 페이징 없음
		// 또는 1번으로 들어오는 경우
		return postMapper.selectPostListByUserId(userId , standardId, direction , POST_MAX_SIZE);
	}
	
	
		// 이전 페이지의 마지막인가?
		public boolean isPrevLastPageByUserId(int userId, int prevId) {
			int maxPostId = postMapper.selectPostIdByUserIdAsSort(userId, "desc");
			return maxPostId == prevId;
		}
		
		// 다음 페이지의 마지막인가?
		public boolean isNextLastPageByUserId(int userId, int nextId) {
			int minPostId = postMapper.selectPostIdByUserIdAsSort(userId, "asc");
			return minPostId == nextId;
		}
	
	
	
	// input : userId,userLoginId, subject, content, file 
	// output : int(성공한 행 개수)
	public int addPost(int userId, String userLoginId,
			String subject, String content, MultipartFile file) {
		
		String imagePath = null;
		
		// file to imagePath
		// file 이 있을 떄만 업로드 -> imagePath
		if (file != null) {
			imagePath = fileManeger.uploadFile(file, userLoginId);
		}
		
		return postMapper.insertPost(userId, subject, content , imagePath);
		
	}
	
	
	public Post getPostByPostIdAndUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdAndUserId(postId, userId);
	}
	
	
	// input : userLoginId(업로드용) , postId, userId, subject, content , file
	// output : x
	public void updatePostByPostIdUserId(String loginId ,int postId, int userId , String subject , String content, MultipartFile file) {
		
		// 기존 글을 가져온다. 1. 이미지 교체 시 기존 이미지 삭제를 위해 2. 업데이트 대상의 존재유무 확인
		Post post = postMapper.selectPostByPostIdAndUserId(postId, userId);
		if (post == null) {
			log.info("[###글 수정] post is null. posiId:{}", postId , "userId:{}" , userId);
			return;
		}
		
		
		log.info("[###글 수정] post is null. posiId:{}", postId , "userId:{}" , userId);

		// 파일이 존재하면 새 이미지 업로드
		String imagePath = null;
		if (file != null) {
			// 새 이미지 업로드 
			// 업로드가 성공하면 기존 이미지는 삭제
			imagePath = fileManeger.uploadFile(file, loginId);
			
			// 업로드 성공 && 기존 이미지 존재 시 삭제
			if(imagePath != null && post.getImagePath() != null) {
				// 폴더 , 이미지 제거(컴퓨터-서버에서)
				fileManeger.deleteFile(post.getImagePath());
			}
		}
		
		// DB update
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
		
	}
	
	
	
	// delete
	public void deletePostByPostIdUserId(int postId , int userId) {
		
		// 기존 글 가져오기
		Post post = postMapper.selectPostByPostIdAndUserId(postId, userId);
		if(post == null) {
			log.info("[글 삭제 조회된 글 가져오기] postId:{}" , postId , "userId : {}" , userId);
			return;
		}
		
		// 
		int rowCount = postMapper.deletePostByPostIdUserId(postId, userId);
		
		if (rowCount > 0 && post.getImagePath() != null) {
			// db 삭제 완료 && 기존글 이미지 존재 -> 이미지 삭제
			fileManeger.deleteFile(post.getImagePath());
		}
		
	}
	
	
	
	/// summernote test
	
	public void addPostSummerNotd(String content, String subject, int id, String[] fileName, String loginId) {
		
		
		if (fileName.length != 0) {
			
			for (int i = 0; i <fileName.length; i++) {
				fileManeger.uploadSummerNoteFile(fileName[i], loginId);
			}
			
		}
		
		int t=  3;
		
		
	}
	
	
	
}
