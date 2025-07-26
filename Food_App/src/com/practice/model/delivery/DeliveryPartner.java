package com.practice.model.delivery;

import java.io.Serializable;

public class DeliveryPartner implements Serializable {
   
	private static final long serialVersionUID = 1L;
	private int id;
    private String name;
    private String contactNumber;

    public DeliveryPartner() {
    }

    public DeliveryPartner(int id, String name, String contactNumber) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
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

    @Override
    public String toString() {
        return "DeliveryPartner [ID=" + id + ", Name=" + name + ", Contact=" + contactNumber + "]";
    }
}
