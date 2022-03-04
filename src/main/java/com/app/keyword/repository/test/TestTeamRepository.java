package com.app.keyword.repository.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.keyword.vo.test.TestTeamVo;

@Repository
public interface TestTeamRepository extends JpaRepository<TestTeamVo, Integer>{

}
