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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 
 * @Description: 仓库领用明细单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_UseCommodityDetail")
public class UseCommodityDetail extends BaseModel {

	// Fields
	private static final long serialVersionUID = -7546745030337182014L;
	/**
	 * 仓库领用明细单Id
	 */
	private Integer useCommodityDetailId;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 仓库领用单
	 */
	private UseCommodity useCommoidty;
	/**
	 * 商品
	 */
	private Commodity commodity;
	/**
	 * 数量
	 */
	private Integer qty;
	/**
	 * 是否需要归还 
	 * 0 -- 不需要归还 
	 * 1 -- 需要归还
	 */
	private Integer isNeedReturn;
	/**
	 * 归还日期
	 */
	private Date returnDate;
	/**
	 * 归还状态 
	 * 0 -- 已还 
	 * 1 -- 未还
	 * 2 -- 部分归还
	 */
	private Integer returnStatus;
	
	/**
	 * 归还单
	 */
	private Set<ReturnCommodityDetail> returnCommodityDetails = new HashSet<ReturnCommodityDetail>(
			0);
	
	// Constructors

	/** default constructor */
	public UseCommodityDetail() {
	}

	/** minimal constructor */
	public UseCommodityDetail(Warehouse warehouse, UseCommodity useCommoidty,
			Commodity commodity, Integer qty, Integer isNeedReturn) {
		this.warehouse = warehouse;
		this.useCommoidty = useCommoidty;
		this.commodity = commodity;
		this.qty = qty;
		this.isNeedReturn = isNeedReturn;
	}

	/** full constructor */
	public UseCommodityDetail(Warehouse warehouse, UseCommodity useCommoidty,
			Commodity commodity, Integer qty, Integer isNeedReturn,
			Date returnDate, Integer returnStatus) {
		this.warehouse = warehouse;
		this.useCommoidty = useCommoidty;
		this.commodity = commodity;
		this.qty = qty;
		this.isNeedReturn = isNeedReturn;
		this.returnDate = returnDate;
		this.returnStatus = returnStatus;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "useCommodityDetailId", unique = true, nullable = false)
	public Integer getUseCommodityDetailId() {
		return this.useCommodityDetailId;
	}

	public void setUseCommodityDetailId(Integer useCommodityDetailId) {
		this.useCommodityDetailId = useCommodityDetailId;
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
	public UseCommodity getUseCommoidty() {
		return this.useCommoidty;
	}

	public void setUseCommoidty(UseCommodity useCommoidty) {
		this.useCommoidty = useCommoidty;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commodityId", nullable = false)
	public Commodity getCommodity() {
		return this.commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	@Column(name = "qty", nullable = false)
	public Integer getQty() {
		return this.qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Column(name = "isNeedReturn", nullable = false)
	public Integer getIsNeedReturn() {
		return this.isNeedReturn;
	}

	public void setIsNeedReturn(Integer isNeedReturn) {
		this.isNeedReturn = isNeedReturn;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "returnDate", length = 10)
	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	@Column(name = "returnStatus")
	public Integer getReturnStatus() {
		return this.returnStatus;
	}

	public void setReturnStatus(Integer returnStatus) {
		this.returnStatus = returnStatus;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "useCommodityDetail")
	public Set<ReturnCommodityDetail> getReturnCommodityDetails() {
		return returnCommodityDetails;
	}

	public void setReturnCommodityDetails(Set<ReturnCommodityDetail> returnCommodityDetails) {
		this.returnCommodityDetails = returnCommodityDetails;
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
	public String getUserName() {
		return useCommoidty.getUser().getUserName();
	}
	@Transient
	public Integer getReturnQty() {
		if(returnCommodityDetails==null||returnCommodityDetails.size()==0){
			return 0;
		}else{
			int returnTotal=0;
			for(ReturnCommodityDetail returnCommodityDetail:returnCommodityDetails){
				returnTotal+=returnCommodityDetail.getQty();
			}
			return returnTotal;
		}
	}
	@Transient
	public Integer getUnReturnQty() {
		if(returnCommodityDetails==null||returnCommodityDetails.size()==0){
			return qty;
		}else{
			int returnTotal=getReturnQty();
			return qty-returnTotal;
		}
	}
}