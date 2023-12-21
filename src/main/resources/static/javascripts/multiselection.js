ShopBeer = ShopBeer || {};

Shop_Beer.Multiselection = (function() {
	
	function Multiselection() {
		this.statusBtn = $('.js-status-btn');
		this.selectionCheckbox = $('.js-selection');
		this.selectionAllCheckbox = $('.js-select-all');
	}

	Multiselection.prototype.start = function() {
		this.statusBtn.on('click', onStatusBtnClicked.bind(this));
		this.selectionAllCheckbox.on('click', onSelectionClicked.bind(this));
		this.selectionCheckbox.on('click', onSelectAllClicked.bind(this));
	}
	
	function onStatusBtnClicked(event) {
		const buttonClicked = $(event.currentTarget);
		const status = buttonClicked.data('status');
		const url = buttonClicked.data('url');
		
		const checkBoxSelected = this.selectionCheckbox.filter(':checked');
		const codes = $.map(checkBoxSelected, function(c) {
			return $(c).data('code');
		});
		
		if (codes.length > 0) {
			$.ajax({
				url: url,
				method: 'PUT',
				data: {
					code: codes,
					status: status
				}, 
				success: function() {
					window.location.reload();
				}
			});
			
		}
	}
	
	function onSelectAllClicked() {
		const status = this.selectionAllCheckbox.prop('checked');
		this.selectionCheckbox.prop('checked', status);
		actionButtonStatus.call(this, status);
	}
	
	function onSelectionClicked() {
		const checkboxSelectionChecked = this.selectionCheckbox.filter(':checked');
		this.selectionAllCheckbox.prop('checked', checkboxSelectionChecked.length >= this.selectionCheckbox.length);
		actionButtonStatus.call(this, checkboxSelectionChecked.length);
	}
	
	function actionButtonStatus(active) {
		active ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
	}
	
	return Multiselection;
	
}());

$(function() {
	const multiselection = new Shop_Beer.Multiselection();
	multiselection.start();
});