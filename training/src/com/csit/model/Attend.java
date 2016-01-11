package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 
 * @Description: 学员出勤表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-3
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Attend")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Attend extends BaseModel {

	//Field  
	
	private static final long serialVersionUID = 5817076663482166753L;
	/**
	 * 学员出勤表Id
	 */
	private Integer attendId;
	/**
	 * 选班
	 */
	private StuClass stuClass;
	/**
	 * 排课
	 */
	private LessonDegree lessonDegree;
	/**
	 * 状态
	 * 0 - 正常
	 * 1 - 迟到
	 * 2 - 早退
	 * 3 - 旷课
	 * 4 - 请假
	 */
	private Integer status;
	/**
	 * 考勤有效课时
	 */
	private Integer lessons;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 签到时间
	 */
	private Date comeTime;
	/**
	 * 签退时间
	 */
	private Date goTime;
	/**
	 * 本科次价值
	 */
	private Double price;

	// Constructors
	
	public Attend(){
		
	}
	
	public Attend(Integer attendId, StuClass stuClass,
			LessonDegree lessonDegree, Integer status, Integer lessons,
			String note, Date comeTime, Date goTime, Double price) {
		super();
		this.attendId = attendId;
		this.stuClass = stuClass;
		this.lessonDegree = lessonDegree;
		this.status = status;
		this.lessons = lessons;
		this.note = note;
		this.comeTime = comeTime;
		this.goTime = goTime;
		this.price = price;
	}

	// Property accessors
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "attendId", unique = true, nullable = false)
	public Integer getAttendId() {
		return attendId;
	}
	public void setAttendId(Integer attendId) {
		this.attendId = attendId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stuClassId")
	public StuClass getStuClass() {
		return stuClass;
	}
	public void setStuClass(StuClass stuClass) {
		this.stuClass = stuClass;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lessonDegreeId")
	public LessonDegree getLessonDegree() {
		return lessonDegree;
	}
	public void setLessonDegree(LessonDegree lessonDegree) {
		this.lessonDegree = lessonDegree;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "lessons")
	public Integer getLessons() {
		return lessons;
	}
	public void setLessons(Integer lessons) {
		this.lessons = lessons;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "comeTime", length = 10)
	public Date getComeTime() {
		return comeTime;
	}
	public void setComeTime(Date comeTime) {
		this.comeTime = comeTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "goTime", length = 10)
	public Date getGoTime() {
		return goTime;
	}
	public void setGoTime(Date goTime) {
		this.goTime = goTime;
	}

	@Column(name = "price")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
