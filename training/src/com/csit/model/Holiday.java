package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description: 假期表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author yk
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Holiday")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Holiday extends BaseModel {

	// Fields
	private static final long serialVersionUID = -89450595872679536L;
	/**
	 * 假期Id
	 */
	private Integer holidayId;
	/**
	 * 假期名称
	 */
	private String holidayName;
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 结束时间
	 */
	private Date endDate;

	// Constructors

	/** default constructor */
	public Holiday() {
	}

	/** minimal constructor */
	public Holiday(Integer holidayId, Date startDate, Date endDate) {
		this.holidayId = holidayId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/** full constructor */
	public Holiday(Integer holidayId, String holidayName, Date startDate,
			Date endDate) {
		this.holidayId = holidayId;
		this.holidayName = holidayName;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "holidayId", unique = true, nullable = false)
	public Integer getHolidayId() {
		return this.holidayId;
	}

	public void setHolidayId(Integer holidayId) {
		this.holidayId = holidayId;
	}

	@Column(name = "holidayName", length = 50)
	public String getHolidayName() {
		return this.holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startDate", nullable = false, length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "endDate", nullable = false, length = 10)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}