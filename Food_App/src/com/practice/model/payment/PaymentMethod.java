package com.practice.model.payment;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String methodName;

    public PaymentMethod(int id, String methodName) {
        this.id = id;
        this.methodName = methodName;
    }

    public int getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return id + ". " + methodName;
    }
}
