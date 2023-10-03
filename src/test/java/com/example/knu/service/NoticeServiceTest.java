package com.example.knu.service;

import com.example.knu.domain.entity.board.Board;
import com.example.knu.domain.entity.board.BoardCategory;
import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.repository.BoardCategoryRepository;
import com.example.knu.domain.repository.BoardPostRepository;
import com.example.knu.domain.repository.BoardRepository;
import com.example.knu.domain.repository.LikeRepository;
import com.example.knu.service.lock.PessimisticLockService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class NoticeServiceTest {

    @Autowired
    PessimisticLockService pessimisticLockService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardCategoryRepository boardCategoryRepository;

    @Autowired
    BoardPostRepository boardPostRepository;

    @Autowired
    LikeRepository likeRepository;

    BoardPost post;
    @BeforeEach
    void setUp() {
        String boardCommunity = "community";
        Board community = createBoard(boardCommunity);

        String free = "free";
        BoardCategory createCategoryByFree = createCategory(free, 2, "자유", community);

        post = boardPostRepository.save(BoardPost.builder()
                .title("자유")
                .contents("자유")
                .boardCategory(createCategoryByFree)
                .build());
    }

    @DisplayName("좋아요 개수 동시성 테스트")
    @Test
//    @Rollback(value = false)
    void findAllPostByCategory() throws InterruptedException {
        // given
//        BoardPost findPost = boardPostRepository.findById(post.getId()).get();
//        BoardPost findPost = boardPostRepository.findByIdWithPessimisticLock(post.getId());
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockService.like(post.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // when
        BoardPost findPost = boardPostRepository.findById(post.getId()).get();

        // then
        assertThat(findPost.getLikeCount()).isEqualTo(100);

    }


    private Board createBoard(String name) {
        Board createBoardCommunity = Board.builder()
                .name(name)
                .priority(1)
                .build();

        Board savedBoard = boardRepository.save(createBoardCommunity);

        return savedBoard;
    }

    private BoardCategory createCategory(String name, int priority, String description, Board board) {
        BoardCategory boardCategory = BoardCategory.builder()
                .name(name)
                .priority(priority)
//                .description(description)
                .board(board)
                .build();

        BoardCategory savedCategory = boardCategoryRepository.save(boardCategory);

        return savedCategory;
    }

}