package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.domain.dto.ReportingPeriodDTO;
import br.com.rafaelvieira.shopbeer.service.report.ReportService;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/reports")
public class ReportController {
	

	private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales-issued")
	public ModelAndView reportSalesIssued() {
		ModelAndView mv = new ModelAndView("report/report-sales-issued");
		mv.addObject(new ReportingPeriodDTO());
		return mv;
	}
	
	@PostMapping("/sales-issued")
	public ResponseEntity<byte[]> generateIssuedSalesReport(ReportingPeriodDTO reportingPeriodDTO) throws Exception {
		byte[] report = reportService.generateIssuedSalesReport(reportingPeriodDTO);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(report);
	}
}












