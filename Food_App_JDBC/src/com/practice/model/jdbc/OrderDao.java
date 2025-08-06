package com.practice.model.jdbc;

import com.practice.model.Order;
import com.practice.model.LineItem;
import com.practice.model.Customer;
import com.practice.model.FoodItem;
import com.practice.model.delivery.DeliveryPartner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

	public Order createOrder(Order order, int customerId) throws SQLException {
		String insertOrderSQL = "INSERT INTO orders (customer_id, delivery_partner_id, total_amount, payment_mode, discount) VALUES (?, ?, ?, ?, ?)";
		String insertOrderItemsSQL = "INSERT INTO order_items (order_id, food_id, quantity, price_per_item) VALUES (?, ?, ?, ?)";

		try (Connection conn = DbUtil.getConnection()) {
			conn.setAutoCommit(false);

			try (PreparedStatement orderPstmt = conn.prepareStatement(insertOrderSQL,
					Statement.RETURN_GENERATED_KEYS)) {
				orderPstmt.setInt(1, customerId);
				if (order.getDeliveryPartner() != null) {
					orderPstmt.setInt(2, order.getDeliveryPartner().getId());
				} else {
					orderPstmt.setNull(2, Types.INTEGER);
				}
				orderPstmt.setDouble(3, order.getTotalPaid());
				orderPstmt.setString(4, order.getPaymentMode());
				orderPstmt.setDouble(5, order.getDiscount());
				orderPstmt.executeUpdate();

				try (ResultSet generatedKeys = orderPstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						order.setId(generatedKeys.getInt(1));
					} else {
						throw new SQLException("Creating order failed, no ID obtained.");
					}
				}

				try (PreparedStatement itemPstmt = conn.prepareStatement(insertOrderItemsSQL)) {
					for (LineItem item : order.getItems()) {
						itemPstmt.setInt(1, order.getId());
						itemPstmt.setInt(2, item.getFoodItem().getId());
						itemPstmt.setInt(3, item.getQuantity());
						itemPstmt.setDouble(4, item.getFoodItem().getDiscountedPrice());
						itemPstmt.addBatch();
					}
					itemPstmt.executeBatch();
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
			}
		}
		return order;
	}

	public List<Order> getAllOrders() throws SQLException {
		List<Order> allOrders = new ArrayList<>();

		String selectAllOrdersSQL = "SELECT o.*, c.name as customer_name, c.address, c.phone_number, c.email, "
				+ "dp.name as partner_name, dp.contact_number " + "FROM orders o "
				+ "JOIN customers c ON o.customer_id = c.customer_id "
				+ "LEFT JOIN delivery_partners dp ON o.delivery_partner_id = dp.partner_id "
				+ "ORDER BY o.order_date DESC";

		try (Connection conn = DbUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectAllOrdersSQL);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {

				Order order = new Order();
				order.setId(rs.getInt("order_id"));
				order.setDate(rs.getTimestamp("order_date"));
				order.setTotalPaid(rs.getDouble("total_amount"));
				order.setPaymentMode(rs.getString("payment_mode"));
				order.setDiscount(rs.getDouble("discount"));

				Customer customer = new Customer(rs.getInt("customer_id"), rs.getString("customer_name"),
						rs.getString("address"), rs.getString("phone_number"), rs.getString("email"));
				order.setCustomer(customer);

				int partnerId = rs.getInt("delivery_partner_id");
				if (!rs.wasNull()) {
					DeliveryPartner partner = new DeliveryPartner(partnerId, rs.getString("partner_name"),
							rs.getString("contact_number"), false);
					order.setDeliveryPartner(partner);
				}

				allOrders.add(order);
			}
		}
		return allOrders;
	}

	public List<Order> getOrdersByCustomerId(int customerId) throws SQLException {
		List<Order> orderHistory = new ArrayList<>();

		String selectOrdersSQL = "SELECT o.*, dp.name AS partner_name, dp.contact_number " + "FROM orders o "
				+ "LEFT JOIN delivery_partners dp ON o.delivery_partner_id = dp.partner_id "
				+ "WHERE o.customer_id = ? " + "ORDER BY o.order_date DESC";

		String selectOrderItemsSQL = "SELECT oi.*, fi.* " + "FROM order_items oi "
				+ "JOIN food_items fi ON oi.food_id = fi.food_id " + "WHERE oi.order_id = ?";

		try (Connection conn = DbUtil.getConnection();
				PreparedStatement ordersPstmt = conn.prepareStatement(selectOrdersSQL)) {

			ordersPstmt.setInt(1, customerId);

			try (ResultSet ordersRs = ordersPstmt.executeQuery()) {
				while (ordersRs.next()) {

					Order order = new Order();
					order.setId(ordersRs.getInt("order_id"));
					order.setDate(ordersRs.getTimestamp("order_date"));
					order.setTotalPaid(ordersRs.getDouble("total_amount"));
					order.setPaymentMode(ordersRs.getString("payment_mode"));
					order.setDiscount(ordersRs.getDouble("discount"));

					int partnerId = ordersRs.getInt("delivery_partner_id");

					if (!ordersRs.wasNull()) {
						String partnerName = ordersRs.getString("partner_name");
						String partnerContact = ordersRs.getString("contact_number");

						DeliveryPartner partner = new DeliveryPartner(partnerId, partnerName, partnerContact, false);
						order.setDeliveryPartner(partner);
					}

					try (PreparedStatement itemsPstmt = conn.prepareStatement(selectOrderItemsSQL)) {
						itemsPstmt.setInt(1, order.getId());

						try (ResultSet itemsRs = itemsPstmt.executeQuery()) {
							List<LineItem> items = new ArrayList<>();
							while (itemsRs.next()) {
								FoodItem foodItem = new FoodItem(itemsRs.getInt("food_id"), itemsRs.getString("name"),
										itemsRs.getDouble("price"), itemsRs.getDouble("discount"),
										itemsRs.getString("cuisine_type"), itemsRs.getString("category"),
										itemsRs.getBoolean("is_deleted"));

								LineItem lineItem = new LineItem(itemsRs.getInt("order_item_id"),
										itemsRs.getInt("quantity"), foodItem);

								items.add(lineItem);
							}
							order.setItems(items);
						}
					}
					orderHistory.add(order);
				}
			}
		}
		return orderHistory;
	}
}
