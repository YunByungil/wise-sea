package com.example.knu.service.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LockController {

    private final PessimisticLockService service;
//    private final OptimisticLockFacade service;

    @PostMapping("/api/all/{postId}/like")
    public String postLike(@PathVariable Long postId) throws InterruptedException {
        service.like(postId);
        return "성공";
    }
}
