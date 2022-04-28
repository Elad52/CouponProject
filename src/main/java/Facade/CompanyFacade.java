package Facade;

/*
Created by Elad on 01/04/2022

*/

import beans.Category;
import beans.Company;
import beans.Coupon;
import exceptions.CouponsException;
import exceptions.Errors;

import java.sql.SQLException;
import java.util.List;

public class CompanyFacade extends ClientFacade{
    private int companyID;

    public CompanyFacade() {
    }

    @Override
    public boolean login(String email, String password) throws SQLException, InterruptedException {
        if(!(companiesDAO.isCompanyExistsByEmailAndPassword(email, password))){
            return false;
        }
        companyID = companiesDAO.getOneCompanyByEmailAndPassword(email, password).getId();
        return true;
    }

    public void addCoupon(Coupon coupon) throws SQLException, InterruptedException, CouponsException {
        if (couponsDAO.IsExistByCompanyAndTitle(coupon.getCompanyId(),coupon.getTitle())){
            throw new CouponsException(Errors.COUPON_TITLE_ALREADY_EXIST.getErrorMessage());
        }
        coupon.setCompanyId(companyID);
        couponsDAO.addCoupon(coupon);
    }

    public void updateCoupon(int couponID, Coupon coupon) throws SQLException, InterruptedException, CouponsException {
        if(!couponsDAO.IsExistByID(couponID)){
            throw new CouponsException(Errors.COUPON_NOT_EXIST.getErrorMessage());
        }
        couponsDAO.updateCoupon(coupon,couponID);
    }

    public void deleteCoupon(int couponID) throws SQLException, InterruptedException, CouponsException {
        if (!(couponsDAO.IsExistByID(couponID))){
            throw new CouponsException(Errors.COUPON_NOT_EXIST.getErrorMessage());
        }
        couponsDAO.deleteCouponPurchaseByCoupon(couponID);
        couponsDAO.deleteCoupon(couponID);
    }

    public List<Coupon> getCompanyCoupons() throws SQLException, CouponsException, InterruptedException {
        return couponsDAO.getAllCouponsByCompany(companyID);
    }

    public List<Coupon> getCompanyCoupons(Category category) throws SQLException, InterruptedException {
        return couponsDAO.getAllCouponsByCompanyAndCategory(companyID, category);
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) throws SQLException, InterruptedException {
        return couponsDAO.getAllCouponsByCompanyAndMaxPrice(companyID, (int) maxPrice);
    }

    public Company getCompanyDetails() throws SQLException, CouponsException, InterruptedException {
        return companiesDAO.getOneCompany(companyID);
    }
}
