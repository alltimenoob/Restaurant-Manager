package com.project.restaurantmanager.Data;

public class Reservation {
    String rdate,starttime,endtime,guests,deposit,tno;

    public Reservation(String rdate, String starttime, String endtime, String guests, String deposit,String tno) {
        this.rdate = rdate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.guests = guests;
        this.deposit = deposit;
        this.tno = tno;
    }

    public String getRdate() {
        return rdate;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getGuests() {
        return guests;
    }

    public String getDeposit() {
        return deposit;
    }

    public String getTno() {
        return tno;
    }
}
