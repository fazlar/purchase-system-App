package com.purchaseSystem.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.purchaseSystem.util.Response;



@RestController
@RequestMapping("api/supplier")
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	@PostMapping("/create")
	public Response create(@RequestBody String reqObj) {
		return supplierService.save(reqObj);
	}

	@PutMapping("/update")
	public Response update(@RequestBody String reqObj) {
		return supplierService.update(reqObj);
	}

	@DeleteMapping("/delete")
	public Response delete(@RequestParam("id") Long reqId) {
		return supplierService.delete(reqId);
	}

	@GetMapping("/list")
	public Response getAll() {
		return supplierService.list(); 
	}

}
