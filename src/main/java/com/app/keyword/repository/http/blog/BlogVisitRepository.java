package com.app.keyword.repository.http.blog;

import com.app.keyword.vo.http.blog.BlogVisitVo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogVisitRepository extends JpaRepository<BlogVisitVo, Integer> {
}