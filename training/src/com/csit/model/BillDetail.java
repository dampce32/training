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
 * @Description: 消费明细表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_BillDetail")
public class BillDetail extends BaseModel {

	// Fields
	private static final long serialVersionUID = 3241107945103579300L;

	/**
	 * 消费明细表Id
	 */
	private Integer billDetailId;
	/**
	 * 消费项
	 */
	private FeeItem feeItem;
	/**
	 * 课程
	 */
	private Course course;
	/**
	 * 消费单
	 */
	private Bill bill;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 商品
	 */
	private Commodity commodity;
	/**
	 * 收费类型 
	 *  课程 
	 *  收费项 
	 *  商品
	 */
	private String productType;
	/**
	 * 单价
	 */
	private Double price;
	/**
	 * 数量
	 */
	private Integer qty;
	/**
	 * 折扣
	 */
	private Double discount;
	/**
	 * 优惠金额
	 */
	private Double discountAmount;
	/**
	 * 单位
	 */
	private String unitName;
	/**
	 * 状态 
	 * 0--未处理
	 * 1--已处理
	 * 2--已退
	 * 3--标记（选择商品使用）
	 * 当收费类型是课程时，用到状态
	 */
	private Integer status;
	/**
	 * 已退数量
	 * 课程、商品退货使用
	 */
	private Integer returnQty;
	/**
	 * 选班表
	 */
	private StuClass stuClass;

	// Constructors
	/** default constructor */
	public BillDetail() {
	}


	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "billDetailId", unique = true, nullable = false)
	public Integer getBillDetailId() {
		return this.billDetailId;
	}

	public void setBillDetailId(Integer billDetailId) {
		this.billDetailId = billDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feeItemId")
	public FeeItem getFeeItem() {
		return this.feeItem;
	}

	public void setFeeItem(FeeItem feeItem) {
		this.feeItem = feeItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseId")
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billId")
	public Bill getBill() {
		return this.bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
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

	@Transient
	public String getBillItemName() {
		if("课程".equals(productType)){
			if(this.course!=null){
				return this.course.getCourseName();
			}
		}
		if("收费项".equals(productType)){
			if(this.feeItem!=null){
				return this.feeItem.getFeeItemName();
			}
		}
		if("商品".equals(productType)){
			if(this.commodity!=null){
				return this.commodity.getCommodityName();
			}
		}
		return null;
	}

	@Column(name = "productType", length = 20)
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	@Column(name = "discount", precision = 22, scale = 0)
	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Column(name = "discountAmount", precision = 22, scale = 0)
	public Double getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Column(name = "unitName", length = 50)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Transient
	public Double getTotalPrice() {
		return price*(qty-returnQty);
	}
	
	@Transient
	public Double getPayed() {
		return price*(qty-returnQty)-discountAmount;
	}
	@Transient
	public Integer getActualQty() {
		return qty-returnQty;
	}
	@Column(name = "returnQty")
	public Integer getReturnQty() {
		return returnQty;
	}
	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stuClassId")
	public StuClass getStuClass() {
		return stuClass;
	}
	public void setStuClass(StuClass stuClass) {
		this.stuClass = stuClass;
	}
}