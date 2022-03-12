package com.app.keyword.vo.http.search;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Data
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "keywordSub")
public class KeywordSubVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int keywordSubSqno;

    private int keywordMainSqno;
    private int pcCnt;
    private int mbCnt;
    private Date searchDt;

    private String keywordNm;
    private Date searchTime;
    private String regYn;


    @ManyToOne
    @JoinColumn(name = "keywordMainSqno", insertable = false, updatable = false)
    private KeywordMainVo keywordMainVo;


    @Builder
    public KeywordSubVo(String keywordNm, int pcCnt, int mbCnt){
        this.keywordNm = keywordNm;
        this.pcCnt = pcCnt;
        this.mbCnt = mbCnt;
    }
}
