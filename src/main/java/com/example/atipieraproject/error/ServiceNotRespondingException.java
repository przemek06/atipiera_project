package com.example.atipieraproject.error;

public class ServiceNotRespondingException extends Exception{
    public ServiceNotRespondingException(){
        super("Requested service is not responding.");
    }

}
