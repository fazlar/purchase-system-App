package com.purchaseSystem.purchase;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("api/purchas")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;

	
	@GetMapping("/grid-list")
	public Response gridList(HttpServletRequest request) {
		return purchaseService.gridList(request);
	}
	
	@PostMapping("/create")
	public Response create(@RequestBody String reqObj) {
		return purchaseService.save(reqObj);
	}
	
	@PutMapping("/update")
	public Response update(@RequestBody String reqObj) {
		return purchaseService.update(reqObj);
	}

	@DeleteMapping("/delete")
	public Response delete(@RequestParam("id") Long reqId) {
		return purchaseService.delete(reqId);
	}

	@GetMapping("/list")
	public Response getAll() {
		return purchaseService.list(); 
	}
	
//	@GetMapping("/findByid")
//	public Response getAll(@RequestParam("id") Long reqId) {
//		return purchaseService.list(); 
//	}

}
