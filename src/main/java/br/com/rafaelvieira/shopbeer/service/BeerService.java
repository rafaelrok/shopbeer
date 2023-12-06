package br.com.rafaelvieira.shopbeer.service;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.repository.BeerRepository;
import br.com.rafaelvieira.shopbeer.service.exception.ImpossibleDeleteEntityException;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import jakarta.persistence.PersistenceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeerService {

    private final BeerRepository beerRepository;
    private final PhotoStorage photoStorage;

    public BeerService(BeerRepository beerRepository, PhotoStorage photoStorage) {
        this.beerRepository = beerRepository;
        this.photoStorage = photoStorage;
    }

    @Transactional
    public void save(Beer beer) {
        beerRepository.save(beer);
    }

    @Transactional
    public void delete(Beer beer) {
        try {
            String photo = beer.getPhoto();
            beerRepository.delete(beer);
            beerRepository.flush();
            photoStorage.delete(photo);
        } catch (PersistenceException e) {
            throw new ImpossibleDeleteEntityException("Impossible to put out beer. It has already been used in some sales.");
        }
    }

}
