package utils;

/*
Created by Elad on 05/04/2022

*/

import beans.Company;
import beans.Coupon;
import beans.Customer;
import exceptions.CouponsException;

import java.sql.SQLException;
import java.util.List;

public class Tables {
    private static final StringBuilder sb = new StringBuilder();

    public static StringBuilder drawCoupon(Coupon coupon) throws SQLException, CouponsException, InterruptedException {
        sb.delete(0,sb.length());
        sb.append("|  id  |   companyId  |    category   |       title      |     description   |     startDate     |      endDate     |   amount  |     price   |   image  | \n ---------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("|  %d   |       %d      |  %-13s|  %-13s   |   %-13s   |   %-13s   |   %-13s  |     %-2s    |   %-6s    |   %-3s    |\n---------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                ,coupon.getId(),coupon.getCompanyId(),coupon.getCategory().name(),coupon.getTitle(),coupon.getDescription(),coupon.getStartDate().toString(),coupon.getEndDate().toString(),
                coupon.getAmount(),coupon.getPrice(),coupon.getImage()));
        return sb;
    }

    public static StringBuilder drawCoupons(List<Coupon> coupons) throws SQLException, CouponsException, InterruptedException {
        sb.delete(0,sb.length());
        sb.append("|  id  |   companyId  |    category   |       title      |     description   |     startDate     |      endDate     |   amount  |     price   |   image  | \n ---------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        for (Coupon coupon:coupons) {
            sb.append(String.format("|  %d   |       %d      |  %-13s|  %-13s   |   %-13s   |   %-13s   |   %-13s  |     %-2s    |   %-6s    |   %-3s    |\n---------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    ,coupon.getId(),coupon.getCompanyId(),coupon.getCategory().name(),coupon.getTitle(),coupon.getDescription(),coupon.getStartDate().toString(),coupon.getEndDate().toString(),
                    coupon.getAmount(),coupon.getPrice(),coupon.getImage()));
        }
        return sb;
    }

    public static StringBuilder drawCustomer(Customer customer){
        sb.delete(0,sb.length());
        sb.append("|   id    |      firstName      |      lastName        |            email             |      password      |\n--------------------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("|   %d     |       %-7s       |       %-7s        |        %-16s         |      %-7s       |\n------------------------------------------------------------------------------------------------------------\n",
                    customer.getId(),customer.getFirstName(),customer.getLastName(),customer.getEmail(),customer.getPassword()));
        return sb;
    }

    public static StringBuilder drawCustomers(List<Customer> customers){
        sb.delete(0,sb.length());
        sb.append("|   id    |      firstName      |      lastName        |             email               |      password      |\n------------------------------------------------------------------------------------------------------------\n");
        for (Customer customer:customers) {
            sb.append(String.format("|   %d     |       %-7s       |       %-7s        |        %-16s         |      %-7s       |\n-------------------------------------------------------------------------------------------------------------\n",
                    customer.getId(),customer.getFirstName(),customer.getLastName(),customer.getEmail(),customer.getPassword()));
        }
        return sb;
    }

    public static StringBuilder drawCompany(Company company){
        sb.delete(0,sb.length());
        sb.append("|   id    |      firstName      |           email              |          password            |\n-----------------------------------------------------------------------------------------------\n");
        sb.append(String.format("|   %d     |       %-7s       |        %-13s         |      %-17s       |\n-------------------------------------------------------------------------------------------------\n",
                company.getId(),company.getName(),company.getEmail(),company.getPassword()));
        return sb;
    }

    public static StringBuilder drawCompanies(List<Company> companies){
        sb.delete(0,sb.length());
        sb.append("|   id    |      firstName      |           email              |          password            |\n-----------------------------------------------------------------------------------------------\n");
        for (Company company:companies) {
            sb.append(String.format("|   %d     |       %-7s       |        %-13s         |      %-17s       |\n-----------------------------------------------------------------------------------------------\n",
                    company.getId(),company.getName(),company.getEmail(),company.getPassword()));
        }

        return sb;
    }
}