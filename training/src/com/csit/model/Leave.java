package com.csit.model;
// default package

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
 * @Description:请假表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name="t_leave" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Leave  implements java.io.Serializable {


	private static final long serialVersionUID = -6357031259160961854L;

	/**
	 * 请假Id
	 */
     private Integer leaveId;
     /**
      * 请假学员
      */
     private Student student;
     /**
      * 经办人
      */
     private User user;
     /**
      * 办理日期
      */
     private Date date;
     /**
      * 开始日期
      */
     private Date beginDate;
     /**
      * 结束日期
      */
     private Date endDate;
     /**
      * 备注
      */
     private String note;


    // Constructors

    /** default constructor */
    public Leave() {
    }

	/** minimal constructor */
    public Leave(Integer leaveId, Date date, Date beginDate, Date endDate) {
        this.leaveId = leaveId;
        this.date = date;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
    
    /** full constructor */
    public Leave(Integer leaveId, Student student, User user, Date date, Date beginDate, Date endDate, String note) {
        this.leaveId = leaveId;
        this.student = student;
        this.user = user;
        this.date = date;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.note = note;
    }

   
    // Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "leaveId", unique = true, nullable = false)
    public Integer getLeaveId() {
        return this.leaveId;
    }
    
    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="userId")

    public Student getStudent() {
        return this.student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="handlerId")

    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="date", nullable=false, length=10)

    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="beginDate", nullable=false, length=10)

    public Date getBeginDate() {
        return this.beginDate;
    }
    
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="endDate", nullable=false, length=10)

    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    @Column(name="note", length=500)
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}