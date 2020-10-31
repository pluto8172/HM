package com.wl.pluto.component.bean;


import java.util.List;

/**
 * Created by Android Studio
 * user : Vondear
 * date : 2017/3/14 ${time}
 * desc :
 */
public class CityModel {

    private String cityName;
    private String cityCode;

    private List<CityModel> countyList;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }


    public List<CityModel> getCountyList() {
        return countyList;
    }

    public void setCountyList(List<CityModel> countyList) {
        this.countyList = countyList;
    }
}
