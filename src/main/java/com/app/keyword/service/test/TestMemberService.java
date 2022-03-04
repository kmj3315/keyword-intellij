package com.app.keyword.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AbstractInterceptorDrivenBeanDefinitionDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DecoratingClassLoader;
import org.springframework.stereotype.Repository;

import com.app.keyword.repository.test.TestMemberRepository;
import com.app.keyword.vo.test.TestMemberVo;

@Repository
public class TestMemberService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TestMemberRepository testRepository;
	
	public List<TestMemberVo> findAll(){
		List<TestMemberVo> members = new ArrayList<>();
		testRepository.findAll().forEach(e -> members.add(e));

		return members;
	}
	
	//id 찾기 -> findById(고정)
	public TestMemberVo findById(int no){
		Optional<TestMemberVo> testVo = testRepository.findById(no);
		logger.debug("main >> findById excute!!");
		
		return testVo.get();
	}
	//where 들어갈 경우 fnidBy <--칼럼명
	public List<TestMemberVo> findByName(String name) {
		List<TestMemberVo> testVoList = testRepository.findByName(name);
		return testVoList;
	}
	//where 여러개 들어갈 경우 fnidBy <-- 칼럼명 and 칼럼명 ...
	public List<TestMemberVo> findByNameAndLoginId(String name, String loginId) {
		List<TestMemberVo> testVoList = testRepository.findByNameAndLoginId(name, loginId);
		return testVoList;
	}
	
	
	
//	public void updateTest(int no, TestVo inputTestVo) {
//		
//		Optional<TestVo> testVo = testRepository.findById(no);
//		
//		if(testVo.isPresent()) {
//			testVo.get().setName(inputTestVo.getName());
//		}
//		testRepository.save(null)
//		
//	}
//	
//	public void deleteById()
//	
	
}
