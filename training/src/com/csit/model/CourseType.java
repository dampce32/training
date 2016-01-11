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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 课程类别
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_CourseType")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CourseType extends BaseModel {

	// Fields

	private static final long serialVersionUID = 3901952728501692933L;
	/**
	 * 课程类别Id
	 */
	private Integer courseTypeId;
	/**
	 * 课程类别名称
	 */
	private String courseTypeName;

	/**
	 * 课程
	 */
	private Set<Course> courses = new HashSet<Course>(0);

	// Constructors

	/** default constructor */
	public CourseType() {
	}

	/** full constructor */
	public CourseType(String courseTypeName, Set<Course> courses) {
		this.courseTypeName = courseTypeName;
		this.courses = courses;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "courseTypeId", unique = true, nullable = false)
	public Integer getCourseTypeId() {
		return this.courseTypeId;
	}

	public void setCourseTypeId(Integer courseTypeId) {
		this.courseTypeId = courseTypeId;
	}

	@Column(name = "courseTypeName", length = 50)
	public String getCourseTypeName() {
		return this.courseTypeName;
	}

	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "courseType")
	public Set<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

}