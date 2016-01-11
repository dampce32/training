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
@Table(name = "T_UseCommodity")
public class UseCommodity extends BaseModel {

	// Fields
	private static final long serialVersionUID = -2838915799642510429L;
	/**
	 * 仓库领用单Id
	 */
	private Integer useCommodityId;
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
	private Date useDate;
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
	private Set<UseCommodityDetail> useCommodityDetails = new HashSet<UseCommodityDetail>(
			0);

	// Constructors

	/** default constructor */
	public UseCommodity() {
	}

	/** minimal constructor */
	public UseCommodity(User user, User handler, Date useDate, Integer qtyTotal) {
		this.user = user;
		this.handler = handler;
		this.useDate = useDate;
		this.qtyTotal = qtyTotal;
	}

	/** full constructor */
	public UseCommodity(User user, User handler, Date useDate,
			Integer qtyTotal, String note,
			Set<UseCommodityDetail> useCommodityDetails) {
		this.user = user;
		this.handler = handler;
		this.useDate = useDate;
		this.qtyTotal = qtyTotal;
		this.note = note;
		this.useCommodityDetails = useCommodityDetails;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "useCommodityId", unique = true, nullable = false)
	public Integer getUseCommodityId() {
		return this.useCommodityId;
	}

	public void setUseCommodityId(Integer useCommodityId) {
		this.useCommodityId = useCommodityId;
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
	@Column(name = "useDate", nullable = false, length = 10)
	public Date getUseDate() {
		return this.useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "useCommoidty")
	public Set<UseCommodityDetail> getUseCommodityDetails() {
		return this.useCommodityDetails;
	}

	public void setUseCommodityDetails(
			Set<UseCommodityDetail> useCommodityDetails) {
		this.useCommodityDetails = useCommodityDetails;
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