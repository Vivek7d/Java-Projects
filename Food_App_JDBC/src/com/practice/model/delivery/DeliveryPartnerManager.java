package com.practice.model.delivery;

import com.practice.model.jdbc.DeliveryPartnerDao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryPartnerManager {

    private final DeliveryPartnerDao partnerDao;

    public DeliveryPartnerManager() {
        this.partnerDao = new DeliveryPartnerDao();
    }

   
    public DeliveryPartner addPartner(DeliveryPartner partner) {
        try {
            return partnerDao.addPartner(partner);
        } catch (SQLException e) {
            System.err.println("Database error adding partner: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets a list of all delivery partners. This replaces the old `loadPartners`.
     * @return A list of all partners.
     */
    public List<DeliveryPartner> getAllPartners() {
        try {
            return partnerDao.getAllPartners();
        } catch (SQLException e) {
            System.err.println("Database error fetching all partners: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<DeliveryPartner> getActivePartners() {
        try {
            // This assumes your DAO has a method to fetch only active partners
            // If not, you can filter the full list here.
            return partnerDao.getAllPartners().stream()
                .filter(p -> !p.isDeleted())
                .collect(Collectors.toList());
        } catch (SQLException e) {
            System.err.println("Database error fetching active partners: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public boolean softDeletePartner(int id) {
        try {
            return partnerDao.setPartnerStatus(id, true); // true for is_deleted
        } catch (SQLException e) {
            System.err.println("Database error deactivating partner: " + e.getMessage());
            return false;
        }
    }
    public boolean reactivatePartner(int id) {
        try {
            return partnerDao.setPartnerStatus(id, false); // false for is_deleted
        } catch (SQLException e) {
            System.err.println("Database error reactivating partner: " + e.getMessage());
            return false;
        }
    }
    public boolean permanentlyDeletePartner(int id) {
        try {
            return partnerDao.removePartnerById(id);
        } catch (SQLException e) {
            System.err.println("Database error permanently deleting partner: " + e.getMessage());
            return false;
        }
    }
    /**
     * Removes a delivery partner by their ID.
     * @param id The ID of the partner to remove.
     * @return true if successful, false otherwise.
     */
    public boolean removePartnerById(int id) {
        try {
            return partnerDao.removePartnerById(id);
        } catch (SQLException e) {
            System.err.println("Database error removing partner: " + e.getMessage());
            return false;
        }
    }
    public boolean existsByName(String name) {
        try {
            return partnerDao.existsByName(name);
        } catch (SQLException e) {
            System.err.println("Database error checking partner existence: " + e.getMessage());
            return false;
        }
    }
    /**
     * Checks if a partner already exists by name.
     * @param name The name to check.
     * @return true if they exist, false otherwise.
     */
    
}