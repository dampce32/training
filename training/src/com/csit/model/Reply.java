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
 * @Description:咨询回访表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name="t_reply")

public class Reply  extends BaseModel {


    
	private static final long serialVersionUID = -4921664857971948730L;

	/**
	 * 咨询回访单Id
	 */
     private Integer replyId;
     /**
 	 * 咨询信息单Id
 	 */
     private Potential potential;
     /**
 	 * 回访人
 	 */
     private User user;
     /**
 	 * 潜在学员状态
 	 */
     private PotentialStuStatus potentialStuStatus;
     /**
 	 * 回访日期
 	 */
     private Date replyDate;
     /**
 	 * 内容
 	 */
     private String content;
     /**
 	 * 下次回访日期
 	 */
     private Date nextReplyDate;



    public Reply() {
    }

    public Reply(Potential potential, User user, PotentialStuStatus potentialStuStatus, Date replyDate, String content) {
        this.potential = potential;
        this.user = user;
        this.potentialStuStatus = potentialStuStatus;
        this.replyDate = replyDate;
        this.content = content;
    }
    
    public Reply(Potential potential, User user, PotentialStuStatus potentialStuStatus, Date replyDate, String content, Date nextReplyDate) {
        this.potential = potential;
        this.user = user;
        this.potentialStuStatus = potentialStuStatus;
        this.replyDate = replyDate;
        this.content = content;
        this.nextReplyDate = nextReplyDate;
    }

   
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="replyId", unique=true, nullable=false)

    public Integer getReplyId() {
        return this.replyId;
    }
    
    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="potentialId", nullable=false)

    public Potential getPotential() {
        return this.potential;
    }
    
    public void setPotential(Potential potential) {
        this.potential = potential;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="userId", nullable=false)

    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="potentialStuStatusId", nullable=false)

    public PotentialStuStatus getPotentialStuStatus() {
        return this.potentialStuStatus;
    }
    
    public void setPotentialStuStatus(PotentialStuStatus potentialStuStatus) {
        this.potentialStuStatus = potentialStuStatus;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="replyDate", nullable=false, length=10)

    public Date getReplyDate() {
        return this.replyDate;
    }
    
    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }
    
	@Column(name = "content", nullable = false, length = 500)

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
   








}