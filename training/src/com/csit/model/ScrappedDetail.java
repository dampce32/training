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
 * @Description:损溢单明细
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_ScrappedDetail")
public class ScrappedDetail extends BaseModel {

	private static final long serialVersionUID = 1637965555557713287L;

	/**
	 * 入库退货单详细Id
	 */
	private Integer scrappedDetailId;
	/**
	 * 入库退货单Id
	 */
	private Scrapped scrapped;
	/**
	 * 仓库Id
	 */
	private Warehouse warehouse;
	/**
	 * 商品Id
	 */
	private Commodity commodity;
	/**
	 * 数量
	 */
	private Integer qty;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scrappedDetailId", unique = true, nullable = false)
	public Integer getScrappedDetailId() {
		return scrappedDetailId;
	}
	public void setScrappedDetailId(Integer scrappedDetailId) {
		this.scrappedDetailId = scrappedDetailId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scrappedId")
	public Scrapped getScrapped() {
		return scrapped;
	}
	public void setScrapped(Scrapped scrapped) {
		this.scrapped = scrapped;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouseId")
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commodityId")
	public Commodity getCommodity() {
		return commodity;
	}
	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}
	@Column(name = "qty")
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	@Transient
	public String getCommodityTypeName() {
		return commodity.getCommodityType().getCommodityTypeName();
	}
	@Transient
	public String getUnitName() {
		return commodity.getUnit().getUnitName();
	}
}