package com.app.keyword.service.test;

import com.app.keyword.vo.test.TestMemberVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TestMemberServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TestMemberService testMemberService;

    @Test
    public void findAll() {
        List<TestMemberVo> testMemberVoList = testMemberService.findAll();
        for(TestMemberVo testMemberVo : testMemberVoList)
            logger.info(testMemberVo.toString());
    }

    @Test
    public void findById() {
        //1	kmj3315	김명준	1
        int testNum = 1;
        TestMemberVo testMemberVo =  testMemberService.findById(1);

        assertEquals(testMemberVo.getLoginId(), "kmj3315");
        assertEquals(testMemberVo.getName(), "김명준");
        assertEquals(testMemberVo.getTeamSqno(), 1);

        logger.info(testMemberVo.toString());
    }

    @Test
    public void findByName() {
        String testNm = "구희진";
        List<TestMemberVo> testMemberVoList = testMemberService.findByName(testNm);

        for(TestMemberVo testMemberVo : testMemberVoList )
            logger.info(testMemberVo.toString());
    }

    @Test
    public void findByNameAndLoginId() {
        String testNm = "김명준";
        String testId = "kmj3315";
        List<TestMemberVo> testMemberVoList = testMemberService.findByNameAndLoginId(testNm, testId);

        assertEquals(testMemberVoList.size() , 1);

        for(TestMemberVo testMemberVo : testMemberVoList )
            logger.info(testMemberVo.toString());
    }
}