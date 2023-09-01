package com.example.atipieraproject.error;

public class GithubUserNotFoundException extends Exception {
    public GithubUserNotFoundException(){
        super("Specified Github user doesn't exist.");
    }
}