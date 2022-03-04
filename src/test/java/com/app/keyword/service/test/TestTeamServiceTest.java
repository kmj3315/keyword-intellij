package com.app.keyword.service.test;

import com.app.keyword.vo.test.TestTeamVo;
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
public class TestTeamServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TestTeamService testTeamService;

    @Test
    public void findAll() {
        List<TestTeamVo> testTeams = testTeamService.findAll();
        testTeamService.findAll();
        for(TestTeamVo testTeamVo : testTeams)
            logger.info(testTeamVo.toString());
    }
}