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
 * @Description: 奖惩类别
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_RewPunType")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RewPunType extends BaseModel {

	// Fields

	private static final long serialVersionUID = -2204947592309405967L;
	/**
	 * 商品分类Id
	 */
	private Integer rewPunTypeId;
	/**
	 * 商品分类名称
	 */
	private String rewPunTypeName;
	/**
	 * 状态
	 * 0 -- 禁用 
	 * 1 -- 可用
	 */
	private Integer status;
	/**
	 * 商品
	 */
	private Set<RewPun> rewPuns = new HashSet<RewPun>(0);

	// Constructors

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rewPunTypeId", unique = true, nullable = false)
	public Integer getRewPunTypeId() {
		return this.rewPunTypeId;
	}

	public void setRewPunTypeId(Integer rewPunTypeId) {
		this.rewPunTypeId = rewPunTypeId;
	}

	@Column(name = "rewPunTypeName", nullable = false, length = 50)
	public String getRewPunTypeName() {
		return this.rewPunTypeName;
	}

	public void setRewPunTypeName(String rewPunTypeName) {
		this.rewPunTypeName = rewPunTypeName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rewPunType")
	public Set<RewPun> getRewPuns() {
		return this.rewPuns;
	}

	public void setRewPuns(Set<RewPun> rewPuns) {
		this.rewPuns = rewPuns;
	}

}