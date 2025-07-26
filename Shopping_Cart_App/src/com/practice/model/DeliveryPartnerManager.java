package com.practice.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryPartnerManager {
    private static final String FILE_NAME = "delivery_partners.txt";

    public static List<DeliveryPartner> loadPartners() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<DeliveryPartner>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); // return empty if file not found
        }
    }

    public static void savePartners(List<DeliveryPartner> partners) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(partners);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPartner(DeliveryPartner partner) {
        List<DeliveryPartner> partners = loadPartners();
        partners.add(partner);
        savePartners(partners);
    }
}
