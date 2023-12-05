package br.com.rafaelvieira.shopbeer.repository.query.userEmployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.rafaelvieira.shopbeer.domain.Group;
import br.com.rafaelvieira.shopbeer.domain.UserGroup;
import br.com.rafaelvieira.shopbeer.repository.filter.UserEmployeeFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;

import org.springframework.util.StringUtils;


public class UserEmployeeImpl implements UserEmployeesQuery {

	@PersistenceContext
	private EntityManager manager;

	/*
	 * Codigo antigo com Criteria do Hibernate (deprecated)
	 */
//	private final PaginationUtil paginationUtil;
//
//	public UserEmployeeImpl(PaginationUtil paginationUtil) {
//		this.paginationUtil = paginationUtil;
//	}

	@Override
	public Optional<UserEmployee> perEmailActive(String email) {
		return manager
				.createQuery("from UserEmployee where lower(email) = lower(:email) and active = true", UserEmployee.class)
				.setParameter("email", email).getResultList().stream().findFirst();
	}

	@Override
	public List<String> permissions(UserEmployee userEmployee) {
		return manager.createQuery(
				"select distinct p.name from UserEmployee u inner join u.groups g inner join g.permissions p where u = :userEmployee", String.class)
				.setParameter("userEmployee", userEmployee)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<UserEmployee> filtered(UserEmployeeFilter filter, Pageable pageable) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UserEmployee> cq = cb.createQuery(UserEmployee.class);
		Root<UserEmployee> root = cq.from(UserEmployee.class);

		// Adicione os filtros aqui, por exemplo:
		List<Predicate> predicates = new ArrayList<>();
		if (filter != null) {
			if (!StringUtils.hasText(filter.getName())) {
				predicates.add(cb.like(root.get("nome"), "%" + filter.getName() + "%"));
			}
			// Adicione mais filtros conforme necessário...
		}
		cq.where(predicates.toArray(new Predicate[0]));

		// Adicione a ordenação aqui, por exemplo:
		if (pageable.getSort() != null && pageable.getSort().isSorted()) {
			pageable.getSort().forEach(order -> {
				if (order.isAscending()) {
					cq.orderBy(cb.asc(root.get(order.getProperty())));
				} else {
					cq.orderBy(cb.desc(root.get(order.getProperty())));
				}
			});
		}

		// Execute a consulta e obtenha o resultado
		int totalRows = manager.createQuery(cq).getResultList().size();
		cq.select(root);
		List<UserEmployee> list = manager.createQuery(cq)
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize())
				.getResultList();

		return new PageImpl<>(list, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), totalRows);
	}

	/*
	* Codigo antigo com Criteria do Hibernate (deprecated)
	*/
//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true)
//	@Override
//	public Page<UserEmployee> filtrar(UserEmployeeFilter filter, Pageable pageable) {
//		Criteria criteria = manager.unwrap(Session.class).createCriteria(UserEmployee.class);
//
//		paginationUtil.prepare(criteria, pageable);
//		adicionarFiltro(filter, criteria);
//
//		List<UserEmployee> filtered = criteria.list();
//		filtered.forEach(u -> Hibernate.initialize(u.getGroups()));
//		return new PageImpl<>(filtered, pageable, total(filter));
//	}

	@Transactional(readOnly = true)
	@Override
	public UserEmployee searchWithGroups(Long codigo) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UserEmployee> cq = cb.createQuery(UserEmployee.class);
		Root<UserEmployee> root = cq.from(UserEmployee.class);

		cq.where(cb.equal(root.get("codigo"), codigo));

		return manager.createQuery(cq).getSingleResult();
	}

	/*
	* Codigo antigo com Criteria do Hibernate (deprecated)
 	*/
//	@Transactional(readOnly = true)
//	@Override
//	public UserEmployee searchWithGroups(Long codigo) {
//		Criteria criteria = manager.unwrap(Session.class).createCriteria(UserEmployee.class);
//		criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
//		criteria.add(Restrictions.eq("codigo", codigo));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		return (UserEmployee) criteria.uniqueResult();
//	}

	private Long total(UserEmployeeFilter filtro) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<UserEmployee> root = cq.from(UserEmployee.class);

		// Adicione os filtros aqui, por exemplo:
		List<Predicate> predicates = new ArrayList<>();
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getName())) {
				predicates.add(cb.like(root.get("nome"), "%" + filtro.getName() + "%"));
			}
			// Adicione mais filtros conforme necessário...
		}
		cq.where(predicates.toArray(new Predicate[0]));

		cq.select(cb.count(root));

		return manager.createQuery(cq).getSingleResult();
	}

	/*
	 * Codigo antigo com Criteria do Hibernate (deprecated)
	 */
//	private Long total(UserEmployeeFilter filtro) {
//		Criteria criteria = manager.unwrap(Session.class).createCriteria(UserEmployee.class);
//		adicionarFiltro(filtro, criteria);
//		criteria.setProjection(Projections.rowCount());
//		return (Long) criteria.uniqueResult();
//	}

	private void addFilter(UserEmployeeFilter filtro, CriteriaBuilder cb, CriteriaQuery<UserEmployee> cq, Root<UserEmployee> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getName())) {
				predicates.add(cb.like(root.get("nome"), "%" + filtro.getName() + "%"));
			}

			if (!StringUtils.isEmpty(filtro.getEmail())) {
				predicates.add(cb.like(root.get("email"), filtro.getEmail() + "%"));
			}

			if (filtro.getGroups() != null && !filtro.getGroups().isEmpty()) {
				for (Long codigoGrupo : filtro.getGroups().stream().mapToLong(Group::getCode).toArray()) {
					Subquery<Long> subquery = cq.subquery(Long.class);
					Root<UserGroup> subRoot = subquery.from(UserGroup.class);
					subquery.select(subRoot.get("id").get("userEmployee"));
					subquery.where(cb.equal(subRoot.get("id").get("grupo").get("codigo"), codigoGrupo));
					predicates.add(cb.in(root.get("codigo")).value(subquery));
				}
			}
		}
		cq.where(predicates.toArray(new Predicate[0]));
	}

	/*
	 * Codigo antigo com Criteria do Hibernate (deprecated)
	 */
//	private void adicionarFiltro(UserEmployeeFilter filtro, Criteria criteria) {
//		if (filtro != null) {
//			if (!StringUtils.isEmpty(filtro.getName())) {
//				criteria.add(Restrictions.ilike("nome", filtro.getName(), MatchMode.ANYWHERE));
//			}
//
//			if (!StringUtils.isEmpty(filtro.getEmail())) {
//				criteria.add(Restrictions.ilike("email", filtro.getEmail(), MatchMode.START));
//			}
//
//			if (filtro.getGroups() != null && !filtro.getGroups().isEmpty()) {
//				List<Criterion> subqueries = new ArrayList<>();
//				for (Long codigoGrupo : filtro.getGroups().stream().mapToLong(Group::getCode).toArray()) {
//					DetachedCriteria dc = DetachedCriteria.forClass(UserGroup.class);
//					dc.add(Restrictions.eq("id.grupo.codigo", codigoGrupo));
//					dc.setProjection(Projections.property("id.userEmployee"));
//
//					subqueries.add(Subqueries.propertyIn("codigo", dc));
//				}
//
//				Criterion[] criterions = new Criterion[subqueries.size()];
//				criteria.add(Restrictions.and(subqueries.toArray(criterions)));
//			}
//		}
//	}

}
