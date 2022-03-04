package com.app.keyword.repository.test;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.keyword.vo.test.TestMemberVo;

@Repository
public interface TestMemberRepository extends JpaRepository<TestMemberVo, Integer>{
	public List<TestMemberVo> findByName(String Name);
	public List<TestMemberVo> deleteByName(String Name);
	public List<TestMemberVo> findByNameLike(String keyword);
	public List<TestMemberVo> findByNameAndLoginId(String name, String loginId);
}
