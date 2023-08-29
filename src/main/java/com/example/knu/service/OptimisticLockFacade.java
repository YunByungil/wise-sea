package com.example.knu.service;

import com.example.knu.common.Response;
import com.example.knu.service.lock.OptimisticLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class OptimisticLockFacade {

    private final LikeService likeService;

    public Response like(Principal principal, Long postId) throws InterruptedException {
        while (true) {
            try {
                likeService.like(principal, postId);

                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
        return Response.success(null);
    }
}
