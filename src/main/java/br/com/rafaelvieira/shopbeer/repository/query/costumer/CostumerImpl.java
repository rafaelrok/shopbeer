package br.com.rafaelvieira.shopbeer.repository.query.costumer;

import br.com.rafaelvieira.shopbeer.domain.Address;
import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.filter.CostumerFilter;
import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


public class CostumerImpl implements CostumerQueries {

	@PersistenceContext
	private EntityManager manager;

	private final PaginationUtil paginacaoUtil;

	public CostumerImpl(PaginationUtil paginacaoUtil) {
		this.paginacaoUtil = paginacaoUtil;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Costumer> filter(CostumerFilter costumerFilter, Pageable pageable) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Costumer> cq = cb.createQuery(Costumer.class);
		Root<Costumer> costumer = cq.from(Costumer.class);
		Predicate predicate = cb.conjunction();
		addFilter(costumerFilter, cb, predicate, costumer);
		Join<Costumer, Address> addressJoin = costumer.join("address", JoinType.LEFT);
		Join<Address, City> cityJoin = addressJoin.join("city", JoinType.LEFT);

		predicate = cb.and(predicate, cb.equal(cityJoin.get("state").get("id"), 1));
//		predicate = cb.equal(cityJoin.get("state").get("id"), 1);

		cq.where(predicate);
		TypedQuery<Costumer> query = manager.createQuery(cq);

		// Apply pagination
		paginacaoUtil.prepare(cb, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(costumerFilter));
	}

	private Long total(CostumerFilter costumerFilter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Costumer> costumer = cq.from(Costumer.class);
		Predicate predicate = cb.conjunction();
		addFilter(costumerFilter, cb, predicate, costumer);

		cq.select(cb.count(costumer)).where(predicate);
		return manager.createQuery(cq).getSingleResult();
	}

	private void addFilter(CostumerFilter costumerFilter, CriteriaBuilder cb, Predicate predicate, Root<Costumer> costumer) {
		if (costumerFilter != null) {
			if (!StringUtils.hasText(costumerFilter.getName())) {
				predicate = cb.and(predicate, cb.like(cb.lower(costumer.get("name")), "%" + costumerFilter.getName().toLowerCase() + "%"));
			}

			if (!StringUtils.hasText(costumerFilter.getCpfOrCnpj())) {
				cb.and(predicate, cb.equal(costumer.get("cpfOrCnpj"), costumerFilter.getCpfOrCnpjNoFormatting()));
			}
		}
	}


//	public Page<Costumer> filter(CostumerFilter costumerFilter, Pageable pageable) {
//		Criteria criteria = manager.unwrap(Session.class).createCriteria(Costumer.class);
//
//		paginacaoUtil.preparar(criteria, pageable);
//		addFilter(costumerFilter, criteria);
//		criteria.createAlias("address.city", "c", JoinType.LEFT_OUTER_JOIN);
//		criteria.createAlias("c.state", "e", JoinType.LEFT_OUTER_JOIN);
//
//		return new PageImpl<>(criteria.list(), pageable, total(filtro));
//	}
//
//	private Long total(CostumerFilter filtro) {
//		Criteria criteria = manager.unwrap(Session.class).createCriteria(Costumer.class);
//		addFilter(filtro, criteria);
//		criteria.setProjection(Projections.rowCount());
//		return (Long) criteria.uniqueResult();
//	}
//
//	private void addFilter(CostumerFilter costumerFilter, Criteria criteria) {
//		if (costumerFilter != null) {
//			if (!StringUtils.hasText(costumerFilter.getName())) {
//				criteria.add(Restrictions.ilike("name", costumerFilter.getName(), MatchMode.ANYWHERE));
//			}
//
//			if (!StringUtils.hasText(filtro.getCpfOuCnpj())) {
//				criteria.add(Restrictions.eq("cpfOrCnpj", costumerFilter.getCpfOuCnpjSemFormatacao()));
//			}
//		}
//	}

}
