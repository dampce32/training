package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @Description:时间段表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_PeriodTime")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class PeriodTime extends BaseModel {

	private static final long serialVersionUID = 1716548807071995867L;

	/**
	 * 上课时间段Id
	 */
	private Integer periodTimeId;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 分组 1 -- 春， 2 -- 夏， 3 -- 秋， 4 -- 冬， 0 -- 其它
	 */
	private Integer groupType;
	/**
	 * 时间段 startTime+endTime,用于前台显示
	 */
	private String Time;

	public PeriodTime() {
	}

	public PeriodTime(Integer periodTimeId, String startTime, String endTime,
			Integer groupType) {
		this.periodTimeId = periodTimeId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.groupType = groupType;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "periodTimeId", unique = true, nullable = false)
	public Integer getPeriodTimeId() {
		return this.periodTimeId;
	}

	public void setPeriodTimeId(Integer periodTimeId) {
		this.periodTimeId = periodTimeId;
	}

	@Column(name = "startTime", nullable = false, length = 10)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "endTime", nullable = false, length = 10)
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "groupType", nullable = false)
	public Integer getGroupType() {
		return this.groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}
	
}