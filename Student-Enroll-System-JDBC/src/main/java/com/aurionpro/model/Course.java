package com.aurionpro.model;

public class Course {
	private int courseId;
	private String title;
	private int instructorId;

	public Course() {
	}

	public Course(String title, int instructorId) {
		this.title = title;
		this.instructorId = instructorId;
	}

	public Course(int courseId, String title, int instructorId) {
		this.courseId = courseId;
		this.title = title;
		this.instructorId = instructorId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(int instructorId) {
		this.instructorId = instructorId;
	}

	@Override
	public String toString() {
		return "Course{" + "courseId=" + courseId + ", title='" + title + '\'' + ", instructorId=" + instructorId + '}';
	}
}