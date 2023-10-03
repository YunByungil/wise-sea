package com.example.knu.domain.entity;

import com.example.knu.domain.entity.board.BoardPost;
import com.example.knu.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
//@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "user_board_post_unique",
                columnNames = {
                        "user_id",
                        "board_post_id"
                }
        )
})
@Getter
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_post_id")
    private BoardPost boardPost;

    @Builder
    public Like(User user, BoardPost boardPost) {
        this.user = user;
        this.boardPost = boardPost;
    }
}
