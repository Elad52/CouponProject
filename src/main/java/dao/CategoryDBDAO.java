package dao;

/*
Created by Elad on 29/03/2022

*/

import beans.Category;
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

import static db.JDBCUtils.execute;

public class CategoryDBDAO implements CategoryDAO{
    private static final String QUERY_ADD_CATEGORY = "INSERT INTO `coupondb`.`categories` (`name`) VALUES (?)";
    private static final String QUERY_DELETE_CATEGORY = "DELETE FROM `coupondb`.`categories` WHERE (`id` = ?);";
    private static final String QUERY_UPDATE_CATEGORY = "UPDATE `coupondb`.`categories` SET `name` = ? WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL = "SELECT * FROM coupondb.categories;";


    @Override
    public boolean isCategoryExist(String category) {
        return false;
    }

    @Override
    public void addCategory(String category) throws SQLException, InterruptedException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,category);

        execute(QUERY_ADD_CATEGORY,params);
    }

    @Override
    public void deleteCategory(int categoryId) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,categoryId);

        execute(QUERY_DELETE_CATEGORY,params);
    }

    @Override
    public void updateCategory(String category, int categoryId) throws SQLException, InterruptedException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1,category);
        params.put(2,categoryId);

        execute(QUERY_UPDATE_CATEGORY,params);
    }

    @Override
    public List<Category> getAllCategories() throws SQLException, InterruptedException, CouponsException {
        List<Category> results = new ArrayList<>();
        List<?> rows = JDBCUtils.executeResults(QUERY_GET_ALL);

        if(rows.size() < 1){
            throw new CouponsException(Errors.NO_OBJECT_TO_RETURN.getErrorMessage());
        }
        for (Object row:rows) {
            results.add(ResultsUtils.fromHashMapReturnCategory((HashMap<String,Object>) row));
        }

        return results;
    }
}
