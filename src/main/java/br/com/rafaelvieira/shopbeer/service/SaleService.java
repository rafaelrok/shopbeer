package br.com.rafaelvieira.shopbeer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusSale;
import br.com.rafaelvieira.shopbeer.repository.SaleRepository;
import br.com.rafaelvieira.shopbeer.service.event.SaleEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ApplicationEventPublisher publisher;

    public SaleService(SaleRepository saleRepository, ApplicationEventPublisher publisher) {
        this.saleRepository = saleRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Sale save(Sale sale) {
        if (sale.isSaveForbidden()) {
            throw new RuntimeException("User trying to save a prohibited sale");
        }

        if (sale.isNew()) {
            sale.setCreateDate(LocalDateTime.now());
        } else {
            Sale existingSale = saleRepository.getReferenceById(sale.getCode());
            sale.setCreateDate(existingSale.getCreateDate());
        }

        if (sale.getDeliveryDate() != null) {
            sale.setDateTimeDelivery(LocalDateTime.of(sale.getDeliveryDate()
                    , sale.getDateTimeDelivery() != null ? sale.getDeliverySchedule() : LocalTime.NOON));
        }

        return saleRepository.saveAndFlush(sale);
    }

    @Transactional
    public void issue(Sale sale) {
        sale.setStatus(StatusSale.ISSUED);
        save(sale);

        publisher.publishEvent(new SaleEvent(sale));
    }

    @PreAuthorize("#sale.userEmployee == principal.username or hasRole('SALE_CANCELED')")
    @Transactional
    public void cancel(Sale sale) {
        Sale existingSale = saleRepository.getReferenceById(sale.getCode());

        existingSale.setStatus(StatusSale.CANCELED);
        saleRepository.save(existingSale);
    }
}
