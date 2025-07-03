//package com.sts.service;
//
//import com.sts.dto.TransactionDTO;
//import com.sts.repository.UserDashboardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class UserDashboardService {
//    @Autowired
//    private UserDashboardRepository userDashboardRepository;
//
//    public List<TransactionDTO> getUserTransactions(Long userId) {
//        return userDashboardRepository.findUserTransactions(userId).stream()
//                .map(row -> new TransactionDTO(
//                        ((Timestamp) row[0]).toLocalDateTime(),
//                        (Integer) row[1]))
//                .collect(Collectors.toList());
//    }
//}
