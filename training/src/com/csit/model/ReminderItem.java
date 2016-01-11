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
 * @Description:系统提醒项
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Entity
@Table(name = "T_ReminderItem")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReminderItem extends BaseModel {

	private static final long serialVersionUID = -4957445495549619349L;
	// Fields
	/**
	 * 系统提醒项Id
	 */
	private Integer reminderItemId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 提示信息
	 */
	private String message;
	/**
	 * 数量语法
	 */
	private String countSql;
	/**
	 * 重定向Url权限
	 */
	private Right right;
	/**
	 * 系统提醒明细
	 */
	private Set<ReminderDetail> reminderDetails = new HashSet<ReminderDetail>(0);

	// Constructors

	/** default constructor */
	public ReminderItem() {
	}

	/** minimal constructor */
	public ReminderItem(String title, String message) {
		this.title = title;
		this.message = message;
	}


	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "reminderItemId", unique = true, nullable = false)
	public Integer getReminderItemId() {
		return this.reminderItemId;
	}

	public void setReminderItemId(Integer reminderItemId) {
		this.reminderItemId = reminderItemId;
	}

	@Column(name = "title", nullable = false, length = 50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "message", nullable = false, length = 200)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "countSql", length = 1000)
	public String getCountSql() {
		return this.countSql;
	}

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reminderItem")
	public Set<ReminderDetail> getReminderDetails() {
		return this.reminderDetails;
	}

	public void setReminderDetails(Set<ReminderDetail> reminderDetails) {
		this.reminderDetails = reminderDetails;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rightId")
	public Right getRight() {
		return right;
	}

	public void setRight(Right right) {
		this.right = right;
	}

}