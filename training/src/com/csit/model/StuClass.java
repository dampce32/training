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

/**
 * 
 * @Description:
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "T_StuClass")
public class StuClass extends BaseModel {

	private static final long serialVersionUID = 7354889773437562417L;

	/**
	 * 选班Id
	 */
	private Integer stuClassId;
	/**
	 * 班级
	 */
	private Clazz clazz;
	/**
	 * 上课校区
	 */
	private School lessonSchool;
	/**
	 * 经办人
	 */
	private User user;
	/**
	 * 学员
	 */
	private Student student;
	/**
	 * 选班校区
	 */
	private School selectSchool;
	/**
	 * 选班日期
	 */
	private Date selectDate;
	/**
	 * 已购总课时
	 */
	private Integer lessons;
	/**
	 * 课程进度
	 */
	private Integer courseProgress;
	/**
	 * 选班状态 1 -- 正常，2 -- 插班，3 -- 转班，4 -- 休学，5 -- 退学，6 --弃学
	 */
	private Integer scStatus;
	/**
	 * 选班类型 0 -- 试听，1 -- 新增，2 -- 续报
	 */
	private Integer continueReg;
	/**
	 * 成绩
	 */
	private Double score;
	/**
	 * 评价日期
	 */
	private Date opinionDate;
	/**
	 * 评价内容
	 */
	private String opinion;
	/**
	 * 成绩等级
	 */
	private String grade;
	/**
	 * 出勤次数
	 */
	private Integer factCount;
	/**
	 * 迟到次数
	 */
	private Integer lateCount;
	/**
	 * 早退次数
	 */
	private Integer advanceCount;
	/**
	 * 旷课次数
	 */
	private Integer truancyCount;
	/**
	 * 请假次数
	 */
	private Integer leaveCount;
	/**
	 * 补课次数
	 */
	private Integer fillCount;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 数据写入时间
	 */
	private Date insertTime;

	// Constructors

	/** default constructor */
	public StuClass() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stuClassId", unique = true, nullable = false)
	public Integer getStuClassId() {
		return this.stuClassId;
	}

	public void setStuClassId(Integer stuClassId) {
		this.stuClassId = stuClassId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classId", nullable = false)
	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lessonSchoolId", nullable = false)
	public School getLessonSchool() {
		return lessonSchool;
	}

	public void setLessonSchool(School lessonSchool) {
		this.lessonSchool = lessonSchool;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "studentId", nullable = false)
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "selectSchoolId", nullable = false)
	public School getSelectSchool() {
		return selectSchool;
	}

	public void setSelectSchool(School selectSchool) {
		this.selectSchool = selectSchool;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "selectDate", nullable = false, length = 10)
	public Date getSelectDate() {
		return this.selectDate;
	}

	public void setSelectDate(Date selectDate) {
		this.selectDate = selectDate;
	}

	@Column(name = "lessons")
	public Integer getLessons() {
		return this.lessons;
	}

	public void setLessons(Integer lessons) {
		this.lessons = lessons;
	}

	@Column(name = "courseProgress")
	public Integer getCourseProgress() {
		return this.courseProgress;
	}

	public void setCourseProgress(Integer courseProgress) {
		this.courseProgress = courseProgress;
	}

	@Column(name = "scStatus")
	public Integer getScStatus() {
		return this.scStatus;
	}

	public void setScStatus(Integer scStatus) {
		this.scStatus = scStatus;
	}

	@Column(name = "continueReg", nullable = false)
	public Integer getContinueReg() {
		return this.continueReg;
	}

	public void setContinueReg(Integer continueReg) {
		this.continueReg = continueReg;
	}

	@Column(name = "score", precision = 22, scale = 0)
	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "opinionDate", length = 10)
	public Date getOpinionDate() {
		return this.opinionDate;
	}

	public void setOpinionDate(Date opinionDate) {
		this.opinionDate = opinionDate;
	}

	@Column(name = "opinion", length = 500)
	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	@Column(name = "grade", length = 10)
	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Column(name = "factCount")
	public Integer getFactCount() {
		return this.factCount;
	}

	public void setFactCount(Integer factCount) {
		this.factCount = factCount;
	}

	@Column(name = "lateCount")
	public Integer getLateCount() {
		return this.lateCount;
	}

	public void setLateCount(Integer lateCount) {
		this.lateCount = lateCount;
	}

	@Column(name = "advanceCount")
	public Integer getAdvanceCount() {
		return this.advanceCount;
	}

	public void setAdvanceCount(Integer advanceCount) {
		this.advanceCount = advanceCount;
	}

	@Column(name = "truancyCount")
	public Integer getTruancyCount() {
		return this.truancyCount;
	}

	public void setTruancyCount(Integer truancyCount) {
		this.truancyCount = truancyCount;
	}

	@Column(name = "leaveCount")
	public Integer getLeaveCount() {
		return this.leaveCount;
	}

	public void setLeaveCount(Integer leaveCount) {
		this.leaveCount = leaveCount;
	}

	@Column(name = "fillCount")
	public Integer getFillCount() {
		return this.fillCount;
	}

	public void setFillCount(Integer fillCount) {
		this.fillCount = fillCount;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "insertTime", length = 10)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

}