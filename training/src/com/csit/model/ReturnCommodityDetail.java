package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @Description: 仓库归还明细单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_ReturnCommodityDetail")
public class ReturnCommodityDetail extends BaseModel {

	// Fields
	private static final long serialVersionUID = -7546745030337182014L;
	/**
	 * 仓库归还用明细单Id
	 */
	private Integer returnCommodityDetailId;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 仓库领用单
	 */
	private ReturnCommodity returnCommodity;
	/**
	 * 商品领用明细单
	 */
	private UseCommodityDetail useCommodityDetail;
	/**
	 * 数量
	 */
	private Integer qty;

	// Constructors

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "returnCommodityDetailId", unique = true, nullable = false)
	public Integer getReturnCommodityDetailId() {
		return this.returnCommodityDetailId;
	}

	public void setReturnCommodityDetailId(Integer returnCommodityDetailId) {
		this.returnCommodityDetailId = returnCommodityDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouseId", nullable = false)
	public Warehouse getWarehouse() {
		return this.warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "useCommodityId", nullable = false)
	public ReturnCommodity getReturnCommodity() {
		return this.returnCommodity;
	}

	public void setReturnCommodity(ReturnCommodity returnCommodity) {
		this.returnCommodity = returnCommodity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "useCommodityDetailId", nullable = false)
	public UseCommodityDetail getUseCommodityDetail() {
		return this.useCommodityDetail;
	}

	public void setUseCommodityDetail(UseCommodityDetail useCommodityDetail) {
		this.useCommodityDetail = useCommodityDetail;
	}

	@Column(name = "qty", nullable = false)
	public Integer getQty() {
		return this.qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}
	@Transient
	public String getCommodityTypeName() {
		return useCommodityDetail.getCommodity().getCommodityType().getCommodityTypeName();
	}
	@Transient
	public String getCommodityName() {
		return useCommodityDetail.getCommodity().getCommodityName();
	}
	@Transient
	public String getUnitName() {
		return  useCommodityDetail.getCommodity().getUnit().getUnitName();
	}
}