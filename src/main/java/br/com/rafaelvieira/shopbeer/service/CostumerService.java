package br.com.rafaelvieira.shopbeer.service;

import java.util.Optional;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.CostumerRepository;
import br.com.rafaelvieira.shopbeer.service.exception.CpfCnpjCustomerAlreadyRegisteredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CostumerService {

    private final CostumerRepository costumerRepository;

    public CostumerService(CostumerRepository costumerRepository) {
        this.costumerRepository = costumerRepository;
    }

    @Transactional
    public void save(Costumer costumer) {
        Optional<Costumer> existingCostumer = costumerRepository.findByCpfOrCnpj(costumer.getCpfOrCnpjNoFormatting());
        if (existingCostumer.isPresent()) {
            throw new CpfCnpjCustomerAlreadyRegisteredException("CPF/CNPJ already registered");
        }

        costumerRepository.save(costumer);
    }
}
