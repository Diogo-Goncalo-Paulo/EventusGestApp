package com.eventusgest.listeners;

public interface LoginListener {
    void onValidateLogin(String username, String password, String response);
}
