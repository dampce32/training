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
 * @Description:学员
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author jcf
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Student")
public class Student extends BaseModel {

	private static final long serialVersionUID = 8813951229828079798L;
	public static final String LOGIN_STUDENTID = "studentId";
	/**
	 * 学员Id
	 */
	private Integer studentId;
	/**
	 * 媒体
	 */
	private Media media;
	/**
	 * 校区
	 */
	private School school;
	/**
	 * 学习顾问
	 */
	private User user;
	/**
	 * 学员姓名
	 */
	private String studentName;
	/**
	 * 称呼
	 */
	private String appellation;
	/**
	 * 性别  0--女 1--男
	 */
	private Integer sex;
	/**
	 * 学员类型 0--学生 1--上班族
	 */
	private Integer studentType;
	/**
	 * 出生日期
	 */
	private Date birthday;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 报名时间
	 */
	private Date enrollDate;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 手机
	 */
	private String mobileTel;
	/**
	 * 其它电话
	 */
	private String tel1;
	/**
	 * QQ
	 */
	private String qq;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 邮编
	 */
	private String postCode;
	/**
	 * 身份证
	 */
	private String idcard;
	/**
	 * 学历
	 */
	private String diploma;
	/**
	 * 下次回访日期
	 */
	private Date nextReplyDate;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 消费金额
	 */
	private Double consumedMoney;
	/**
	 * 数据写入时间
	 */
	private Date insertDate;
	/**
	 * 消费单数量
	 */
	private Integer billCount;
	/**
	 * 欠费金额
	 */
	private Double arrearageMoney;
	/**
	 * 可用金额
	 */
	private Double availableMoney;
	/**
	 * 借款到期时间
	 */
	private Date creditExpiration;
	/**
	 * 借款原因
	 */
	private String creditRemark;
	/**
	 * 最新选班
	 */
	private String newStuClass;
	
	

	private Set<Bill> bills = new HashSet<Bill>(0);
	private Set<StuClass> stuClasses = new HashSet<StuClass>(0);
	private Set<Payment> payments = new HashSet<Payment>(0);

	// Constructors

	/** default constructor */
	public Student() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "studentId", unique = true, nullable = false)
	public Integer getStudentId() {
		return this.studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mediaId")
	public Media getMedia() {
		return this.media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId")
	public School getSchool() {
		return this.school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "studentName", nullable = false, length = 50)
	public String getStudentName() {
		return this.studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Column(name = "appellation", length = 50)
	public String getAppellation() {
		return this.appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}
	
	@Column(name = "sex")
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "studentType", nullable = false)
	public Integer getStudentType() {
		return this.studentType;
	}

	public void setStudentType(Integer studentType) {
		this.studentType = studentType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "birthday", length = 10)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "password", nullable = false, length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "enrollDate", nullable = false, length = 10)
	public Date getEnrollDate() {
		return this.enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	@Column(name = "tel", nullable = false, length = 15)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "mobileTel", nullable = false, length = 15)
	public String getMobileTel() {
		return this.mobileTel;
	}

	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}

	@Column(name = "tel1", length = 15)
	public String getTel1() {
		return this.tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	@Column(name = "QQ", length = 15)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "address", length = 50)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "postCode", length = 50)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "IDcard", length = 30)
	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(name = "diploma", length = 0)
	public String getDiploma() {
		return this.diploma;
	}

	public void setDiploma(String diploma) {
		this.diploma = diploma;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NextReplyDate", length = 10)
	public Date getNextReplyDate() {
		return this.nextReplyDate;
	}

	public void setNextReplyDate(Date nextReplyDate) {
		this.nextReplyDate = nextReplyDate;
	}

	@Column(name = "note", length = 500)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "consumedMoney", precision = 22, scale = 0)
	public Double getConsumedMoney() {
		return this.consumedMoney;
	}

	public void setConsumedMoney(Double consumedMoney) {
		this.consumedMoney = consumedMoney;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "insertDate", nullable = false, length = 10)
	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Column(name = "billCount")
	public Integer getBillCount() {
		return this.billCount;
	}

	public void setBillCount(Integer billCount) {
		this.billCount = billCount;
	}

	@Column(name = "arrearageMoney", precision = 22, scale = 0)
	public Double getArrearageMoney() {
		return this.arrearageMoney;
	}

	public void setArrearageMoney(Double arrearageMoney) {
		this.arrearageMoney = arrearageMoney;
	}

	@Column(name = "availableMoney", precision = 22, scale = 0)
	public Double getAvailableMoney() {
		return this.availableMoney;
	}

	public void setAvailableMoney(Double availableMoney) {
		this.availableMoney = availableMoney;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "creditExpiration", length = 10)
	public Date getCreditExpiration() {
		return this.creditExpiration;
	}

	public void setCreditExpiration(Date creditExpiration) {
		this.creditExpiration = creditExpiration;
	}

	@Column(name = "creditRemark", length = 500)
	public String getCreditRemark() {
		return this.creditRemark;
	}

	public void setCreditRemark(String creditRemark) {
		this.creditRemark = creditRemark;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
	public Set<Bill> getBills() {
		return this.bills;
	}

	public void setBills(Set<Bill> bills) {
		this.bills = bills;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
	public Set<StuClass> getStuClasses() {
		return this.stuClasses;
	}
	public void setStuClasses(Set<StuClass> stuClasses) {
		this.stuClasses = stuClasses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
	public Set<Payment> getPayments() {
		return this.payments;
	}
	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}
	@Column(name = "newStuClass", length = 100)
	public String getNewStuClass() {
		return newStuClass;
	}

	public void setNewStuClass(String newStuClass) {
		this.newStuClass = newStuClass;
	}
}