package com.sts.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.sts.dto.*;
import com.sts.entity.*;
import com.sts.enums.Role;
import com.sts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopService {
	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private Shopkeeper_SettingRepository settingRepository;

	@Autowired
	private PurchaseRewardRepository purchaseRepo;

	@Autowired
	private MilestoneRewardRepository milestoneRepo;

	@Autowired
	private SpecialBonusRepository bonusRepo;

	@Autowired
	private LoginRepository loginRepository;

	public Shop createShop(Shop shop) {
		return shopRepository.save(shop);
	}
	
	public List<Shop> getAllShop(){
		return shopRepository.findAll();
	}

	public void deleteShopById(Long shopId) {
		shopRepository.deleteById(shopId);
	}

	@Transactional
	public void saveSetting(ShopkeeperSettingDTO request) {
		Shop shop = shopRepository.findById(request.getShopId())
				.orElseThrow(() -> new RuntimeException("Shop not found"));

		// Save core setting
		Shopkeeper_Setting setting = shop.getShopSetting();
		if (setting == null) setting = new Shopkeeper_Setting();
		setting.setShop(shop);
		setting.setSign_upBonuspoints(request.getSign_upBonuspoints());
		setting.setBonusdescription(request.getBonusdescription());
		setting.setBeginDate(request.getBeginDate());
		setting.setEndDate(request.getEndDate());
		setting.setAmountOff(request.getAmountOff());
		settingRepository.save(setting);

		// Remove previous rewards
		purchaseRepo.deleteByShop_ShopId(shop.getShopId());
		milestoneRepo.deleteByShop_ShopId(shop.getShopId());
		bonusRepo.deleteByShop_ShopId(shop.getShopId());

		// Save new rewards
		if (request.getPurchaseRewards() != null) {
			for (PurchaseRewardDTO dto : request.getPurchaseRewards()) {
				PurchaseReward pr = new PurchaseReward();
				pr.setThreshold(dto.getThreshold());
				pr.setPoints(dto.getPoints());
				pr.setShop(shop);
				purchaseRepo.save(pr);
			}
		}

		if (request.getMilestoneRewards() != null) {
			for (MilestoneRewardDTO dto : request.getMilestoneRewards()) {
				MilestoneReward mr = new MilestoneReward();
				mr.setThreshold(dto.getThreshold());
				mr.setAmount(dto.getAmount());
				mr.setShop(shop);
				milestoneRepo.save(mr);
			}
		}

		if (request.getSpecialBonuses() != null) {
			for (SpecialBonusDTO dto : request.getSpecialBonuses()) {
				SpecialBonus sb = new SpecialBonus();
				sb.setName(dto.getName());
				sb.setPoints(dto.getPoints());
				sb.setStartDate(dto.getStartDate());
				sb.setEndDate(dto.getEndDate());
				sb.setShop(shop);
				bonusRepo.save(sb);
			}
		}
	}

	public ShopkeeperSettingDTO getSetting(Long shopId) {
		Shop shop = shopRepository.findById(shopId)
				.orElseThrow(() -> new RuntimeException("Shop not found"));
		Shopkeeper_Setting setting = shop.getShopSetting();

		ShopkeeperSettingDTO response = new ShopkeeperSettingDTO();
		response.setShopId(shopId);
		response.setSign_upBonuspoints(setting.getSign_upBonuspoints());
		response.setBonusdescription(setting.getBonusdescription());
		response.setBeginDate(setting.getBeginDate());
		response.setEndDate(setting.getEndDate());
		response.setAmountOff(setting.getAmountOff());

		response.setPurchaseRewards(purchaseRepo.findByShop_ShopId(shopId)
				.stream().map(p -> {
					PurchaseRewardDTO dto = new PurchaseRewardDTO();
					dto.setThreshold(p.getThreshold());
					dto.setPoints(p.getPoints());
					return dto;
				}).toList());

		response.setMilestoneRewards(milestoneRepo.findByShop_ShopId(shopId)
				.stream().map(m -> {
					MilestoneRewardDTO dto = new MilestoneRewardDTO();
					dto.setThreshold(m.getThreshold());
					dto.setAmount(m.getAmount());
					return dto;
				}).toList());

		response.setSpecialBonuses(bonusRepo.findByShop_ShopId(shopId)
				.stream().map(s -> {
					SpecialBonusDTO dto = new SpecialBonusDTO();
					dto.setName(s.getName());
					dto.setPoints(s.getPoints());
					dto.setStartDate(s.getStartDate());
					dto.setEndDate(s.getEndDate());
					return dto;
				}).toList());

		return response;
	}

	public ShopkeeperProfileDTO getProfile(Long shopId){
		Shop shopkeeper = shopRepository.findShopByShopId(shopId).orElseThrow(()-> new RuntimeException("Shop not found with ID: " + shopId));

		ShopkeeperProfileDTO dto = new ShopkeeperProfileDTO();
		dto.setShopId(shopkeeper.getShopId());
		dto.setShopName(shopkeeper.getShopName());
		dto.setPhone(shopkeeper.getPhone());
		dto.setEmail(shopkeeper.getEmail());
		dto.setCompanyName(shopkeeper.getCompanyName());
		dto.setCompanyAddress(shopkeeper.getCompanyAddress());
		dto.setCompanyEmail(shopkeeper.getCompanyEmail());
		dto.setCompanyPhone(shopkeeper.getCompanyPhone());
		return dto;
	}

	public ShopkeeperProfileDTO updatePersonalProfile(ShopkeeperProfileDTO shopkeeperDTO) {
		Shop shopkeeper = shopRepository.findShopByShopId(shopkeeperDTO.getShopId()).orElse(null);

		if (shopkeeper != null){
			shopkeeper.setShopName(shopkeeperDTO.getShopName());
			shopkeeper.setPhone(shopkeeperDTO.getPhone());
			String newEmail = shopkeeperDTO.getEmail();
			shopkeeper.setEmail(newEmail);
			shopkeeper.setCompanyName(shopkeeperDTO.getCompanyName());
			shopkeeper.setCompanyAddress(shopkeeperDTO.getCompanyAddress());
			shopkeeper.setCompanyEmail(shopkeeperDTO.getCompanyEmail());
			shopkeeper.setCompanyPhone(shopkeeperDTO.getCompanyPhone());
			shopRepository.save(shopkeeper);

			// Find Login by refId and role
			Login login = loginRepository.findByRefIdAndRole(shopkeeperDTO.getShopId(), Role.SHOPKEEPER).orElse(null);
			if (login != null) {
				login.setEmail(newEmail);
				loginRepository.save(login);
			} else {
				throw new RuntimeException("Login not found for this shopkeeper");
			}
			return convertShopkeeperToDto(shopkeeper);
		}
		throw new NoSuchElementException("Shopkeeper not found with ID: " + shopkeeperDTO.getShopId());
	}

	private ShopkeeperProfileDTO convertShopkeeperToDto(Shop shopkeeper) {
		ShopkeeperProfileDTO dto = new ShopkeeperProfileDTO();
		dto.setShopId(shopkeeper.getShopId());
		dto.setShopName(shopkeeper.getShopName());
		dto.setPhone(shopkeeper.getPhone());
		dto.setEmail(shopkeeper.getEmail());
		dto.setCompanyName(shopkeeper.getCompanyName());
		dto.setCompanyAddress(shopkeeper.getCompanyAddress());
		dto.setCompanyEmail(shopkeeper.getCompanyEmail());
		dto.setCompanyPhone(shopkeeper.getCompanyPhone());
		return dto;
	}
}
