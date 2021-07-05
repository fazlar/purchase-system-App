package com.purchaseSystem.itemDtl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.purchaseSystem.util.Response;


@Service
public class PurItemdtlService {

	@Autowired
	PurItemdtlRepository purItemdtlRepository;

	public Response save(String reqObj) {
		return purItemdtlRepository.save(reqObj);
	}

//	public Response update(String reqObj) {
//		return purItemdtlRepository.update(reqObj);
//	}

	public Response delete(long reqId) {
		return purItemdtlRepository.delete(reqId);
	}

	public Response list(String reqObj) {
		return purItemdtlRepository.list(reqObj);
	}

	public Response getAll() {
		return purItemdtlRepository.getAll();
	}

//	public PurItemDtlEntity updateGrade(PurItemDtlEntity purItemDtlEntity) {
//		return purItemdtlRepository.updateGrade(purItemDtlEntity);
//	}

	public PurItemDtlEntity findById(Long id) {
		return purItemdtlRepository.findById(id);
	}
	
	public Response getqty(String reqObj) throws SQLException {
		return purItemdtlRepository.getStockQty(reqObj);
	}

}
