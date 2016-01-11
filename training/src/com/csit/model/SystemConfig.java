package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @Description:系统配置
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name="T_SystemConfig")

public class SystemConfig  extends BaseModel {  
	
	private static final long serialVersionUID = -737871967736408641L;
	/**
	 * 系统配置Id
	 */
	private Integer systemConfigId;
	/**
	 * 系统名称
	 */
     private String systemName;
     /**
      * 班级模式
      */
     private String classType;


    // Constructors

    /** default constructor */
    public SystemConfig() {
    }

    
    /** full constructor */
    public SystemConfig(String systemName, String classType) {
        this.systemName = systemName;
        this.classType = classType;
    }

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="systemConfigId", unique=true, nullable=false)

    public Integer getSystemConfigId() {
        return this.systemConfigId;
    }
    
    public void setSystemConfigId(Integer systemConfigId) {
        this.systemConfigId = systemConfigId;
    }
    
    @Column(name="systemName", length=100)

    public String getSystemName() {
        return this.systemName;
    }
    
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    
    @Column(name="classType", length=20)

    public String getClassType() {
        return this.classType;
    }
    
    public void setClassType(String classType) {
        this.classType = classType;
    }
   








}