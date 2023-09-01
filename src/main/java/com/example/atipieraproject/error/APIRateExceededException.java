package com.example.atipieraproject.error;

public class APIRateExceededException extends Exception{
    public APIRateExceededException(){
        super("Github API request rate exceeded.");
    }

}
