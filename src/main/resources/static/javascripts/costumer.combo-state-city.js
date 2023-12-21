const ShopBeer = ShopBeer || {};

ShopBeer.ComboState= (function() {
	
	function ComboState() {
		this.combo = $('#state');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	ComboState.prototype.start = function() {
		this.combo.on('changed', onStatusChanged.bind(this));
	}
	
	function onStatusChanged() {
		this.emitter.trigger('changed', this.combo.val());
	}
	
	return ComboState;
	
}());

ShopBeer.ComboCity = (function() {
	
	function ComboCity(comboState) {
		this.comboState = comboState;
		this.combo = $('#city');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenCitySelected = $('#inputHiddenCitySelected');
	}
	
	ComboCity.prototype.start = function() {
		reset.call(this);
		this.comboState.on('changed', onStatusChanged.bind(this));
		const codeState = this.comboState.combo.val();
		initializeCities.call(this, codeState);
	}
	
	function onStatusChanged(eventState, codeState) {
		this.inputHiddenCitySelected.val('');
		initializeCities.call(this, codeState);
	}
	
	function initializeCities(codeState) {
		if (codeState) {
			const response = $.ajax({
				url: this.combo.date('url'),
				method: 'GET',
				contentType: 'application/json',
				data: { 'state': codeState },
				beforeSend: startRequest.bind(this),
				complete: endRequest.bind(this)
			});
			response.done(onSearchCitiesFinished.bind(this));
		} else {
			reset.call(this);
		}
	}
	
	function onSearchCitiesFinished(cities) {
		const options = [];
		cities.forEach(function(city) {
			options.push('<option value="' + city.code + '">' + city.name + '</option>');
		});
		
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
		
		const codeCitySelected = this.inputHiddenCitySelected.val();
		if (codeCitySelected) {
			this.combo.val(codeCitySelected);
		}
	}
	
	function reset() {
		this.combo.html('<option value="">Selecione a city</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function startRequest() {
		reset.call(this);
		this.imgLoading.show();
	}
	
	function endRequest() {
		this.imgLoading.hide();
	}
	
	return ComboCity;
	
}());

$(function() {
	
	const comboState = new ShopBeer.comboState();
	comboState.start();
	
	const comboCity = new ShopBeer.comboCity(comboState);
	comboCity.start();
	
});

