package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.Certificate;
import com.example.mary.hospital.Model.User;

import java.util.List;
import java.util.Map;

public interface CertificateService {
    Certificate getCertificateByUser(User user);
    List<Certificate> getAllCertificates();
    Map<String, Certificate> getAllCertificatesWithUsersNames();
}
