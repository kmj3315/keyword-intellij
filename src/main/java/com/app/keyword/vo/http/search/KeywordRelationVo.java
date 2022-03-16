package com.app.keyword.vo.http.search;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity(name = "keywordRelation")
@DynamicUpdate
public class KeywordRelationVo {

    @Id
    private String keywordNm;

    private int pcCnt;
    private int mbCnt;
    private int totCnt;

    private Date searchDt;
    private Date searchTime;
    @Column(insertable=false, updatable=false)
    private Date createDt;
    private String regYn;

    @Builder
    public KeywordRelationVo(String keywordNm, int pcCnt, int mbCnt, String regYn){
        this.keywordNm = keywordNm;
        this.pcCnt = pcCnt;
        this.mbCnt = mbCnt;
        this.totCnt = pcCnt + mbCnt;
        Date date = new Date();

        this.searchDt = date;
        this.searchTime = date;
        this.regYn = regYn;


    }


}
