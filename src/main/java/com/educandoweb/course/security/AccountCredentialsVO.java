package com.educandoweb.course.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsVO implements Serializable {

    private String username;
    private String passwor;

    public AccountCredentialsVO(){}

    public AccountCredentialsVO(String username, String passwor) {
        this.username = username;
        this.passwor = passwor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswor() {
        return passwor;
    }

    public void setPasswor(String passwor) {
        this.passwor = passwor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountCredentialsVO that)) return false;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPasswor(), that.getPasswor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPasswor());
    }
}
