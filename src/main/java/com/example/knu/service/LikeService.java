package com.example.knu.service;

import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.Like;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.*;
import com.example.knu.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final BoardPostRepository boardPostRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Response like(Principal principal, Long postid) {
//        Optional<BoardPost> foundBoardPost = boardPostRepository.findById(postid);
//        if (foundBoardPost.isEmpty()) throw new CommonException("해당 공지사항이 존재하지 않습니다.");
//        BoardPost boardPost = foundBoardPost.get();
        BoardPost boardPost = boardPostRepository.findByIdWithOptimisticLock(postid);
        System.out.println("좋아요 기능");
        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();

        Optional<Like> foundLike = likeRepository.findByUserAndBoardPost(user, boardPost);

        if (foundLike.isEmpty()) {
            Like like = Like.builder()
                    .user(user)
                    .boardPost(boardPost)
                    .build();
            likeRepository.save(like);
            boardPost.addLikeCount(1);
            return Response.success(null);
        }

        likeRepository.deleteByUserAndBoardPost(user, boardPost);
        boardPost.addLikeCount(-1);
        return Response.success(null);
    }
}
