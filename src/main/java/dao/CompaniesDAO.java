package dao;

import beans.Company;
import exceptions.CouponsException;

import java.sql.SQLException;
import java.util.List;

public interface CompaniesDAO {
    boolean isCompanyExistsByEmailAndPassword(String email, String password) throws SQLException, InterruptedException;
    boolean isCompanyExistsByNameOrEmail(String name, String email) throws SQLException, InterruptedException;
    boolean isCompanyExistsID(int companyID) throws SQLException, InterruptedException;
    void addCompany(Company company) throws SQLException, InterruptedException;
    void updateCompany(Company company, int companyID) throws SQLException, InterruptedException;
    void deleteCompany(int companyId) throws SQLException, InterruptedException;
    List<Company> getAllCompanies() throws SQLException, InterruptedException, CouponsException;
    Company getOneCompany(int companyID) throws SQLException, InterruptedException, CouponsException;
    Company getOneCompanyByEmailAndPassword(String email, String password) throws SQLException, InterruptedException;
}
