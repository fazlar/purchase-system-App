package com.purchaseSystem.purchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import com.purchaseSystem.base.BaseRepository;
import com.purchaseSystem.dataTable.DataTableRequest;
import com.purchaseSystem.dataTable.DataTableResults;
import com.purchaseSystem.dataTable.PaginationCriteria;
import com.purchaseSystem.itemDtl.PurItemDtlEntity;
import com.purchaseSystem.util.CommonUtils;
import com.purchaseSystem.util.Response;

@Repository
@Transactional
public class PurchaseRepository extends BaseRepository {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response gridList(HttpServletRequest request) {
		Response response = new Response();
		DataTableResults<PurchaseEntity> dataTableResults = null;

		PurchaseEntity purchaseEntity = new PurchaseEntity();
		DataTableRequest dataTableInRQ = new DataTableRequest(request);
		List gridList = new ArrayList<>();
		String fDateStr = request.getParameter("fromDate");
		String tDateStr = request.getParameter("toDate");

		if (!StringUtils.isEmpty(fDateStr)) {
			purchaseEntity.setFromDate(deateParse(fDateStr, "dd/MM/yyyy"));
		}

		if (!StringUtils.isEmpty(tDateStr)) {
			purchaseEntity.setToDate(deateParse(tDateStr, "dd/MM/yyyy"));
		}

		Long totalRowCount = totalCount(purchaseEntity);
		response = baseList(typedQuery(purchaseEntity, dataTableInRQ));
		if (response.isSuccess()) {
			if (response.getItems() != null) {
				gridList = response.getItems();
			}
			dataTableResults = dataTableResults(dataTableInRQ, countTypedQuery(purchaseEntity, dataTableInRQ), gridList,
					totalRowCount);
		}
		response.setItems(null);
		response.setObj(dataTableResults);
		return response;
	}

	public Response save(String reqObj) {

		PurchaseEntity purchaseEntity = objectMapperReadValue(reqObj, PurchaseEntity.class);

		Response responseForDuplicateCheck = duplicateChecEmployeeId(purchaseEntity);

		if (!responseForDuplicateCheck.isValid()) {
			return getErrorResponse("Duplicate Not Allow !!");
		}

		Random random = new Random();

		int generateId = random.nextInt(9999) + 999999;
		purchaseEntity.setPurchaseId(generateId);

		Response res = baseOnlySave(purchaseEntity);

		if (res.isSuccess()) {
			return getSuccessResponse("Saved Successfully");
		}

		return getErrorResponse("Save fail !!");

	}

//	EmployeeEntity obj = findById(employeeEntity.getId());
//
//	Response response = new Response();
//	employeeEntity.setId(null);
//
//	employeeEntity.setIdNotEqual(obj.getId());
//	Response responseForDuplicateCheck = duplicateChecEmployeeId(employeeEntity);
//
//	if (!responseForDuplicateCheck.isValid()) {
//		return getErrorResponse("Duplicate Not Allow !!");
//	}

	public Response update(String reqObj) {

		PurchaseEntity purchaseEntity = objectMapperReadValue(reqObj, PurchaseEntity.class);
		PurchaseEntity obj = findById(purchaseEntity.getId());

		Response response = new Response();
		purchaseEntity.setId(null);

		purchaseEntity.setIdNotEqual(obj.getInvoiceId());
		Response responseForDuplicateCheck = duplicateChecEmployeeId(purchaseEntity);

		if (!responseForDuplicateCheck.isValid()) {
			return getErrorResponse("Duplicate Not Allow !!");
		}

		if (obj == null) {
			return getErrorResponse("Record Not Found!!.");
		}

		purchaseEntity.setId(purchaseEntity.getId());
		response = baseUpdate(purchaseEntity);
		if (response.isSuccess()) {
			return getSuccessResponse("Update Successfully");
		}

		return getErrorResponse("Update fail !!");
	}

	public Response detele(Long id) {
		PurchaseEntity purchaseEntity = findById(id);
		if (purchaseEntity == null) {
			return getErrorResponse("Record not found!");
		}
		return baseDelete(purchaseEntity);
	}

	public Response list() {
		PurchaseEntity billsBToBLabPolicyObj = new PurchaseEntity();
		String data = objectToJson(baseList(criteriaQuery(billsBToBLabPolicyObj)));
		return baseList(criteriaQuery(billsBToBLabPolicyObj));
	}
	
	
	public Response getreortDta(String reqObj) {
		JSONObject json = new JSONObject(reqObj);
		String fDateStr = json.getString("fromDate");
		String tDateStr = json.getString("toDate");

		PurchaseEntity purchaseEntity = new PurchaseEntity();
		
		if (!StringUtils.isEmpty(fDateStr)) {
			purchaseEntity.setFromDate(deateParse(fDateStr, "dd/MM/yyyy"));
		}

		if (!StringUtils.isEmpty(tDateStr)) {
			purchaseEntity.setToDate(deateParse(tDateStr, "dd/MM/yyyy"));
		}	
		
		String data = objectToJson(baseList(criteriaQuery(purchaseEntity)));
		return baseList(criteriaQuery(purchaseEntity));
	}

	public PurchaseEntity findById(Long id) {
		PurchaseEntity purchaseEntity = new PurchaseEntity();
		purchaseEntity.setId(id);
		Response response = baseFindById(criteriaQuery(purchaseEntity));
		if (response.isSuccess()) {
			System.out.println(objectToJson(response.getObj()));
			return getValueFromObject(response.getObj(), PurchaseEntity.class);
		}
		return null;
	}

	private Response duplicateChecEmployeeId(PurchaseEntity purchaseEntity) {
		Response responce = new Response();

		if (purchaseEntity == null) {
			return getErrorResponse("Search Id not found!");
		}

		Long totalCount = totalCountWithDConjunction(purchaseEntity);

		if (totalCount.longValue() > 0) {
			responce.setValid(false);
			return responce;
		} else {
			responce.setValid(true);
			return responce;
		}

	}

	private Long totalCount(PurchaseEntity filter) {
		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<PurchaseEntity> root = from(PurchaseEntity.class, criteriaQuery);
		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root));
	}

	private Long totalCountWithDConjunction(PurchaseEntity filter) {

		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<PurchaseEntity> root = from(PurchaseEntity.class, criteriaQuery);

		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root));
	}

	// Non API

	@SuppressWarnings({ "rawtypes" })
	private <T> TypedQuery typedQuery(PurchaseEntity filter, DataTableRequest<T> dataTableInRQ) {
		init();
		List<Predicate> pArrayJoin = new ArrayList<Predicate>();
		List<Predicate> pConjunction = criteriaCondition(filter, null, null);
		List<Predicate> pDisJunction = dataTablefilter(dataTableInRQ);
		Predicate predicateAND = null;
		Predicate predicateOR = null;

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			predicateAND = builder.and(pArray);
		}
		if (!CollectionUtils.isEmpty(pDisJunction)) {
			Predicate[] pArray = pDisJunction.toArray(new Predicate[] {});
			predicateOR = builder.or(pArray);
		}
		if (predicateAND != null) {
			pArrayJoin.add(predicateAND);
		}
		if (predicateOR != null) {
			pArrayJoin.add(predicateOR);
		}
		if (dataTableInRQ.getOrder().getName() != null && !dataTableInRQ.getOrder().getName().isEmpty()) {
			if (dataTableInRQ.getOrder().getSortDir().equals("ASC")) {
				criteria.orderBy(builder.asc(root.get(dataTableInRQ.getOrder().getName())));
			} else {
				criteria.orderBy(builder.desc(root.get(dataTableInRQ.getOrder().getName())));
			}

		}
		criteria.where(pArrayJoin.toArray(new Predicate[0]));
		return baseTypedQuery(criteria, dataTableInRQ);
	}

	private <T> Long countTypedQuery(PurchaseEntity filter, DataTableRequest<T> dataTableInRQ) {

		if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
			return 0l;
		}

		CriteriaBuilder builder = criteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = longCriteriaQuery(builder);
		Root<PurchaseEntity> root = from(PurchaseEntity.class, criteriaQuery);
		return totalCount(builder, criteriaQuery, root, criteriaCondition(filter, builder, root),
				dataTablefilter(dataTableInRQ, builder, root));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private CriteriaQuery criteriaQuery(PurchaseEntity filter) {
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
	private List<Predicate> criteriaCondition(PurchaseEntity filter, CriteriaBuilder builder,
			Root<PurchaseEntity> root) {
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
				Predicate condition = builder.notEqual(root.get("invoiceId"), filter.getIdNotEqual());
				p.add(condition);
			}

			if (filter.getInvoiceId() != null) {
				Predicate condition = builder.equal(root.get("invoiceId"), filter.getInvoiceId());
				p.add(condition);
			}

			if (filter.getFromDate() != null && filter.getToDate() != null) {
				Date fromDate = addHourMinutesSeconds(00, 00, 00, filter.getFromDate());
				Date toDate = addHourMinutesSeconds(23, 59, 59, filter.getToDate());
				p.add(builder.between(root.get("purchaseDate"), fromDate, toDate));
			}

		}
		return p;
	}

	@Override
	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ, CriteriaBuilder builder, Root root) {
		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();
		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();
			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();
				if (!pair.getKey().equals("ssModifiedOn")) {

					// System.out.println("pair.getKey() " + pair.getKey());

					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							CommonUtils.PERCENTAGE_SIGN + pair.getValue().toLowerCase() + CommonUtils.PERCENTAGE_SIGN));
				}
			}

		}
		return p;
	}

	@SuppressWarnings("rawtypes")
	private void init() {
		initEntityManagerBuilderCriteriaQueryRoot(PurchaseEntity.class);
		@SuppressWarnings("unused")
		CriteriaBuilder builder = super.builder;
		@SuppressWarnings("unused")
		CriteriaQuery criteria = super.criteria;
		@SuppressWarnings("unused")
		Root root = super.root;
	}

}
