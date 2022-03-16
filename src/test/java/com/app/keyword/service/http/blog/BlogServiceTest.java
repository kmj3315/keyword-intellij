package com.app.keyword.service.http.blog;

import com.app.keyword.repository.http.blog.BlogBoardRepository;
import com.app.keyword.repository.http.blog.BlogMainRepository;
import com.app.keyword.repository.http.search.KeywordMainRepository;
import com.app.keyword.service.http.HttpConnection;
import com.app.keyword.vo.http.HttpConnVo;
import com.app.keyword.vo.http.blog.BlogBoardVo;
import com.app.keyword.vo.http.blog.BlogMainVo;
import com.app.keyword.vo.http.blog.BlogVisitVo;
import com.app.keyword.vo.http.search.KeywordMainVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.bytecode.enhance.spi.interceptor.BytecodeInterceptorLogging;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class BlogServiceTest {

    @Autowired
    KeywordMainRepository keywordMainRepository;

    @Autowired
    HttpConnection httpConnection;

    @Autowired
    BlogService blogService;

    @Autowired
    BlogBoardRepository blogBoardRepository;

    @Autowired
    BlogMainRepository blogMainRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    String html;

    @BeforeEach
    void setup(){
        html =
                "{ \"lastBuildDate\": \"Sun, 13 Mar 2022 17:57:03 +0900\", \"total\": 5285, \"start\": 1, \"display\": 10, \"items\": [ { \"title\": \"<b>아이폰</b>12프로 자급제 실버 <b>256</b> <b>아이폰8</b> 크기 비교... \", \"link\": \"https:\\/\\/blog.naver.com\\/rirurang?Redirect=Log&amp;logNo=222305331732\", \"description\": \"<b>아이폰</b>12프로 자급제 실버 <b>256</b> <b>아이폰8</b> 크기 비교 마이그레이션 2017년 11월부터 잘 사용했던 <b>아이폰8</b>을 이제는 보내주기로 했다. 3년하고도 5개월 정도를 쓰다니, 여태 사용했던 폰들 중에서 가장 오래 사용한... \", \"bloggername\": \"언제나 여행중\", \"bloggerlink\": \"https://blog.naver.com/rirurang\", \"postdate\": \"20210410\" }, { \"title\": \"외관 깨끗 배터리성능 양호 <b>아이폰8</b> 골드 <b>256</b>기가\", \"link\": \"https:\\/\\/blog.naver.com\\/isp6263?Redirect=Log&amp;logNo=222613754276\", \"description\": \"큰 찍힘 눈에 띄는 기스 없구요 전체적으로 상태 너무 깨끗합니다 <b>아이폰8</b> 골드 <b>256</b>기가 제품이 왔어요!^^ 크..<b>아이폰8</b> 너무 너무 예쁘지 않나요? 주인장이 최애하는 <b>아이폰</b> 디자인~ 제 개인적인 생각으론... \", \"bloggername\": \"망설이지말고 지금사야 내꺼된다_\", \"bloggerlink\": \"https://blog.naver.com/isp6263\", \"postdate\": \"20220105\" }, { \"title\": \"<b>아이폰</b>12 프로 퍼시픽블루 <b>256</b> 구입 후기 (잘가 <b>아이폰8</b>)\", \"link\": \"https:\\/\\/blog.naver.com\\/sulbe3000?Redirect=Log&amp;logNo=222152446733\", \"description\": \"<b>아이폰8</b>까지 팔면 <b>아이폰</b>12 프로를 한 110만원에 산걸로ㅋㅋㅋ (근데 아직 못 판건 함정.. 얼른 팔아야겠습니다) 이건 <b>아이폰</b>12 일명 #용달블루 회사에서 테스트폰 구입하는데, 너무 궁금해서 용달블루 색상으로... \", \"bloggername\": \"백곰의 생활백서\", \"bloggerlink\": \"https://blog.naver.com/sulbe3000\", \"postdate\": \"20201123\" }, { \"title\": \"[<b>아이폰</b>12미니 퍼플 <b>256</b>] 개봉기 &gt;&lt; 오늘 도착했어요! <b>아이폰8</b>... \", \"link\": \"https:\\/\\/blog.naver.com\\/sjky0513?Redirect=Log&amp;logNo=222329384834\", \"description\": \"핸펀인 <b>아이폰8</b> 골드(<b>256</b>GB), 내 뉴 핸펀 <b>아이폰</b> 12 mini 퍼플(<b>256</b>GB) 투샷 찰칵 이렇게 보니 <b>아이폰8</b> 골드도 참예뻤다. 나는 화이트, 그레이, 실버, 블랙같은 모노컬러의 <b>아이폰</b>으로 그동안 구매한적이 없이 골드만... \", \"bloggername\": \"나의 봄\", \"bloggerlink\": \"https://blog.naver.com/sjky0513\", \"postdate\": \"20210430\" }, { \"title\": \"<b>아이폰8</b> <b>256</b>기가 가격 0원 실화?!\", \"link\": \"https:\\/\\/blog.naver.com\\/poiu357a?Redirect=Log&amp;logNo=221950369355\", \"description\": \"<b>아이폰8</b> 같은경우는 사전예약 시작으로 지금도 출시된지 좀 지났지만 여전히 많은 인기를 받고 있는 <b>아이폰</b>시리즈중 한모델인데요. 더군다나 지금 <b>아이폰8</b>이 무려 <b>256</b>기가가 공짜로 풀려 갑자기 핫하게 더... \", \"bloggername\": \"스마트폰 싸게사는Tip:)\", \"bloggerlink\": \"https://blog.naver.com/poiu357a\", \"postdate\": \"20200507\" }, { \"title\": \"(판매완료) <b>아이폰8</b> <b>256</b>, <b>아이폰</b>7 128 판매합니다\", \"link\": \"https:\\/\\/blog.naver.com\\/doohan500?Redirect=Log&amp;logNo=222106372612\", \"description\": \"모서리 액정찍힘있습니다 어두워서 잘안보이네요 <b>아이폰8</b> <b>256</b> -18만원 <b>아이폰</b>7 128 -12만원 가격 잘모르겠습니다 중고나라보니 이쯤할듯해서 대략 정해봤습니다 다들 상태가 좋더라구요 박스도 있고해서 저는... \", \"bloggername\": \"- 대머리총각의 낙서장 -\", \"bloggerlink\": \"https://blog.naver.com/doohan500\", \"postdate\": \"20201004\" }, { \"title\": \"미니 스타라이트(화이트) <b>256</b> 기가 언박싱(feat.<b>아이폰8</b>)\", \"link\": \"https:\\/\\/blog.naver.com\\/regina4579?Redirect=Log&amp;logNo=222531176327\", \"description\": \"없었던… <b>아이폰</b> 13 &amp; <b>아이폰 8</b> 옆면 테두리 같은 부분을 보면 확실히 13미니가 미묘하게 더 두껍긴... 배터리 용량 안보이는건 조금 많이 거슬리기는… 한게 사실… 용량은 <b>256</b>으로 샀는데 64에서 <b>256</b>넘어가니... \", \"bloggername\": \"炯炯萬事成\", \"bloggerlink\": \"https://blog.naver.com/regina4579\", \"postdate\": \"20211008\" }, { \"title\": \"<b>아이폰8</b> <b>256</b>기가 중고거래 완료!\", \"link\": \"https:\\/\\/blog.naver.com\\/gkatjrdlff?Redirect=Log&amp;logNo=222616235005\", \"description\": \"5년동안 잘 사용한 <b>아이폰8</b>을 <b>아이폰</b> 13미니를 구입하면서 중고나라와 당근마켓에 내놨었다. 처음 판매가격은 19만원에서 시작했는데... 팔리지 않아 가격을 내리다보니 16만원까지 내려갔다.ㅋㅋㅋ <b>아이폰</b>을... \", \"bloggername\": \"깜찍이토끼\", \"bloggerlink\": \"https://blog.naver.com/gkatjrdlff\", \"postdate\": \"20220108\" }, { \"title\": \"<b>아이폰 8</b> 플러스 <b>256</b> 골드 배터리 100%\", \"link\": \"https:\\/\\/blog.naver.com\\/osiris410?Redirect=Log&amp;logNo=222451049135\", \"description\": \"<b>아이폰</b> se 2세대를 제외하곤 플러스 모델 중 홈 버튼이 있는 가장 마지막 세대의 <b>아이폰</b>이라고 할 수 있는 <b>아이폰 8</b> 플러스 모델은 아직 안면인식 Face ID가 불편하신 분들께서 정말로 많이 찾아주시고 있는... \", \"bloggername\": \"선릉 폰연금술사\", \"bloggerlink\": \"https://blog.naver.com/osiris410\", \"postdate\": \"20210730\" }, { \"title\": \"<b>아이폰8</b> <b>256</b>기가 블랙중고폰\", \"link\": \"https:\\/\\/blog.naver.com\\/vhfflxpffpzha?Redirect=Log&amp;logNo=222487751882\", \"description\": \"#<b>아이폰8</b> #<b>아이폰8</b>중고 #IP8 #IP8중고 제품번호: I-1501931 모델명: <b>아이폰8</b> 용량: <b>256</b>기가 상태: 정상 색상... 제주도<b>아이폰</b>중고 #서귀포중고폰 #제주도<b>아이폰</b> #제주<b>아이폰</b> #제주<b>아이폰</b>중고 #제주아이패드중고... \", \"bloggername\": \"중고폰거래소\", \"bloggerlink\": \"https://blog.naver.com/vhfflxpffpzha\", \"postdate\": \"20210829\" } ] }" ;


    }

    @Test
    void blogConnection(){
        HttpConnVo httpConnVo  = blogService.searchBlog("아이폰8256");
        Document doc = httpConnection.getConnection(httpConnVo);
        logger.info(doc.toString());

    }

    @Test
    void blogParser() throws Exception {
        HttpConnVo httpConnVo  = blogService.searchBlog("아이폰8256");
        Document doc = httpConnection.getConnection(httpConnVo);

        Map<String, Object> stringObjectMap = blogService.blogParser(doc);
        logger.info(stringObjectMap.get("blogMain").toString());
        logger.info(stringObjectMap.get("blogBoard").toString());

    }

    @Test
    void blogXmlParser(){
        HttpConnVo httpConnVo = blogService.searchBlog("아이폰");
        Document document = httpConnection.getConnection(httpConnVo);
        Map<String, Object> stringObjectMap = blogService.blogXmlParser(document);
        logger.info(stringObjectMap.toString());

    }


    @Test
    void blogVisit() throws JsonProcessingException {
        HttpConnVo httpConnVo = blogService.searchVisit("dlwlqk");
        Document document = httpConnection.getConnection(httpConnVo);
        logger.info(document.toString());

        String text = document.select("body ").text();
        Elements es = document.select("visitorcnt");

        logger.info(es.toString());

        for(Element e : es){
            logger.info(e.attr("id"));
            logger.info(e.attr("cnt"));
        }


//        HashMap hashMap = new ObjectMapper().readValue(text, HashMap.class);

    }

    @Test
    void blogVisitParser(){
//        HttpConnVo httpConnVo = blogService.searchVisit("dlwlqk");
//        Document document = httpConnection.getConnection(httpConnVo);
//
////        List<BlogVisitVo> voList = blogService.visitParser(document);
//
//        for(BlogVisitVo v : voList){
//            logger.info(v.toString());
//        }
    }

    @Test
    void findJob(){
        KeywordMainVo job = blogService.findJob();
        logger.info(job.toString());

    }

    @Test
    void processTest() throws InterruptedException {
        blogService.blogProcess();

    }

}