package ru.netology.web.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;

public class TableHelper {
    public static void cleanData() throws SQLException {
        val runner = new QueryRunner();
        val creditRequest = "DELETE FROM credit_request_entity";
        val order = "DELETE FROM order_entity";
        val payment = "DELETE FROM payment_entity";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, creditRequest);
            runner.update(conn, order);
            runner.update(conn, payment);
        }
    }

    public static String getAnyData(String getStatus, String column) throws SQLException {

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                val countStmt = conn.createStatement();
        ) {
            try (val rs = countStmt.executeQuery(getStatus)) {
                if (rs.next()) {
                    // выборка значения по индексу столбца (нумерация с 1)
                    val data = rs.getString(column);
                    return data;
                }
            }
        }
        return null;
    }

    public static String getCreditStatus() throws SQLException {
        val getStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        val column = "status";
        return TableHelper.getAnyData(getStatus, column);
    }

    public static String getPaymentStatus() throws SQLException {
        val getStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        val column = "status";
        return TableHelper.getAnyData(getStatus, column);
    }

    public static String getPaymentID() throws SQLException {
        val getStatus = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        val column = "payment_id";
        return TableHelper.getAnyData(getStatus, column);
    }

    public static String getCreditID() throws SQLException {
        val getStatus = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        val column = "bank_id";
        return TableHelper.getAnyData(getStatus, column);
    }

    public static String getTransactionID() throws SQLException {
        val getStatus = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1";
        val column = "transaction_id";
        return TableHelper.getAnyData(getStatus, column);
    }

    public static void comparePaymentAndTransactionID() throws SQLException {
        getPaymentID().equals(getTransactionID());
    }

    public static void compareCreditAndTransactionID() throws SQLException {
        getCreditID().equals(getTransactionID());
    }
}
