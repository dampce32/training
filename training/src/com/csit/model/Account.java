package com.csit.model;

// default package

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @Description:帐户表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "t_Account")
public class Account implements java.io.Serializable {

	private static final long serialVersionUID = 2633588119253697554L;

	/**
	 * 帐户Id
	 */
	private Integer accountId;
	/**
	 * 帐户名称
	 */
	private String accountName;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 状态
	 * 0禁用
	 * 1可用
	 */
	private Integer status;
	private Set<Income> incomes = new HashSet<Income>(0);

	// Constructors

	/** default constructor */
	public Account() {
	}

	/** minimal constructor */
	public Account(String accountName, String note, Integer status) {
		this.accountName = accountName;
		this.note = note;
		this.status = status;
	}

	/** full constructor */
	public Account(String accountName, String note, Integer status, Set<Income> incomes) {
		this.accountName = accountName;
		this.note = note;
		this.status = status;
		this.incomes = incomes;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "accountId", unique = true, nullable = false)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Column(name = "AccountName", nullable = false, length = 50)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "note", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	public Set<Income> getIncomes() {
		return this.incomes;
	}

	public void setIncomes(Set<Income> incomes) {
		this.incomes = incomes;
	}

}