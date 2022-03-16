package com.app.keyword.vo.http.blog;

import com.app.keyword.vo.http.search.KeywordMainVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "blog_main")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogMainVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_main_sqno", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywrod_main_sqno")
    private KeywordMainVo  keywordMainVo;

    @Column(name = "tot_cnt")
    private Integer totCnt;

    @Column(name = "reg_date", insertable = false, updatable = false)
    private LocalDate regDate;



    @Builder
    public BlogMainVo(Integer totCnt) {
        this.totCnt = totCnt;
    }


}