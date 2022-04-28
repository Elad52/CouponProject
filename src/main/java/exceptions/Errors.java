package exceptions;

public enum Errors {
    SCHEMA_ALREADY_EXIST("Can't create schema, Schema name already exist."),
    SCHEMA_NOT_EXIST("Can't drop schema, schema does not exist."),
    TABLE_ALREADY_EXIST("Can't create table, table already exist."),
    NO_OBJECT_TO_RETURN("No suitable results."),
    COMPANY_ALREADY_EXISTS("Company with the same name or email already exists."),
    COMPANY_NOT_EXISTS("Company with the id specified does not exist."),
    CUSTOMER_ALREADY_EXISTS("Customer with the same email already exists."),
    CUSTOMER_NOT_EXISTS("Customer with the id specified does not exist."),
    COUPON_TITLE_ALREADY_EXIST("Coupon with the same title already exist for this company."),
    COUPON_NOT_EXIST("Coupon with the id specified does not exist."),
    COUPON_CANNOT_BE_BOUGHT("This coupon currently is either expired, out of stock or you already bought this coupon.");

    private final String errorMessage;

    Errors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
