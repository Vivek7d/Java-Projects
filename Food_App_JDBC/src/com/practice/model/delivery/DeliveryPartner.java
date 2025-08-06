package com.practice.model.delivery;

public class DeliveryPartner {
	private int id;
	private String name;
	private String contactNumber;
	private boolean isDeleted;

	public DeliveryPartner(String name, String contactNumber) {
		this.name = name;
		this.contactNumber = contactNumber;
		this.isDeleted = false;
	}

	public DeliveryPartner(int id, String name, String contactNumber, boolean isDeleted) {
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
		this.isDeleted = isDeleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "DeliveryPartner [ID=" + id + ", Name=" + name + ", Contact=" + contactNumber + ", Status="
				+ (isDeleted ? "Inactive" : "Active") + "]";
	}
}