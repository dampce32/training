package com.csit.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @Description: 权限实体
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-5
 * @author lys
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Right")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Right extends BaseModel{

	// Fields
	private static final long serialVersionUID = 1L;
	/**
	 * 权限Id
	 */
	private Integer rightId;
	/**
	 * 权限名称
	 */
	private String rightName;
	/**
	 * 是否为叶子节点
	 */
	private Boolean isLeaf;
	/**
	 * 权限Url
	 */
	private String rightUrl;
	/**
	 * 是否有权限
	 */
	private Boolean state;
	/**
	 * 父权限
	 */
	private Right parentRight;
	/**
	 * 排序
	 */
	private Integer array;
	/**
	 * 权限类型
	 * 1--Url权限
	 * 2--界面按钮
	 * 3--数据显示
	 */
	private Integer kind;
	/**
	 * 子权限
	 */
	private Set<Right> childrenRights = new HashSet<Right>(0);
	/**
	 * 角色权限
	 */
	private Set<RoleRight> roleRights = new HashSet<RoleRight>(0);
	/**
	 * 子权限List
	 */
	private List<Right> childrenRightList = new ArrayList<Right>();

	// Constructors

	/** default constructor */
	public Right() {
	}

	/** minimal constructor */
	public Right(Integer rightId) {
		this.rightId = rightId;
	}

	// Property accessors
	@Id
	@Column(name = "rightId", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getRightId() {
		return this.rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	@Column(name = "rightName", length = 50)
	public String getRightName() {
		return this.rightName;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	@Column(name = "rightUrl", length = 100)
	public String getRightUrl() {
		return this.rightUrl;
	}

	public void setRightUrl(String rightUrl) {
		this.rightUrl = rightUrl;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentRightId")
	public Right getParentRight() {
		return parentRight;
	}

	public void setParentRight(Right parentRight) {
		this.parentRight = parentRight;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentRight")
	public Set<Right> getChildrenRights() {
		return childrenRights;
	}

	public void setChildrenRights(Set<Right> childrenRights) {
		this.childrenRights = childrenRights;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	@Transient
	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}
	@Column(name = "array", length = 100)
	public Integer getArray() {
		return array;
	}

	public void setArray(Integer array) {
		this.array = array;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "right")
	public Set<RoleRight> getRoleRights() {
		return roleRights;
	}

	public void setRoleRights(Set<RoleRight> roleRights) {
		this.roleRights = roleRights;
	}
	@Transient
	public List<Right> getChildrenRightList() {
		return childrenRightList;
	}

	public void setChildrenRightList(List<Right> childrenRightList) {
		this.childrenRightList = childrenRightList;
	}
	@Column(name = "kind", length = 100, nullable = false)
	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

}