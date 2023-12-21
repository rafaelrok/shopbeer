package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.repository.CostumerRepository;
import br.com.rafaelvieira.shopbeer.repository.query.beer.BeersQuery;
import br.com.rafaelvieira.shopbeer.repository.query.sale.SalesQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {

	private final SalesQuery salesQuery;
	private final BeersQuery beersQuery;
	private final CostumerRepository costumerRepository;

    public DashboardController(SalesQuery salesQuery, BeersQuery beersQuery, CostumerRepository costumerRepository) {
        this.salesQuery = salesQuery;
        this.beersQuery = beersQuery;
        this.costumerRepository = costumerRepository;
    }

    @GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		
		mv.addObject("totalValueInYear", salesQuery.totalValueInYear());
		mv.addObject("totalValueInMonth", salesQuery.totalValueInMonth());
		mv.addObject("ticketMedia", salesQuery.averageTicketValueInYear());
		
		mv.addObject("stockItemValue", beersQuery.stockItemValue());
		mv.addObject("totalCostumers", costumerRepository.count());
		
		return mv;
	}
	
}
