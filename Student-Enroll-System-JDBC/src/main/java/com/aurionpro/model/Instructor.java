package com.aurionpro.model;

public class Instructor {
	private int instructorId;
	private String name;
	private int deptId;

	public Instructor() {
	}

	public Instructor(String name, int deptId) {
		this.name = name;
		this.deptId = deptId;
	}

	public Instructor(int instructorId, String name, int deptId) {
		this.instructorId = instructorId;
		this.name = name;
		this.deptId = deptId;
	}

	public int getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(int instructorId) {
		this.instructorId = instructorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	@Override
	public String toString() {
		return "Instructor{" + "instructorId=" + instructorId + ", name='" + name + '\'' + ", deptId=" + deptId + '}';
	}
}