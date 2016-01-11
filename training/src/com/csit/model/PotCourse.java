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
 * @Description:咨询课程表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_PotCourse")
public class PotCourse extends BaseModel {

	private static final long serialVersionUID = -6565957301980131465L;

	/**
	 * 咨询课程Id
	 */
	private Integer potCourseId;
	/**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 咨询信息表
	 */
	private Set<Potential> potentials = new HashSet<Potential>(0);

	public PotCourse() {
	}

	public PotCourse(String courseName, Integer status) {
		this.courseName = courseName;
		this.status = status;
	}

	public PotCourse(String courseName, Integer status,
			Set<Potential> potentials) {
		this.courseName = courseName;
		this.status = status;
		this.potentials = potentials;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "potCourseId", unique = true, nullable = false)
	public Integer getPotCourseId() {
		return this.potCourseId;
	}

	public void setPotCourseId(Integer potCourseId) {
		this.potCourseId = potCourseId;
	}

	@Column(name = "CourseName", nullable = false, length = 50)
	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "potCourse")
	public Set<Potential> getPotentials() {
		return this.potentials;
	}

	public void setPotentials(Set<Potential> potentials) {
		this.potentials = potentials;
	}
}