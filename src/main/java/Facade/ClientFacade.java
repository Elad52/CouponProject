package Facade;/*
Created by Elad on 01/04/2022

*/

import dao.*;
import exceptions.CouponsException;

import java.sql.SQLException;

public abstract class ClientFacade {
    protected CompaniesDAO companiesDAO = new CompaniesDBDAO();
    protected CustomersDAO customersDAO = new CustomersDBDAO();
    protected CouponsDAO couponsDAO = new CouponsDBDAO();

    public abstract boolean login(String email, String password) throws SQLException, InterruptedException, CouponsException;
}
