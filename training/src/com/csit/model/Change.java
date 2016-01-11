package com.csit.model;
// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 
 * @Description:	学院异动表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-1
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name="t_Change")

public class Change  implements java.io.Serializable {

	private static final long serialVersionUID = -5575542116733761499L;
	
	/**
	 * 异动id
	 */
	private Integer changeId;
	/**
	 *经办人
	 */
     private User user;
     /**
      * 异动学员
      */
     private Student student;
     /**
      * 学员账户表
      */
     private Payment payment;
     /**
      * 异动学员班级
      */
     private StuClass stuClass;
     /**
      * 学员转到的班级
      */
     private StuClass newStuClass;
     /**
      * 异动类型。（3转班，4休学，5退学，6弃学）
      */
     private Integer changeType;
     /**
      * 异动日期
      */
     private Date date;
     /**
      * 返还金额
      */
     private Double intoAccount;
     /**
      * 返还课时
      */
     private Integer lessons;
     /**
      * 到期日期
      */
     private Date expireDate;
     /**
      * 备注
      */
     private String note;


    // Constructors

    /** default constructor */
    public Change() {
    }

	/** minimal constructor */
    public Change(Integer changeType, Double intoAccount, Integer lessons) {
        this.changeType = changeType;
        this.intoAccount = intoAccount;
        this.lessons = lessons;
    }
    
    /** full constructor */
    public Change(User user, Student student, Payment payment, StuClass stuClass, Integer changeType, Date date, Double intoAccount, Integer lessons, Date expireDate, String note) {
        this.user = user;
        this.student = student;
        this.payment = payment;
        this.stuClass = stuClass;
        this.changeType = changeType;
        this.date = date;
        this.intoAccount = intoAccount;
        this.lessons = lessons;
        this.expireDate = expireDate;
        this.note = note;
    }

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="changeId", unique=true, nullable=false)

    public Integer getChangeId() {
        return this.changeId;
    }
    
    public void setChangeId(Integer changeId) {
        this.changeId = changeId;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="userId")

    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="studentId")

    public Student getStudent() {
        return this.student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="paymentId")

    public Payment getPayment() {
        return this.payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="stuClassId")

    public StuClass getStuClass() {
        return this.stuClass;
    }
    
    public void setStuClass(StuClass stuClass) {
        this.stuClass = stuClass;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="newStuClassId")
    
    public StuClass getNewStuClass() {
    	return this.newStuClass;
    }
    
    public void setNewStuClass(StuClass newStuClass) {
    	this.newStuClass = newStuClass;
    }
    
    @Column(name="changeType", nullable=false)

    public Integer getChangeType() {
        return this.changeType;
    }
    
    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="date", length=10)

    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    @Column(name="intoAccount", nullable=false, precision=22, scale=0)

    public Double getIntoAccount() {
        return this.intoAccount;
    }
    
    public void setIntoAccount(Double intoAccount) {
        this.intoAccount = intoAccount;
    }
    
    @Column(name="lessons", nullable=false)

    public Integer getLessons() {
        return this.lessons;
    }
    
    public void setLessons(Integer lessons) {
        this.lessons = lessons;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="expireDate", length=10)

    public Date getExpireDate() {
        return this.expireDate;
    }
    
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    
    @Column(name="note", length=500)

    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    @Transient
	public String getNewStuClassName() {
    	if(newStuClass!=null){
    		return newStuClass.getClazz().getClassName();
    	}else{
    		return null;
    	}
	}
    @Transient
    public Integer getNewStuClassId() {
    	if(newStuClass!=null){
    		return newStuClass.getClazz().getClassId();
    	}else{
    		return null;
    	}
    }
    @Transient
    public String getClassName() {
    	return stuClass.getClazz().getClassName();
    }
    @Transient
    public Integer getClassId() {
    	return stuClass.getClazz().getClassId();
    }

}