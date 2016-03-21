package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.Certificate;

import java.util.List;
import java.util.Map;

public interface CertificateService {
    List<Certificate> getAllCertificates();
    Map<String, Certificate> getAllCertificatesWithUsersNames();
}
