package com.purchaseSystem.purchase;

import java.util.ArrayList;
import java.util.Date;
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
@Table(name = "purchase")
public class PurchaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "INVOICE_ID")
	private String invoiceId;
	
	@Column(name = "puchase_ID")
	private int purchaseId;


	@Column(name = "SUPPLIER_NAME")
	private String supplierName;
	
	@Column(name = "SUPPLIER_No")
	private Long supplierNo;

//	@Column(name = "PURCHASE_DATE", length = 100)
//	private String stockQty;

	@Column(name = "DETAILS")
	private String details;

	@Column(name = "PAY_TYPE")
	private Long payType;

	@Column(name = "PURCHASE_DATE")
	private Date purchaseDate;

	@Column(name = "SUB_TOTAL")
	private Double supTotal;

	@Column(name = "DISCOUNT")
	private Double discount;

	@Column(name = "GRAND_TOTAL")
	private Double grandTotal;

	@Column(name = "PAY_AMOUNT")
	private Double payAmount;

	@Column(name = "DUE")
	private Double due;

	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)  
	@JoinColumn(name="pur_id")  
	private List<PurItemDtlEntity> item;

	@Transient
	private String idNotEqual;

	@Transient
	private Date fromDate;

	@Transient
	private Date toDate;

	
	@Transient
	private List<PurchaseEntity> employeeList = new ArrayList<PurchaseEntity>();

	public List<PurchaseEntity> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<PurchaseEntity> employeeList) {
		this.employeeList = employeeList;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public int getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Long getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(Long supplierNo) {
		this.supplierNo = supplierNo;
	}

	public Double getSupTotal() {
		return supTotal;
	}

	public void setSupTotal(Double supTotal) {
		this.supTotal = supTotal;
	}

	public String getIdNotEqual() {
		return idNotEqual;
	}

	public void setIdNotEqual(String idNotEqual) {
		this.idNotEqual = idNotEqual;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	

	public Long getPayType() {
		return payType;
	}

	public void setPayType(Long payType) {
		this.payType = payType;
	}

	

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Double getSuptotal() {
		return supTotal;
	}

	public void setSuptotal(Double suptotal) {
		this.supTotal = suptotal;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Double getDue() {
		return due;
	}

	public void setDue(Double due) {
		this.due = due;
	}

	public List<PurItemDtlEntity> getItem() {
		return item;
	}

	public void setItem(List<PurItemDtlEntity> item) {
		this.item = item;
	}

}
