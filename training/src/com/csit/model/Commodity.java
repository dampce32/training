package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 商品
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Commodity")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Commodity extends BaseModel {

	// Fields
	private static final long serialVersionUID = 2113582514291774870L;
	/**
	 * 商品
	 */
	private Integer commodityId;
	/**
	 * 商品分类
	 */
	private CommodityType commodityType;
	/**
	 * 商品单位
	 */
	private Unit unit;
	/**
	 * 商品名称
	 */
	private String commodityName;
	/**
	 * 进价
	 */
	private Double purchasePrice;
	/**
	 * 售价
	 */
	private Double salePrice;
	/**
	 * 规格
	 */
	private String size;
	/**
	 * 库存数量
	 */
	private Integer qtyStore;
	/**
	 * 库存预警数量
	 */
	private Integer qtyWarn;
	/**
	 * 状态
	 * 0 -- 禁用
	 * 1 -- 可用
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 消费明细表
	 */
	private Set<BillDetail> billDetails = new HashSet<BillDetail>(0);
	/**
	 * 库存
	 */
	private Set<Store> stores = new HashSet<Store>(0);
	/**
	 * 仓库领用明细单
	 */
	private Set<UseCommodityDetail> useCommodityDetails = new HashSet<UseCommodityDetail>(
			0);
	/**
	 * 入库单和退货单明细
	 */
	private Set<RecRejDetail> recRejDetails = new HashSet<RecRejDetail>(0);

	// Constructors

	/** default constructor */
	public Commodity() {
	}

	/** minimal constructor */
	public Commodity(String commodityName, Double purchasePrice,
			Double salePrice, Integer qtyStore, Integer status) {
		this.commodityName = commodityName;
		this.purchasePrice = purchasePrice;
		this.salePrice = salePrice;
		this.qtyStore = qtyStore;
		this.status = status;
	}

	/** full constructor */
	public Commodity(CommodityType commodityType, Unit unit,
			String commodityName, Double purchasePrice, Double salePrice,
			String size, Integer qtyStore, Integer status, String note,
			Set<BillDetail> billDetails, Set<Store> stores,
			Set<UseCommodityDetail> useCommodityDetails,
			Set<RecRejDetail> recRejDetails) {
		this.commodityType = commodityType;
		this.unit = unit;
		this.commodityName = commodityName;
		this.purchasePrice = purchasePrice;
		this.salePrice = salePrice;
		this.size = size;
		this.qtyStore = qtyStore;
		this.status = status;
		this.note = note;
		this.billDetails = billDetails;
		this.stores = stores;
		this.useCommodityDetails = useCommodityDetails;
		this.recRejDetails = recRejDetails;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "commodityId", unique = true, nullable = false)
	public Integer getCommodityId() {
		return this.commodityId;
	}

	public void setCommodityId(Integer commodityId) {
		this.commodityId = commodityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commodityTypeId")
	public CommodityType getCommodityType() {
		return this.commodityType;
	}

	public void setCommodityType(CommodityType commodityType) {
		this.commodityType = commodityType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unitId")
	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Column(name = "commodityName", nullable = false, length = 50)
	public String getCommodityName() {
		return this.commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	@Column(name = "purchasePrice", nullable = false, precision = 22, scale = 0)
	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Column(name = "salePrice", nullable = false, precision = 22, scale = 0)
	public Double getSalePrice() {
		return this.salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	@Column(name = "size", length = 50)
	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "qtyStore", nullable = false)
	public Integer getQtyStore() {
		return this.qtyStore;
	}

	public void setQtyStore(Integer qtyStore) {
		this.qtyStore = qtyStore;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commodity")
	public Set<BillDetail> getBillDetails() {
		return this.billDetails;
	}

	public void setBillDetails(Set<BillDetail> billDetails) {
		this.billDetails = billDetails;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commodity")
	public Set<Store> getStores() {
		return this.stores;
	}

	public void setStores(Set<Store> stores) {
		this.stores = stores;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commodity")
	public Set<UseCommodityDetail> getUseCommodityDetails() {
		return this.useCommodityDetails;
	}

	public void setUseCommodityDetails(
			Set<UseCommodityDetail> useCommodityDetails) {
		this.useCommodityDetails = useCommodityDetails;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commodity")
	public Set<RecRejDetail> getRecRejDetails() {
		return this.recRejDetails;
	}

	public void setRecRejDetails(Set<RecRejDetail> recRejDetails) {
		this.recRejDetails = recRejDetails;
	}
	@Column(name = "qtyWarn", nullable = false)
	public Integer getQtyWarn() {
		return qtyWarn;
	}

	public void setQtyWarn(Integer qtyWarn) {
		this.qtyWarn = qtyWarn;
	}

}