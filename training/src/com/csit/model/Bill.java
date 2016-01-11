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
 * @Description: 消费表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @Author lys
 */
@Entity
@Table(name = "T_Bill")
public class Bill extends BaseModel {

	// Fields

	private static final long serialVersionUID = -72048396436630771L;
	/**
	 * 消费单Id
	 */
	private Integer billId;
	/**
	 * 消费单号
	 */
	private String billCode;
	/**
	 * 办理校区
	 */
	private School school;
	/**
	 * 经办人
	 */
	private User user;
	/**
	 * 学员
	 */
	private Student student;
	/**
	 * 账单
	 */
	private Payment payment;
	/**
	 * 消费单类型 
	 * 0 -- 退费 
	 * 1 -- 消费
	 */
	private Integer billType;
	/**
	 * 消费日期
	 */
	private Date billDate;
	/**
	 * 应收金额
	 */
	private Double pay;
	/**
	 * 优惠金额/扣除金额
	 */
	private Double favourable;
	/**
	 * 实收金额/实退金额
	 */
	private Double payed;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 数据写入时间
	 */
	private Date insertDate;
	/**
	 * 消费单明细
	 */
	private Set<BillDetail> billDetails = new HashSet<BillDetail>(0);

	// Constructors

	/** default constructor */
	public Bill() {
	}

	/** minimal constructor */
	public Bill(School school, User user, Student student, Integer billType,
			Double pay, Double payed) {
		this.school = school;
		this.user = user;
		this.student = student;
		this.billType = billType;
		this.pay = pay;
		this.payed = payed;
	}

	/** full constructor */
	public Bill(School school, User user, Student student, Payment payment,
			Integer billType, Date billDate, Double pay, Double favourable,
			Double payed, String note, Date insertDate,
			Set<BillDetail> billDetails) {
		this.school = school;
		this.user = user;
		this.student = student;
		this.payment = payment;
		this.billType = billType;
		this.billDate = billDate;
		this.pay = pay;
		this.favourable = favourable;
		this.payed = payed;
		this.note = note;
		this.insertDate = insertDate;
		this.billDetails = billDetails;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "billId", unique = true, nullable = false)
	public Integer getBillId() {
		return this.billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentId")
	public Payment getPayment() {
		return this.payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Column(name = "billType", nullable = false)
	public Integer getBillType() {
		return this.billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "billDate", length = 10)
	public Date getBillDate() {
		return this.billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	@Column(name = "pay", nullable = false, precision = 22, scale = 0)
	public Double getPay() {
		return this.pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	@Column(name = "favourable", precision = 22, scale = 0)
	public Double getFavourable() {
		return this.favourable;
	}

	public void setFavourable(Double favourable) {
		this.favourable = favourable;
	}

	@Column(name = "payed", nullable = false, precision = 22, scale = 0)
	public Double getPayed() {
		return this.payed;
	}

	public void setPayed(Double payed) {
		this.payed = payed;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "insertDate", length = 10)
	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bill")
	public Set<BillDetail> getBillDetails() {
		return this.billDetails;
	}

	public void setBillDetails(Set<BillDetail> billDetails) {
		this.billDetails = billDetails;
	}

	@Column(name = "billCode", nullable = false, length = 50)
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

}