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
 * @Description:用户，员工
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @Author lys
 */
@Entity
@Table(name = "T_User")
public class User extends BaseModel {
	private static final long serialVersionUID = 2616477341510333337L;
	/**
	 * 登陆用户Id
	 */
	public static final String LOGIN_USERID = "userId";
	/**
	 * 登陆用户校区Id
	 */
	public static final String LOGIN_SCHOOLID = "schoolId";
	/**
	 * 登陆用户校区编号
	 */
	public static final String LOGIN_SCHOOLCODE = "schoolCode";
	// Fields
	/**
	 * 用户Id
	 */
	private Integer userId;
	/**
	 * 校区
	 */
	private School school;
	/**
	 * 登录名称
	 */
	private String userCode;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 登录密码
	 */
	private String userPwd;
	/**
	 * 性别 0女，1男
	 */
	private Integer sex;
	/**
	 * 出生日期
	 */
	private Date birthday;
	/**
	 * 是否兼职
	 * 0否，1是
	 */
	private Integer isPartTime;
	/**
	 * 是否授课 
	 * 0不授，1授课
	 */
	private Integer isTeacher;
	/**
	 * 任聘日期
	 */
	private Date comeDate;
	/**
	 * 离职日期
	 */
	private Date outDate;
	/**
	 * 所授课程
	 */
	private String course;
	/**
	 * 毕业学校
	 */
	private String finishSchool;
	/**
	 * 学历
	 */
	private String diploma;
	/**
	 * 简历
	 */
	private String resume;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 家庭地址
	 */
	private String address;
	/**
	 * 邮编
	 */
	private String postCode;
	/**
	 * 职务
	 */
	private String headship;
	/**
	 * 身份证号
	 */
	private String idcard;
	/**
	 * 基本工资
	 */
	private Double basePay;
	/**
	 * 课时费
	 */
	private Double hourFee;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 用户角色
	 */
	private Set<UserRole> userRoles = new HashSet<UserRole>(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(School school, String userCode, String userName,
			String userPwd, Integer sex, Date birthday, Integer isPartTime,
			Integer isTeacher, Date comeDate, Integer status) {
		this.school = school;
		this.userCode = userCode;
		this.userName = userName;
		this.userPwd = userPwd;
		this.sex = sex;
		this.birthday = birthday;
		this.isPartTime = isPartTime;
		this.isTeacher = isTeacher;
		this.comeDate = comeDate;
		this.status = status;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userId", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schoolId", nullable = false)
	public School getSchool() {
		return this.school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@Column(name = "userCode", nullable = false, length = 50)
	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "userName", nullable = false, length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "userPwd", nullable = false, length = 50)
	public String getUserPwd() {
		return this.userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Column(name = "sex", nullable = false)
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "birthday", nullable = false, length = 10)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "isPartTime", nullable = false)
	public Integer getIsPartTime() {
		return this.isPartTime;
	}

	public void setIsPartTime(Integer isPartTime) {
		this.isPartTime = isPartTime;
	}

	@Column(name = "IsTeacher", nullable = false)
	public Integer getIsTeacher() {
		return this.isTeacher;
	}

	public void setIsTeacher(Integer isTeacher) {
		this.isTeacher = isTeacher;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "comeDate", nullable = false, length = 10)
	public Date getComeDate() {
		return this.comeDate;
	}

	public void setComeDate(Date comeDate) {
		this.comeDate = comeDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "outDate", length = 10)
	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	@Column(name = "course", length = 50)
	public String getCourse() {
		return this.course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@Column(name = "finishSchool", length = 50)
	public String getFinishSchool() {
		return this.finishSchool;
	}

	public void setFinishSchool(String finishSchool) {
		this.finishSchool = finishSchool;
	}

	@Column(name = "diploma", length = 50)
	public String getDiploma() {
		return this.diploma;
	}

	public void setDiploma(String diploma) {
		this.diploma = diploma;
	}

	@Column(name = "resume", length = 1000)
	public String getResume() {
		return this.resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	@Column(name = "tel", length = 15)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "postCode", length = 10)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "headship", length = 50)
	public String getHeadship() {
		return this.headship;
	}

	public void setHeadship(String headship) {
		this.headship = headship;
	}

	@Column(name = "IDcard", length = 30)
	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(name = "basePay", precision = 22, scale = 0)
	public Double getBasePay() {
		return this.basePay;
	}

	public void setBasePay(Double basePay) {
		this.basePay = basePay;
	}

	@Column(name = "hourFee", precision = 22, scale = 0)
	public Double getHourFee() {
		return this.hourFee;
	}

	public void setHourFee(Double hourFee) {
		this.hourFee = hourFee;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}


}