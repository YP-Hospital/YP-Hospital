package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;

import java.util.List;

public interface DiseaseHistoryService {
    Boolean addHistoryInDB(DiseaseHistory history);
    Boolean updateHistoryInDB(DiseaseHistory history);
    List<DiseaseHistory> getAllHistories();
    List<DiseaseHistory> getAllUsersHistory(User user);
}
