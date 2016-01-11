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
 * @Description:帐目分类表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "t_incomeclass")
public class IncomeClass implements java.io.Serializable {

	private static final long serialVersionUID = 7919441326153596122L;

	/**
	 * 帐目分类Id
	 */
	private Integer incomeClassId;
	/**
	 * 帐目分类名称
	 */
	private String incomeClassName;
	/**
	 * 类型
	 * 0——支出
	 * 1——收入
	 */
	private Integer type;
	/**
	 * 状态
	 * 0——禁用
	 * 1——启用
	 */
	private Integer status;
	private Set<Income> incomes = new HashSet<Income>(0);

	// Constructors

	/** default constructor */
	public IncomeClass() {
	}

	/** minimal constructor */
	public IncomeClass(String incomeClassName, Integer type, Integer status) {
		this.incomeClassName = incomeClassName;
		this.type = type;
		this.status = status;
	}

	/** full constructor */
	public IncomeClass(String incomeClassName, Integer type, Integer status, Set<Income> incomes) {
		this.incomeClassName = incomeClassName;
		this.type = type;
		this.status = status;
		this.incomes = incomes;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "incomeClassId", unique = true, nullable = false)
	public Integer getIncomeClassId() {
		return this.incomeClassId;
	}

	public void setIncomeClassId(Integer incomeClassId) {
		this.incomeClassId = incomeClassId;
	}

	@Column(name = "incomeClassName", nullable = false, length = 50)
	public String getIncomeClassName() {
		return this.incomeClassName;
	}

	public void setIncomeClassName(String incomeClassName) {
		this.incomeClassName = incomeClassName;
	}

	@Column(name = "type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incomeClass")
	public Set<Income> getIncomes() {
		return this.incomes;
	}

	public void setIncomes(Set<Income> incomes) {
		this.incomes = incomes;
	}

}