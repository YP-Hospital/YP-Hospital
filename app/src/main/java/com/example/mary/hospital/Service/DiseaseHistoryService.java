package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;

import java.util.List;

public interface DiseaseHistoryService {
    Boolean addHistoryInDB(DiseaseHistory history);
    Boolean updateHistoryInDB(DiseaseHistory history, User user, String privateKey);
    List<DiseaseHistory> getAllHistories();
    List<DiseaseHistory> getAllUsersHistories(User user);
    List<String> getTitlesOfAllUsersHistories(User user);
}
