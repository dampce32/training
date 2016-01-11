package com.csit.model;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @Description:库存
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Store")
public class Store extends BaseModel {

	private static final long serialVersionUID = 3489494251706085763L;
	// Fields

	/**
	 * 库存Id
	 */
	private Integer storeId;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 商品
	 */
	private Commodity commodity;
	/**
	 * 库存数量
	 */
	private Integer qtyStore;

	// Constructors

	/** default constructor */
	public Store() {
	}

	/** full constructor */
	public Store(Warehouse warehouse, Commodity commodity, Integer qtyStore) {
		this.warehouse = warehouse;
		this.commodity = commodity;
		this.qtyStore = qtyStore;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "storeId", unique = true, nullable = false)
	public Integer getStoreId() {
		return this.storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouseId")
	public Warehouse getWarehouse() {
		return this.warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commodityId")
	public Commodity getCommodity() {
		return this.commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	@Column(name = "qtyStore")
	public Integer getQtyStore() {
		return this.qtyStore;
	}

	public void setQtyStore(Integer qtyStore) {
		this.qtyStore = qtyStore;
	}
	@Transient
	public String getUnitName() {
		return commodity.getUnit().getUnitName();
	}
	@Transient
	public String getCommodityTypeName() {
		return commodity.getCommodityType().getCommodityTypeName();
	}
	@Transient
	public Integer getQtyTotal() {
		return commodity.getQtyStore();
	}
}