package Facade;

/*
Created by Elad on 01/04/2022

*/

import beans.Category;
import beans.Coupon;
import beans.Customer;
import exceptions.CouponsException;
import exceptions.Errors;

import java.sql.SQLException;
import java.util.List;

public class CustomerFacade extends ClientFacade{

    private int customerID;

    public CustomerFacade() {
    }

    public int getCustomerID() {
        return customerID;
    }

    @Override
    public boolean login(String email, String password) throws SQLException, InterruptedException {
        if(!(customersDAO.isCustomerExistsByEmailAndPassword(email, password))){
            return false;
        }
        customerID = customersDAO.getOneCustomerByEmailAndPassword(email, password).getId();
        return true;
    }

    public void purchaseCoupon(Coupon coupon) throws SQLException, InterruptedException, CouponsException {
        if (!(couponsDAO.isPurchasable(coupon, customerID))) {
                throw new CouponsException(Errors.COUPON_CANNOT_BE_BOUGHT.getErrorMessage());
        }

        couponsDAO.addCouponPurchase(customerID,coupon.getId());
        coupon.setAmount(coupon.getAmount() -1);
        couponsDAO.updateCoupon(coupon,coupon.getId());
    }

    public List<Coupon> getCustomerCoupons() throws SQLException, InterruptedException {
        return couponsDAO.getAllCouponsPurchasedByCustomer(customerID);
    }

    public List<Coupon> getCustomerCoupons(Category category) throws SQLException, InterruptedException {
        return couponsDAO.getAllCouponsPurchasedByCustomerAndCategory(customerID,category);
    }

    public List<Coupon> getCustomerCoupons(double maxPrice) throws SQLException, InterruptedException {
        return couponsDAO.getAllCouponsPurchasedByCustomerAndMaxPrice(customerID,maxPrice);
    }

    public Customer getCustomerDetails() throws SQLException, CouponsException, InterruptedException {
        return customersDAO.getOneCustomer(customerID);
    }
}
