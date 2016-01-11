package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @Description: 班级
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Class")
public class Clazz extends BaseModel {

	// Fields
	private static final long serialVersionUID = -1493248386652440657L;
	/**
	 * 班级Id
	 */
	private Integer classId;
	/**
	 * 课程
	 */
	private Course course;
	/**
	 * 校区
	 */
	private School school;
	/**
	 * 教师
	 */
	private User teacher;
	/**
	 * 教室
	 */
	private Classroom classroom;
	/**
	 * 班级名称
	 */
	private String className;
	/**
	 * 总课时
	 */
	private Integer lessons;
	/**
	 * 开课时间
	 */
	private Date startDate;
	/**
	 * 上课规律
	 */
	private String timeRule;
	/**
	 * 课程进度
	 */
	private Integer courseProgress;
	/**
	 * 计划招生数
	 */
	private Integer planCount;
	/**
	 * 当前学员数
	 */
	private Integer stuCount;
	/**
	 * 班级介绍
	 */
	private String note;
	/**
	 * 班级类型 
	 * 0 -- 一对多大班 
	 * 1 -- 一对一
	 */
	private Integer classType;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 排课进度
	 */
	private Integer arrangeLessons;
	/**
	 * 每课时多少分钟
	 */
	private Integer lessonMinute;
	/**
	 * 每课时教师提成多少元
	 */
	private Double lessonCommission;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 建立者
	 */
	private User creater;
	/**
	 * 选班表
	 */
	private Set<StuClass> stuClasses = new HashSet<StuClass>(0);
	/**
	 * 排课表
	 */
	private Set<LessonDegree> leassonDegrees = new HashSet<LessonDegree>(0);

	// Constructors

	/** default constructor */
	public Clazz() {
	}

	/** minimal constructor */
	public Clazz(Integer classId, Course course, School school, User teacher,
			Classroom classroom, String className, Date startDate,
			Integer planCount, Integer classType, Date createDate,
			User creater) {
		this.classId = classId;
		this.course = course;
		this.school = school;
		this.teacher = teacher;
		this.classroom = classroom;
		this.className = className;
		this.startDate = startDate;
		this.planCount = planCount;
		this.classType = classType;
		this.createDate = createDate;
		this.creater = creater;
	}

	/** full constructor */
	public Clazz(Integer classId, Course course, School school, User teacher,
			Classroom classroom, String className, Integer lessons,
			Date startDate, String timeRule, Integer courseProgress,
			Integer planCount, Integer stuCount, String note,
			Integer classType, Date endDate, Integer arrangeLessons,
			Integer lessonMinute, Double lessonCommission, Date createDate,
			User creater, Set<StuClass> stuClasses,
			Set<LessonDegree> leassonDegrees) {
		this.classId = classId;
		this.course = course;
		this.school = school;
		this.teacher = teacher;
		this.classroom = classroom;
		this.className = className;
		this.lessons = lessons;
		this.startDate = startDate;
		this.timeRule = timeRule;
		this.courseProgress = courseProgress;
		this.planCount = planCount;
		this.stuCount = stuCount;
		this.note = note;
		this.classType = classType;
		this.endDate = endDate;
		this.arrangeLessons = arrangeLessons;
		this.lessonMinute = lessonMinute;
		this.lessonCommission = lessonCommission;
		this.createDate = createDate;
		this.creater = creater;
		this.stuClasses = stuClasses;
		this.leassonDegrees = leassonDegrees;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "classId", unique = true, nullable = false)
	public Integer getClassId() {
		return this.classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseId", nullable = false)
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false)
	public School getSchool() {
		return this.school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacherId", nullable = false)
	public User getTeacher() {
		return this.teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classroomId", nullable = false)
	public Classroom getClassroom() {
		return this.classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	@Column(name = "className", nullable = false, length = 50)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "lessons", nullable = false)
	public Integer getLessons() {
		return this.lessons;
	}

	public void setLessons(Integer lessons) {
		this.lessons = lessons;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startDate", nullable = false, length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "timeRule", length = 500)
	public String getTimeRule() {
		return this.timeRule;
	}

	public void setTimeRule(String timeRule) {
		this.timeRule = timeRule;
	}

	@Column(name = "courseProgress")
	public Integer getCourseProgress() {
		return this.courseProgress;
	}

	public void setCourseProgress(Integer courseProgress) {
		this.courseProgress = courseProgress;
	}

	@Column(name = "planCount", nullable = false)
	public Integer getPlanCount() {
		return this.planCount;
	}

	public void setPlanCount(Integer planCount) {
		this.planCount = planCount;
	}

	@Column(name = "stuCount")
	public Integer getStuCount() {
		return this.stuCount;
	}

	public void setStuCount(Integer stuCount) {
		this.stuCount = stuCount;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "classType", nullable = false)
	public Integer getClassType() {
		return this.classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "endDate", length = 10)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "arrangeLessons")
	public Integer getArrangeLessons() {
		return this.arrangeLessons;
	}

	public void setArrangeLessons(Integer arrangeLessons) {
		this.arrangeLessons = arrangeLessons;
	}

	@Column(name = "lessonMinute", nullable = false)
	public Integer getLessonMinute() {
		return this.lessonMinute;
	}

	public void setLessonMinute(Integer lessonMinute) {
		this.lessonMinute = lessonMinute;
	}

	@Column(name = "lessonCommission")
	public Double getLessonCommission() {
		return this.lessonCommission;
	}

	public void setLessonCommission(Double lessonCommission) {
		this.lessonCommission = lessonCommission;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createDate", nullable = false, length = 10)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createrId", nullable = false)
	public User getCreater() {
		return this.creater;
	}

	public void setCreater(User creater) {
		this.creater = creater;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "clazz")
	public Set<StuClass> getStuClasses() {
		return this.stuClasses;
	}

	public void setStuClasses(Set<StuClass> stuClasses) {
		this.stuClasses = stuClasses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "clazz")
	public Set<LessonDegree> getLeassonDegrees() {
		return this.leassonDegrees;
	}

	public void setLeassonDegrees(Set<LessonDegree> leassonDegrees) {
		this.leassonDegrees = leassonDegrees;
	}

}