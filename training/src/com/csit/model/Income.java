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

/**
 * @Description:记帐表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "t_Income")
public class Income implements java.io.Serializable {

	private static final long serialVersionUID = 9103336403549196282L;
	/**
	 * 帐目Id
	 */
	private Integer incomeId;
	/**
	 * 校区
	 */
	private School school;
	/**
	 * 帐户
	 */
	private Account account;
	/**
	 * 录入人
	 */
	private User user;
	/**
	 * 帐目分类
	 */
	private IncomeClass incomeClass;
	/**
	 * 建帐日期
	 */
	private Date accountDate;
	/**
	 * 系统时间
	 */
	private Date insertDate;
	/**
	 * 金额
	 */
	private Double price;
	/**
	 * 备注
	 */
	private String note;

	// Constructors

	/** default constructor */
	public Income() {
	}

	/** minimal constructor */
	public Income(Integer incomeId, Date accountDate, Date insertDate, Double price) {
		this.incomeId = incomeId;
		this.accountDate = accountDate;
		this.insertDate = insertDate;
		this.price = price;
	}

	/** full constructor */
	public Income(Integer incomeId, School school, Account account, User user, IncomeClass incomeClass, Date accountDate, Date insertDate, Double price, String note) {
		this.incomeId = incomeId;
		this.school = school;
		this.account = account;
		this.user = user;
		this.incomeClass = incomeClass;
		this.accountDate = accountDate;
		this.insertDate = insertDate;
		this.price = price;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "incomeId", unique = true, nullable = false)
	public Integer getIncomeId() {
		return this.incomeId;
	}

	public void setIncomeId(Integer incomeId) {
		this.incomeId = incomeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId")
	public School getSchool() {
		return this.school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId")
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "incomeClassId")
	public IncomeClass getIncomeClass() {
		return this.incomeClass;
	}

	public void setIncomeClass(IncomeClass incomeClass) {
		this.incomeClass = incomeClass;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AccountDate", nullable = false, length = 10)
	public Date getAccountDate() {
		return this.accountDate;
	}

	public void setAccountDate(Date accountDate) {
		this.accountDate = accountDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "InsertDate", nullable = false, length = 10)
	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Column(name = "price", nullable = false, precision = 22, scale = 0)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "note", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}