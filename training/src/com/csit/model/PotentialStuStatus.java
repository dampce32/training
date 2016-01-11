package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @Description:潜在学院状态表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_PotentialStuStatus")
public class PotentialStuStatus extends BaseModel {

	private static final long serialVersionUID = 682330437947551773L;

	/**
	 * 潜在学员状态Id
	 */
	private Integer potentialStuStatusId;
	/**
	 * 潜在学员状态姓名
	 */
	private String potentialStuStatusName;
	/**
	 * 状态 0--禁用 1--可用
	 */
	private Integer status;
	/**
	 * 咨询信息单
	 */
	private Set<Potential> potentials = new HashSet<Potential>(0);
	/**
	 * 咨询回访单
	 */
	private Set<Reply> replies = new HashSet<Reply>(0);

	public PotentialStuStatus() {
	}

	public PotentialStuStatus(String potentialStuStatusName, Integer status) {
		this.potentialStuStatusName = potentialStuStatusName;
		this.status = status;
	}

	public PotentialStuStatus(String potentialStuStatusName, Integer status,
			Set<Potential> potentials, Set<Reply> replies) {
		this.potentialStuStatusName = potentialStuStatusName;
		this.status = status;
		this.potentials = potentials;
		this.replies = replies;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "potentialStuStatusId", unique = true, nullable = false)
	public Integer getPotentialStuStatusId() {
		return this.potentialStuStatusId;
	}

	public void setPotentialStuStatusId(Integer potentialStuStatusId) {
		this.potentialStuStatusId = potentialStuStatusId;
	}

	@Column(name = "potentialStuStatusName", nullable = false, length = 50)
	public String getPotentialStuStatusName() {
		return this.potentialStuStatusName;
	}

	public void setPotentialStuStatusName(String potentialStuStatusName) {
		this.potentialStuStatusName = potentialStuStatusName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "potentialStuStatus")
	public Set<Potential> getPotentials() {
		return this.potentials;
	}

	public void setPotentials(Set<Potential> potentials) {
		this.potentials = potentials;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "potentialStuStatus")
	public Set<Reply> getReplies() {
		return this.replies;
	}

	public void setReplies(Set<Reply> replies) {
		this.replies = replies;
	}

}