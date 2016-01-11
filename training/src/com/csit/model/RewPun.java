package com.csit.model;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 奖惩
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_RewPun")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RewPun extends BaseModel {

	// Fields
	private static final long serialVersionUID = 2113582514291774870L;
	/**
	 * 奖惩
	 */
	private Integer rewPunId;
	/**
	 * 奖惩分类
	 */
	private RewPunType rewPunType;
	/**
	 * 奖惩员工
	 */
	private User user;
	/**
	 * 经办人
	 */
	private User handler;
	/**
	 * 奖惩金额
	 */
	private Double rewPunPrice;
	/**
	 * 奖惩时间
	 */
	private Date rewPunDate;

	/**
	 * 奖惩理由
	 */
	private String note;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rewPunId", unique = true, nullable = false)
	public Integer getRewPunId() {
		return this.rewPunId;
	}

	public void setRewPunId(Integer rewPunId) {
		this.rewPunId = rewPunId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rewPunTypeId")
	public RewPunType getRewPunType() {
		return this.rewPunType;
	}

	public void setRewPunType(RewPunType rewPunType) {
		this.rewPunType = rewPunType;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "rewPunPrice", nullable = false, precision = 22, scale = 0)
	public Double getRewPunPrice() {
		return rewPunPrice;
	}

	public void setRewPunPrice(Double rewPunPrice) {
		this.rewPunPrice = rewPunPrice;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "rewPunDate", nullable = false, length = 10)
	public Date getRewPunDate() {
		return rewPunDate;
	}

	public void setRewPunDate(Date rewPunDate) {
		this.rewPunDate = rewPunDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "handlerId", nullable = false)
	public User getHandler() {
		return this.handler;
	}

	public void setHandler(User handler) {
		this.handler = handler;
	}
	@Transient
	public String getHandlerName() {
		return this.handler.getUserName();
	}
	@Transient
	public Integer getHandlerId() {
		return this.handler.getUserId();
	}
}