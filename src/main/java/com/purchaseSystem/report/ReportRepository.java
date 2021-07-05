package com.purchaseSystem.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.purchaseSystem.base.BaseRepository;
import com.purchaseSystem.purchase.PurchaseEntity;
import com.purchaseSystem.purchase.PurchaseRepository;
import com.purchaseSystem.supplier.SupplierEntity;
import com.purchaseSystem.supplier.SupplierRepository;
import com.purchaseSystem.util.CoreJasperService;
import com.purchaseSystem.util.CusJasperReportDef;
import com.purchaseSystem.util.Def;
import com.purchaseSystem.util.JasperExportFormat;
import com.purchaseSystem.util.Response;



@Repository
public class ReportRepository extends BaseRepository {

	@Autowired
	private CoreJasperService coreJasperService;
	@Autowired
	PurchaseRepository purchaseRepository;
	
	@Autowired
	SupplierRepository supplierRepository;

	@SuppressWarnings("unchecked")
	public CusJasperReportDef purchaseRpt(String reqObj) throws IOException {

//		Object obj = new JSONParser().parse(new FileReader("billDtl.json"));

		JSONObject json = new JSONObject(reqObj);
		Long id = Def.getLong(json, "id");
		
		PurchaseEntity rpt = new PurchaseEntity();
		List<PurchaseEntity> purchseDtlList = new ArrayList<PurchaseEntity>();
		SupplierEntity supplierEntity = new SupplierEntity();
		PurchaseEntity res = purchaseRepository.findById(id);

		if (res!= null ) {
			supplierEntity=supplierRepository.findById(res.getSupplierNo());
			purchseDtlList.add(res);
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		
		if (supplierEntity !=null) {
			parameterMap.put("address",supplierEntity.getAddress());
			parameterMap.put("email",supplierEntity.getEMAIL());
			parameterMap.put("phone",supplierEntity.getMobile());
		}



		CusJasperReportDef report = new CusJasperReportDef();

		report.setReportName("purchase");
		report.setOutputFilename("purchase");
		report.setReportDir(getResoucePath("/report") + "/");
		report.setReportFormat(JasperExportFormat.PDF_FORMAT);
		report.setParameters(parameterMap);
		report.setReportData(purchseDtlList);

		ByteArrayOutputStream baos = null;
		try {
			baos = coreJasperService.generateReport(report);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			baos.close();
		}

		report.setContent(baos.toByteArray());
		return report;
	}
	
	public CusJasperReportDef purchaseDtlRpt(String reqObj) throws IOException {

//		Object obj = new JSONParser().parse(new FileReader("billDtl.json"));

		JSONObject json = new JSONObject(reqObj);
		String printFormat = Def.getString(json, "printFormat");
		
		
		String fDateStr = json.getString("fromDate");
		String tDateStr = json.getString("toDate");

		PurchaseEntity purchaseEntity = new PurchaseEntity();
		
		
		PurchaseEntity rpt = new PurchaseEntity();
		List<PurchaseEntity> purchseDtlList = new ArrayList<PurchaseEntity>();
		
		
		Response res = purchaseRepository.getreortDta(reqObj);
		
		
		if (res.getItems() != null && res.getItems().size() > 0) {
		purchseDtlList = res.getItems();
	
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		
		

		if (!StringUtils.isEmpty(fDateStr)) {
			
			parameterMap.put("fromDate", deateParse(fDateStr, "dd/MM/yyyy"));
			purchaseEntity.setFromDate(deateParse(fDateStr, "dd/MM/yyyy"));
		}

		if (!StringUtils.isEmpty(tDateStr)) {
			parameterMap.put("tomDate", deateParse(tDateStr, "dd/MM/yyyy"));
		}	

		CusJasperReportDef report = new CusJasperReportDef();

		report.setReportName("purchaseDtl");
		report.setOutputFilename("purchaseDtl");
		report.setReportDir(getResoucePath("/report") + "/");
		if (printFormat.equalsIgnoreCase("pdf")) {
			report.setReportFormat(JasperExportFormat.PDF_FORMAT);
		} else if(printFormat.equalsIgnoreCase("XLSX")) {
			report.setReportFormat(JasperExportFormat.XLSX_FORMAT);
		}else if(printFormat.equalsIgnoreCase("csv")) {
			report.setReportFormat(JasperExportFormat.CSV_FORMAT);
		}else {
			report.setReportFormat(JasperExportFormat.PDF_FORMAT);
		}
		
		report.setParameters(parameterMap);
		report.setReportData(purchseDtlList);

		ByteArrayOutputStream baos = null;
		try {
			baos = coreJasperService.generateReport(report);
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			baos.close();
		}

		report.setContent(baos.toByteArray());
		return report;
	}

	public static String getResoucePath(String filePath) {
		Resource resource = new ClassPathResource(filePath);
		try {
			return resource.getFile().getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
