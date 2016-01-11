package com.csit.model;

// default package

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
 * @Description:商品单位
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Unit")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Unit extends BaseModel {

	private static final long serialVersionUID = 8799531733542519361L;
	/**
	 * 商品单位Id
	 */
	private Integer unitId;
	/**
	 * 商品单位名称
	 */
	private String unitName;
	/**
	 * 状态 1--启用 0--禁用
	 */
	private Integer status;
	private Set<Commodity> commodities = new HashSet<Commodity>(0);

	// Constructors

	/** default constructor */
	public Unit() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "unitId", unique = true, nullable = false)
	public Integer getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	@Column(name = "unitName", nullable = false, length = 50)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unit")
	public Set<Commodity> getCommodities() {
		return this.commodities;
	}

	public void setCommodities(Set<Commodity> commodities) {
		this.commodities = commodities;
	}

}