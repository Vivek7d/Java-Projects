package com.aurionpro.model;

public class Department {
	private int deptId;
	private String name;

	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public Department(int deptId, String name) {
		this.deptId = deptId;
		this.name = name;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Department{" + "deptId=" + deptId + ", name='" + name + '\'' + '}';
	}
}