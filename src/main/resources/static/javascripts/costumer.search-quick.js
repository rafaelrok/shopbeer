ShopBeer = ShopBeer || {};

ShopBeer.SearchQuickCostumer = (function() {
	
	function SearchQuickCostumer() {
		this.searchQuickCostumersModal = $('#searchQuickCostumer');
		this.nameInput = $('#nameCostumerModal');
		this.quickSearchBtn = $('.js-search-quick-costumer-btn');
		this.containerTableSearch = $('#containerTableSearchQuickCostumers');
		this.htmlTableSearch = $('#table-search-quick-costumer').html();
		this.template = Handlebars.compile(this.htmlTableSearch);
		this.messageErro = $('.js-message-erro');
	}

	SearchQuickCostumer.prototype.start = function() {
		this.quickSearchBtn.on('click', onQuickSearchClicked.bind(this));
		this.searchQuickCostumersModal.on('shown.bs.modal', onModalShow.bind(this));

	}
	
	function onModalShow() {
		this.nameInput.focus();
	}
	
	function onQuickSearchClicked(event) {
		event.preventDefault();
		
		$.ajax({
			url: this.searchQuickCostumersModal.find('form').attr('action'),
			method: 'GET',
			contentType: 'application/json',
			data: {
				name: this.nameInput.val()
			}, 
			success: onResearchCompleted.bind(this),
			error: onErroSearch.bind(this)
		});
	}
	
	function onResearchCompleted(response) {
		this.messageErro.addClass('hidden');
		
		const html = this.template(response);
		this.containerTableSearch.html(html);
		
		const tableCostumerQuickSearch = new Brewer.CostumerTableQuickSearch(this.searchQuickCostumersModal);
		tableCostumerQuickSearch.iniciar();
	} 
	
	function onErroSearch() {
		this.messageErro.removeClass('hidden');
	}
	
	return SearchQuickCostumer;
	
}());

Shop_Beer.CostumerTableQuickSearch = (function() {
	
	function CostumerTableQuickSearch(modal) {
		this.modalCostumer = modal;
		this.costumer = $('.js-costumer-search-quick');
	}

	CostumerTableQuickSearch.prototype.start = function() {
		this.costumer.on('click', onSelectedCostumer.bind(this));
	}
	
	function onSelectedCostumer(eventCostumer) {
		this.modalCostumer.modal('hide');
		
		const CostumerSelected = $(eventCostumer.currentTarget);
		$('#nameCostumer').val(CostumerSelected.data('name'));
		$('#codeCostumer').val(CostumerSelected.data('code'));
	}
	
	return CostumerTableQuickSearch;
	
}());

$(function() {
	const searchQuickCostumer = new ShopBeer.SearchQuickCostumer();
	searchQuickCostumer.start();
});