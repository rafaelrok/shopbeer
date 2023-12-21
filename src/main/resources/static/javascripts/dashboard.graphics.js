const Shopbeer = Shopbeer || {};

Shopbeer.GraphicSaleByMonth = (function() {
	
	function GraphicSaleByMonth() {
		this.ctx = $('#graphicSalesByMonth')[0].getContext('2d');
	}
	
	GraphicSaleByMonth.prototype.start = function() {
		$.ajax({
			url: 'sales/totalByMonth',
			method: 'GET', 
			success: onDataReceived.bind(this)
		});
	}
	
	function onDataReceived(saleMonth) {
		const months = [];
		const values = [];
		saleMonth.forEach(function(obj) {
			months.unshift(obj.month);
			values.unshift(obj.total);
		});
		
		const graphicSalesByMonth = new Chart(this.ctx, {
		    type: 'line',
		    data: {
		    	labels: meses,
		    	datasets: [{
		    		label: 'Sales por mês',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                pointBorderColor: "rgba(26,179,148,1)",
	                pointBackgroundColor: "#fff",
	                data: values
		    	}]
		    },
		});
	}
	
	return GraphicSaleByMonth;
	
}());

Shopbeer.GraphicSaleByOrigin = (function() {
	
	function GraphicSaleByOrigin() {
		this.ctx = $('#graphicSaleByOrigin')[0].getContext('2d');
	}

	GraphicSaleByOrigin.prototype.start = function() {
		$.ajax({
			url: 'sales/byOrigin',
			method: 'GET', 
			success: onDataReceived.bind(this)
		});
	}
	
	function onDataReceived(saleOrigin) {
		const months = [];
		const nationalBeers = [];
		const internationalBeers = [];

		saleOrigin.forEach(function(obj) {
			months.unshift(obj.month);
			nationalBeers.unshift(obj.totalNational);
			internationalBeers.unshift(obj.totalInternational)
		});
		
		const graphicSalesByOrigin = new Chart(this.ctx, {
		    type: 'bar',
		    data: {
		    	labels: months,
		    	datasets: [{
		    		label: 'Nacional',
		    		backgroundColor: "rgba(220,220,220,0.5)",
	                data: nationalBeers
		    	},
		    	{
		    		label: 'Internacional',
		    		backgroundColor: "rgba(26,179,148,0.5)",
	                data: internationalBeers
		    	}]
		    },
		});
	}
	
	return GraphicSaleByOrigin;
	
}());


$(function() {
	const graphicSaleByMonth = new Shopbeer.GraphicSaleByMonth();
	graphicSaleByMonth.start();
	
	const graphicSaleByOrigin = new Shopbeer.GraphicSaleByOrigin();
	graphicSaleByOrigin.start();
});
