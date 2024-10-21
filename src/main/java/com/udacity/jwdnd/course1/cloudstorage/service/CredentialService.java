package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public List<Credential> getCredentials(int userId){
        return credentialMapper.getCredentialsByUserId(userId);
    }

    public void createCredential(Credential credential, int userId){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredentials = new Credential();
        newCredentials.setUrl(credential.getUrl());
        newCredentials.setUsername(credential.getUsername());
        newCredentials.setKey(encodedKey);
        newCredentials.setPassword(encryptedPassword);
        newCredentials.setUserId(userId);

        credentialMapper.insertCredential(newCredentials);
    }

    public void deleteCredential(int credentialId){
        credentialMapper.deleteCredential(credentialId);
    }

    public void updateCredential(Credential credential){
        Credential storedCredential = credentialMapper.getCredentialById(credential.getCredentialId());

        credential.setKey(storedCredential.getKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        credentialMapper.updateCredential(credential);
    }
}
