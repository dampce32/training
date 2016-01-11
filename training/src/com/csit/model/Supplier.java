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
 * @Description:供应商
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Supplier")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Supplier extends BaseModel {

	private static final long serialVersionUID = -6137865612294331168L;
	/**
	 * 供应商Id
	 */
	private Integer supplierId;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 联系人
	 */
	private String linkMan;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 状态 0--启用 1--禁用
	 */
	private Integer status;
	private Set<RecRej> recRejs = new HashSet<RecRej>(0);

	// Constructors

	/** default constructor */
	public Supplier() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "supplierId", unique = true, nullable = false)
	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name = "supplierName", nullable = false, length = 50)
	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Column(name = "linkMan", length = 50)
	public String getLinkMan() {
		return this.linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	@Column(name = "tel", length = 15)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "address", length = 50)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
	public Set<RecRej> getRecRejs() {
		return this.recRejs;
	}

	public void setRecRejs(Set<RecRej> recRejs) {
		this.recRejs = recRejs;
	}

}