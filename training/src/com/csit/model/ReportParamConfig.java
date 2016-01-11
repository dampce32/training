package com.csit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @Description:报表参数配置
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
@Entity
@Table(name = "T_ReportParamConfig")
public class ReportParamConfig extends BaseModel {

	// Fields
	private static final long serialVersionUID = -2215378329297144293L;
	/**
	 * 报表参数配置Id
	 */
	private String reportParamConfigId;
	/**
	 * 报表参数
	 */
	private ReportParam reportParam;
	/**
	 * 报表配置
	 */
	private ReportConfig reportConfig;
	/**
	 * 是否必选
	 */
	private Integer isNeedChoose;
	/**
	 * 参数位置
	 */
	private Integer array;

	// Constructors
	/** default constructor */
	public ReportParamConfig() {
	}

	/** minimal constructor */
	public ReportParamConfig(String reportParamConfigId, Integer isNeedChoose,
			Integer array) {
		this.reportParamConfigId = reportParamConfigId;
		this.isNeedChoose = isNeedChoose;
		this.array = array;
	}

	/** full constructor */
	public ReportParamConfig(String reportParamConfigId,
			ReportParam reportParam, ReportConfig reportConfig,
			Integer isNeedChoose, Integer array) {
		this.reportParamConfigId = reportParamConfigId;
		this.reportParam = reportParam;
		this.reportConfig = reportConfig;
		this.isNeedChoose = isNeedChoose;
		this.array = array;
	}

	// Property accessors
	@Id
	@Column(name = "reportParamConfigId", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getReportParamConfigId() {
		return this.reportParamConfigId;
	}

	public void setReportParamConfigId(String reportParamConfigId) {
		this.reportParamConfigId = reportParamConfigId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reportParamId")
	public ReportParam getReportParam() {
		return this.reportParam;
	}

	public void setReportParam(ReportParam reportParam) {
		this.reportParam = reportParam;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reportConfigId")
	public ReportConfig getReportConfig() {
		return this.reportConfig;
	}

	public void setReportConfig(ReportConfig reportConfig) {
		this.reportConfig = reportConfig;
	}

	@Column(name = "isNeedChoose", nullable = false)
	public Integer getIsNeedChoose() {
		return this.isNeedChoose;
	}

	public void setIsNeedChoose(Integer isNeedChoose) {
		this.isNeedChoose = isNeedChoose;
	}

	@Column(name = "array", nullable = false)
	public Integer getArray() {
		return this.array;
	}

	public void setArray(Integer array) {
		this.array = array;
	}
}