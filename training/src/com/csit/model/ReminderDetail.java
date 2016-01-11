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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @Description:
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Entity
@Table(name = "T_ReminderDetail")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReminderDetail extends BaseModel {

	private static final long serialVersionUID = -5105545128351747139L;
	// Fields
	/**
	 * 系统提醒明细Id
	 */
	private Integer reminderDetailId;
	/**
	 * 系统提醒
	 */
	private Reminder reminder;
	/**
	 * 系统提醒项
	 */
	private ReminderItem reminderItem;

	// Constructors

	/** default constructor */
	public ReminderDetail() {
	}

	/** full constructor */
	public ReminderDetail(Reminder reminder, ReminderItem reminderItem) {
		this.reminder = reminder;
		this.reminderItem = reminderItem;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "reminderDetailId", unique = true, nullable = false)
	public Integer getReminderDetailId() {
		return this.reminderDetailId;
	}

	public void setReminderDetailId(Integer reminderDetailId) {
		this.reminderDetailId = reminderDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reminderId")
	public Reminder getReminder() {
		return this.reminder;
	}

	public void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reminderItemId")
	public ReminderItem getReminderItem() {
		return this.reminderItem;
	}

	public void setReminderItem(ReminderItem reminderItem) {
		this.reminderItem = reminderItem;
	}

}