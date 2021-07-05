package com.purchaseSystem.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.purchaseSystem.base.BaseRepository;
import com.purchaseSystem.itemDtl.PurItemDtlEntity;
import com.purchaseSystem.util.Response;

@Repository
@Transactional
public class ItemRepository extends BaseRepository {

	public Response save(String reqObj) {

		ItemEntity itemEntity = objectMapperReadValue(reqObj, ItemEntity.class);
		

		Response res = baseOnlySave(itemEntity);

		if (res.isSuccess()) {
			return getSuccessResponse("Saved Successfully");
		}

		return getErrorResponse("Save fail !!");

	}

	public Response update(String reqObj) {

		ItemEntity itemEntity = objectMapperReadValue(reqObj, ItemEntity.class);
		ItemEntity obj = findById(itemEntity.getId());

		Response response = new Response();
		itemEntity.setId(null);

		itemEntity.setIdNotEqual(obj.getId());

		if (obj == null) {
			return getErrorResponse("Record Not Found!!.");
		}

		itemEntity.setId(itemEntity.getIdNotEqual());
		response = baseUpdate(itemEntity);
		if (response.isSuccess()) {
			return getSuccessResponse("Update Successfully");
		}

		return getErrorResponse("Update fail !!");
	}

	public Response detele(Long id) {
		ItemEntity itemEntity = findById(id);
		if (itemEntity == null) {
			return getErrorResponse("Record not found!");
		}
		return baseDelete(itemEntity);
	}

	public Response list() {
		ItemEntity billsBToBLabPolicyObj = new ItemEntity();
		String data = objectToJson(baseList(criteriaQuery(billsBToBLabPolicyObj)));
		return baseList(criteriaQuery(billsBToBLabPolicyObj));
	}

	public ItemEntity findById(Long id) {
		ItemEntity itemEntity = new ItemEntity();
		itemEntity.setId(id);
		Response response = baseFindById(criteriaQuery(itemEntity));
		if (response.isSuccess()) {
			System.out.println(objectToJson(response.getObj()));
			return getValueFromObject(response.getObj(), ItemEntity.class);
		}
		return null;
	}


	private Long totalCountWithDConjunction(ItemEntity filter) {

		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<ItemEntity> root = from(ItemEntity.class, criteriaQuery);

		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root));
	}

	// Non API
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private CriteriaQuery criteriaQuery(ItemEntity filter) {
		init();

		List<Predicate> p = new ArrayList<Predicate>();
		p = criteriaCondition(filter, null, null);

		if (!CollectionUtils.isEmpty(p)) {
			Predicate[] pArray = p.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}
		return criteria;
	}

	@SuppressWarnings({ "unchecked" })
	private List<Predicate> criteriaCondition(ItemEntity filter, CriteriaBuilder builder,
			Root<ItemEntity> root) {
		if (builder == null) {
			builder = super.builder;
		}
		if (root == null) {
			root = super.root;
		}
		List<Predicate> p = new ArrayList<Predicate>();
		if (filter != null) {

			if (filter.getId() != null && filter.getId() > 0) {
				Predicate condition = builder.equal(root.get("id"), filter.getId());
				p.add(condition);
			}

			if (filter.getIdNotEqual() != null) {
				Predicate condition = builder.notEqual(root.get("id"), filter.getIdNotEqual());
				p.add(condition);
			}

			
		}
		return p;
	}

	@SuppressWarnings("rawtypes")
	private void init() {
		initEntityManagerBuilderCriteriaQueryRoot(ItemEntity.class);
		@SuppressWarnings("unused")
		CriteriaBuilder builder = super.builder;
		@SuppressWarnings("unused")
		CriteriaQuery criteria = super.criteria;
		@SuppressWarnings("unused")
		Root root = super.root;
	}

}
