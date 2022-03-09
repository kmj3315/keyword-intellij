package com.app.keyword.vo.http.search;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "keywordMain")
public class KeywordMainVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int keywordMainSqno;
    private int pcCnt;
    private int mbCnt;
    private Date searchDt;
    private String exStatus;
    private String keywordNm;

    @Builder
    public KeywordMainVo(String keywordNm, int pcCnt, int mbCnt){
        this.keywordNm = keywordNm;
        this.pcCnt = pcCnt;
        this.mbCnt = mbCnt;
    }
}
