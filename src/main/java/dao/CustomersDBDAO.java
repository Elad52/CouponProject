package dao;

/*
Created by Elad on 27/03/2022

*/

import beans.Customer;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.CouponsException;
import exceptions.Errors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static db.JDBCUtils.*;

public class CustomersDBDAO implements CustomersDAO{
    private static final String QUERY_IS_EXIST_BY_EMAIL = "select exists (SELECT * FROM `coupondb`.`customers` WHERE email = ?) as res;";
    private static final String QUERY_IS_EXIST_BY_EMAIL_AND_PASSWORD = "select exists (SELECT * FROM `coupondb`.`customers` WHERE email = ? AND password = ?) as res;";
    private static final String QUERY_IS_EXIST_BY_ID = "select exists (SELECT * FROM `coupondb`.`customers` where id = ?) as res;";
    private static final String QUERY_ADD_CUSTOMER = "INSERT INTO `coupondb`.`customers` (`first_name`, `last_name`, `email`, `password`) VALUES (?, ?, ?, ?);";
    private static final String QUERY_UPDATE_CUSTOMER = "UPDATE `coupondb`.`customers` SET `first_name` = ?, `last_name` = ?, `email` = ?, `password` = ? WHERE (`id` = ?);";
    private static final String QUERY_DELETE_CUSTOMER = "DELETE FROM `coupondb`.`customers` WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL_CUSTOMERS = "SELECT * FROM coupondb.customers;";
    private static final String QUERY_GET_ONE_CUSTOMER = "SELECT * FROM coupondb.customers WHERE id = ?;";
    private static final String QUERY_GET_ONE_CUSTOMER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM coupondb.customers WHERE email = ? AND password = ?;";

    @Override
    public boolean isCustomerExistsByEmail(String email) throws SQLException, InterruptedException {
        boolean results = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,email);

        List<?> rows = executeResults(QUERY_IS_EXIST_BY_EMAIL,params);
        for (Object row:rows) {
            results = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return results;
    }

    @Override
    public boolean isCustomerExistsByEmailAndPassword(String email,String password) throws SQLException, InterruptedException {
        boolean results = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,email);
        params.put(2,password);

        List<?> rows = executeResults(QUERY_IS_EXIST_BY_EMAIL_AND_PASSWORD,params);
        for (Object row:rows) {
            results = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return results;
    }

    @Override
    public boolean isCustomerExistsById(int id) throws SQLException, InterruptedException {
        boolean results = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,id);

        List<?> rows = executeResults(QUERY_IS_EXIST_BY_ID,params);
        for (Object row:rows) {
            results = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return results;
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,customer.getFirstName());
        params.put(2,customer.getLastName());
        params.put(3,customer.getEmail());
        params.put(4,customer.getPassword());

        execute(QUERY_ADD_CUSTOMER,params);
    }

    @Override
    public void updateCustomer(Customer customer, int customerID) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,customer.getFirstName());
        params.put(2,customer.getLastName());
        params.put(3,customer.getEmail());
        params.put(4,customer.getPassword());
        params.put(5,customerID);

        execute(QUERY_UPDATE_CUSTOMER,params);
    }

    @Override
    public void deleteCustomer(int customerID) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,customerID);

        execute(QUERY_DELETE_CUSTOMER,params);
    }

    @Override
    public List<Customer> getAllCustomers() throws SQLException, InterruptedException, CouponsException {
        List<Customer> results = new ArrayList<>();
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL_CUSTOMERS);

        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCustomer((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public Customer getOneCustomer(int customerID) throws SQLException, InterruptedException, CouponsException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,customerID);
        Customer c1 = new Customer();

        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ONE_CUSTOMER,params);

        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }
        for (Object row: rows) {
            c1 = ResultsUtils.fromHashMapReturnCustomer((HashMap<String,Object>) row);
        }

        return c1;
    }

    public Customer getOneCustomerByEmailAndPassword(String email, String password) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,email);
        params.put(2,password);
        Customer c1 = new Customer();

        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ONE_CUSTOMER_BY_EMAIL_AND_PASSWORD,params);

        for (Object row: rows) {
            c1 = ResultsUtils.fromHashMapReturnCustomer((HashMap<String,Object>) row);
        }

        return c1;
    }
}
