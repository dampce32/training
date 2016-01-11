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

/***
 * 
 * @Description:账单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Payment")
public class Payment extends BaseModel {

	private static final long serialVersionUID = -8865398026256728169L;
	/**
	 * 账单Id
	 */
	private Integer paymentId;
	/**
	 * 办理校区
	 */
	private School school;
	/**
	 * 经办人
	 */
	private User user;
	/**
	 * 学生
	 */
	private Student student;
	/**
	 * 账单类型
	 * 1--交费  客服 财务
	 * 2--退费  财务
	 * 3--借款  客服 财务
	 * 4--扣除借款  客服 财务
	 * 5--业务扣费  消费单产生 客服
	 * 6--业务退费	退货办理产生,而且退货办理只能办理未处理的消费单 客服 财务
	 */
	private Integer paymentType;
	/**
	 * 支付方式
	 * 1--现金，
	 * 2--刷卡，
	 * 3--转账，
	 * 4--支票，
	 * 5--网银，
	 * 6---支付宝，
	 * 7--其它
	 */
	private Integer payway;
	/**
	 * 金额
	 */
	private Double payMoney;
	/**
	 * 办理时间
	 */
	private Date transactionDate;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 借款到期时间
	 */
	private Date creditExpiration;
	/**
	 * 数据写入时间
	 */
	private Date insertTime;
	/**
	 * 消费单
	 */
	private Set<Bill> bills = new HashSet<Bill>(0);

	public Payment() {
	}

	public Payment(School school, User user, Student student,
			Integer paymentType, Integer payway, Double payMoney) {
		this.school = school;
		this.user = user;
		this.student = student;
		this.paymentType = paymentType;
		this.payway = payway;
		this.payMoney = payMoney;
	}

	public Payment(School school, User user, Student student,
			Integer paymentType, Integer payway, Double payMoney,
			Date transactionDate, String note, Date creditExpiration,
			Date insertTime, Set<Bill> bills) {
		this.school = school;
		this.user = user;
		this.student = student;
		this.paymentType = paymentType;
		this.payway = payway;
		this.payMoney = payMoney;
		this.transactionDate = transactionDate;
		this.note = note;
		this.creditExpiration = creditExpiration;
		this.insertTime = insertTime;
		this.bills = bills;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "paymentId", unique = true, nullable = false)
	public Integer getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
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

	@Column(name = "paymentType", nullable = false)
	public Integer getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	@Column(name = "payway")
	public Integer getPayway() {
		return this.payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}

	@Column(name = "payMoney", nullable = false, precision = 22, scale = 0)
	public Double getPayMoney() {
		return this.payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "transactionDate", length = 10)
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "creditExpiration", length = 10)
	public Date getCreditExpiration() {
		return this.creditExpiration;
	}

	public void setCreditExpiration(Date creditExpiration) {
		this.creditExpiration = creditExpiration;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "insertTime", length = 10)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
	public Set<Bill> getBills() {
		return this.bills;
	}

	public void setBills(Set<Bill> bills) {
		this.bills = bills;
	}

}