package com.practice.model.payment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentManager {
    private static final String FILE_NAME = "payment_methods.txt";

    public static List<PaymentMethod> loadMethods() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<PaymentMethod>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void saveMethods(List<PaymentMethod> methods) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(methods);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addMethod(PaymentMethod method) {
        List<PaymentMethod> methods = loadMethods();
        for (PaymentMethod pm : methods) {
            if (pm.getMethodName().equalsIgnoreCase(method.getMethodName())) {
                System.out.println("⚠️ Payment method already exists.");
                return;
            }
        }
        methods.add(method);
        saveMethods(methods);
        System.out.println("✅ Payment method added successfully.");
    }
    public static boolean removeMethodById(int id) {
        List<PaymentMethod> methods = loadMethods();
        boolean found = methods.removeIf(m -> m.getId() == id);
        if (found) {
            saveMethods(methods); 
        }
        return found;
    }

}
