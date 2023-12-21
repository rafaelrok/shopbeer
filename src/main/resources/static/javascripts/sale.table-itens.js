ShopBeer.TableItems = (function() {
	
	function TableItems(autocomplete) {
		this.autocomplete = autocomplete;
		this.tableBeersContainer = $('.js-table-beers-container');
		this.uuid = $('#uuid').val();
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	TableItems.prototype.start = function() {
		this.autocomplete.on('item-selected', onItemSelected.bind(this));

		bindQuantity.call(this);
		bindTableItem.call(this);
	}

	TableItems.prototype.amount = function() {
		return this.tableBeersContainer.data('value');
	}
	
	function onItemSelected(eventItem, item) {
		const response = $.ajax({
			url: 'item',
			method: 'POST',
			data: {
				codeBeer: item.code,
				uuid: this.uuid
			}
		});

		response.done(onItemUpdatedOnServer.bind(this));
	}
	
	function onItemUpdatedOnServer(html) {
		this.tableBeersContainer.html(html);
		
		bindQuantity().call(this);
		
		const tableItem = bindTableItem.call(this);
		this.emitter.trigger('table-itens-update', tableItem.data('amount'));
	}
	
	function onQuantityItemChanged(eventQuantity) {
		const input = $(eventQuantity.target);
		let quantity = input.val();
		
		if (quantity <= 0) {
			input.val(1);
			quantity = 1;
		}
		
		const codeBeer = input.data('code-beer');
		
		const response = $.ajax({
			url: 'item/' + codeBeer,
			method: 'PUT',
			data: {
				quantity: quantity,
				uuid: this.uuid
			}
		});
		
		response.done(onItemUpdatedOnServer.bind(this));
	}
	
	function onDoubleClick(eventClick) {
		$(this).toggleClass('requesting-deletion');
	}
	
	function onExclusionItemClick(eventExclusion) {
		const codeBeer = $(eventExclusion.target).data('code-beer');
		const response = $.ajax({
			url: 'item/' + this.uuid + '/' + codeBeer,
			method: 'DELETE'
		});

		response.done(onItemUpdatedOnServer.bind(this));
	}
	
	function bindQuantity() {
		const quantityItemInput = $('.js-table-beer-quantity-item');
		quantityItemInput.on('change', onQuantityItemChanged.bind(this));
		quantityItemInput.maskNumber({ integer: true, thousands: '' });
	}
	
	function bindTableItem() {
		const tableItem = $('.js-tabela-item');
		tableItem.on('dblclick', onDoubleClick);
		$('.js-exclusion-item-btn').on('click', onExclusionItemClick.bind(this));
		return tableItem;
	}
	
	return TableItems;
	
}());
