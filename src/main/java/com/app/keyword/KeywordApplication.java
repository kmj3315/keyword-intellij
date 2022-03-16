package com.app.keyword;

import com.app.keyword.service.http.HttpConnection;
import com.app.keyword.service.http.blog.BlogService;
import com.app.keyword.service.http.search.KeywordSearchService;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
import com.app.keyword.vo.http.search.KeywordRelationVo;
import com.app.keyword.vo.http.search.KeywordSubVo;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class KeywordApplication implements CommandLineRunner {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	KeywordSearchService keywordSearchService;
	@Autowired
	BlogService blogService;

	@Autowired
	HttpConnection httpConnection;

	public static void main(String[] args) {
		SpringApplication.run(KeywordApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//keywordSearchService.keywordSearchProcess();
		//blogService.blogProcess();
	}
}
