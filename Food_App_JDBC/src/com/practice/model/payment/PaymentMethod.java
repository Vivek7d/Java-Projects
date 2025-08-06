package com.practice.model.payment;

public class PaymentMethod {
	private int id;
	private String methodName;
	private boolean isDeleted;

	public PaymentMethod(String methodName) {
		this.methodName = methodName;
		this.isDeleted = false;
	}

	public PaymentMethod(int id, String methodName, boolean isDeleted) {
		this.id = id;
		this.methodName = methodName;
		this.isDeleted = isDeleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {

		return id + ". " + methodName;
	}
}