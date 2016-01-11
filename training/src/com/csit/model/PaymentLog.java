package com.csit.model;

// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @Description:支付宝日志表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "t_PaymentLog")
public class PaymentLog implements java.io.Serializable {

	private static final long serialVersionUID = -2031928899533490236L;

	/**
	 * 支付日志Id
	 */
	private Integer paymentLogId;
	/**
	 * 学员
	 */
	private Student student;
	/**
	 * 帐单
	 */
	private Payment payment;
	/**
	 * 金额
	 */
	private Double pay;
	/**
 	 * 备注
 	 */
     private String note;
	/**
	 * 时间
	 */
	private Date insertDate;
	/**
	 * 支付状态
	 */
	private Integer status;
	/**
	 * 支付宝帐号
	 */
	private String alipayAccount;

	// Constructors

	/** default constructor */
	public PaymentLog() {
	}

	/** minimal constructor */
	public PaymentLog(Integer paymentLogId, Double pay, Date insertDate, Integer status) {
		this.paymentLogId = paymentLogId;
		this.pay = pay;
		this.insertDate = insertDate;
		this.status = status;
	}

	/** full constructor */
	public PaymentLog(Integer paymentLogId, Student student, Payment payment, Double pay, Date insertDate, Integer status, String alipayAccount) {
		this.paymentLogId = paymentLogId;
		this.student = student;
		this.payment = payment;
		this.pay = pay;
		this.insertDate = insertDate;
		this.status = status;
		this.alipayAccount = alipayAccount;
	}

	// Property accessors
	@Id
	@Column(name = "paymentLogID", unique = true, nullable = false)
	public Integer getPaymentLogId() {
		return this.paymentLogId;
	}

	public void setPaymentLogId(Integer paymentLogId) {
		this.paymentLogId = paymentLogId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "studentId")
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentId")
	public Payment getPayment() {
		return this.payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Column(name = "pay", nullable = false, precision = 22, scale = 0)
	public Double getPay() {
		return this.pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "insertDate", nullable = false, length = 10)
	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "alipayAccount", length = 50)
	public String getAlipayAccount() {
		return this.alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}