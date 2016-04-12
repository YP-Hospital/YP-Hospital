package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.User;

import java.util.List;
import java.util.Map;

public interface CertificateService {
    Boolean deleteCertificate(Integer id, String key);
    public String getAllCertificatesSignature();
    Certificate getCertificateByUser(User user);
    List<Certificate> getAllCertificates();
    Map<User, Certificate> getAllCertificatesWithUsersNames();
}
