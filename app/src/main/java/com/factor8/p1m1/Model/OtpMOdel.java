package com.factor8.p1m1.Model;

public class OtpMOdel {


    /**
     * success : true
     * message : OTP sent.
     * data : {"otp":3519}
     */

    private boolean success;
    private String message;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * otp : 3519
         */

        private int otp;

        public int getOtp() {
            return otp;
        }

        public void setOtp(int otp) {
            this.otp = otp;
        }
    }
}
