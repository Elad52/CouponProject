/*
Created by Elad on 27/03/2022

*/

import Facade.AdminFacade;
import Facade.CompanyFacade;
import Facade.CustomerFacade;
import beans.*;
import dao.*;
import db.ConnectionPool;
import db.JDBCUtils;
import exceptions.CouponsException;
import tasks.CouponExpirationDailyJob;
import utils.Tables;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import static db.JDBCUtils.*;
import static utils.Art.*;

public class Test {
    public static void testAll() throws SQLException, CouponsException, InterruptedException {
        //TODO ADD EXCEPTIONS
        List<Category> categoryList = List.of(Category.values());
        List<Coupon> coupons = new ArrayList<>();

        CategoryDAO categoryDAO = new CategoryDBDAO();
        CouponsDAO couponsDAO = new CouponsDBDAO();
        CompaniesDAO companiesDAO = new CompaniesDBDAO();
        CustomersDAO customersDAO = new CustomersDBDAO();

        LoginManager loginManager = LoginManager.getInstance();


        Coupon coupon1 = new Coupon(1, Category.Food, "Bamba", "Peanuts snack", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(5)), 30, 19.9, "URL");
        Coupon coupon2 = new Coupon(3, Category.Restaurant, "Bisli", "Pasta snack", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(5)), 40, 15.9, "BRL");
        Coupon coupon3 = new Coupon(3, Category.Vacation, "Batumi", "Not a snack", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusYears(1)), 5, 1500.9, "BBB");
        Coupon coupon4 = new Coupon(2, Category.Vacation, "Venezuela", "south america", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusYears(1)), 5, 1200, "CCC");
        Coupon coupon5 = new Coupon(2, Category.Electricity, "Toaster", "Makes Toast", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusMonths(1)), 23, 150, "DDD");
        Coupon coupon6 = new Coupon(1, Category.Electricity, "Toaster", "Makes Toast", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusMonths(1)), 23, 150, "DDD");
        coupons.add(coupon1);


        Company c1 = new Company("Osem", "e@osem.com", "1234Secure", coupons);
        Company c2 = new Company("Kinley", "e@kinley.com", "1234MoreSecure", coupons);
        Company c3 = new Company("Strause", "e@strause.com", "1234TheMostSecure", coupons);


        Customer customer1 = new Customer("Elad", "Gabay", "e@gabay.com", "1234", coupons);
        Customer customer2 = new Customer("Tzvika", "Pick", "t@Pick.com", "5678", coupons);
        Customer customer3 = new Customer("Alon", "De Loco", "a@DeLoco.com", "3456", coupons);

        startArt();

        databaseStrategy();
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        System.out.println("Add all categories");
        for (Category c : categoryList) {
            categoryDAO.addCategory(c.toString());
        }

        System.out.println("Print all categories");
        categoryDAO.getAllCategories().forEach(System.out::println);

        adminArt();

        AdminFacade adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin", ClientType.Administrator);

        System.out.println("Add companies");
        adminFacade.addCompany(c1);
        adminFacade.addCompany(c2);
        adminFacade.addCompany(c3);
        System.out.println(Tables.drawCompanies(adminFacade.getAllCompanies()));

        System.out.println("Update company");
        c1.setEmail("e@looloo.com");
        c1.setName("eee");
        c1.setPassword("ffff");
        adminFacade.updateCompany(1, c1);
        System.out.println(Tables.drawCompanies(adminFacade.getAllCompanies()));

        System.out.println("If exists");
        System.out.println(companiesDAO.isCompanyExistsByEmailAndPassword("e@strause.com", "1234TheMostSecure"));

        System.out.println("Get one company");
        System.out.println(Tables.drawCompany(adminFacade.getOneCompany(1)));

        System.out.println("Add customers");
        adminFacade.addCustomer(customer1);
        adminFacade.addCustomer(customer2);
        adminFacade.addCustomer(customer3);
        System.out.println(Tables.drawCustomers(adminFacade.getAllCustomers()));

        System.out.println("Delete customer");
        adminFacade.deleteCustomer(2);
        System.out.println(Tables.drawCustomers(adminFacade.getAllCustomers()));

        System.out.println("Update customer");
        customer1.setEmail("Elad@HaGever.com");
        adminFacade.updateCustomer(3, customer1);
        System.out.println(Tables.drawCustomers(adminFacade.getAllCustomers()));

        System.out.println("If exists");
        System.out.println(customersDAO.isCustomerExistsByEmail("Elad@HaGever.com"));

        System.out.println("Get one customer");
        System.out.println(Tables.drawCustomer(adminFacade.getOneCustomer(3)));

        companyArt();

        System.out.println("Company login");
        CompanyFacade companyFacade = (CompanyFacade) loginManager.login("e@strause.com", "1234TheMostSecure", ClientType.Company);

        System.out.println("add coupons");
        companyFacade.addCoupon(coupon1);
        companyFacade.addCoupon(coupon2);
        companyFacade.addCoupon(coupon3);
        companyFacade.addCoupon(coupon4);
        companyFacade.addCoupon(coupon5);
        companyFacade.addCoupon(coupon6);
        System.out.println(Tables.drawCoupons(couponsDAO.getAllCoupons()));

        System.out.println("Update coupon");
        companyFacade.updateCoupon(3, coupon2);
        System.out.println(Tables.drawCoupons(couponsDAO.getAllCoupons()));
        System.out.println("Delete coupon");
        companyFacade.deleteCoupon(2);
        System.out.println(Tables.drawCoupons(couponsDAO.getAllCoupons()));

        System.out.println("Get one");
        System.out.println(Tables.drawCoupon(couponsDAO.getOneCoupon(1)));


        System.out.println("Get all by company");
        System.out.println(Tables.drawCoupons(companyFacade.getCompanyCoupons()));

        System.out.println("Get all by company and max price");
        System.out.println(Tables.drawCoupons(companyFacade.getCompanyCoupons(18)));
        System.out.println(Tables.drawCoupons(companyFacade.getCompanyCoupons(12)));

        System.out.println("Get all by company and category");
        System.out.println(Tables.drawCoupons(companyFacade.getCompanyCoupons(Category.Restaurant)));
        System.out.println(Tables.drawCoupons(companyFacade.getCompanyCoupons(Category.Food)));


        System.out.println("Get company details");
        System.out.println(Tables.drawCompany(companyFacade.getCompanyDetails()));

        customerArt();

        System.out.println("Customer login");
        CustomerFacade customerFacade = (CustomerFacade) loginManager.login("e@gabay.com", "1234", ClientType.Customer);


        System.out.println("Adding purchase");
        customerFacade.purchaseCoupon(coupon1);
        customerFacade.purchaseCoupon(coupon4);
        customerFacade.purchaseCoupon(coupon5);

        System.out.println("Get all purchased by logged user");
        System.out.println(Tables.drawCoupons(customerFacade.getCustomerCoupons()));

        System.out.println("Get all purchased by logged user and category");
        System.out.println(Tables.drawCoupons(customerFacade.getCustomerCoupons(Category.Electricity)));

        System.out.println("Get all purchased by logged user and max price");
        System.out.println(Tables.drawCoupons(customerFacade.getCustomerCoupons(100)));

        System.out.println("Get logged customer details");
        System.out.println(Tables.drawCustomer(customerFacade.getCustomerDetails()));

        System.out.println("Delete company");
        adminFacade.deleteCompany(1);
        System.out.println(Tables.drawCompanies(adminFacade.getAllCompanies()));

        ExceptionsArt();
        System.out.println("\nCompany already Exists Exception");
        try{
            adminFacade.addCompany(c2);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCompany not exists Exception");
        try{
            adminFacade.deleteCompany(-1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCustomer already exists Exception");
        try{
            adminFacade.addCustomer(customer1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCustomer not exists Exception");
        try{
            adminFacade.deleteCustomer(-1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCoupon already exists Exception");
        try{
            companyFacade.addCoupon(coupon1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCoupon not exists Exception");
        try{
            companyFacade.deleteCoupon(-1);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCoupon cannot be bought (out of stock) Exception");
        try{
            customerFacade.purchaseCoupon(coupon3);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("\nCoupon cannot be bought (already bought) Exception");
        try{
            customerFacade.purchaseCoupon(coupon4);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }



        endArt();

        connectionPool.closeAllConnections();
        CouponExpirationDailyJob.stop();
    }
}