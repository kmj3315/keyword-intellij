package com.app.keyword.repository.http.blog;

import com.app.keyword.vo.http.blog.BlogMainVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BlogMainRepository extends JpaRepository<BlogMainVo, Integer> {
    BlogMainVo findFirstByTotCntIsNullAndKeywordMainVo_TotCntBetweenOrderByKeywordMainVo_SearchTimeAsc(int totCntStart, int totCntEnd);

}