const Shop_Beer = Shop_Beer || {};

ShopBeer.MaskCpfCnpj = (function() {
	
	function MaskCpfCnpj() {
		this.radioTypePerson = $('.js-radio-type-person');
		this.labelCpfCnpj = $('[for=cpfOrCnpj]');
		this.inputCpfCnpj = $('#cpfOrCnpj');
	}

	MaskCpfCnpj.prototype.start = function() {
		this.radioTypePerson.on('change', onPersonTypeChanged.bind(this));
		const typePersonSelected = this.radioTypePerson.filter(':checked')[0];
		if (typePersonSelected) {
			applyMask.call(this, $(typePersonSelected));
		}
	}
	
	function onPersonTypeChanged(eventTypePerson) {
		const typePersonSelected = $(eventTypePerson.currentTarget);
		applyMask.call(this, typePersonSelected);
		this.inputCpfCnpj.val('');
	}
	
	function applyMask(typePersonSelected) {
		this.labelCpfCnpj.text(typePersonSelected.data('document'));
		this.inputCpfCnpj.mask(typePersonSelected.data('mask'));
		this.inputCpfCnpj.removeAttr('disabled');
	}
	
	return MaskCpfCnpj;
	
}());

$(function() {
	const maskCpfCnpj = new ShopBeer.MaskCpfCnpj();
	maskCpfCnpj.start();
});