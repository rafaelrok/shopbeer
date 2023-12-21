const ShopBeer = ShopBeer || {};

ShopBeer.MaskMoney = (function() {
	
	function MaskMoney() {
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	
	MaskMoney.prototype.enable = function() {
//		this.decimal.maskMoney({ decimal: ',', thousands: '.' });
//		this.plain.maskMoney({ precision: 0, thousands: '.' });
		this.decimal.maskNumber({ decimal: ',', thousands: '.' });
		this.plain.maskNumber({ integer: true, thousands: '.' });
	}
	
	return MaskMoney;
	
}());

ShopBeer.MaskPhoneNumber = (function() {
	
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone-number');
	}
	
	MaskPhoneNumber.prototype.enable = function() {
		const maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		};
		
		const options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		
		this.inputPhoneNumber.mask(maskBehavior, options);
	}
	
	return MaskPhoneNumber;
	
}());

ShopBeer.MaskCep = (function() {
	
	function MaskCep() {
		this.inputZipCode = $('.js-cep');
	}
	
	MaskCep.prototype.enable = function() {
		this.inputZipCode.mask('00.000-000');
	}
	
	return MaskCep;
	
}());

ShopBeer.MaskDate = (function() {
	
	function MaskDate() {
		this.inputDate = $('.js-date');
	}
	
	MaskDate.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoClose: true
		});
	}
	
	return MaskDate;
	
}());

ShopBeer.Security = (function() {
	
	function Security() {
		this.token = $('input[name=_csrf]').val();
		this.header = $('input[name=_csrf_header]').val();
	}
	
	Security.prototype.enable = function() {
		$(document).ajaxSend(function(event, jqxhr, settings) {
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
	
}());

numeral.language('pt-br');

ShopBeer.formatCurrency = function(valor) {
	return numeral(valor).format('0,0.00');
}

ShopBeer.retrieveValue = function(valorFormatado) {
	return numeral().unformat(valorFormatado);
}

$(function() {
	const maskMoney = new ShopBeer.MaskMoney();
	maskMoney.enable();
	
	const maskPhoneNumber = new ShopBeer.MaskPhoneNumber();
	maskPhoneNumber.enable();
	
	const maskCep = new ShopBeer.MaskCep();
	maskCep.enable();
	
	const maskDate = new ShopBeer.MaskDate();
	maskDate.enable();
	
	const security = new ShopBeer.Security();
	security.enable();
	
});
