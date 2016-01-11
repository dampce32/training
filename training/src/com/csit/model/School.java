package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @Description: 校区
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @Author lys
 */
@Entity
@Table(name = "T_School")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class School extends BaseModel {

	// Fields

	private static final long serialVersionUID = 1053349731780622768L;
	/**
	 * 校区Id
	 */
	private Integer schoolId;
	/**
	 * 父校区
	 */
	private School parentSchool;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 排序
	 */
	private Integer array;
	/**
	 * 是否叶子节点
	 */
	private Boolean isLeaf;
	/**
	 * 校区编号
	 */
	private String schoolCode;
	/**
	 * 校区名称
	 */
	private String schoolName;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 电话
	 */
	private String tel;
	private Set<StuClass> selectSchools = new HashSet<StuClass>(0);
	private Set<StuClass> lessonSchools = new HashSet<StuClass>(0);
	private Set<Bill> bills = new HashSet<Bill>(0);
	private Set<Clazz> classes = new HashSet<Clazz>(0);
	private Set<Payment> payments = new HashSet<Payment>(0);
	private Set<School> schools = new HashSet<School>(0);
	private Set<Potential> potentials = new HashSet<Potential>(0);
	private Set<Warehouse> warehouses = new HashSet<Warehouse>(0);
	private Set<User> users = new HashSet<User>(0);
	private Set<Student> students = new HashSet<Student>(0);

	List<School> childrenSchoolList;

	// Constructors

	/** default constructor */
	public School() {
	}

	/** minimal constructor */
	public School(Integer array, String schoolCode, String schoolName,
			Integer status) {
		this.array = array;
		this.schoolCode = schoolCode;
		this.schoolName = schoolName;
		this.status = status;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "schoolId", unique = true, nullable = false)
	public Integer getSchoolId() {
		return this.schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "array", nullable = false)
	public Integer getArray() {
		return this.array;
	}

	public void setArray(Integer array) {
		this.array = array;
	}

	@Column(name = "isLeaf")
	public Boolean getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Column(name = "schoolCode", nullable = false, length = 100)
	public String getSchoolCode() {
		return this.schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	@Column(name = "schoolName", nullable = false, length = 50)
	public String getSchoolName() {
		return this.schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "tel", length = 30)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<Bill> getBills() {
		return this.bills;
	}

	public void setBills(Set<Bill> bills) {
		this.bills = bills;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<Clazz> getClasses() {
		return this.classes;
	}

	public void setClasses(Set<Clazz> classes) {
		this.classes = classes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<Payment> getPayments() {
		return this.payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentSchool")
	public Set<School> getSchools() {
		return this.schools;
	}

	public void setSchools(Set<School> schools) {
		this.schools = schools;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<Potential> getPotentials() {
		return this.potentials;
	}

	public void setPotentials(Set<Potential> potentials) {
		this.potentials = potentials;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<Warehouse> getWarehouses() {
		return this.warehouses;
	}

	public void setWarehouses(Set<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
	public Set<Student> getStudents() {
		return this.students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentSchoolId")
	public School getParentSchool() {
		return parentSchool;
	}

	public void setParentSchool(School parentSchool) {
		this.parentSchool = parentSchool;
	}

	@Transient
	public List<School> getChildrenSchoolList() {
		return childrenSchoolList;
	}

	public void setChildrenSchoolList(List<School> childrenSchoolList) {
		this.childrenSchoolList = childrenSchoolList;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "selectSchool")
	public Set<StuClass> getSelectSchools() {
		return selectSchools;
	}

	public void setSelectSchools(Set<StuClass> selectSchools) {
		this.selectSchools = selectSchools;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lessonSchool")
	public Set<StuClass> getLessonSchools() {
		return lessonSchools;
	}

	public void setLessonSchools(Set<StuClass> lessonSchools) {
		this.lessonSchools = lessonSchools;
	}

}