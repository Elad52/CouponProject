package tasks;

/*
Created by Elad on 02/04/2022

*/

import beans.Coupon;
import dao.CouponsDAO;
import dao.CouponsDBDAO;
import exceptions.CouponsException;

import java.time.LocalTime;
import java.util.List;

public class CouponExpirationDailyJob implements Runnable {
    private final static CouponsDAO couponsDAO = new CouponsDBDAO();
    private static boolean quit = false;
    private final static int SINGLE_DAY = 1000*60*60*24;

    public CouponExpirationDailyJob() {
    }

    @Override
    public void run() {
        while (!quit) {
            if (LocalTime.now() == LocalTime.MIDNIGHT) {
                try {
                    List<Coupon> expiredCoupons = couponsDAO.getAllExpiredCoupons();
                    for (Coupon coupon : expiredCoupons) {
                        couponsDAO.deleteCoupon(coupon.getId());
                    }
                    Thread.sleep(SINGLE_DAY);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void stop(){
        quit = true;
    }
}
