package com.purchaseSystem.supplier;

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
import com.purchaseSystem.util.Response;



@Repository
@Transactional
public class SupplierRepository extends BaseRepository {

	public Response save(String reqObj) {

		SupplierEntity supplierEntity = objectMapperReadValue(reqObj, SupplierEntity.class);

		Response res = baseOnlySave(supplierEntity);

		if (res.isSuccess()) {
			return getSuccessResponse("Saved Successfully");
		}

		return getErrorResponse("Save fail !!");

	}

	public Response update(String reqObj) {

		SupplierEntity supplierEntity = objectMapperReadValue(reqObj, SupplierEntity.class);
		SupplierEntity obj = findById(supplierEntity.getId());

		Response response = new Response();
		supplierEntity.setId(null);


//		if (employeeEntity.getEmpId() != null && employeeEntity.getEmpId().length() != 4) {
//			return getErrorResponse("Employee Id must be 4 digit");
//		}

		if (obj == null) {
			return getErrorResponse("Record Not Found!!.");
		}

		supplierEntity.setId(supplierEntity.getIdNotEqual());
		response = baseUpdate(supplierEntity);
		if (response.isSuccess()) {
			return getSuccessResponse("Update Successfully");
		}

		return getErrorResponse("Update fail !!");
	}

	public Response detele(Long id) {
		SupplierEntity supplierEntity = findById(id);
		if (supplierEntity == null) {
			return getErrorResponse("Record not found!");
		}
		return baseDelete(supplierEntity);
	}

	public Response list() {
		SupplierEntity billsBToBLabPolicyObj = new SupplierEntity();
		String data = objectToJson(baseList(criteriaQuery(billsBToBLabPolicyObj)));
		return baseList(criteriaQuery(billsBToBLabPolicyObj));
	}

	public SupplierEntity findById(Long id) {
		SupplierEntity supplierEntity = new SupplierEntity();
		supplierEntity.setId(id);
		Response response = baseFindById(criteriaQuery(supplierEntity));
		if (response.isSuccess()) {
			System.out.println(objectToJson(response.getObj()));
			return getValueFromObject(response.getObj(), SupplierEntity.class);
		}
		return null;
	}

	

	private Long totalCountWithDConjunction(SupplierEntity filter) {

		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<SupplierEntity> root = from(SupplierEntity.class, criteriaQuery);

		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root));
	}

	// Non API
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private CriteriaQuery criteriaQuery(SupplierEntity filter) {
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
	private List<Predicate> criteriaCondition(SupplierEntity filter, CriteriaBuilder builder,
			Root<SupplierEntity> root) {
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

//			if (filter.getEmpId() != null) {
//				Predicate condition = builder.equal(root.get("empId"), filter.getEmpId());
//				p.add(condition);
//			}

		}
		return p;
	}

	@SuppressWarnings("rawtypes")
	private void init() {
		initEntityManagerBuilderCriteriaQueryRoot(SupplierEntity.class);
		@SuppressWarnings("unused")
		CriteriaBuilder builder = super.builder;
		@SuppressWarnings("unused")
		CriteriaQuery criteria = super.criteria;
		@SuppressWarnings("unused")
		Root root = super.root;
	}

}
