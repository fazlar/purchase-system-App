package com.purchaseSystem.itemDtl;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;



@Entity
@Table(name = "pur_item_dtl")
public class PurItemDtlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;


	@Column(name = "ITEM_id", length = 50)
	private Long itemId;
	
	@Column(name = "ITEM_NAME", length = 50)
	private String itemName;

	@Column(name = "STOCK_QTY", length = 100)
	private Long stockQty;

	@Column(name = "QTY", length = 15)
	private Long qty;

	@Column(name = "RATE")
	private Double rate;

	@Column(name = "ITEM_TOTAL")
	private Double itemTotal;
	
//	
//	 @ManyToOne(cascade = CascadeType.ALL)
//	    @JoinColumn(name = "pur_id")
//	    private Long pur_id;

	

//	public Long getPur_id() {
//		return pur_id;
//	}
//
//	public void setPur_id(Long pur_id) {
//		this.pur_id = pur_id;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getStockQty() {
		return stockQty;
	}

	public void setStockQty(Long stockQty) {
		this.stockQty = stockQty;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}


	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(Double itemTotal) {
		this.itemTotal = itemTotal;
	}

}
