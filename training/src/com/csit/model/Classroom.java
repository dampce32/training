package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 教室
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Classroom")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Classroom extends BaseModel {

	// Fields
	private static final long serialVersionUID = 1383152060487607160L;
	/**
	 * 教室Id
	 */
	private Integer classroomId;
	/**
	 * 教室名称
	 */
	private String classroomName;
	/**
	 * 容纳最大人数
	 */
	private Integer seating;
	/**
	 * 状态 
	 * 0 -- 禁用 
	 * 1 -- 可用
	 */
	private Integer status;
	/**
	 * 校区
	 */
	private School school;
	/**
	 * 排课表
	 */
	private Set<LessonDegree> leassonDegrees = new HashSet<LessonDegree>(0);
	/**
	 * 班级
	 */
	private Set<Clazz> classes = new HashSet<Clazz>(0);

	// Constructors

	/** default constructor */
	public Classroom() {
	}

	/** minimal constructor */
	public Classroom(Integer classroomId, String classroomName) {
		this.classroomId = classroomId;
		this.classroomName = classroomName;
	}

	/** full constructor */
	public Classroom(Integer classroomId, String classroomName,
			Integer seating, Integer status, Set<LessonDegree> leassonDegrees,
			Set<Clazz> classes) {
		this.classroomId = classroomId;
		this.classroomName = classroomName;
		this.seating = seating;
		this.status = status;
		this.leassonDegrees = leassonDegrees;
		this.classes = classes;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "classroomId", unique = true, nullable = false)
	public Integer getClassroomId() {
		return this.classroomId;
	}

	public void setClassroomId(Integer classroomId) {
		this.classroomId = classroomId;
	}

	@Column(name = "classroomName", nullable = false, length = 50)
	public String getClassroomName() {
		return this.classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	@Column(name = "seating", nullable = false)
	public Integer getSeating() {
		return this.seating;
	}

	public void setSeating(Integer seating) {
		this.seating = seating;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom")
	public Set<LessonDegree> getLeassonDegrees() {
		return this.leassonDegrees;
	}

	public void setLeassonDegrees(Set<LessonDegree> leassonDegrees) {
		this.leassonDegrees = leassonDegrees;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom")
	public Set<Clazz> getClasses() {
		return this.classes;
	}

	public void setClasses(Set<Clazz> classes) {
		this.classes = classes;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false)
	public School getSchool() {
		return this.school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

}