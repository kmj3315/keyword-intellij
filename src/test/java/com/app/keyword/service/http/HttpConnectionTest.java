package com.app.keyword.service.http;

import com.app.keyword.vo.http.HttpConnVo;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class HttpConnectionTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SearchCrawlerImpl searchCrawlerImpl;

    @Autowired
    HttpConnection httpConnection;

    @Test
    void getConnection() {
        String paramName = "냉장고";
        HttpConnVo httpConnVo = searchCrawlerImpl.search(paramName);

        Document doc = httpConnection.getConnection(httpConnVo);
        logger.info(doc.toString());
        }
}