package com.sts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sts.dto.PurchaseRequestDTO;
import com.sts.entity.Shop;
import com.sts.entity.UserProfile;
import com.sts.entity.UserPurchase_History;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserProfileRepository;
import com.sts.repository.UserPurchaseHistory_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.User;

import com.sts.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QRCodeService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserPurchaseHistory_Repository historyRepository;
//
//	public void savePurchase(PurchaseRequestDTO request) {
//		User user = userRepository.findById(request.getUserId())
//				.orElseThrow(() -> new RuntimeException("User not found"));
//		Shop shop = shopRepository.findById(request.getShopId())
//				.orElseThrow(() -> new RuntimeException("Shop not found"));
//
//		UserPurchase_History history = new UserPurchase_History();
//		history.setUser(user);
//		history.setShop(shop);
//		history.setTransactionAmount(request.getTransactionAmount());
//		history.setPurchaseDate(LocalDateTime.now());
//
//		historyRepository.save(history);
//	}
}