CREATE DATABASE IF NOT EXISTS food_app;

USE food_app;


CREATE TABLE food_items (
    food_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(5, 2) DEFAULT 0.00,
    cuisine_type VARCHAR(100),
    category VARCHAR(100),
    is_deleted BOOLEAN DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS delivery_partners (
    partner_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    contact_number VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    delivery_partner_id INT,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_mode VARCHAR(50),
    discount DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (delivery_partner_id) REFERENCES delivery_partners(partner_id)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    food_id INT NOT NULL,
    quantity INT NOT NULL,
    price_per_item DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (food_id) REFERENCES food_items(food_id)
);
CREATE TABLE payment_methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    method_name VARCHAR(255) NOT NULL UNIQUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

select * from payment_methods;
select * from orders;
select * from order_items;
ALTER TABLE delivery_partners
ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;
select * from food_items;
select * from customers;
select * from delivery_partners;
UPDATE customers
SET address = 'Thane'
WHERE customer_id = 2;

INSERT INTO food_items (name, price, discount, cuisine_type, category) VALUES
    ('Paneer Tikka', 180.00, 10.00, 'Indian', 'Starter'),
    ('Veg Biryani', 220.00, 15.00, 'Indian', 'Main Course'),
    ('Chicken Wings', 250.00, 8.00, 'Continental', 'Starter'),
    ('Margherita Pizza', 300.00, 12.00, 'Italian', 'Main Course'),
    ('Gulab Jamun', 90.00, 20.00, 'Indian', 'Dessert'),
    ('Cheesecake', 150.00, 6.00, 'Continental', 'Dessert'),
    ('Sushi Roll', 400.00, 5.00, 'Japanese', 'Main Course'),
    ('Spring Rolls', 120.00, 18.00, 'Chinese', 'Starter'),
    ('Tandoori Roti', 20.00, 30.00, 'Indian', 'Bread'),
    ('Butter Chicken', 260.00, 10.00, 'Indian', 'Main Course');
    
    INSERT INTO food_items (name, price, discount, cuisine_type, category) VALUES
-- Indian
('Chole Bhature', 140.00, 12.00, 'Indian', 'Main Course'),
('Samosa', 30.00, 10.00, 'Indian', 'Starter'),
('Rasgulla', 80.00, 15.00, 'Indian', 'Dessert'),

-- Chinese
('Hakka Noodles', 180.00, 10.00, 'Chinese', 'Main Course'),
('Manchurian', 200.00, 8.00, 'Chinese', 'Main Course'),
('Dim Sums', 160.00, 5.00, 'Chinese', 'Starter'),

-- Japanese
('Ramen', 420.00, 7.00, 'Japanese', 'Main Course'),
('Tempura', 350.00, 10.00, 'Japanese', 'Starter'),
('Mochi', 180.00, 12.00, 'Japanese', 'Dessert'),

-- Italian
('Pasta Alfredo', 280.00, 10.00, 'Italian', 'Main Course'),
('Garlic Bread', 120.00, 15.00, 'Italian', 'Starter'),
('Tiramisu', 200.00, 8.00, 'Italian', 'Dessert'),

-- Continental
('Grilled Sandwich', 150.00, 5.00, 'Continental', 'Starter'),
('Steak', 500.00, 10.00, 'Continental', 'Main Course'),
('Brownie', 130.00, 7.00, 'Continental', 'Dessert');
