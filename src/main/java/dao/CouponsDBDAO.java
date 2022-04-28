package dao;

/*
Created by Elad on 27/03/2022

*/

import beans.Category;
import beans.Coupon;
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

public class CouponsDBDAO implements CouponsDAO{
    private static final String QUERY_ADD_COUPON = "INSERT INTO `coupondb`.`coupons` (`category_id`, `company_id`, `title`, `description`, `start_date`, `end_date`, `amount`, `price`, `image`) select coupondb.categories.id, ?, ?, ?, ?, ?, ?, ?, ? from coupondb.categories where name = ?;";
    private static final String QUERY_UPDATE_COUPON = "update coupondb.coupons c inner join coupondb.categories ca on ca.name = ? set `category_id` = ca.id, `title` = ?, `description` = ?, `start_date` = ?," +
            "`end_date` = ?, `amount` = ?, `price` = ?, `image` = ?  where c.id = ?;"; //
    private static final String QUERY_DELETE_COUPON = "DELETE FROM `coupondb`.`coupons` WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL_COUPONS = "SELECT c.id, c.company_id, ca.name, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image FROM coupondb.coupons c JOIN coupondb.categories ca on c.category_id = ca.id";
    private static final String QUERY_GET_EXPIRED_TODAY_COUPONS = "SELECT * FROM coupondb.coupons WHERE end_date <= curdate();";
    private static final String QUERY_IS_EXIST_COUPONS_BY_COMPANY_AND_TITLE = "SELECT exists(select * FROM coupondb.coupons WHERE company_id = ? AND title = ?) as res;";
    private static final String QUERY_IS_EXIST_COUPONS_BY_ID = "SELECT exists(select * FROM coupondb.coupons WHERE id = ?) as res;";
    private static final String QUERY_GET_ALL_COUPONS_BY_COMPANY = "SELECT c.id, c.company_id, ca.name, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image FROM coupondb.coupons c JOIN coupondb.categories ca on c.category_id = ca.id WHERE company_id = ?";
    private static final String QUERY_GET_ALL_COUPONS_BY_COMPANY_AND_CATEGORY = "SELECT c.id, c.company_id, ca.name, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image FROM coupondb.coupons c JOIN coupondb.categories ca on c.category_id = ca.id WHERE company_id = ? AND ca.name = ?;";
    private static final String QUERY_GET_ALL_COUPONS_BY_COMPANY_AND_MAX_PRICE = "SELECT c.id, c.company_id, ca.name, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image FROM coupondb.coupons c JOIN coupondb.categories ca on c.category_id = ca.id WHERE company_id = ? AND price < ?;";
    private static final String QUERY_GET_ONE_COUPON = "SELECT c.id, c.company_id, ca.name, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image FROM coupondb.coupons c JOIN coupondb.categories ca on c.category_id = ca.id WHERE c.id = ?";
    private static final String QUERY_GET_COUPON_ID = "SELECT id FROM coupondb.coupons WHERE company_id = ? AND title = ?";
    private static final String QUERY_ADD_COUPON_PURCHASE = "INSERT INTO `coupondb`.`customers_vs_coupons` (`customer_id`, `coupon_id`) VALUES (?, ?);";
    private static final String QUERY_DELETE_COUPON_PURCHASE = "DELETE FROM `coupondb`.`customers_vs_coupons` WHERE (`customer_id` = ?) and (`coupon_id` = ?);";
    private static final String QUERY_DELETE_COUPON_PURCHASE_BY_COUPON = "DELETE FROM `coupondb`.`customers_vs_coupons` WHERE (`coupon_id` = ?);";
    private static final String QUERY_DELETE_COUPON_PURCHASE_BY_CUSTOMER = "DELETE FROM `coupondb`.`customers_vs_coupons` WHERE (`customer_id` = ?);";
    private static final String QUERY_GET_COUPONS_PURCHASED_BY_CUSTOMER = "SELECT c.id,  c.company_id, c.category_id, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image, cat.name FROM coupondb.coupons c JOIN coupondb.customers_vs_coupons cvc JOIN coupondb.categories cat ON c.id = cvc.coupon_id  AND cat.id = c.category_id where cvc.customer_id = ?;";
    private static final String QUERY_GET_COUPONS_PURCHASED_BY_CUSTOMER_AND_CATEGORY = "SELECT c.id,  c.company_id, c.category_id, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image, cat.name FROM coupondb.coupons c JOIN coupondb.customers_vs_coupons cvc JOIN coupondb.categories cat ON c.id = cvc.coupon_id  AND cat.id = c.category_id where cvc.customer_id = ? AND cat.name = ?;";
    private static final String QUERY_GET_COUPONS_PURCHASED_BY_CUSTOMER_AND_MAX_PRICE = "SELECT c.id,  c.company_id, c.category_id, c.title, c.description, c.start_date, c.end_date, c.amount, c.price, c.image, cat.name FROM coupondb.coupons c JOIN coupondb.customers_vs_coupons cvc JOIN coupondb.categories cat ON c.id = cvc.coupon_id  AND cat.id = c.category_id where cvc.customer_id = ? AND price < ?;";

    private static final String QUERY_IS_PURCHASABLE = "SELECT exists(SELECT * FROM coupondb.coupons WHERE amount > 0 AND end_date > curdate() AND id = ?)  AND NOT exists(SELECT * FROM coupondb.customers_vs_coupons where coupon_id = ? and customer_id = ?) as res;";

    @Override
    public void addCoupon(Coupon coupon) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,coupon.getCompanyId());
        params.put(2,coupon.getTitle());
        params.put(3,coupon.getDescription());
        params.put(4,coupon.getStartDate().toString());
        params.put(5,coupon.getEndDate().toString());
        params.put(6,coupon.getAmount());
        params.put(7,coupon.getPrice());
        params.put(8,coupon.getImage());
        params.put(9,coupon.getCategory().toString());

        execute(QUERY_ADD_COUPON,params);
    }

    @Override
    public void updateCoupon(Coupon coupon, int couponID) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,coupon.getCategory().toString());
        params.put(2,coupon.getTitle());
        params.put(3,coupon.getDescription());
        params.put(4,coupon.getStartDate().toString());
        params.put(5,coupon.getEndDate().toString());
        params.put(6,coupon.getAmount());
        params.put(7,coupon.getPrice());
        params.put(8,coupon.getImage());
        params.put(9,couponID);

        execute(QUERY_UPDATE_COUPON,params);
    }

    @Override
    public void deleteCoupon(int couponID) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,couponID);

        execute(QUERY_DELETE_COUPON,params);
    }

    @Override
    public List<Coupon> getAllCoupons() throws SQLException, InterruptedException, CouponsException {
        List<Coupon> results = new ArrayList<>();
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL_COUPONS);

        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public List<Coupon> getAllCouponsByCompany(int companyID) throws SQLException, InterruptedException, CouponsException {
        List<Coupon> results = new ArrayList<>();
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,companyID);
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL_COUPONS_BY_COMPANY,params);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public List<Coupon> getAllCouponsByCompanyAndMaxPrice(int companyID,double amount) throws SQLException, InterruptedException {
        List<Coupon> results = new ArrayList<>();
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,companyID);
        params.put(2,amount);
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL_COUPONS_BY_COMPANY_AND_MAX_PRICE,params);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public List<Coupon> getAllCouponsByCompanyAndCategory(int companyID,Category category) throws SQLException, InterruptedException {
        List<Coupon> results = new ArrayList<>();
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,companyID);
        params.put(2,category.name());
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL_COUPONS_BY_COMPANY_AND_CATEGORY,params);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public Coupon getOneCoupon(int couponID) throws SQLException, InterruptedException, CouponsException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,couponID);
        Coupon c1 = new Coupon();

        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ONE_COUPON,params);

        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }
        for (Object row: rows) {
            c1 = ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row);
        }

        return c1;
    }

    @Override
    public int getCouponID(int companyID, String title) throws SQLException, InterruptedException, CouponsException {
        int id = 0;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,companyID);
        params.put(2,title);

        List<?> rows = JDBCUtils.executeResults(QUERY_GET_COUPON_ID,params);

        for (Object row: rows) {
            id = ResultsUtils.fromHashMapReturnInt((HashMap<String,Object>) row);
        }

        return id;

    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,customerID);
        params.put(2,couponID);

        execute(QUERY_ADD_COUPON_PURCHASE,params);
    }

    @Override
    public void deleteCouponPurchaseByCoupon(int couponID) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,couponID);

        execute(QUERY_DELETE_COUPON_PURCHASE_BY_COUPON,params);
    }

    @Override
    public void deleteCouponPurchaseByCustomer(int customerID) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,customerID);

        execute(QUERY_DELETE_COUPON_PURCHASE_BY_CUSTOMER,params);
    }

    public boolean IsExistByCompanyAndTitle(int companyID, String title) throws SQLException, InterruptedException {
        boolean isExist = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,companyID);
        params.put(2,title);

        List<?> rows = JDBCUtils.executeResults(QUERY_IS_EXIST_COUPONS_BY_COMPANY_AND_TITLE,params);

        for (Object row: rows) {
            isExist = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return isExist;
    }

    public boolean IsExistByID(int couponID) throws SQLException, InterruptedException {
        boolean isExist = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,couponID);

        List<?> rows = JDBCUtils.executeResults(QUERY_IS_EXIST_COUPONS_BY_ID,params);

        for (Object row: rows) {
            isExist = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return isExist;
    }

    @Override
    public List<Coupon> getAllCouponsPurchasedByCustomer(int customerID) throws SQLException, InterruptedException {
        List<Coupon> results = new ArrayList<>();
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,customerID);
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_COUPONS_PURCHASED_BY_CUSTOMER,params);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public List<Coupon> getAllCouponsPurchasedByCustomerAndCategory(int customerID, Category category) throws SQLException, InterruptedException {
        List<Coupon> results = new ArrayList<>();
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,customerID);
        params.put(2,category.name());
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_COUPONS_PURCHASED_BY_CUSTOMER_AND_CATEGORY,params);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public List<Coupon> getAllCouponsPurchasedByCustomerAndMaxPrice(int customerID, double maxPrice) throws SQLException, InterruptedException {
        List<Coupon> results = new ArrayList<>();
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,customerID);
        params.put(2,maxPrice);
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_COUPONS_PURCHASED_BY_CUSTOMER_AND_MAX_PRICE,params);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public List<Coupon> getAllExpiredCoupons() throws SQLException, InterruptedException {
        List<Coupon> results = new ArrayList<>();
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_EXPIRED_TODAY_COUPONS);

        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCoupons((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public boolean isPurchasable(Coupon coupon, int customerId) throws SQLException, InterruptedException, CouponsException {
        boolean isPurchasable = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,coupon.getId());
        params.put(2,coupon.getId());
        params.put(3,customerId);
        List<?> rows = JDBCUtils.executeResults(QUERY_IS_PURCHASABLE,params);

        for (Object row:rows) {
            isPurchasable = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return isPurchasable;
    }


}