const ShopBeer = ShopBeer || {};

ShopBeer.StyleRegistrationQuick = (function() {
	
	function StyleRegistrationQuick() {
		this.modal = $('#modalStyleRegistrationQuick');
		this.buttonSave = this.modal.find('.js-modal-register-style-save-btn');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action');
		this.inputNameStyle = $('#nameStyle');
		this.containerMessageErro = $('.js-message-register-quick-style');
	}

	StyleRegistrationQuick.prototype.iniciar = function() {
		this.form.on('submit', function(event) { event.preventDefault() });
		this.modal.on('shown.bs.modal', onModalShow.bind(this));
		this.modal.on('hide.bs.modal', onModalClose.bind(this))
		this.buttonSave.on('click', onButtonSaveClick.bind(this));
	}
	
	function onModalShow() {
		this.inputNameStyle.focus();
	}
	
	function onModalClose() {
		this.inputNameStyle.val('');
		this.containerMessageErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onButtonSaveClick() {
		const nameStyle = this.inputNameStyle.val().trim();
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nameStyle }),
			error: onErroSaveStyle.bind(this),
			success: onStyleSave.bind(this)
		});
	}
	
	function onErroSaveStyle(obj) {
		const messageErro = obj.responseText;
		this.containerMessageErro.removeClass('hidden');
		this.containerMessageErro.html('<span>' + messageErro + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onStyleSave(style) {
		const comboStyle = $('#style');
		comboStyle.append('<option value=' + style.code + '>' + style.name + '</option>');
		comboStyle.val(style.code);
		this.modal.modal('hide');
	}
	
	return StyleRegistrationQuick;
	
}());

$(function() {
	const styleRegistrationQuick = new ShopBeer.StyleRegistrationQuick();
	styleRegistrationQuick.start();
});
