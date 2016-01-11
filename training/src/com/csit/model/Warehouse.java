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
 * @Description: 仓库
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name="T_WareHouse")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Warehouse extends BaseModel {

	
	// Fields    
	private static final long serialVersionUID = 7601776679404574006L;
	/**
	 * 仓库Id
	 */
     private Integer warehouseId;
 	/**
 	 * 校区
 	 */
     private School school;
 	/**
 	 * 仓库名称
 	 */
     private String warehouseName;
 	/**
 	 * 电话
 	 */
     private String tel;
 	/**
 	 * 地址
 	 */
     private String address;
 	/**
 	 * 状态
 	 * 0 -- 禁用
 	 * 1 -- 可用
 	 */
     private Integer status;
 	/**
 	 * 仓库领用明细单
 	 */
     private Set<UseCommodityDetail> useCommodityDetails = new HashSet<UseCommodityDetail>(0);
 	/**
 	 * 消费明细单
 	 */
     private Set<BillDetail> billDetails = new HashSet<BillDetail>(0);
 	/**
 	 * 库存
 	 */
     private Set<Store> stores = new HashSet<Store>(0);
 	/**
 	 * 入库单和退货单明细
 	 */
     private Set<RecRejDetail> recRejDetails = new HashSet<RecRejDetail>(0);


    // Constructors

    /** default constructor */
    public Warehouse() {
    }

	/** minimal constructor */
    public Warehouse(School school, String warehouseName, Integer status) {
        this.school = school;
        this.warehouseName = warehouseName;
        this.status = status;
    }
    
    /** full constructor */
    public Warehouse(School school, String warehouseName, String tel, String address, Integer status, Set<UseCommodityDetail> useCommodityDetails, Set<BillDetail> billDetails, Set<Store> stores, Set<RecRejDetail> recRejDetails) {
        this.school = school;
        this.warehouseName = warehouseName;
        this.tel = tel;
        this.address = address;
        this.status = status;
        this.useCommodityDetails = useCommodityDetails;
        this.billDetails = billDetails;
        this.stores = stores;
        this.recRejDetails = recRejDetails;
    }

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="warehouseId", unique=true, nullable=false)

    public Integer getWarehouseId() {
        return this.warehouseId;
    }
    
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="schoolId", nullable=false)

    public School getSchool() {
        return this.school;
    }
    
    public void setSchool(School school) {
        this.school = school;
    }
    
    @Column(name="warehouseName", nullable=false, length=50)

    public String getWarehouseName() {
        return this.warehouseName;
    }
    
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    
    @Column(name="tel", length=15)

    public String getTel() {
        return this.tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    @Column(name="address", length=50)

    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Column(name="status", nullable=false)

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="warehouse")

    public Set<UseCommodityDetail> getUseCommodityDetails() {
        return this.useCommodityDetails;
    }
    
    public void setUseCommodityDetails(Set<UseCommodityDetail> useCommodityDetails) {
        this.useCommodityDetails = useCommodityDetails;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="warehouse")

    public Set<BillDetail> getBillDetails() {
        return this.billDetails;
    }
    
    public void setBillDetails(Set<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="warehouse")

    public Set<Store> getStores() {
        return this.stores;
    }
    
    public void setStores(Set<Store> stores) {
        this.stores = stores;
    }
@OneToMany(fetch=FetchType.LAZY, mappedBy="warehouse")

    public Set<RecRejDetail> getRecRejDetails() {
        return this.recRejDetails;
    }
    
    public void setRecRejDetails(Set<RecRejDetail> recRejDetails) {
        this.recRejDetails = recRejDetails;
    }
   








}