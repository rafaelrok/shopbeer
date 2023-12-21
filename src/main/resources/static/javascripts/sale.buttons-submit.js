ShopBeer = ShopBeer || {};

ShopBeer.ButtonSubmit = (function() {
	
	function ButtonSubmit() {
		this.submitBtn = $('.js-submit-btn');
		this.form = $('.js-main-form');
	}

	ButtonSubmit.prototype.start = function() {
		this.submitBtn.on('click', onSubmit.bind(this));
	}
	
	function onSubmit(eventSubmit) {
		eventSubmit.preventDefault();
		
		const buttonClicked = $(eventSubmit.target);
		const action = buttonClicked.data('action');
		
		const actionInput = $('<input>');
		actionInput.attr('name', action);
		
		this.form.append(actionInput);
		this.form.submit();
	}
	
	return ButtonSubmit
	
}());

$(function() {
	
	const buttonSubmit = new ShopBeer.ButtonSubmit();
	buttonSubmit.start();
	
});