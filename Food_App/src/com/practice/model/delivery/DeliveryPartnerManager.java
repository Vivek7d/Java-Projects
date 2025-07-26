package com.practice.model.delivery;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
    public static boolean removePartnerById(int id) {
        List<DeliveryPartner> partners = loadPartners();
        Iterator<DeliveryPartner> iterator = partners.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                found = true;
                break;
            }
        }

        if (found) {
            savePartners(partners);
        }

        return found;
    }

    public static boolean removePartnerByName(String name) {
        List<DeliveryPartner> partners = loadPartners();
        Iterator<DeliveryPartner> iterator = partners.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(name)) {
                iterator.remove();
                found = true;
                break;
            }
        }

        if (found) {
            savePartners(partners);
        }

        return found;
    }

}
