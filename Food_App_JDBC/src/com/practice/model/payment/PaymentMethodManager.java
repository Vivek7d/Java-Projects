package com.practice.model.payment;

import com.practice.model.jdbc.PaymentMethodDao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentMethodManager {

    private final PaymentMethodDao methodDao;

    public PaymentMethodManager() {
        this.methodDao = new PaymentMethodDao();
    }

    public PaymentMethod addMethod(PaymentMethod method) {
        try {
            return methodDao.addMethod(method);
        } catch (SQLException e) {
            System.err.println("Database error adding payment method: " + e.getMessage());
            return null;
        }
    }

    public List<PaymentMethod> getAllMethods() {
        try {
            return methodDao.getAllMethods();
        } catch (SQLException e) {
            System.err.println("Database error fetching all payment methods: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PaymentMethod> getActiveMethods() {
        try {
            // Filter the full list to get only non-deleted methods
            return methodDao.getAllMethods().stream()
                .filter(m -> !m.isDeleted())
                .collect(Collectors.toList());
        } catch (SQLException e) {
            System.err.println("Database error fetching active payment methods: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean softDeleteMethod(int id) {
        try {
            return methodDao.setMethodStatus(id, true); // true means is_deleted
        } catch (SQLException e) {
            System.err.println("Database error deactivating method: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivateMethod(int id) {
        try {
            return methodDao.setMethodStatus(id, false); // false means not deleted
        } catch (SQLException e) {
            System.err.println("Database error reactivating method: " + e.getMessage());
            return false;
        }
    }

    public boolean permanentlyDeleteMethod(int id) {
        try {
            return methodDao.removeMethodById(id);
        } catch (SQLException e) {
            System.err.println("Database error permanently deleting method: " + e.getMessage());
            return false;
        }
    }

    public boolean existsByName(String name) {
        try {
            return methodDao.existsByName(name);
        } catch (SQLException e) {
            System.err.println("Database error checking method existence: " + e.getMessage());
            return false;
        }
    }
}