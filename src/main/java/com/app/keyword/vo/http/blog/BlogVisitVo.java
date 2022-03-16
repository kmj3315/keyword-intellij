package com.app.keyword.vo.http.blog;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "blog_visit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogVisitVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_visit_sqno", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_board_sqno")
    private BlogBoardVo blogBoardVo;

    @Column(name = "visit_cnt")
    private Integer visitCnt;

    @Column(name = "visit_date")
    private Date visitDate;

    @Column(name = "reg_date" , insertable = false, updatable = false)
    private LocalDate regDate;

    @Builder
    public BlogVisitVo( BlogBoardVo blogBoardVo, Integer visitCnt, Date visitDate) {
        this.blogBoardVo = blogBoardVo;
        this.visitCnt = visitCnt;
        this.visitDate = visitDate;
    }
}