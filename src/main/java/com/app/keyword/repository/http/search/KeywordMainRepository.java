package com.app.keyword.repository.http.search;

import com.app.keyword.vo.http.search.KeywordMainVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface KeywordMainRepository extends JpaRepository<KeywordMainVo, Integer> {
    public List<KeywordMainVo> findByKeywordNmAndSearchDtBetween(String keywordNm, Date d1, Date d2);

    KeywordMainVo findFirstByTotCntBetweenAndBlogMains_TotCntIsNullOrderBySearchTimeAsc(int totCntStart, int totCntEnd);


}
