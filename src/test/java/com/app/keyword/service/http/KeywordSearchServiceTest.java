package com.app.keyword.service.http;

import com.app.keyword.repository.http.search.KeywordMainRepository;
import com.app.keyword.repository.http.search.KeywordSubRepository;
import com.app.keyword.service.http.search.KeywordSearchService;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
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

import java.time.LocalDate;
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
    void parser(){
        Document doc = Jsoup.parse(html);

        Map<String, Object> htmlParser = keywordSearchService.htmlParser(doc);

        logger.info(htmlParser.get("keywordM").toString());

        for(KeywordSubVo keywordSubVo : (List<KeywordSubVo>)htmlParser.get("keywordSubList"))
            logger.info(keywordSubVo.toString());

    }

    @Test
    void insertReuslt(){
        KeywordMainVo km = KeywordMainVo.builder()
                .keywordNm("아이폰")
                .pcCnt(94200)
                .mbCnt(322900)
                .build();

        KeywordSubVo keywordSubVo1 = KeywordSubVo.builder()
                .keywordNm("하이마트오픈점")
                .pcCnt(1430)
                .mbCnt(5850)
                .build();

        KeywordSubVo keywordSubVo2 = KeywordSubVo.builder()
                .keywordNm("하이마트")
                .pcCnt(164800)
                .mbCnt(409400)
                .build();

        keywordMainRepository.save(km);

        keywordSubVo1.setKeywordMainSqno(km.getKeywordMainSqno());
        keywordSubVo2.setKeywordMainSqno(km.getKeywordMainSqno());

        List<KeywordSubVo> list = Arrays.asList(keywordSubVo1, keywordSubVo2);

        keywordSubRepository.saveAll(list);


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
    void totalTest(){
        String parma ="아이폰";
        
        //조회 할 내용 셋팅
        HttpConnVo httpConnVo = keywordSearchService.search(parma);
        
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
    }

    @Test
    void top1(){
        KeywordSubVo firstByOrderBySearchTime = keywordSubRepository.findFirstByRegYnOrderBySearchTimeAsc("N");

        logger.info(firstByOrderBySearchTime.toString());

    }
}