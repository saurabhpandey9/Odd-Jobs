package com.developerdesk9.ecommerce;

import java.text.NumberFormat;
import java.util.Locale;

class CommaSeperate {


    public static String getFormatedNumber(String number){
        if(number!=null) {
            try {
                double val = Double.parseDouble(number);
                return NumberFormat.getNumberInstance(Locale.US).format(val);
            }catch (Exception e){
                return "Error";
            }

        }else{
            return "XX,XXX";
        }
    }
}
