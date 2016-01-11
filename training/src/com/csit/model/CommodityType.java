package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 商品分类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_CommodityType")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CommodityType extends BaseModel {

	// Fields

	private static final long serialVersionUID = -2204947592309405967L;
	/**
	 * 商品分类Id
	 */
	private Integer commodityTypeId;
	/**
	 * 商品分类名称
	 */
	private String commodityTypeName;
	/**
	 * 状态
	 * 0 -- 禁用 
	 * 1 -- 可用
	 */
	private Integer status;
	/**
	 * 商品
	 */
	private Set<Commodity> commodities = new HashSet<Commodity>(0);

	// Constructors

	/** default constructor */
	public CommodityType() {
	}

	/** minimal constructor */
	public CommodityType(String commodityTypeName, Integer status) {
		this.commodityTypeName = commodityTypeName;
		this.status = status;
	}

	/** full constructor */
	public CommodityType(String commodityTypeName, Integer status,
			Set<Commodity> commodities) {
		this.commodityTypeName = commodityTypeName;
		this.status = status;
		this.commodities = commodities;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "commodityTypeId", unique = true, nullable = false)
	public Integer getCommodityTypeId() {
		return this.commodityTypeId;
	}

	public void setCommodityTypeId(Integer commodityTypeId) {
		this.commodityTypeId = commodityTypeId;
	}

	@Column(name = "commodityTypeName", nullable = false, length = 50)
	public String getCommodityTypeName() {
		return this.commodityTypeName;
	}

	public void setCommodityTypeName(String commodityTypeName) {
		this.commodityTypeName = commodityTypeName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commodityType")
	public Set<Commodity> getCommodities() {
		return this.commodities;
	}

	public void setCommodities(Set<Commodity> commodities) {
		this.commodities = commodities;
	}

}