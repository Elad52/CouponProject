package beans;

/*
Created by Elad on 02/04/2022

*/

import Facade.AdminFacade;
import Facade.ClientFacade;
import Facade.CompanyFacade;
import Facade.CustomerFacade;

import java.sql.SQLException;

public class LoginManager {
    private final static LoginManager instance = new LoginManager();

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        return instance;
    }

    public ClientFacade login(String email, String password, ClientType clientType) throws SQLException, InterruptedException {
        switch (clientType.name()) {
            case "Administrator":
                AdminFacade a1 = new AdminFacade();
                if (a1.login(email, password)) {
                    return a1;
                }
                break;
            case "Company":
                CompanyFacade company1 = new CompanyFacade();
                if (company1.login(email, password)) {
                    return company1;
                }
                break;
            case "Customer":
                CustomerFacade customer1 = new CustomerFacade();
                if (customer1.login(email, password)) {
                    return customer1;
                }
                break;
        }
        return null;
    }
}
