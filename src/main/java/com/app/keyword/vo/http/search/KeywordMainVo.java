package com.app.keyword.vo.http.search;

import com.app.keyword.vo.http.blog.BlogMainVo;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "keywordMain")
public class KeywordMainVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_main_sqno", nullable = false)
    private int keywordMainSqno;



    @Column(name = "pc_cnt")
    private Integer pcCnt;

    @Column(name = "mb_cnt")
    private Integer mbCnt;

    @Column(name = "search_dt", insertable=false, updatable=false)
    private LocalDate searchDt;


    @Column(name = "use_yn")
    private String useYn;


    @Column(name = "keyword_nm")
    private String keywordNm;

    @Column(name = "search_time", insertable=false, updatable=false)
    private Instant searchTime;

    @Column(name = "tot_cnt")
    private Integer totCnt;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "keywordMainVo")
    private BlogMainVo blogMains;

    @Builder
    public KeywordMainVo(String keywordNm, int pcCnt, int mbCnt){
        this.keywordNm = keywordNm;
        this.pcCnt = pcCnt;
        this.mbCnt = mbCnt;
        this.totCnt = pcCnt + mbCnt;
    }
}
