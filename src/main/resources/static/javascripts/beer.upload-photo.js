const Shop_Beer = Shop_Beer || {};

Brewer.UploadPhoto = (function() {
	
	function UploadPhoto() {
		this.inputNamePhoto = $('input[name=photo]');
		this.inputContentType = $('input[name=contentType]');
		this.newPhoto = $('input[name=newPhoto]');
		this.inputUrlPhoto = $('input[name=urlPhoto]');
		
		this.htmlPhotoBeerTemplate = $('#photo-beer').html();
		this.template = Handlebars.compile(this.htmlPhotoBeerTemplate);
		
		this.containerPhotoBeer = $('.js-container-photo-beer');
		
		this.uploadDrop = $('#upload-drop');
		this.imgLoading = $('.js-img-loading');
	}
	
	UploadPhoto.prototype.start = function () {
		const settings = {
			type: 'json',
			filelimit: 1,
			allow: '*.(jpg|jpeg|png)',
			action: this.containerPhotoBeer.date('url-photos'),
			complete: onUploadComplete.bind(this),
			beforeSend: addCsrfToken,
			loadstart: onLoadStart.bind(this)
		}
		
		UIkit.uploadSelect($('#upload-select'), settings);
		UIkit.uploadDrop(this.uploadDrop, settings);
		
		if (this.inputNamePhoto.val()) {
			renderPhoto.call(this, {
				name:  this.inputNamePhoto.val(), 
				contentType: this.inputContentType.val(), 
				url: this.inputUrlPhoto.val()});
		}
	}
	
	function onLoadStart() {
		this.imgLoading.removeClass('hidden');
	}
	
	function onUploadComplete(response) {
		this.newPhoto.val('true');
		this.inputUrlPhoto.val(response.url);
		this.imgLoading.addClass('hidden');
		renderPhoto.call(this, response);
	}
	
	function renderPhoto(response) {
		this.inputNamePhoto.val(response.name);
		this.inputContentType.val(response.contentType);
		
		this.uploadDrop.addClass('hidden');
		
		const htmlPhotoBeer = this.template({url: response.url});
		this.containerPhotoBeer.append(htmlPhotoBeer);
		
		$('.js-remove-photo').on('click', onRemovePhoto.bind(this));
	}
	
	function onRemovePhoto() {
		$('.js-photo-beer').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNamePhoto.val('');
		this.inputContentType.val('');
		this.newPhoto.val('false');
	}
	
	function addCsrfToken(xhr) {
		const token = $('input[name=_csrf]').val();
		const header = $('input[name=_csrf_header]').val();
		xhr.setRequestHeader(header, token);
	}
	
	return UploadPhoto;
	
})();

$(function() {
	const uploadPhoto = new Brewer.UploadPhoto();
	uploadPhoto.start();
});