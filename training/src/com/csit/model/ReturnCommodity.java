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
import javax.persistence.Transient;

/**
 * 
 * @Description: 仓库领用单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_ReturnCommodity")
public class ReturnCommodity extends BaseModel {

	// Fields
	private static final long serialVersionUID = -2838915799642510429L;
	/**
	 * 商品归还单Id
	 */
	private Integer returnCommodityId;
	/**
	 * 领用人
	 */
	private User user;
	/**
	 * 经办人
	 */
	private User handler;
	/**
	 * 领用日期
	 */
	private Date returnDate;
	/**
	 * 总数量
	 */
	private Integer qtyTotal;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 仓库领用明细单
	 */
	private Set<ReturnCommodityDetail> returnCommodityDetails = new HashSet<ReturnCommodityDetail>(
			0);

	
	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "returnCommodityId", unique = true, nullable = false)
	public Integer getReturnCommodityId() {
		return this.returnCommodityId;
	}

	public void setReturnCommodityId(Integer returnCommodityId) {
		this.returnCommodityId = returnCommodityId;
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
	@JoinColumn(name = "handlerId", nullable = false)
	public User getHandler() {
		return this.handler;
	}

	public void setHandler(User handler) {
		this.handler = handler;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "returnDate", nullable = false, length = 10)
	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	@Column(name = "qtyTotal", nullable = false)
	public Integer getQtyTotal() {
		return this.qtyTotal;
	}

	public void setQtyTotal(Integer qtyTotal) {
		this.qtyTotal = qtyTotal;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "returnCommodity")
	public Set<ReturnCommodityDetail> getReturnCommodityDetails() {
		return this.returnCommodityDetails;
	}

	public void setReturnCommodityDetails(
			Set<ReturnCommodityDetail> returnCommodityDetails) {
		this.returnCommodityDetails = returnCommodityDetails;
	}
	
	@Transient
	public String getHandleName() {
		return handler.getUserName();
	}
	@Transient
	public Integer getHandleId() {
		return handler.getUserId();
	}

}