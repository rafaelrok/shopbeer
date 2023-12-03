package br.com.rafaelvieira.shopbeer.repository.pagination;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    public void prepare(Criteria criteria, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

        criteria.setFirstResult(primeiroRegistro);
        criteria.setMaxResults(totalRegistrosPorPagina);

        Sort sort = pageable.getSort();
        if (sort != null && sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
        }
    }
}
