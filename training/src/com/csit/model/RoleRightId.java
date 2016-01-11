package com.csit.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 * @Description: 角色权限Id
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
@Embeddable
public class RoleRightId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private Integer rightId;
	private Integer roleId;

	// Constructors

	/** default constructor */
	public RoleRightId() {
	}

	/** full constructor */
	public RoleRightId(Integer rightId, Integer roleId) {
		this.rightId = rightId;
		this.roleId = roleId;
	}

	// Property accessors

	@Column(name = "rightId", nullable = false)
	public Integer getRightId() {
		return this.rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	@Column(name = "roleId", nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RoleRightId))
			return false;
		RoleRightId castOther = (RoleRightId) other;

		return ((this.getRightId() == castOther.getRightId()) || (this
				.getRightId() != null && castOther.getRightId() != null && this
				.getRightId().equals(castOther.getRightId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this
						.getRoleId() != null && castOther.getRoleId() != null && this
						.getRoleId().equals(castOther.getRoleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRightId() == null ? 0 : this.getRightId().hashCode());
		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		return result;
	}

}