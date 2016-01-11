package com.csit.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * @Description:报表参数
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
@Entity
@Table(name = "T_ReportParam")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportParam extends BaseModel {

	// Fields

	private static final long serialVersionUID = -2881630191183672937L;
	/**
	 * 报表参数Id
	 */
	private String reportParamId;
	/**
	 * 报表参数名称
	 */
	private String paramName;
	/**
	 * 参数编号
	 */
	private String paramCode;
	/**
	 * 报表参数配置
	 */
	private Set<ReportParamConfig> reportParamConfigs = new HashSet<ReportParamConfig>(
			0);

	// Constructors

	/** default constructor */
	public ReportParam() {
	}

	/** minimal constructor */
	public ReportParam(String reportParamId, String paramCode) {
		this.reportParamId = reportParamId;
		this.paramCode = paramCode;
	}

	/** full constructor */
	public ReportParam(String reportParamId, String paramName,
			String paramCode, Set<ReportParamConfig> reportParamConfigs) {
		this.reportParamId = reportParamId;
		this.paramName = paramName;
		this.paramCode = paramCode;
		this.reportParamConfigs = reportParamConfigs;
	}

	// Property accessors
	@Id
	@Column(name = "reportParamId", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getReportParamId() {
		return this.reportParamId;
	}

	public void setReportParamId(String reportParamId) {
		this.reportParamId = reportParamId;
	}

	@Column(name = "paramName", length = 50)
	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@Column(name = "paramCode", nullable = false, length = 100)
	public String getParamCode() {
		return this.paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reportParam")
	public Set<ReportParamConfig> getReportParamConfigs() {
		return this.reportParamConfigs;
	}

	public void setReportParamConfigs(Set<ReportParamConfig> reportParamConfigs) {
		this.reportParamConfigs = reportParamConfigs;
	}
}