package com.app.keyword.repository.http.search;

import com.app.keyword.vo.http.search.KeywordSubVo;
import com.app.keyword.vo.test.TestMemberVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordSubRepository extends JpaRepository<KeywordSubVo, Integer> {
    public KeywordSubVo findFirstByRegYnOrderBySearchTimeAsc(String regYn);

}
