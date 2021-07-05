package com.purchaseSystem.report;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.purchaseSystem.util.CusJasperReportDef;

@Service
public class ReportService {

	@Autowired
	ReportRepository reportRepository;
public CusJasperReportDef purchaseRpt(String reqObj) throws IOException  {
		
    	return reportRepository.purchaseRpt(reqObj);

	}


public CusJasperReportDef purchaseDtlRpt(String reqObj) throws IOException  {
	
	return reportRepository.purchaseDtlRpt(reqObj);

}
}
