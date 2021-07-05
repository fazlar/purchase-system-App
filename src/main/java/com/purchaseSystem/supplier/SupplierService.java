package com.purchaseSystem.supplier;

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
public class SupplierService {

	@Autowired
	SupplierRepository supplierRepository;

	public Response save(String reqObj) {
		return supplierRepository.save(reqObj);
	}

	public Response update(String reqObj) {
		return supplierRepository.update(reqObj);
	}

	public Response delete(Long reqId) {
		return supplierRepository.detele(reqId);
	}

	public Response list() {
		return supplierRepository.list();
	}

}
