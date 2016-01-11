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
 * @Description:入库单和退货单明细
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_RecRejDetail")
public class RecRejDetail extends BaseModel {

	private static final long serialVersionUID = 1637965555557713287L;

	/**
	 * 入库退货单详细Id
	 */
	private Integer recRejDetailId;
	/**
	 * 入库退货单Id
	 */
	private RecRej recRej;
	/**
	 * 仓库Id
	 */
	private Warehouse warehouse;
	/**
	 * 商品Id
	 */
	private Commodity commodity;
	/**
	 * 进价
	 */
	private Double price;
	/**
	 * 数量
	 */
	private Integer qty;

	public RecRejDetail() {
	}

	public RecRejDetail(RecRej recRej, Warehouse warehouse,
			Commodity commodity, Double price, Integer qty) {
		this.recRej = recRej;
		this.warehouse = warehouse;
		this.commodity = commodity;
		this.price = price;
		this.qty = qty;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "recRejDetailId", unique = true, nullable = false)
	public Integer getRecRejDetailId() {
		return this.recRejDetailId;
	}

	public void setRecRejDetailId(Integer recRejDetailId) {
		this.recRejDetailId = recRejDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recRejId")
	public RecRej getRecRej() {
		return this.recRej;
	}

	public void setRecRej(RecRej recRej) {
		this.recRej = recRej;
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

	@Column(name = "price", precision = 22, scale = 0)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "qty")
	public Integer getQty() {
		return this.qty;
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
	@Transient
	public Double getTotalPrice() {
		return price*qty;
	}

}