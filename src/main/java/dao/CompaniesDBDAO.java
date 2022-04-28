package dao;

/*
Created by Elad on 27/03/2022

*/

import beans.Company;
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

public class CompaniesDBDAO implements CompaniesDAO{
    private static final String QUERY_IS_EXIST_EMAIL_AND_PASSWORD = "select exists (SELECT * FROM coupondb.companies where email = ? AND password = ?) as res;";
    private static final String QUERY_IS_EXIST_NAME_OR_EMAIL = "select exists (SELECT * FROM coupondb.companies where name = ? OR email = ?) as res;";
    private static final String QUERY_IS_EXIST_BY_ID = "select exists (SELECT * FROM coupondb.companies where id = ?) as res;";
    private static final String QUERY_ADD_COMPANY = "INSERT INTO `coupondb`.`companies` (`name`, `email`, `password`) VALUES (?, ?, ?);";
    private static final String QUERY_DELETE_COMPANY = "DELETE FROM `coupondb`.`companies` WHERE (`id` = ?);";
    private static final String QUERY_UPDATE_COMPANY = "UPDATE `coupondb`.`companies` SET `email` = ?, `password` = ? WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL_COMPANIES = "SELECT * FROM coupondb.companies;";
    private static final String QUERY_GET_ONE_COMPANY = "SELECT * FROM coupondb.companies WHERE id = ?;";
    private static final String QUERY_GET_ONE_COMPANY_BY_EMAIL_AND_PASSWORD = "SELECT * FROM coupondb.companies WHERE email = ? AND password = ?;";

    @Override
    public boolean isCompanyExistsByEmailAndPassword(String email, String password) throws SQLException, InterruptedException {
        boolean results = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,email);
        params.put(2,password);

        List<?> rows = executeResults(QUERY_IS_EXIST_EMAIL_AND_PASSWORD,params);
        for (Object row:rows) {
            results = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return results;
    }

    @Override
    public boolean isCompanyExistsByNameOrEmail(String name, String email) throws SQLException, InterruptedException {
        boolean results = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,name);
        params.put(2,email);

        List<?> rows = executeResults(QUERY_IS_EXIST_NAME_OR_EMAIL,params);
        for (Object row:rows) {
            results = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return results;
    }

    @Override
    public boolean isCompanyExistsID(int companyID) throws SQLException, InterruptedException {
        boolean results = false;
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,companyID);

        List<?> rows = executeResults(QUERY_IS_EXIST_BY_ID,params);
        for (Object row:rows) {
            results = ResultsUtils.fromHashMapToBool((HashMap<String,Object>) row);
        }

        return results;
    }

    @Override
    public void addCompany(Company company) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,company.getName());
        params.put(2,company.getEmail());
        params.put(3,company.getPassword());

        execute(QUERY_ADD_COMPANY,params);
    }

    @Override
    public void updateCompany(Company company, int companyID) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,company.getEmail());
        params.put(2,company.getPassword());
        params.put(3,companyID);

        execute(QUERY_UPDATE_COMPANY,params);
    }

    @Override
    public void deleteCompany(int companyId) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,companyId);

        execute(QUERY_DELETE_COMPANY,params);
    }

    @Override
    public List<Company> getAllCompanies() throws SQLException, InterruptedException, CouponsException {
        List<Company> results = new ArrayList<>();
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL_COMPANIES);

        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }
        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCompany((HashMap<String,Object>) row));
        }

        return results;
    }

    @Override
    public Company getOneCompany(int companyID) throws SQLException, InterruptedException, CouponsException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,companyID);
        Company c1 = new Company();

        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ONE_COMPANY,params);
        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }
        for (Object row: rows) {
            c1 = ResultsUtils.fromHashMapReturnCompany((HashMap<String,Object>) row);
        }

        return c1;
    }

    @Override
    public Company getOneCompanyByEmailAndPassword(String email, String password) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,email);
        params.put(2,password);
        Company c1 = new Company();

        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ONE_COMPANY_BY_EMAIL_AND_PASSWORD,params);
        for (Object row: rows) {
            c1 = ResultsUtils.fromHashMapReturnCompany((HashMap<String,Object>) row);
        }

        return c1;
    }
}
