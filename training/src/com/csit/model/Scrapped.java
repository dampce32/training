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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @Description:损溢单
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Scrapped")
public class Scrapped extends BaseModel {

	private static final long serialVersionUID = 8186450069060573102L;
	/**
	 * 损溢单Id
	 */
	private Integer scrappedId;
	/**
	 * 经办人
	 */
	private User user;
	/**
	 * 类型
	 * 0-报废 
	 * 1-溢出 
	 */
	private Integer scrappedType;
	/**
	 * 损溢日期
	 */
	private Date scrappedDate;
	/**
	 * 总数量
	 */
	private Integer qtyTotal;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 入库退货单详细
	 */
	private Set<ScrappedDetail> scrappedDetails = new HashSet<ScrappedDetail>(0);
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scrappedId", unique = true, nullable = false)
	public Integer getScrappedId() {
		return scrappedId;
	}
	public void setScrappedId(Integer scrappedId) {
		this.scrappedId = scrappedId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Column(name = "scrappedType", nullable = false)
	public Integer getScrappedType() {
		return scrappedType;
	}
	public void setScrappedType(Integer scrappedType) {
		this.scrappedType = scrappedType;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "scrappedDate", nullable = false, length = 10)
	public Date getScrappedDate() {
		return scrappedDate;
	}
	public void setScrappedDate(Date scrappedDate) {
		this.scrappedDate = scrappedDate;
	}
	@Column(name = "qtyTotal", nullable = false)
	public Integer getQtyTotal() {
		return qtyTotal;
	}
	public void setQtyTotal(Integer qtyTotal) {
		this.qtyTotal = qtyTotal;
	}
	@Column(name = "note", length = 500)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scrapped")
	public Set<ScrappedDetail> getScrappedDetails() {
		return scrappedDetails;
	}
	public void setScrappedDetails(Set<ScrappedDetail> scrappedDetails) {
		this.scrappedDetails = scrappedDetails;
	}

	

}