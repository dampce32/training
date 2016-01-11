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
 * @Description:报表配置
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-23
 * @author lys
 * @vesion 1.0
 */
@Entity
@Table(name = "T_ReportConfig")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportConfig extends BaseModel {

	// Fields
	private static final long serialVersionUID = 1483112986622755080L;
	/**
	 * 报表配置Id
	 */
	private String reportConfigId;
	/**
	 * 报表编号
	 */
	private String reportCode;
	/**
	 * 报表名称
	 */
	private String reportName;
	/**
	 * 报表明细网格Sql
	 */
	private String reportDetailSql;
	/**
	 * 报表参数Sql
	 */
	private String reportParamsSql;
	/**
	 * 报表类型：
	 * 统计报表 1
	 * 模块报表 0 
	 */
	private Integer reportKind;
	
	private Set<ReportParamConfig> reportParamConfigs = new HashSet<ReportParamConfig>(0);

	// Constructors

	/** default constructor */
	public ReportConfig() {
	}

	/** minimal constructor */
	public ReportConfig(String reportConfigId) {
		this.reportConfigId = reportConfigId;
	}

	// Property accessors
	@Id
	@Column(name = "reportConfigId", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	public String getReportConfigId() {
		return this.reportConfigId;
	}

	public void setReportConfigId(String reportConfigId) {
		this.reportConfigId = reportConfigId;
	}

	@Column(name = "reportParamsSql", length = 100)
	public String getReportParamsSql() {
		return this.reportParamsSql;
	}

	public void setReportParamsSql(String reportParamsSql) {
		this.reportParamsSql = reportParamsSql;
	}

	@Column(name = "reportCode", length = 50)
	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "reportName", length = 1000)
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "reportDetailSql", length = 1000)
	public String getReportDetailSql() {
		return reportDetailSql;
	}

	public void setReportDetailSql(String reportDetailSql) {
		this.reportDetailSql = reportDetailSql;
	}
	@Column(name = "reportKind",nullable=false)
	public Integer getReportKind() {
		return reportKind;
	}

	public void setReportKind(Integer reportKind) {
		this.reportKind = reportKind;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reportConfig")
	public Set<ReportParamConfig> getReportParamConfigs() {
		return reportParamConfigs;
	}

	public void setReportParamConfigs(Set<ReportParamConfig> reportParamConfigs) {
		this.reportParamConfigs = reportParamConfigs;
	}

}