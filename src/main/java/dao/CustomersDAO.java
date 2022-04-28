package dao;

import beans.Customer;
import exceptions.CouponsException;

import java.sql.SQLException;
import java.util.List;

public interface CustomersDAO {
    boolean isCustomerExistsByEmail(String email) throws SQLException, InterruptedException;
    boolean isCustomerExistsByEmailAndPassword(String email,String password) throws SQLException, InterruptedException;
    boolean isCustomerExistsById(int id) throws SQLException, InterruptedException;
    void addCustomer(Customer customer) throws SQLException, InterruptedException;
    void updateCustomer(Customer customer,int customerID) throws SQLException, InterruptedException;
    void deleteCustomer(int customerID) throws SQLException, InterruptedException;
    List<Customer> getAllCustomers() throws SQLException, InterruptedException, CouponsException;
    Customer getOneCustomer(int customerID) throws SQLException, InterruptedException, CouponsException;
    Customer getOneCustomerByEmailAndPassword(String email, String password) throws SQLException, InterruptedException;
}
