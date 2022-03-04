package com.app.keyword.vo.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "testMember")
public class TestMemberVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mbrNo;
	private String loginId;
	private String name;
	private int teamSqno;
	
	@ManyToOne
	@JoinColumn(name = "teamSqno", insertable = false, updatable = false)
	private TestTeamVo testTeamVo;
	
	@Builder
	public TestMemberVo(String loginId, String name, int teamSqno) {
		this.loginId = loginId;
		this.name = name;
		this.teamSqno = teamSqno;
	}
}
