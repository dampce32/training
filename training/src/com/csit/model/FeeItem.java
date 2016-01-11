package com.csit.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 消费项
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_FeeItem")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class FeeItem extends BaseModel {

	// Fields
	private static final long serialVersionUID = 8285258446603270213L;
	/**
	 * 消费项Id
	 */
	private Integer feeItemId;
	/**
	 * 消费项名称
	 */
	private String feeItemName;
	/**
	 * 金额
	 */
	private Double fee;
	/**
	 * 状态
	 * 0 -- 禁用
	 * 1 -- 可用
	 */
	private Integer status;
	/**
	 * 消费明细表
	 */
	private Set<BillDetail> billDetails = new HashSet<BillDetail>(0);

	// Constructors

	/** default constructor */
	public FeeItem() {
	}

	/** minimal constructor */
	public FeeItem(Integer status) {
		this.status = status;
	}

	/** full constructor */
	public FeeItem(String feeItemName, Double fee, Integer status,
			Set<BillDetail> billDetails) {
		this.feeItemName = feeItemName;
		this.fee = fee;
		this.status = status;
		this.billDetails = billDetails;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "feeItemId", unique = true, nullable = false)
	public Integer getFeeItemId() {
		return this.feeItemId;
	}

	public void setFeeItemId(Integer feeItemId) {
		this.feeItemId = feeItemId;
	}

	@Column(name = "feeItemName", length = 50)
	public String getFeeItemName() {
		return this.feeItemName;
	}

	public void setFeeItemName(String feeItemName) {
		this.feeItemName = feeItemName;
	}

	@Column(name = "fee", precision = 22, scale = 0)
	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "feeItem")
	public Set<BillDetail> getBillDetails() {
		return this.billDetails;
	}

	public void setBillDetails(Set<BillDetail> billDetails) {
		this.billDetails = billDetails;
	}

}