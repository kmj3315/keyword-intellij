package com.app.keyword.service.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.keyword.repository.test.TestTeamRepository;
import com.app.keyword.vo.test.TestTeamVo;

@Component
public class TestTeamService {

	@Autowired
	private TestTeamRepository testTeamrepository;
	
	public List<TestTeamVo> findAll(){
		List<TestTeamVo> teams = new ArrayList<>();
		testTeamrepository.findAll().forEach(e -> teams.add(e));
		return teams;
	}  
	

}
