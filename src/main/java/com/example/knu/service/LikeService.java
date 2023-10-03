package com.example.knu.service;

import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.Like;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.*;
import com.example.knu.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final BoardPostRepository boardPostRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public String like(Principal principal, Long postid) {
        Optional<BoardPost> foundBoardPost = boardPostRepository.findById(postid);
        if (foundBoardPost.isEmpty()) throw new CommonException("해당 공지사항이 존재하지 않습니다.");
        BoardPost boardPost = foundBoardPost.get();
//        BoardPost boardPost = boardPostRepository.findByIdWithOptimisticLock(postid);
        log.info("좋아요 기능");
        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();

//        likeRepository.findByUserAndBoardPost(user, boardPost)
//                .ifPresentOrElse(cancelLike(), clickLike(user, boardPost));
//        boardPost.addLikeCount(1);
        Optional<Like> foundLike = likeRepository.findByUserAndBoardPost(user, boardPost);
//        likeRepository.save(Like.builder()
//                .boardPost(boardPost)
//                .user(user)
//                .build());
//        Optional<Like> foundLike = likeRepository.findByUserAndBoardPost(user, boardPost)
//                .ifPresentOrElse(cancelLike(), clickLike(user, boardPost));

        if (foundLike.isEmpty()) {
            Like like = Like.builder()
                    .user(user)
                    .boardPost(boardPost)
                    .build();
            likeRepository.save(like);
            boardPost.addLikeCount(1);
//            return Response.success(null);
            return "좋아요!";
        } else {
            likeRepository.deleteByUserAndBoardPost(user, boardPost);
            boardPost.addLikeCount(-1);
            return "좋아요 취소!";
        }
//        return Response.success(null);
    }

    private Consumer<Like> cancelLike() {
        log.info("좋아요 취소 메서드입니다.");
        return likeRepository::delete;
    }
//    private Consumer<Like> cancelLike() {
//        log.info("좋아요 취소 메서드입니다.");
//        return likeRepository::delete;
//    }

    private Runnable clickLike(User user, BoardPost boardPost) {
        log.info("좋아요 추가입니다.");
        return () -> likeRepository.save(Like.builder()
                .user(user)
                .boardPost(boardPost)
                .build());
    }
//    private Runnable clickLike(User user, BoardPost boardPost) {
//        log.info("좋아요 추가입니다.");
//        System.out.println("boardPost = " + boardPost.getLikeCount());
////        boardPost.addLikeCount(1);
//        return () -> likeRepository.save(Like.builder()
//                .user(user)
//                .boardPost(boardPost)
//                .build());
//    }
}
