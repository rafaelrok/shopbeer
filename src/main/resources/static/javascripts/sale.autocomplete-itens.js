ShopBeer = ShopBeer || {};

ShopBeer.Autocomplete = (function() {
	
	function Autocomplete() {
		this.skuOrNameInput = $('.js-sku-name-beer-input');
		const htmlTemplateAutocomplete = $('#template-autocomplete-beer').html();
		this.template = Handlebars.compile(htmlTemplateAutocomplete);
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	Autocomplete.prototype.start = function() {
		const options = {
			url: function(skuOrName) {
				return this.skuOrNameInput.data('url') + '?skuOrName=' + skuOrName;
			}.bind(this),
			getValue: 'name',
			minCharNumber: 3,
			requestDelay: 300,
			ajaxSettings: {
				contentType: 'application/json'
			},
			template: {
				type: 'custom',
				method: template.bind(this)
			},
			list: {
				onChooseEvent: onItemSelected.bind(this)
			}
		};
		
		this.skuOrNameInput.easyAutocomplete(options);
	}
	
	function onItemSelected() {
		this.emitter.trigger('item-selected', this.skuOrNameInput.getSelectedItemData());
		this.skuOrNameInput.val('');
		this.skuOrNameInput.focus();
	}
	
	function template(name, beer) {
		beer.valueFormatted = ShopBeer.formatCurrency(beer.value);
		return this.template(beer);
	}
	
	return Autocomplete
	
}());
