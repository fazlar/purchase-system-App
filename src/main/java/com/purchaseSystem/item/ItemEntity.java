package com.purchaseSystem.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.purchaseSystem.itemDtl.PurItemDtlEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "item")
public class ItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;



	@Column(name = "ITEM_NAME", length = 50)
	private String itemName;

	@Column(name = "RATE", length = 100)
	private Double itemRate;

	@Transient
	private Long idNotEqual;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getItemRate() {
		return itemRate;
	}

	public void setItemRate(Double itemRate) {
		this.itemRate = itemRate;
	}

	public Long getIdNotEqual() {
		return idNotEqual;
	}

	public void setIdNotEqual(Long idNotEqual) {
		this.idNotEqual = idNotEqual;
	}

	

	

}
