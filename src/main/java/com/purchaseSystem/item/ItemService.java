package com.purchaseSystem.item;

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
public class ItemService {

	@Autowired
	ItemRepository itemRepository;

	public Response save(String reqObj) {
		return itemRepository.save(reqObj);
	}

	public Response update(String reqObj) {
		return itemRepository.update(reqObj);
	}

	public Response delete(Long reqId) {
		return itemRepository.detele(reqId);
	}

	public Response list() {
		return itemRepository.list();
	}

}
