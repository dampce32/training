package com.csit.model;

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


/**
 * StuReply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_StuReply")

public class StuReply  extends BaseModel { 
	
	private static final long serialVersionUID = -7290991132923721583L;

     private Integer replyId;
     private User user;
     private Student student;
     private Date replyDate;
     private String content;
     private Date nextReplyDate;
     private Date insertTime;



    /** default constructor */
    public StuReply() {
    }

	/** minimal constructor */
    public StuReply(Date replyDate, String content, Date insertTime) {
        this.replyDate = replyDate;
        this.content = content;
        this.insertTime = insertTime;
    }
    
    /** full constructor */
    public StuReply(User user, Student student, Date replyDate, String content, Date nextReplyDate, Date insertTime) {
        this.user = user;
        this.student = student;
        this.replyDate = replyDate;
        this.content = content;
        this.nextReplyDate = nextReplyDate;
        this.insertTime = insertTime;
    }

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="replyID", unique=true, nullable=false)

    public Integer getReplyId() {
        return this.replyId;
    }
    
    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
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
    @Temporal(TemporalType.DATE)
    @Column(name="replyDate", nullable=false, length=10)

    public Date getReplyDate() {
        return this.replyDate;
    }
    
    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }
    
    @Column(name="Content", nullable=false, length=500)

    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="nextReplyDate", length=10)

    public Date getNextReplyDate() {
        return this.nextReplyDate;
    }
    
    public void setNextReplyDate(Date nextReplyDate) {
        this.nextReplyDate = nextReplyDate;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="insertTime", nullable=false, length=10)

    public Date getInsertTime() {
        return this.insertTime;
    }
    
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
   








}