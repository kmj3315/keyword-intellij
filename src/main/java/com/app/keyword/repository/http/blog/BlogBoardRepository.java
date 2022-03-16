package com.app.keyword.repository.http.blog;

import com.app.keyword.vo.http.blog.BlogBoardVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BlogBoardRepository extends JpaRepository<BlogBoardVo, Integer> {
}