package com.pjh.plusproject.Like.Entity;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Global.Common.BaseEntity;
import com.pjh.plusproject.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fromMemberId")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "toMemberId")
    private Member toMember;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;
}
