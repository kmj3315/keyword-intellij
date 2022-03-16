package com.app.keyword.service.http;

import com.app.keyword.repository.http.search.KeywordMainRepository;
import com.app.keyword.repository.http.search.KeywordRelationRepository;
import com.app.keyword.repository.http.search.KeywordSubRepository;
import com.app.keyword.service.http.search.KeywordSearchService;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
import com.app.keyword.vo.http.search.KeywordRelationVo;
import com.app.keyword.vo.http.search.KeywordSubVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class KeywordSearchServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    KeywordSearchService keywordSearchService;

    @Autowired
    KeywordMainRepository keywordMainRepository;

    @Autowired
    KeywordSubRepository keywordSubRepository;

    @Autowired
    KeywordRelationRepository keywordRelationRepository;

    @Autowired
    HttpConnection httpConnection;

    String html;

    @BeforeEach
    void setup(){
        html="<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  {\"keywordList\":[{\"relKeyword\":\"아이폰\",\"monthlyPcQcCnt\":94200,\"monthlyMobileQcCnt\":322900},{\"relKeyword\":\"하이마트오픈점\",\"monthlyPcQcCnt\":1430,\"monthlyMobileQcCnt\":5850},{\"relKeyword\":\"하이마트\",\"monthlyPcQcCnt\":164800,\"monthlyMobileQcCnt\":409400},{\"relKeyword\":\"아이폰12\",\"monthlyPcQcCnt\":20900,\"monthlyMobileQcCnt\":133000},{\"relKeyword\":\"하이마트오픈\",\"monthlyPcQcCnt\":370,\"monthlyMobileQcCnt\":960}]}\n" +
                " </body>\n" +
                "</html>";
    }





    @Test
    void keywordInsertCheck(){
        String keywordNm = "아이폰";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        Date d1 = cal.getTime();
        Date d2 = new Date();


        List<KeywordMainVo> keywordMains = keywordMainRepository.findByKeywordNmAndSearchDtBetween(keywordNm, d1, d2);

        logger.info("check size : " + keywordMains.size());
        for(KeywordMainVo keywordMainVo : keywordMains)
            logger.info(keywordMainVo.toString());
    }



    @Test
    void totalTest() throws InterruptedException {
        logger.info("start job");
		int jobnum = 1;

		while (true){
			logger.info("["+jobnum++ + "] start =======================================================");
			KeywordRelationVo job = keywordSearchService.findJob();
			logger.info(job.toString());

			//조회 할 내용 셋팅
			HttpConnVo httpConnVo = keywordSearchService.search(job.getKeywordNm());
			//연결 값
			Document doc = httpConnection.getConnection(httpConnVo);
			Map<String, Object> htmlParser = keywordSearchService.htmlParser(doc);
			//메인키워드값
			KeywordMainVo keywordM = (KeywordMainVo) htmlParser.get("keywordMain");
			//릴레이션 키워드
			List<KeywordRelationVo> keywordSubList = (List<KeywordRelationVo>) htmlParser.get("keywordRelList");

			keywordSearchService.insertResult2(keywordM, keywordSubList);

			job.setRegYn("Y");
			keywordSearchService.savekeywordRelation(job);
			logger.info("["+jobnum + "] end =======================================================");

			Thread.sleep(1000);
		}

    }


    @Test
    void findJob(){
        KeywordMainVo first = keywordMainRepository.findFirstByTotCntBetweenAndBlogMains_TotCntIsNullOrderBySearchTimeAsc(300, 500);
        logger.info(first.toString());

    }



}