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
 * @Description:排课表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name="T_LessonDegree")

public class LessonDegree  extends BaseModel {

	private static final long serialVersionUID = 7981887852871206706L;
	
	/*** 
	 * 排课课次编号
	 */
	private Integer lessonDegreeId;
	/**
	 * 上课时间
	 */
	private String startTime;
	/**
	 * 下课时间
	 */
	private String endTime;
     /**
      * 班级
      */
     private Clazz clazz;
     /**
      * 经办人
      */
     private User user;
     /**
      * 教师
      */
     private User teacher;
     /**
      * 教室
      */
     private Classroom classroom;
     /**
      * 主题
      */
     private String subject;
     /**
      * 课时
      */
     private Integer lessons;
     /**
      * 出勤人数
      */
     private Integer factCount;
     /**
      * 迟到人数
      */
     private Integer lateCount;
     /**
      * 早退人数
      */
     private Integer advanceCount;
     /**
      * 旷课人数
      */
     private Integer truancyCount;
     /**
      * 请假人数
      */
     private Integer leaveCount;
     /**
      * 上课类型
      *   0 -- 补课
      *   1 -- 正常
      */
     private Integer lessonType;
     /**
      * 上课状态  
      * 0 -- 未上课 
      * 1 -- 已上课
      */
     private Integer lessonStatus;
     /**
      * 上课日期
      */
     private Date lessonDegreeDate;
	 /**
	 * 备注
	 */
     private String note;
     /**
 	 * 时间段 startTime+endTime,用于前台显示
 	 */
 	private String Time;

 	public LessonDegree() {
    }
 	
    public LessonDegree(Integer lessonDegreeId, User teacher, Integer lessons, Date lessonDegreeDate) {
        this.lessonDegreeId = lessonDegreeId;
        this.teacher = teacher;
        this.lessons = lessons;
        this.lessonDegreeDate = lessonDegreeDate;
    }
    
     public LessonDegree(Integer lessonDegreeId, String startTime, String endTime,
    			Clazz clazz, User user, User teacher, Classroom classroom,
    			String subject, Integer lessons, Integer factCount, Integer lateCount,
    			Integer advanceCount, Integer truancyCount, Integer leaveCount,
    			Integer lessonType, Integer lessonStatus, Date lessonDegreeDate,
    			String note) {
		this.lessonDegreeId = lessonDegreeId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.clazz = clazz;
		this.user = user;
		this.teacher = teacher;
		this.classroom = classroom;
		this.subject = subject;
		this.lessons = lessons;
		this.factCount = factCount;
		this.lateCount = lateCount;
		this.advanceCount = advanceCount;
		this.truancyCount = truancyCount;
		this.leaveCount = leaveCount;
		this.lessonType = lessonType;
		this.lessonStatus = lessonStatus;
		this.lessonDegreeDate = lessonDegreeDate;
		this.note = note;
	}
     
    @Id 
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="lessonDegreeId", unique=true, nullable=false)
    public Integer getLessonDegreeId() {
        return this.lessonDegreeId;
    }
    
	public void setLessonDegreeId(Integer lessonDegreeId) {
        this.lessonDegreeId = lessonDegreeId;
    }
        
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="classId", nullable = false)
	public Clazz getClazz() {
		return clazz;
	}
	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId", nullable = false)
    public User getUser() {
        return this.user;
    }
	public void setUser(User user) {
        this.user = user;
    }
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="teacherId", nullable=false)
    public User getTeacher() {
        return this.teacher;
    }
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="classroomId", nullable = false)
    public Classroom getClassroom() {
        return this.classroom;
    }
    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
    
    @Column(name="subject", length=50)
    public String getSubject() {
        return this.subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    @Column(name="lessons", nullable=false)
    public Integer getLessons() {
        return this.lessons;
    }
    public void setLessons(Integer lessons) {
        this.lessons = lessons;
    }
    
    @Column(name="factCount")
    public Integer getFactCount() {
        return this.factCount;
    }
    public void setFactCount(Integer factCount) {
        this.factCount = factCount;
    }
    
    @Column(name="lateCount")
    public Integer getLateCount() {
        return this.lateCount;
    }
    public void setLateCount(Integer lateCount) {
        this.lateCount = lateCount;
    }
    
    @Column(name="advanceCount")
    public Integer getAdvanceCount() {
        return this.advanceCount;
    }
    public void setAdvanceCount(Integer advanceCount) {
        this.advanceCount = advanceCount;
    }
    
    @Column(name="truancyCount")
    public Integer getTruancyCount() {
        return this.truancyCount;
    }
    public void setTruancyCount(Integer truancyCount) {
        this.truancyCount = truancyCount;
    }
    
    @Column(name="leaveCount")
    public Integer getLeaveCount() {
        return this.leaveCount;
    }
    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }
    
    @Column(name="lessonType", nullable = false)
    public Integer getLessonType() {
        return this.lessonType;
    }
    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }
    
    @Column(name="lessonStatus", nullable = false)
    public Integer getLessonStatus() {
        return this.lessonStatus;
    }
    public void setLessonStatus(Integer lessonStatus) {
        this.lessonStatus = lessonStatus;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="lessonDegreeDate", nullable=false, length=10)
    public Date getLessonDegreeDate() {
        return this.lessonDegreeDate;
    }
    public void setLessonDegreeDate(Date lessonDegreeDate) {
        this.lessonDegreeDate = lessonDegreeDate;
    }
    
    @Column(name="note", length=500)
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    
	@Column(name = "startTime", nullable = false, length = 10)
    public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "endTime", nullable = false, length = 10)
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
}