package com.eventusgest.listeners;

import com.eventusgest.modelo.Credential;

public interface CredentialFlagBlockListener {
    void onFlagCredential(Credential credential);

    void onBlockCredential();
}
