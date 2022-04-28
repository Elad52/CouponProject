package dao;

/*
Created by Elad on 29/03/2022

*/

import beans.Category;
import exceptions.CouponsException;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {
    boolean isCategoryExist(String category);
    void addCategory(String category) throws SQLException, InterruptedException;
    void deleteCategory(int categoryId) throws SQLException, InterruptedException;
    void updateCategory(String category, int categoryId) throws SQLException, InterruptedException;
    List<Category> getAllCategories() throws SQLException, InterruptedException, CouponsException;
}
