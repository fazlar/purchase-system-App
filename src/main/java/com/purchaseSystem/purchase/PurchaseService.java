package com.purchaseSystem.purchase;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.purchaseSystem.util.Response;



@Service
public class PurchaseService {

	@Autowired
	PurchaseRepository purchaseRepository;

	
	public Response gridList(HttpServletRequest request) {
		return purchaseRepository.gridList(request);
	}
	public Response save(String reqObj) {
		return purchaseRepository.save(reqObj);
	}

	public Response update(String reqObj) {
		return purchaseRepository.update(reqObj);
	}

	public Response delete(Long reqId) {
		return purchaseRepository.detele(reqId);
	}

	public Response list() {
		return purchaseRepository.list();
	}

}
