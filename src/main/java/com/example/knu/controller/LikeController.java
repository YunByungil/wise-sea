package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.service.NoticeService;
import com.example.knu.service.OptimisticLockFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class LikeController {

    private final OptimisticLockFacade optimisticLockFacade;
    private final NoticeService noticeService;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/api/user/boards/categories/{postid}/like")
    public Response likeNotice(Principal principal,
                               @PathVariable Long postid) throws InterruptedException {

//        Response response = noticeService.likeNotice(principal, postid);
        Response response = optimisticLockFacade.like(principal, postid);

        return response;
    }
}
