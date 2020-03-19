package com.factor8.p1m1.Model;

public class OtpVModel {

    /**
     * error : 0
     * loggedIn : 1
     * message : Number Verified.
     * accessToken :
     * userData : {"user_id":"2","fullname":null,"email":null,"password":null,"mobile":"9654829994","salary":"0101001","picture":null,"created_at":"2020-03-06 08:52:53","updated_at":"2020-03-06 08:52:53"}
     */

    private int error;
    private int loggedIn;
    private String message;
    private String accessToken;
    private UserDataBean userData;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(int loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserDataBean getUserData() {
        return userData;
    }

    public void setUserData(UserDataBean userData) {
        this.userData = userData;
    }

    public static class UserDataBean {
        /**
         * user_id : 2
         * fullname : null
         * email : null
         * password : null
         * mobile : 9654829994
         * salary : 0101001
         * picture : null
         * created_at : 2020-03-06 08:52:53
         * updated_at : 2020-03-06 08:52:53
         */

        private String user_id;
        private Object fullname;
        private Object email;
        private Object password;
        private String mobile;
        private String salary;
        private Object picture;
        private String created_at;
        private String updated_at;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public Object getFullname() {
            return fullname;
        }

        public void setFullname(Object fullname) {
            this.fullname = fullname;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public Object getPicture() {
            return picture;
        }

        public void setPicture(Object picture) {
            this.picture = picture;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
