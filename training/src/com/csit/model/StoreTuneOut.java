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

/**
 * 
 * @Description: 仓库调拨单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_StoreTuneOut")
public class StoreTuneOut extends BaseModel {

	// Fields
	private static final long serialVersionUID = -2838915799642510429L;
	
	/**
	 * 仓库调拨单Id
	 */
	private Integer storeTuneOutId;
	
	/**
	 * 调拨日期
	 */
	private Date tuneOutDate;
	
	/**
	 * 数量
	 */
	private int qty;
	
	/**
	 * 备注
	 */
	private String note;
	
	/**
	 * 调入仓库
	 */
	private Warehouse tuneInWarehouse;
	
	/**
	 * 调出仓库
	 */
	private Warehouse tuneOutWarehouse;
	
	/**
	 * 调拨商品
	 */
	private Commodity commodity;
	
	/**
	 * 经办人
	 */
	private User user;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "storeTuneOutId", unique = true, nullable = false)
	public Integer getStoreTuneOutId() {
		return storeTuneOutId;
	}

	public void setStoreTuneOutId(Integer storeTuneOutId) {
		this.storeTuneOutId = storeTuneOutId;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "tuneOutDate", nullable = false, length = 10)
	public Date getTuneOutDate() {
		return tuneOutDate;
	}

	public void setTuneOutDate(Date tuneOutDate) {
		this.tuneOutDate = tuneOutDate;
	}
	@Column(name="qty")
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@Column(name="note")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tuneInWarehouseId")
	public Warehouse getTuneInWarehouse() {
		return tuneInWarehouse;
	}

	public void setTuneInWarehouse(Warehouse tuneInWarehouse) {
		this.tuneInWarehouse = tuneInWarehouse;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tuneOutWarehouseId")
	public Warehouse getTuneOutWarehouse() {
		return tuneOutWarehouse;
	}

	public void setTuneOutWarehouse(Warehouse tuneOutWarehouse) {
		this.tuneOutWarehouse = tuneOutWarehouse;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commodityId")
	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Transient
	public String getTuneOutWarehouseName() {
		return tuneOutWarehouse.getWarehouseName();
	}
	@Transient
	public Integer getTuneOutWarehouseId() {
		return tuneOutWarehouse.getWarehouseId();
	}
	@Transient
	public String getTuneInWarehouseName() {
		return tuneInWarehouse.getWarehouseName();
	}
	@Transient
	public Integer getTuneInWarehouseId() {
		return tuneInWarehouse.getWarehouseId();
	}
}