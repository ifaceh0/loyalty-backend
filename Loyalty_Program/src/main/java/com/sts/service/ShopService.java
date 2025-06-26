package com.sts.service;

import java.util.List;
import java.util.Optional;

import com.sts.dto.ShopSignupRequest;
import com.sts.dto.ShopkeeperSettingDTO;
import com.sts.entity.Login;
import com.sts.entity.Shopkeeper_Setting;
import com.sts.enums.Role;
import com.sts.repository.LoginRepository;
import com.sts.repository.Shopkeeper_SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.Shop;
import com.sts.repository.ShopRepository;

@Service
public class ShopService {
	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private Shopkeeper_SettingRepository shopkeeperSettingRepository;

	@Autowired
	private LoginRepository loginRepository;

	public Shop createShop(Shop shop) {
		return shopRepository.save(shop);
	}
	
	public List<Shop> getAllShop(){
		return shopRepository.findAll();
	}
	
	public Optional<Shop> getShopById(Long shopId) {
		return shopRepository.findById(shopId);
	}
	
	public void deleteShopById(Long shopId) {
		shopRepository.deleteById(shopId);
	}



	public ShopkeeperSettingDTO getSetting(Long shopId) {
//		Shopkeeper_Setting setting =shopkeeperSettingRepository.findByShop_Id(shopId).orElse(null);

		return shopkeeperSettingRepository.findByShop_ShopId(shopId)
				.map(this::toDTO)
				.orElse(null);
	}

	public void saveSetting(ShopkeeperSettingDTO dto) {
		Shopkeeper_Setting setting = shopkeeperSettingRepository.findByShop_ShopId(dto.getShopId())
				.orElse(new Shopkeeper_Setting());

		Shop shop = shopRepository.findById(dto.getShopId())
				.orElseThrow(() -> new RuntimeException("Shop not found"));

		setting.setShop(shop);
		setting.setDollarToPointMapping(dto.getDollarToPointMapping());
		setting.setSign_upBonuspoints(dto.getSign_upBonuspoints());
		setting.setMilestoneBonusAmount(dto.getMilestoneBonusAmount());
		setting.setSpecialBonusName(dto.getSpecialBonusName());
		setting.setSpecialBonusPoints(dto.getSpecialBonusPoints());
		setting.setSpecialBonusStartDate(dto.getSpecialBonusStartDate());
		setting.setSpecialBonusEndDate(dto.getSpecialBonusEndDate());
		setting.setBonusdescription(dto.getBonusdescription());
		setting.setBeginDate(dto.getBeginDate());
		setting.setEndDate(dto.getEndDate());
		setting.setAmountOff(dto.getAmountOff());

		shopkeeperSettingRepository.save(setting);
	}

	private ShopkeeperSettingDTO toDTO(Shopkeeper_Setting setting) {
		ShopkeeperSettingDTO dto = new ShopkeeperSettingDTO();
		dto.setShopId(setting.getShop().getShopId());
		dto.setDollarToPointMapping(setting.getDollarToPointMapping());
		dto.setSign_upBonuspoints(setting.getSign_upBonuspoints());
		dto.setMilestoneBonusAmount(setting.getMilestoneBonusAmount());
		dto.setSpecialBonusName(setting.getSpecialBonusName());
		dto.setSpecialBonusPoints(setting.getSpecialBonusPoints());
		dto.setSpecialBonusStartDate(setting.getSpecialBonusStartDate());
		dto.setSpecialBonusEndDate(setting.getSpecialBonusEndDate());
		dto.setBonusdescription(setting.getBonusdescription());
		dto.setBeginDate(setting.getBeginDate());
		dto.setEndDate(setting.getEndDate());
		dto.setAmountOff(setting.getAmountOff());
		return dto;
	}
}
