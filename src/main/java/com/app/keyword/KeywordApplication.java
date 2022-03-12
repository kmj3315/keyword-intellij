package com.app.keyword;

import com.app.keyword.service.http.HttpConnection;
import com.app.keyword.service.http.search.KeywordSearchService;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
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
	HttpConnection httpConnection;

	public static void main(String[] args) {
		SpringApplication.run(KeywordApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("start job");
		int jobnum = 1;

		while(true){

			KeywordSubVo job = keywordSearchService.findJob();
			logger.info("["+jobnum++ + "] start =======================================================");
			logger.info(job.toString());

			//조회 할 내용 셋팅
			HttpConnVo httpConnVo = keywordSearchService.search(job.getKeywordNm());
			//연결 값
			Document doc = httpConnection.getConnection(httpConnVo);

			Map<String, Object> htmlParser = keywordSearchService.htmlParser(doc);

			//메인키워드값
			KeywordMainVo keywordM = (KeywordMainVo) htmlParser.get("keywordMain");

			//서브키워드
			List<KeywordSubVo> keywordSubList = (List<KeywordSubVo>) htmlParser.get("keywordSubList");

			//기존 키워드가 존재 하는지..?
			List<KeywordMainVo> keywordMainVos = keywordSearchService.checkInsert(keywordM.getKeywordNm());

			if(keywordMainVos.size() == 0){
				keywordSearchService.insertResult(keywordM, keywordSubList);
			}

			job.setRegYn("Y");
			keywordSearchService.saveKeywordSub(job);

			logger.info("["+jobnum + "] end =======================================================");

			Thread.sleep(1000);

		}

	}
}
