package com.app.keyword.vo.http.blog;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Table(name = "blog_board")
@NoArgsConstructor
@AllArgsConstructor
public class BlogBoardVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_board_sqno", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_main_sqno")
    private BlogMainVo blogMainVo;

    @Column(name = "blog_title")
    private String blogTitle;

    @Column(name = "blog_link")
    private String blogLink;

    @Column(name = "blog_name")
    private String blogName;

    @Column(name = "post_date")
    private Date postDate;

    @Column(name = "blog_id")
    private String blogId;

    @Column(name = "blog_visit_avg")
    private Integer blogVisitAvg;

    @Column(name = "reg_date", insertable = false, updatable = false)
    private LocalDate regDate;

    @Builder
    public BlogBoardVo(BlogMainVo blogMainVo, String blogTitle, String blogLink, String blogName, Date postDate, String blogId, Integer blogVisitAvg) {
        this.blogMainVo = blogMainVo;
        this.blogTitle = blogTitle;
        this.blogLink = blogLink;
        this.blogName = blogName;
        this.postDate = postDate;
        this.blogId = blogId;
        this.blogVisitAvg = blogVisitAvg;
    }


}