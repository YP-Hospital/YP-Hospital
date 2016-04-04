package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.User;

import java.util.List;
import java.util.Map;

public interface CertificateService {
    Boolean deleteCertificate(Integer id);
    Certificate getCertificateByUser(User user);
    String getSignatureByPrivateKey(String privateKey);
    List<Certificate> getAllCertificates();
    Map<User, Certificate> getAllCertificatesWithUsersNames();
}
