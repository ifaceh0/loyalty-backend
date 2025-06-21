package com.sts.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.UserPurchase_History;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPurchaseHistory_Repository extends JpaRepository<UserPurchase_History, Long>
{

}

