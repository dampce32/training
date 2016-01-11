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
 * @Description:咨询信息表
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Potential")
public class Potential extends BaseModel {

	private static final long serialVersionUID = -4904385787034573628L;

	/**
	 * 咨询信息单Id
	 */
	private Integer potentialId;
	/**
	 * 最后回访人
	 */
	private User lastReplyUser;
	/**
	 * 咨询课程表
	 */
	private PotCourse potCourse;
	/**
	 * 媒体
	 */
	private Media media;
	/**
	 * 校区
	 */
	private School school;
	/**
	 * 咨询顾问
	 */
	private User user;
	/**
	 * 潜在学院状态
	 */
	private PotentialStuStatus potentialStuStatus;
	/**
	 * 潜在学员姓名
	 */
	private String potentialName;
	/**
	 * 咨询日期
	 */
	private Date potentialDate;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 称呼
	 */
	private String appellation;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 手机
	 */
	private String mobileTel;
	/**
	 * 其他电话
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
	 * 上课规律
	 */
	private String timeRule;
	/**
	 * 最后回访时间
	 */
	private Date lastReplyDate;
	/**
	 * 回访次数
	 */
	private Integer reCount;
	/**
	 * 是否转正
	 * 0--未转正
	 * 1--已转正
	 */
	private Integer isStu;
	/**
	 * 下次回访日期
	 */
	private Date nextReplyDate;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 数据写入时间
	 */
	private Date insertDate;
	/**
	 * 咨询回访单
	 */
	private Set<Reply> replies = new HashSet<Reply>(0);

	public Potential() {
	}

	public Potential(String potentialName, Date potentialDate, String tel,
			String mobileTel, Integer reCount, Date insertDate) {
		this.potentialName = potentialName;
		this.potentialDate = potentialDate;
		this.tel = tel;
		this.mobileTel = mobileTel;
		this.reCount = reCount;
		this.insertDate = insertDate;
	}

	public Potential(User lastReplyUser, PotCourse potCourse, Media media,
			School school, User user, PotentialStuStatus potentialStuStatus,
			String potentialName, Date potentialDate, Integer sex,
			String appellation, String tel, String mobileTel, String tel1,
			String qq, String email, String timeRule, Date lastReplyDate,
			Integer reCount, Date nextReplyDate, String note, Date insertDate,
			Set<Reply> replies) {
		this.lastReplyUser = lastReplyUser;
		this.potCourse = potCourse;
		this.media = media;
		this.school = school;
		this.user = user;
		this.potentialStuStatus = potentialStuStatus;
		this.potentialName = potentialName;
		this.potentialDate = potentialDate;
		this.sex = sex;
		this.appellation = appellation;
		this.tel = tel;
		this.mobileTel = mobileTel;
		this.tel1 = tel1;
		this.qq = qq;
		this.email = email;
		this.timeRule = timeRule;
		this.lastReplyDate = lastReplyDate;
		this.reCount = reCount;
		this.nextReplyDate = nextReplyDate;
		this.note = note;
		this.insertDate = insertDate;
		this.replies = replies;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "potentialId", unique = true, nullable = false)
	public Integer getPotentialId() {
		return this.potentialId;
	}

	public void setPotentialId(Integer potentialId) {
		this.potentialId = potentialId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastReplyUserId")
	public User getlastReplyUser() {
		return this.lastReplyUser;
	}

	public void setlastReplyUser(User lastReplyUser) {
		this.lastReplyUser = lastReplyUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "potCourseId")
	public PotCourse getPotCourse() {
		return this.potCourse;
	}

	public void setPotCourse(PotCourse potCourse) {
		this.potCourse = potCourse;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "potentialStuStatusId")
	public PotentialStuStatus getPotentialStuStatus() {
		return this.potentialStuStatus;
	}

	public void setPotentialStuStatus(PotentialStuStatus potentialStuStatus) {
		this.potentialStuStatus = potentialStuStatus;
	}

	@Column(name = "potentialName", nullable = false, length = 50)
	public String getPotentialName() {
		return this.potentialName;
	}

	public void setPotentialName(String potentialName) {
		this.potentialName = potentialName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "potentialDate", nullable = false, length = 10)
	public Date getPotentialDate() {
		return this.potentialDate;
	}

	public void setPotentialDate(Date potentialDate) {
		this.potentialDate = potentialDate;
	}

	@Column(name = "sex")
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "appellation", length = 50)
	public String getAppellation() {
		return this.appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
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

	@Column(name = "timeRule", length = 100)
	public String getTimeRule() {
		return this.timeRule;
	}

	public void setTimeRule(String timeRule) {
		this.timeRule = timeRule;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "lastReplyDate", length = 10)
	public Date getLastReplyDate() {
		return this.lastReplyDate;
	}

	public void setLastReplyDate(Date lastReplyDate) {
		this.lastReplyDate = lastReplyDate;
	}

	@Column(name = "reCount", nullable = false)
	public Integer getReCount() {
		return this.reCount;
	}

	public void setReCount(Integer reCount) {
		this.reCount = reCount;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "insertDate", nullable = false, length = 10)
	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "potential")
	public Set<Reply> getReplies() {
		return this.replies;
	}

	public void setReplies(Set<Reply> replies) {
		this.replies = replies;
	}

	@Column(name = "isStu")
	public Integer getIsStu() {
		return isStu;
	}

	public void setIsStu(Integer isStu) {
		this.isStu = isStu;
	}

}