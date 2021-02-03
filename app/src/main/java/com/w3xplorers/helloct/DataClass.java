package com.w3xplorers.helloct;

/**
 * Created by Avijit on 3/7/2017.
 */

public class DataClass {

    private String crime_info,division,district,thana,provider_phone,provider_address;

    public DataClass(String crime_info,String division,String district,String thana,String provider_phone,String provider_address){
        this.setCrime_info(crime_info);
        this.setDivision(division);
        this.setDistrict(district);
        this.setThana(thana);
        this.setProvider_phone(provider_phone);
        this.setProvider_address(provider_address);
    }


    public String getCrime_info(){
        return crime_info;
    }

    public void setCrime_info(String crime_info){
        this.crime_info=crime_info;
    }

    public String getDivision(){
        return division;
    }

    public void setDivision(String division){
        this.division=division;
    }

    public String getDistrict(){
        return district;
    }

    public void setDistrict(String district){
        this.district=district;
    }

    public String getThana(){
        return thana;
    }

    public void setThana(String thana){
        this.thana=thana;
    }

    public String getProvider_phone(){
        return provider_phone;
    }

    public void setProvider_phone(String provider_phone){
        this.provider_phone=provider_phone;
    }

    public String getProvider_address(){
        return provider_address;
    }

    public void setProvider_address(String provider_address){
        this.provider_address=provider_address;
    }




}
