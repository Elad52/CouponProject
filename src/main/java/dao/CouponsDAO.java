package dao;

import beans.Category;
import beans.Coupon;
import exceptions.CouponsException;

import java.sql.SQLException;
import java.util.List;

public interface CouponsDAO {
    void addCoupon(Coupon coupon) throws SQLException, InterruptedException;
    void updateCoupon(Coupon coupon, int couponID) throws SQLException, InterruptedException;
    void deleteCoupon(int couponID) throws SQLException, InterruptedException;
    List<Coupon> getAllCoupons() throws SQLException, InterruptedException, CouponsException;
    List<Coupon> getAllCouponsByCompany(int companyID) throws SQLException, InterruptedException, CouponsException;
    List<Coupon> getAllCouponsByCompanyAndMaxPrice(int companyID,double amount) throws SQLException, InterruptedException;
    List<Coupon> getAllCouponsByCompanyAndCategory(int companyID, Category category) throws SQLException, InterruptedException;
    List<Coupon> getAllExpiredCoupons() throws SQLException, InterruptedException;
    Coupon getOneCoupon(int couponID) throws SQLException, InterruptedException, CouponsException;
    int getCouponID(int companyID, String title) throws SQLException, InterruptedException, CouponsException;
    void addCouponPurchase(int customerID, int couponID) throws SQLException, InterruptedException;
    void deleteCouponPurchaseByCoupon(int couponID) throws SQLException, InterruptedException;
    void deleteCouponPurchaseByCustomer(int customerID) throws SQLException, InterruptedException;
    boolean IsExistByCompanyAndTitle(int companyID, String title) throws SQLException, InterruptedException;
    boolean IsExistByID(int couponID) throws SQLException, InterruptedException;
    List<Coupon> getAllCouponsPurchasedByCustomer(int customerID) throws SQLException, InterruptedException;
    List<Coupon> getAllCouponsPurchasedByCustomerAndCategory(int customerID, Category category) throws SQLException, InterruptedException;
    List<Coupon> getAllCouponsPurchasedByCustomerAndMaxPrice(int customerID, double maxPrice) throws SQLException, InterruptedException;
    public boolean isPurchasable(Coupon coupon, int customerId) throws SQLException, InterruptedException, CouponsException;

}
