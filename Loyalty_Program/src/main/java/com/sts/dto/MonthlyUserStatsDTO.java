package com.sts.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data

public class MonthlyUserStatsDTO {
    private String month;
    private long registeredUsers;
    private long visitedUsers;

    public MonthlyUserStatsDTO(String month, long registeredUsers, long visitedUsers) {
        this.month = month;
        this.registeredUsers = registeredUsers;
        this.visitedUsers = visitedUsers;
    }
}