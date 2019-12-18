package com.apps.amit.lawofattraction.utils;

public class PrivateWishesUtils {

    private String userName;
    private String userWish;
    private String userDate;

    public PrivateWishesUtils() {
    }

    public PrivateWishesUtils(String userName, String userWish, String userDate) {

        this.userName = userName;
        this.userWish = userWish;
        this.userDate = userDate;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserWish() {
        return userWish;
    }

    public void setUserWish(String userWish) {
        this.userWish = userWish;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }
}
