package com.purchaseSystem.itemDtl;

import java.sql.SQLException;

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
@RequestMapping("api/purItem")
public class PurItemdtlController {

	@Autowired
	private PurItemdtlService purItemdtlService;

	@PostMapping("/create")
	public Response create(@RequestBody String reqObj) {
		return purItemdtlService.save(reqObj);
	}


	@DeleteMapping("/delete")
	public Response delete(@RequestParam("id") long reqId) {
		return purItemdtlService.delete(reqId);
	}

	@GetMapping("/list")
	public Response getAll(@RequestBody(required = false) String reqObj) {
		return purItemdtlService.list(reqObj);
	}
	@GetMapping("/li7st")
	public Response findbyId(@RequestBody(required = false) String reqObj) {
		return purItemdtlService.list(reqObj);
	}
	
	@PostMapping("/getqty")
	public Response getqty(@RequestBody String reqObj) throws SQLException {
		return purItemdtlService.getqty(reqObj);
	}
}
