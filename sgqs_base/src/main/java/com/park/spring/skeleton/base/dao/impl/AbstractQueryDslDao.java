package com.park.spring.skeleton.base.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.Assert;

import com.mysema.query.FilteredClause;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpressionBase;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.OrderSpecifier.NullHandling;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.EntityPathBase;
import com.park.spring.skeleton.base.dao.EntityDao;
import com.park.spring.skeleton.base.entity.BaseEntity;
import com.park.spring.skeleton.base.handler.Pagination;

/**
 * 
 * @author park
 *
 * @param <T>
 * @param <Q>
 */
public abstract class AbstractQueryDslDao<T extends BaseEntity, Q extends EntityPathBase<T>, PK extends Serializable>
		implements EntityDao<T, PK> {

	// @Autowired protected JpaRepository<T, PK> repository;
	// @Autowired protected QueryDslPredicateExecutor<T> queryDsl;

	@Autowired protected EntityManager entityManager;
	protected final Q q;
	protected final Class<?> clazz;

	protected AbstractQueryDslDao(Class<?> clazz, Q q) {
		this.clazz = clazz;
		this.q = q;
	}

	protected JPAQuery getSelectQuery() {
		return new JPAQuery(entityManager).from(q);
	}

	protected JPADeleteClause getDeleteQuery() {
		return new JPADeleteClause(entityManager, q);
	}

	protected JPAUpdateClause getUpdateQuery() {
		return new JPAUpdateClause(entityManager, q);
	}

	// **********************************************************************
	// Override CRUD
	// **********************************************************************
	
	// C
	@Override
	public T create(T entity) {
		entityManager.persist(entity);
		return entity;
	}
	
	// C s
	@Override
	public List<T> create(List<T> entitys) {
		int idx = 51;
		for (T entity : entitys) {
			entityManager.persist(entity);
			if (idx % 50 == 0) {
				//50回ごとにクリア
				entityManager.flush();
				entityManager.clear();
			}
			idx++;
		}

		entityManager.flush();
		entityManager.clear();
		return entitys;
	}
	
	// R
	@Override
	public List<T> findAll() {
		return getSelectQuery()
				.list(q);
	}
	
	@Override
	public Page<T> findAll(final Pageable pageable) {
		return findAll((Predicate) null, pageable);
	}

	@Override
	public Page<T> findAll(final Predicate predicate, final Pageable pageable) {
		return findAll(predicate, pageable, (FactoryExpressionBase<T>)null);
	}
	
	@Override
	public Page<T> findAll(final Predicate predicate, final Pageable pageable,  final FactoryExpressionBase<T> factoryExpressionBase) {
		JPAQuery query = getSelectQuery();
		applyWhere(query, predicate);
		applyPagination(query, pageable);
		List<T> content = query.list(factoryExpressionBase == null ? q : factoryExpressionBase);
		
		JPAQuery countQuery = getSelectQuery();
		applyWhere(countQuery, predicate);
		long total = countQuery.count();

		return new Pagination<T>(content, pageable, total);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T findByPk(PK pk) {
		return (T) entityManager.find(clazz, pk);
	}
	
	@Override
	public int countAll() {
		Long count = getSelectQuery().count();
		return count.intValue();
	}

	
	// U
	@Override
	public void update(T entity) {
		entityManager.merge(entity);
	}
	
	// D
	@Override
	public void delete(T entity) {
		entityManager.remove(entity);
	}
	
	
	/**
	 * Applies the given {@link Predicate} to the given {@link FilteredClause}.
	 * @param query
	 * @param predicate
	 * @return
	 */
	public FilteredClause<?> applyWhere(FilteredClause<?> query,
			Predicate predicate) {
		if (predicate == null) {
			return query;
		}
		query.where(predicate);
		return query;
	}

	/**
	 * Applies the given {@link Pageable} to the given {@link JPQLQuery}.
	 *
	 * @param query
	 *            must not be {@literal null}.
	 * @param pageable
	 * @return the Querydsl {@link JPQLQuery}.
	 */
	public JPQLQuery applyPagination(JPQLQuery query, Pageable pageable) {

		if (pageable == null) {
			return query;
		}

		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		return applySorting(pageable.getSort(), query);
	}

	/**
	 * Applies sorting to the given {@link JPQLQuery}.
	 * 
	 * @param sort
	 * @param query
	 *            must not be {@literal null}.
	 * @return the Querydsl {@link JPQLQuery}
	 */
	public JPQLQuery applySorting(Sort sort, JPQLQuery query) {

		if (sort == null) {
			return query;
		}

		if (sort instanceof QSort) {
			return addOrderByFrom((QSort) sort, query);
		}

		return addOrderByFrom(sort, query);
	}

	/**
	 * Applies the given {@link OrderSpecifier}s to the given {@link JPQLQuery}.
	 * Potentially transforms the given {@code OrderSpecifier}s to be able to
	 * injection potentially necessary left-joins.
	 * 
	 * @param qsort
	 *            must not be {@literal null}.
	 * @param query
	 *            must not be {@literal null}.
	 */

	private JPQLQuery addOrderByFrom(QSort qsort, JPQLQuery query) {

		List<OrderSpecifier<?>> orderSpecifiers = qsort.getOrderSpecifiers();
		return query.orderBy(orderSpecifiers
				.toArray(new OrderSpecifier[orderSpecifiers.size()]));
	}

	/**
	 * Converts the {@link Order} items of the given {@link Sort} into
	 * {@link OrderSpecifier} and attaches those to the given {@link JPQLQuery}.
	 * 
	 * @param sort
	 *            must not be {@literal null}.
	 * @param query
	 *            must not be {@literal null}.
	 * @return
	 */
	private JPQLQuery addOrderByFrom(Sort sort, JPQLQuery query) {

		Assert.notNull(sort, "Sort must not be null!");
		Assert.notNull(query, "Query must not be null!");

		for (Order order : sort) {
			query.orderBy(toOrderSpecifier(order, query));
		}

		return query;
	}

	/**
	 * Transforms a plain {@link Order} into a QueryDsl specific
	 * {@link OrderSpecifier}.
	 * 
	 * @param order
	 *            must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private OrderSpecifier<?> toOrderSpecifier(Order order, JPQLQuery query) {

		return new OrderSpecifier(
				order.isAscending() ? com.mysema.query.types.Order.ASC
						: com.mysema.query.types.Order.DESC,
				buildOrderPropertyPathFrom(order),
				toQueryDslNullHandling(order.getNullHandling()));
	}

	/**
	 * Converts the given
	 * {@link org.springframework.data.domain.Sort.NullHandling} to the
	 * appropriate Querydsl {@link NullHandling}.
	 * 
	 * @param nullHandling
	 *            must not be {@literal null}.
	 * @return
	 * @since 1.6
	 */
	private NullHandling toQueryDslNullHandling(
			org.springframework.data.domain.Sort.NullHandling nullHandling) {

		Assert.notNull(nullHandling, "NullHandling must not be null!");

		switch (nullHandling) {

		case NULLS_FIRST:
			return NullHandling.NullsFirst;

		case NULLS_LAST:
			return NullHandling.NullsLast;

		case NATIVE:
		default:
			return NullHandling.Default;
		}
	}

	/**
	 * Creates an {@link Expression} for the given {@link Order} property.
	 * 
	 * @param order
	 *            must not be {@literal null}.
	 * @return
	 */
	private Expression<?> buildOrderPropertyPathFrom(Order order) {

		Assert.notNull(order, "Order must not be null!");

		PropertyPath path = PropertyPath.from(order.getProperty(), q.getType());
		Expression<?> sortPropertyExpression = q;

		while (path != null) {

			if (!path.hasNext() && order.isIgnoreCase()) {
				// if order is ignore-case we have to treat the last path
				// segment as a String.
				sortPropertyExpression = Expressions.stringPath(
						(Path<?>) sortPropertyExpression, path.getSegment())
						.lower();
			} else {
				sortPropertyExpression = Expressions.path(path.getType(),
						(Path<?>) sortPropertyExpression, path.getSegment());
			}

			path = path.next();
		}

		return sortPropertyExpression;
	}

}