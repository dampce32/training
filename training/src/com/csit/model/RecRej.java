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
 * 
 * @Description:入库退货单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_RecRej")
public class RecRej extends BaseModel {

	private static final long serialVersionUID = 8186450069060573102L;
	/**
	 * 入库退货单Id
	 */
	private Integer recRejId;
	/**
	 * 供应商
	 */
	private Supplier supplier;
	/**
	 * 经办人
	 */
	private User user;
	/**
	 * 入库退货单编号
	 */
	private String recRejCode;
	/**
	 * 类型
	 * 进货 1  
	 * 退货 -1
	 */
	private Integer recRejType;
	/**
	 * 进货日期
	 */
	private Date recRejDate;
	/**
	 * 总数量
	 */
	private Integer qtyTotal;
	/**
	 * 总金额
	 */
	private Double amountTotal;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 入库退货单详细
	 */
	private Set<RecRejDetail> recRejDetails = new HashSet<RecRejDetail>(0);

	public RecRej() {
	}

	public RecRej(Supplier supplier, User user, String recRejCode,
			Integer recRejType, Date recRejDate, Integer qtyTotal,
			Double amountTotal) {
		this.supplier = supplier;
		this.user = user;
		this.recRejCode = recRejCode;
		this.recRejType = recRejType;
		this.recRejDate = recRejDate;
		this.qtyTotal = qtyTotal;
		this.amountTotal = amountTotal;
	}

	public RecRej(Supplier supplier, User user, String recRejCode,
			Integer recRejType, Date recRejDate, Integer qtyTotal,
			Double amountTotal, String note, Set<RecRejDetail> recRejDetails) {
		this.supplier = supplier;
		this.user = user;
		this.recRejCode = recRejCode;
		this.recRejType = recRejType;
		this.recRejDate = recRejDate;
		this.qtyTotal = qtyTotal;
		this.amountTotal = amountTotal;
		this.note = note;
		this.recRejDetails = recRejDetails;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "recRejId", unique = true, nullable = false)
	public Integer getRecRejId() {
		return this.recRejId;
	}

	public void setRecRejId(Integer recRejId) {
		this.recRejId = recRejId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplierId", nullable = false)
	public Supplier getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "recRejCode", nullable = false, length = 50)
	public String getRecRejCode() {
		return this.recRejCode;
	}

	public void setRecRejCode(String recRejCode) {
		this.recRejCode = recRejCode;
	}

	@Column(name = "recRejType", nullable = false)
	public Integer getRecRejType() {
		return this.recRejType;
	}

	public void setRecRejType(Integer recRejType) {
		this.recRejType = recRejType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "recRejDate", nullable = false, length = 10)
	public Date getRecRejDate() {
		return this.recRejDate;
	}

	public void setRecRejDate(Date recRejDate) {
		this.recRejDate = recRejDate;
	}

	@Column(name = "qtyTotal", nullable = false)
	public Integer getQtyTotal() {
		return this.qtyTotal;
	}

	public void setQtyTotal(Integer qtyTotal) {
		this.qtyTotal = qtyTotal;
	}

	@Column(name = "amountTotal", nullable = false)
	public Double getAmountTotal() {
		return this.amountTotal;
	}

	public void setAmountTotal(Double amountTotal) {
		this.amountTotal = amountTotal;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recRej")
	public Set<RecRejDetail> getRecRejDetails() {
		return this.recRejDetails;
	}

	public void setRecRejDetails(Set<RecRejDetail> recRejDetails) {
		this.recRejDetails = recRejDetails;
	}

}