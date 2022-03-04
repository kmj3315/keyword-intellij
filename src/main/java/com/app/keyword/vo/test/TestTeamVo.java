package com.app.keyword.vo.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "testTeam")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestTeamVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int teamSqno;
	private String teamName;
}
