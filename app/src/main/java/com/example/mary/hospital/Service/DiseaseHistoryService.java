package com.example.mary.hospital.Service;

import com.example.mary.hospital.Model.DiseaseHistory;
import com.example.mary.hospital.Model.User;

import java.util.List;

public interface DiseaseHistoryService {
    Boolean insertHistoryInDB(DiseaseHistory history, String privateKey);
    Boolean updateHistoryInDB(DiseaseHistory history, User user, String privateKey);
    DiseaseHistory getHistoryById(Integer id);
    List<DiseaseHistory> getAllHistories();
    List<DiseaseHistory> getAllUsersHistories(User user);
    List<String> getTitlesOfAllUsersHistories(User user);
}
