package com.eventusgest.listeners;

import com.eventusgest.modelo.Credential;

import java.util.ArrayList;

public interface CredentialListener {
    void onRefreshCredentialList(ArrayList<Credential> credentialList);
}
