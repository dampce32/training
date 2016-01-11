package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

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

/**
 * @Description:系统提醒
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Entity
@Table(name = "T_Reminder")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Reminder extends BaseModel {

	// Fields
	private static final long serialVersionUID = 5496925029424942804L;
	/**
	 * 系统提醒Id
	 */
	private Integer reminderId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 状态 1--启用 0--禁用
	 */
	private Integer status;
	/**
	 * 排序
	 */
	private Integer array;
	/**
	 * 系统提醒明细
	 */
	private Set<ReminderDetail> reminderDetails = new HashSet<ReminderDetail>(0);

	// Constructors

	/** default constructor */
	public Reminder() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "reminderId", unique = true, nullable = false)
	public Integer getReminderId() {
		return this.reminderId;
	}

	public void setReminderId(Integer reminderId) {
		this.reminderId = reminderId;
	}

	@Column(name = "title", nullable = false, length = 50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "array", nullable = false)
	public Integer getArray() {
		return this.array;
	}

	public void setArray(Integer array) {
		this.array = array;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "reminder")
	public Set<ReminderDetail> getReminderDetails() {
		return this.reminderDetails;
	}

	public void setReminderDetails(Set<ReminderDetail> reminderDetails) {
		this.reminderDetails = reminderDetails;
	}

}