package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.domain.dto.PhotoDTO;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorageRunnable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
public class PhotoController {

	private final PhotoStorage photoStorage;

    public PhotoController(PhotoStorage photoStorage) {
        this.photoStorage = photoStorage;
    }

    @PostMapping
	public DeferredResult<PhotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<PhotoDTO> result = new DeferredResult<>();

		Thread thread = new Thread(new PhotoStorageRunnable(files, result, photoStorage));
		thread.start();
		
		return result;
	}
	
	@GetMapping("/{name:.*}")
	public byte[] recover(@PathVariable String name) {
		return photoStorage.recover(name);
	}
	
}
