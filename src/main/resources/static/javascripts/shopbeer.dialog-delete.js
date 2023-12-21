ShopBeer = ShopBeer || {};

ShopBeer.DialogDelete = (function() {
	
	function DialogDelete() {
		this.exclusionBtn = $('.js-exclusion-btn')
	}

	DialogDelete.prototype.start = function() {
		this.exclusionBtn.on('click', onDeleteClicked.bind(this));
		
		if (window.location.search.indexOf('exclude') > -1) {
			swal('Pronto!', 'Excluído com sucesso!', 'success');
		}
	}
	
	function onDeleteClicked(eventButton) {
		event.preventDefault();
		const buttonClicked = $(eventButton.currentTarget);
		const url = buttonClicked.data('url');
		const object = buttonClicked.data('objeto');
		
		swal({
			title: 'Tem certeza?',
			text: 'Delete "' + object + '"? Você não poderá recuperar depois.',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Sim, exclua agora!',
			closeOnConfirm: false
		}, onDeleteConfirmed.bind(this, url));
	}
	
	function onDeleteConfirmed(url) {
		$.ajax({
			url: url,
			method: 'DELETE',
			success: onDeletedSuccess.bind(this),
			error: onErroDelete.bind(this)
		});
	}
	
	function onDeletedSuccess() {
		const urlCurrent = window.location.href;
		const separator = urlCurrent.indexOf('?') > -1 ? '&' : '?';
		const newUrl = urlCurrent.indexOf('exclude') > -1 ? urlCurrent : urlCurrent + separator + 'exclude';

		window.location = newUrl;
	}
	
	function onErroDelete(e) {
		console.log('ahahahah', e.responseText);
		swal('Oops!', e.responseText, 'error');
	}
	
	return DialogDelete;
	
}());

$(function() {
	const dialog = new ShopBeer.DialogDelete();
	dialog.start();
});
