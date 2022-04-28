package Facade;

/*
Created by Elad on 01/04/2022

*/

import beans.Company;
import beans.Coupon;
import beans.Customer;
import exceptions.CouponsException;
import exceptions.Errors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminFacade extends ClientFacade{

    public AdminFacade() {
    }

    @Override
    public boolean login(String email, String password) {
        return Objects.equals(email, "admin@admin.com") && Objects.equals(password, "admin");
    }

    public void addCompany(Company company) throws SQLException, InterruptedException, CouponsException {
        if (companiesDAO.isCompanyExistsByNameOrEmail(company.getName(),company.getEmail())){
            throw new CouponsException(Errors.COMPANY_ALREADY_EXISTS.getErrorMessage());
        }
        companiesDAO.addCompany(company);
    }

    public void updateCompany(int companyID, Company company) throws SQLException, InterruptedException, CouponsException {
        if(!(companiesDAO.isCompanyExistsID(companyID))){
            throw new CouponsException(Errors.COMPANY_NOT_EXISTS.getErrorMessage());
        }
        companiesDAO.updateCompany(company,companyID);
    }

    public void deleteCompany(int companyID) throws SQLException, InterruptedException, CouponsException {
        if(!(companiesDAO.isCompanyExistsID(companyID))){
            throw new CouponsException(Errors.COMPANY_NOT_EXISTS.getErrorMessage());
        }
        for (Coupon coupon: couponsDAO.getAllCouponsByCompany(companyID)) {
            couponsDAO.deleteCouponPurchaseByCoupon(coupon.getId());
            couponsDAO.deleteCoupon(coupon.getId());
        }
        companiesDAO.deleteCompany(companyID);
    }

    public List<Company> getAllCompanies() throws SQLException, CouponsException, InterruptedException {
        return companiesDAO.getAllCompanies();
    }

    public Company getOneCompany(int companyID) throws SQLException, CouponsException, InterruptedException {
        if (!companiesDAO.isCompanyExistsID(companyID)){
            throw new CouponsException(Errors.COMPANY_NOT_EXISTS.getErrorMessage());
        }
        return companiesDAO.getOneCompany(companyID);
    }

    public void addCustomer(Customer customer) throws SQLException, InterruptedException, CouponsException {
        if (customersDAO.isCustomerExistsByEmail(customer.getEmail())){
            throw new CouponsException(Errors.CUSTOMER_ALREADY_EXISTS.getErrorMessage());
        }
        customersDAO.addCustomer(customer);
    }

    public void updateCustomer(int customerID, Customer customer) throws SQLException, InterruptedException, CouponsException {
        if(!(customersDAO.isCustomerExistsById(customerID))){
            throw new CouponsException(Errors.CUSTOMER_NOT_EXISTS.getErrorMessage());
        }
        customersDAO.updateCustomer(customer,customerID);
    }

    public void deleteCustomer(int customerID) throws SQLException, InterruptedException, CouponsException {
        if(!(customersDAO.isCustomerExistsById(customerID))){
            throw new CouponsException(Errors.CUSTOMER_NOT_EXISTS.getErrorMessage());
        }
        couponsDAO.deleteCouponPurchaseByCustomer(customerID);
        customersDAO.deleteCustomer(customerID);
    }

    public List<Customer> getAllCustomers() throws SQLException, CouponsException, InterruptedException {
        return (ArrayList<Customer>) customersDAO.getAllCustomers();
    }

    public Customer getOneCustomer(int customerID) throws SQLException, CouponsException, InterruptedException {
        if(!customersDAO.isCustomerExistsById(customerID)){
            throw new CouponsException(Errors.CUSTOMER_NOT_EXISTS.getErrorMessage());
        }
        return customersDAO.getOneCustomer(customerID);
    }
}
