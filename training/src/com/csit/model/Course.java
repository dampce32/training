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
 * @Description: 课程
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Course")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Course extends BaseModel {

	// Fields
	private static final long serialVersionUID = 5659508601525938586L;
	/**
	 * 课程Id
	 */
	private Integer courseId;
	/**
	 * 课程类别
	 */
	private CourseType courseType;
	/**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 单位
	 */
	private String courseUnit;
	/**
	 * 单价
	 */
	private Double unitPrice;
	/**
	 * 状态
	 * 0 -- 禁用
	 * 1 -- 可用
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 班级
	 */
	private Set<Clazz> classes = new HashSet<Clazz>(0);
	/**
	 * 消费明细表
	 */
	private Set<BillDetail> billDetails = new HashSet<BillDetail>(0);

	// Constructors

	/** default constructor */
	public Course() {
	}

	/** minimal constructor */
	public Course(String courseName, String courseUnit, Double unitPrice,
			Integer status) {
		this.courseName = courseName;
		this.courseUnit = courseUnit;
		this.unitPrice = unitPrice;
		this.status = status;
	}

	/** full constructor */
	public Course(CourseType courseType, String courseName, String courseUnit,
			Double unitPrice, Integer status, String note, Set<Clazz> classes,
			Set<BillDetail> billDetails) {
		this.courseType = courseType;
		this.courseName = courseName;
		this.courseUnit = courseUnit;
		this.unitPrice = unitPrice;
		this.status = status;
		this.note = note;
		this.classes = classes;
		this.billDetails = billDetails;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "courseId", unique = true, nullable = false)
	public Integer getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseTypeId")
	public CourseType getCourseType() {
		return this.courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	@Column(name = "courseName", nullable = false, length = 50)
	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Column(name = "courseUnit", nullable = false, length = 20)
	public String getCourseUnit() {
		return this.courseUnit;
	}

	public void setCourseUnit(String courseUnit) {
		this.courseUnit = courseUnit;
	}

	@Column(name = "unitPrice", nullable = false, precision = 22, scale = 0)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	public Set<Clazz> getClasses() {
		return this.classes;
	}

	public void setClasses(Set<Clazz> classes) {
		this.classes = classes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	public Set<BillDetail> getBillDetails() {
		return this.billDetails;
	}

	public void setBillDetails(Set<BillDetail> billDetails) {
		this.billDetails = billDetails;
	}

}