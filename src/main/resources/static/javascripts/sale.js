ShopBeer.Sale = (function() {
	
	function Sale(tableItems) {
		this.tableItems = tableItems;
		this.valueTotalBox = $('.js-value-total-box');
		this.valueShippingInput = $('#valueShipping');
		this.valueDiscountInput = $('#valueDiscount');
		this.valueTotalBoxContainer = $('.js-value-total-box-container');
		
		this.valueTotalItems = this.tableItems.amount();
		this.valueShipping = this.valueShippingInput.data('value');
		this.valueDiscount = this.valueDiscountInput.data('value');
	}
	
	Sale.prototype.start = function() {
		this.tableItems.on('table-itens-update', onTableItemsUpdated.bind(this));
		this.valueShippingInput.on('keyup', onShippingValueChange.bind(this));
		this.valueDiscountInput.on('keyup', onValorDescontoAlterado.bind(this));
		
		this.tableItems.on('table-itens-update', onAlteredValues.bind(this));
		this.valueShippingInput.on('keyup', onAlteredValues.bind(this));
		this.valueDiscountInput.on('keyup', onAlteredValues.bind(this));

		onAlteredValues.call(this);
	}
	
	function onTableItemsUpdated(eventTotalItems, valueTotalItems) {
		this.valueTotalItems = valueTotalItems == null ? 0 : valueTotalItems;
	}
	
	function onShippingValueChange(eventShipping) {
		this.valueShipping = ShopBeer.retrieveValue($(eventShipping.target).val());
	}
	
	function onValorDescontoAlterado(eventDesc) {
		this.valueDiscount = ShopBeer.retrieveValue($(eventDesc.target).val());
	}
	
	function onAlteredValues() {
		const amount = numeral(this.valueTotalItems) + numeral(this.valueShipping) - numeral(this.valueDiscount);
		this.valueTotalBox.html(ShopBeer.formatCurrency(amount));
		
		this.valueTotalBoxContainer.toggleClass('negative', amount < 0);
	}
	
	return Sale;
	
}());

$(function() {
	
	const autocomplete = new ShopBeer.Autocomplete();
	autocomplete.start();
	
	const tableItems = new ShopBeer.TableItems(autocomplete);
	tableItems.iniciar();
	
	const Sale = new ShopBeer.Sale(tableItems);
	Sale.start();
	
});