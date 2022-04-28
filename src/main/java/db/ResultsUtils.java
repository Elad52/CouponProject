package db;

/*
Created by Elad on 27/03/2022

*/

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;

import java.util.Date;
import java.util.HashMap;
public class ResultsUtils {

    public static boolean fromHashMapToBool(HashMap<String,Object> row){
        long isExist = (long) row.get("res");
        return isExist==1;
    }

    public static Company fromHashMapReturnCompany(HashMap<String,Object> row){
        int id = (int) row.get("id");
        String name = (String) row.get("name");
        String email = (String) row.get("email");
        String password = (String) row.get("password");

        return new Company(id,name,email,password);
    }

    public static Customer fromHashMapReturnCustomer(HashMap<String,Object> row){
        int id = (int) row.get("id");
        String firstName = (String) row.get("first_name");
        String lastName = (String) row.get("last_name");
        String email = (String) row.get("email");
        String password = (String) row.get("password");

        return new Customer(id,firstName,lastName,email,password);
    }

    public static Coupon fromHashMapReturnCoupons(HashMap<String,Object> row){
        int id = (int) row.get("id");
        int companyId = (int) row.get("company_id");
        String categoryString = (String) row.get("name");
        Category category = Category.valueOf(categoryString);
        String title = (String) row.get("title");
        String description = (String) row.get("description");
        Date startDate = (Date) row.get("start_date");
        Date endDate = (Date) row.get("end_date");
        int amount = (int) row.get("amount");
        double price = (double) row.get("price");
        String image = (String) row.get("image");

        return new Coupon(id,companyId,category,title,description,startDate,endDate,amount,price,image);
    }

    public static Category fromHashMapReturnCategory(HashMap<String,Object> row){
        String name = (String) row.get("name");

        return Category.valueOf(name);
    }

    public static int fromHashMapReturnInt(HashMap<String,Object> row){
        return  (Integer) row.get("id");
    }
}
