package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.service.LikeService;
import com.example.knu.service.NoticeService;
import com.example.knu.service.OptimisticLockFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class LikeController {

    private final OptimisticLockFacade optimisticLockFacade;
    private final LikeService likeService;
    private final NoticeService noticeService;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/api/user/boards/categories/{postid}/like")
    public ResponseEntity<Void> likeNotice(Principal principal,
                                             @PathVariable Long postid) throws InterruptedException {

//        Response response = noticeService.likeNotice(principal, postid);
//        Response response = likeService.like(principal, postid);
        likeService.like(principal, postid);
//        Response response = optimisticLockFacade.like(principal, postid);

        return ResponseEntity.ok().build();
    }
}
