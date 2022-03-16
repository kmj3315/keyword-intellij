package com.app.keyword.repository.http.search;

import com.app.keyword.vo.http.search.KeywordRelationVo;
import com.app.keyword.vo.http.search.KeywordSubVo;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRelationRepository extends JpaRepository<KeywordRelationVo, String> {
    KeywordRelationVo findByKeywordNm(String keywordNm);

    KeywordRelationVo findFirstByRegYnOrderByCreateDtAsc(String regYn);





}