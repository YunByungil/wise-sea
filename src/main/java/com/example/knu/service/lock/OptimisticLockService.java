package com.example.knu.service.lock;

import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptimisticLockService {

    private final BoardPostRepository postRepository;

    @Transactional
    public void like(Long postId) {
//        BoardPost findPost = postRepository.findById(postId).get();
        BoardPost findPost = postRepository.findByIdWithOptimisticLock(postId);

        findPost.addLikeCount(1);
//        System.out.println("findPost.getLikeCount() = " + findPost.getLikeCount());
//        postRepository.save(findPost);

    }
}
