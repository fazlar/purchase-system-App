package com.purchaseSystem.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.hibernate.dialect.Dialect;
import org.hibernate.jpa.QueryHints;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.purchaseSystem.dataTable.DataTableRequest;
import com.purchaseSystem.dataTable.PaginationCriteria;
import com.purchaseSystem.util.CommonFunctions;
import com.purchaseSystem.util.CommonUtils;
import com.purchaseSystem.util.Response;




public class BaseRepository implements CommonFunctions {

	private final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);
	public CriteriaBuilder builder = null;
	public CriteriaQuery criteria = null;
	public Root root = null;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private Environment env;

	public Response baseOnlySave(Object obj) {
		Response response = new Response();
		try {
			entityManager.persist(obj);
			response.setObj(obj);
			return getSuccessResponse("Saved Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseOnlySave Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Save fail !!");
		}

	}

	public Response baseBatchOnlySave(List<Object> objects) {
		Response response = new Response();
		int batchSize = batchSize();
		try {
			List<Object> items = new ArrayList<Object>();
			for (int i = 0; i < objects.size(); i++) {
				if (i > 0 && i % batchSize == 0) {
					entityManager.flush();
					entityManager.clear();

				}
				Object object = objects.get(i);
				entityManager.persist(object);
				entityManager.persist(object);
				items.add(object);
			}
			response.setItems(items);
			return getSuccessResponse("Saved Successfully", response);
		} catch (Exception e) {

			LOGGER.error(" baseBatchOnlySave Exception == " + e.getCause().getCause().getMessage());

			return getErrorResponse("Batch Save Fail !!");
		}
	}

	public Response baseBatchSaveOrUpdate(List<Object> objects) {
		Response response = new Response();
		int batchSize = batchSize();
		try {
			List<Object> items = new ArrayList<Object>();
			for (int i = 0; i < objects.size(); i++) {
				if (i > 0 && i % batchSize == 0) {
					entityManager.flush();
					entityManager.clear();
				}
				Object object = objects.get(i);
				entityManager.merge(object);
				items.add(object);
			}
			response.setItems(items);
			return getSuccessResponse("Update Successfully", response);
		} catch (Exception e) {
			LOGGER.error(" baseBatchSaveOrUpdate Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Batch Save Fail !!");
		}
	}

	public Response baseSaveOrUpdate(Object obj) {
		Response response = new Response();
		try {
			response.setObj(entityManager.merge(obj));
			return getSuccessResponse("Update Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseSaveOrUpdate Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Save fail !!");
		}

	}

	public Response baseSaveOrUpdate1(Object obj) {
		Response response = new Response();
		try {
			entityManager.getTransaction().begin();
			response.setObj(entityManager.merge(obj));
			entityManager.getTransaction().commit();

			return getSuccessResponse("Update Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseSaveOrUpdate1 Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Save fail !!");
		}

	}

	public Response baseUpdate(Object obj) {

		try {
			entityManager.merge(obj);
			return getSuccessResponse("Update Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			LOGGER.error(" baseUpdate Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Update fail !!");
		}

	}

	public Response baseRemove(Object obj) {

		try {
			entityManager.merge(obj);
			return getSuccessResponse("Remove Successfully");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseRemove Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Update fail !!");
		}

	}

	public Response baseDelete(Object obj) {
		try {
			entityManager.remove(obj);
			return getSuccessResponse("Delete Successfully");
		} catch (Exception e) {
			LOGGER.error(" baseDelete Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Delete fail !!");
		}

	}

	public Response baseBatchDelete(String entityName, String columnName, @SuppressWarnings("rawtypes") List ids) {

		try {
			Query query = entityManager.createQuery("DELETE " + entityName + " WHERE " + columnName + " IN (:ids)");
			query.setParameter("ids", ids);
			query.executeUpdate();
			return getSuccessResponse("Deleted Successfully");
		} catch (Exception e) {
			LOGGER.error(" baseBatchDelete Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Delete fail !!");
		}

	}

	// TODO
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseDataList(CriteriaQuery criteria) {
		Response response = new Response();
		List list = null;
		try {
			list = entityManager.createQuery(criteria).getResultList();

			if (list.size() > 0) {
				response.setItems(list);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseDataList Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseTupleSingleColumn(Map<String, String> columnMap, CriteriaQuery criteria) {

		Response response = new Response();
		Object obj = null;
		try {
			obj = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getSingleResult();
			if (obj != null) {
				Tuple tuple = (Tuple) obj;
				response.setObj(tuple.get(0));
				return getSuccessResponse("find data Successfully", response);
			}
			return getErrorResponse("Data not found !!");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseTupleSingleColumn Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseTupleList(Map<String, String> columnMap, CriteriaQuery criteria) {
		Response response = new Response();
		List list = null;
		try {
			list = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getResultList();

			if (list.size() > 0) {

				List itemList = new ArrayList<>();

				List<Tuple> tupleList = list;

				for (Tuple tuple : tupleList) {

					Map<String, Object> tupleMap = new HashMap<String, Object>();
					int index = 0;

					for (Map.Entry<String, String> columName : columnMap.entrySet()) {

						tupleMap.put(columName.getValue(), tuple.get(index));

						index++;

					}

					itemList.add(tupleMap);

				}
				response.setItems(itemList);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" baseTupleList Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Data not found !!");
		}

	}

	public <T> CriteriaQuery<Tuple> baseDistinctMultiSeletCriteria(CriteriaBuilder builder,
			CriteriaQuery<Tuple> criteria, Root<T> root, Map<String, String> columnNameMap,
			List<Predicate> pConjunction) {

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}

		List<Selection<?>> columnSelection = new LinkedList<Selection<?>>();

		for (Map.Entry<String, String> columName : columnNameMap.entrySet()) {
			columnSelection.add(root.get(columName.getValue()));
		}

		criteria.multiselect(columnSelection).distinct(true);

		return criteria;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseList(CriteriaQuery criteria) {
		Response response = new Response();
		List list = null;
		try {
			list = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getResultList();

			if (list.size() > 0) {
				response.setItems(list);
				System.out.println(objectToJson(response.getObj()));
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(" BaseList Exception == " + e.getCause().getCause().getMessage());
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseFindByParent(CriteriaQuery criteria) {
		Response response = new Response();
		List list = null;
		try {
			list = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getResultList();

			if (list.size() > 0) {

				response.setItems(list);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes" })
	public Response baseList(TypedQuery typedQuery) {
		Response response = new Response();
		List list = null;

		try {

			list = typedQuery.setHint(QueryHints.HINT_READONLY, true).getResultList();

			if (list.size() > 0) {

				response.setItems(list);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");

		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Response getMaxRecord(CriteriaQuery criteria) {

		Response response = new Response();
		try {

			CriteriaQuery<T> select = criteria.select(root);

			TypedQuery<T> typedQuery = entityManager.createQuery(select);
			typedQuery.setFirstResult(0);
			typedQuery.setMaxResults(1);
			Object obj = typedQuery.getSingleResult();
			if (obj != null) {
				response.setObj(obj);
				return getSuccessResponse("Data found ", response);
			}
			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Response getMaxSecodRecord(CriteriaQuery criteria) {

		Response response = new Response();

		try {

			CriteriaQuery<T> select = criteria.select(root);

			TypedQuery<T> typedQuery = entityManager.createQuery(select);
			typedQuery.setFirstResult(1);
			typedQuery.setMaxResults(1);
			Object obj = typedQuery.getSingleResult();
			if (obj != null) {
				response.setObj(obj);
				return getSuccessResponse("Data found ", response);
			}
			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseFindById(CriteriaQuery criteria) {
		Response response = new Response();
		Object obj = null;
		try {
			obj = entityManager.createQuery(criteria).getSingleResult();
			response.setObj(obj);
			return getSuccessResponse("find data Successfully", response);
		} catch (NoResultException e) {
			// TODO: handle exception
			return getErrorResponse("Data not Found !!");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("exception occured !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseSingleObject(CriteriaQuery criteria) {
		Response response = new Response();
		Object obj = null;
		try {
			obj = entityManager.createQuery(criteria).getSingleResult();
			response.setObj(obj);
			return getSuccessResponse("find data Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not Found !!");
		}

	}

	public Response findByAppImageInsubdir(String imageName, String subDir) {

		String fileApiUri = env.getProperty("file.api.url");

		String uri = fileApiUri + "/findAppImageInsubdir/" + imageName;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("subDir", subDir);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		Response photoResponse = restTemplate.postForObject(uri, requestEntity, Response.class);

		return photoResponse;
	}

	public Response findByInSubdir(String fileName, String subDir) {

		String fileApiUri = env.getProperty("file.api.url");

		String uri = fileApiUri + "/findPatientReport/" + fileName;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("subDir", subDir);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		Response photoResponse = restTemplate.postForObject(uri, requestEntity, Response.class);

		return photoResponse;

	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> TypedQuery baseTypedQuery(CriteriaQuery criteria) {

		CriteriaQuery<T> select = criteria.select(root);
		TypedQuery<T> typedQuery = entityManager.createQuery(select);

		return typedQuery;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response getListFindById(CriteriaQuery criteria) {
		Response response = new Response();
		Object obj = null;
		try {
			obj = entityManager.createQuery(criteria).getResultList();
			response.setItems((List) obj);
			return getSuccessResponse("find data Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not Found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> entityManagerBuilderCriteriaQueryRoot(Class clazz) {

		Map<String, Object> entityManagerParams = new HashMap<String, Object>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(clazz);
		entityManagerParams.put("builder", builder);
		entityManagerParams.put("criteria", criteria);
		entityManagerParams.put("root", root);

		return entityManagerParams;

	}

	@SuppressWarnings({ "rawtypes" })
	public void initEntityManagerBuilderCriteriaQueryRoot(Class clazz) {
		criteriaRoot(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Root criteriaRoot(Class clazz) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(clazz);
		Root root = criteria.from(clazz);
		this.builder = builder;
		this.criteria = criteria;
		this.root = root;

		return root;
	}

	public <T> void totalCriteriaQuery(Class<T> clazz) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<T> root = criteria.from(clazz);

		this.builder = builder;
		this.criteria = criteria;
		this.root = root;

	}

	public CriteriaBuilder criteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}

	public CriteriaQuery<Long> longCriteriaQuery(CriteriaBuilder builder) {
		return builder.createQuery(Long.class);
	}

	public CriteriaQuery<Tuple> baseCiteriaQueryTuple(CriteriaBuilder builder) {
		return builder.createTupleQuery();
	}

	public <T> Root<T> from(Class<T> clazz, CriteriaQuery<Long> criteria) {
		return criteria.from(clazz);
	}

	public <T> Root<T> baseFrom(Class<T> clazz, CriteriaQuery<Tuple> criteria) {
		return criteria.from(clazz);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Root LongCriteriaQuery(Class clazz) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root root = criteria.from(clazz);
		this.builder = builder;
		this.criteria = criteria;
		this.root = root;
		return root;

	}

	public <T> String criteriaQuery(Class<T> clazz) {
		CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		return criteriaQuery.select(root).toString();
	}

	

	public <T> Long totalCount(Class<T> clazz, List<Predicate> p) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		criteria.select(builder.count(criteria.from(clazz)));

		/*
		 * if (!CollectionUtils.isEmpty(p)) {
		 *
		 * Predicate[] pArray = p.toArray(new Predicate[] {}); Predicate predicate =
		 * builder.and(pArray); criteria.where(predicate); }
		 */

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}

	public <T> Long totalCount(CriteriaBuilder builder, CriteriaQuery<Long> criteria, Root<T> root,
			List<Predicate> pConjunction) {

		criteria.select(builder.count(root));

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}

	public <T> CriteriaQuery<Tuple> baseMultiSeletCriteria(CriteriaBuilder builder, CriteriaQuery<Tuple> criteria,
			Root<T> root, Map<String, String> columnNameMap, List<Predicate> pConjunction) {

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}

		List<Selection<?>> columnSelection = new LinkedList<Selection<?>>();

		for (Map.Entry<String, String> columName : columnNameMap.entrySet()) {
			columnSelection.add(root.get(columName.getValue()));
		}

		criteria.multiselect(columnSelection);

		return criteria;

	}

	public <T> CriteriaQuery<Tuple> baseMultiSeletCriteria(CriteriaBuilder builder, CriteriaQuery<Tuple> criteria,
			Root<T> root, Map<String, String> columnNameMap, List<Predicate> pConjunction,
			List<Predicate> pDisJunction) {

		List<Predicate> pArrayJoin = new ArrayList<Predicate>();

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

		criteria.where(pArrayJoin.toArray(new Predicate[0]));

		List<Selection<?>> columnSelection = new LinkedList<Selection<?>>();

		for (Map.Entry<String, String> columName : columnNameMap.entrySet()) {

			if (columName.getKey().equals("unitNo")) {
				root.join(columName.getValue(), JoinType.LEFT);
			}

			if (columName.getKey().equals("serviceCategory")) {
				root.join(columName.getValue(), JoinType.LEFT);
			}

			if (columName.getKey().equals("rankNo")) {
				root.join(columName.getValue(), JoinType.LEFT);
			}

			columnSelection.add(root.get(columName.getValue()));
		}

		criteria.multiselect(columnSelection);

		return criteria;

	}

	public Long maxValue(CriteriaBuilder builder, CriteriaQuery<Long> criteria, Root root, List<Predicate> pConjunction,
			String columnName) {

		criteria.select(builder.max(root.get(columnName)));

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}

		Long maxVal = entityManager.createQuery(criteria).getSingleResult();

		return maxVal;

	}

	@SuppressWarnings("unchecked")
	public Date maxDate(CriteriaBuilder builder, CriteriaQuery<Date> criteria, @SuppressWarnings("rawtypes") Root root,
			List<Predicate> pConjunction, String columnName) {

		criteria.select(builder.max(root.get(columnName)));

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			Predicate predicate = builder.and(pArray);
			criteria.where(predicate);
		}

		Date maxDate = entityManager.createQuery(criteria).getSingleResult();

		return maxDate;

	}

	public <T> Long filterTotalCount(CriteriaBuilder builder, CriteriaQuery<Long> criteria, Root<T> root,
			List<Predicate> pArrayJoin) {

		criteria.select(builder.count(root));

		criteria.where(pArrayJoin.toArray(new Predicate[0]));

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}

	public <T> Long totalCount(CriteriaBuilder builder, CriteriaQuery<Long> criteria, Root<T> root,
			List<Predicate> pConjunction, List<Predicate> pDisjunction) {

		criteria.select(builder.count(root));

		List<Predicate> pArrayJoin = new ArrayList<Predicate>();

		Predicate predicateAND = null;
		Predicate predicateOR = null;

		if (!CollectionUtils.isEmpty(pConjunction)) {
			Predicate[] pArray = pConjunction.toArray(new Predicate[] {});
			predicateAND = builder.and(pArray);
		}
		if (!CollectionUtils.isEmpty(pDisjunction)) {
			Predicate[] pArray = pDisjunction.toArray(new Predicate[] {});
			predicateOR = builder.or(pArray);
		}
		if (predicateAND != null) {
			pArrayJoin.add(predicateAND);
		}
		if (predicateOR != null) {
			pArrayJoin.add(predicateOR);
		}

		criteria.where(pArrayJoin.toArray(new Predicate[0]));

		Long totalRowCount = entityManager.createQuery(criteria).getSingleResult();

		return totalRowCount;

	}

	public Field[] getClassFields(Class claz) {
		return claz.getDeclaredFields();
	}

	public String getClassFieldDataType(Field[] fields, String fieldName) {

		for (Field field : fields) {
			if (field.getName().trim().equals(fieldName.trim())) {
				return field.getType().toString();
			}
		}
		return null;
	}

	

	

	@SuppressWarnings({ "rawtypes" })
	public <T> TypedQuery typedQuery(List<Predicate> pConjunctionParam, List<Predicate> pDisJunctionParam) {

		List<Predicate> pArrayJoin = new ArrayList<Predicate>();

		List<Predicate> pConjunction = pConjunctionParam;
		List<Predicate> pDisJunction = pDisJunctionParam;

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
		criteria.where(pArrayJoin.toArray(new Predicate[0]));

		return baseTypedQuery(criteria);
	}

	public Connection getOraConnection() {
		try {

			Class.forName(env.getProperty("spring.datasource.driver-class-name"));

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");

		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
		}
		if (connection != null) {
			return connection;
		} else {
			System.out.println("Failed to make connection!");
			return null;
		}

	}

	public void finalyConStmRs(Connection con, Statement stm, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
			if (stm != null) {
				stm.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void finalyConPstmRs(Connection con, PreparedStatement pstm, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
			if (pstm != null) {
				pstm.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void finalyStmRs(Statement stm, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
			if (stm != null) {
				stm.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void finalyRs(ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] jsonArryToStringQuotsArry(JSONArray arr) {
		String[] item = new String[arr.length()];
		for (int i = 0; i < arr.length(); ++i) {
			item[i] = "'" + arr.optString(i) + "'";
		}
		return item;
	}

	@SuppressWarnings("unused")
	private String[] jsonArryToStringArry(JSONArray arr) {
		String[] item = new String[arr.length()];
		for (int i = 0; i < arr.length(); ++i) {
			item[i] = arr.optString(i);
		}
		return item;
	}

	@SuppressWarnings("unused")
	private int[] jsonArryToIntArry(JSONArray arr) {
		int[] item = new int[arr.length()];
		for (int i = 0; i < arr.length(); ++i) {
			item[i] = arr.optInt(i);
		}
		return item;
	}

	@SuppressWarnings("unused")
	private Float[] jsonArryToFloatArry(JSONArray arr) {
		Float[] item = new Float[arr.length()];
		for (int i = 0; i < arr.length(); ++i) {
			item[i] = arr.optFloat(i);
		}
		return item;
	}

	protected int batchSize() {
		return Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);
	}

	public Response findByPhotoName(String photoName) {

		String fileApiUri = env.getProperty("file.api.url");

		LOGGER.info("File api url " + fileApiUri);

		String uri = fileApiUri + "/findPhoto/" + photoName;
		RestTemplate restTemplate = new RestTemplate();

		Response photoResponse = restTemplate.getForObject(uri, Response.class);

		LOGGER.info("Photo response " + photoResponse);

		return photoResponse;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response getListFindByIdReadonly(CriteriaQuery criteria) {
		Response response = new Response();
		Object obj = null;
		try {

			obj = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getResultList();
			response.setItems((List) obj);
			return getSuccessResponse("find data Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not Found !!");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseFindByIdReadOnly(CriteriaQuery criteria) {
		Response response = new Response();
		Object obj = null;
		try {

			obj = entityManager.createQuery(criteria).setHint(QueryHints.HINT_READONLY, true).getSingleResult();
			response.setObj(obj);
			return getSuccessResponse("find data Successfully", response);
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not Found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> TypedQuery baseTypedQuery(CriteriaQuery criteria, DataTableRequest dataTableInRQ) {

		CriteriaQuery<T> select = criteria.select(root);

		TypedQuery<T> typedQuery = entityManager.createQuery(select);
		typedQuery.setFirstResult(dataTableInRQ.getStart());
		typedQuery.setMaxResults(dataTableInRQ.getLength());

		return typedQuery;
	}

	public void finallyOutputStream(ByteArrayOutputStream baos) {

		if (baos != null) {
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings({ "rawtypes" })
	public Response baseTop1(CriteriaQuery criteria) {
		Response response = new Response();
		List list = new ArrayList<>();

		response = baseTopNList(criteria, 0);

		if (response.isSuccess()) {
			list = response.getItems();

			if (list == null || list.size() <= 0) {
				return getSuccessResponse("Data Empty ");
			}

			response.setItems(null);
			response.setObj(list.get(0));

			return getSuccessResponse("find data Successfully", response);

		} else {
			return getErrorResponse("Data not found !!");
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response baseTopNList(CriteriaQuery criteria, int topN) {

		Response response = new Response();
		List list = null;
		try {
			list = entityManager.createQuery(criteria).setFirstResult(topN).setHint(QueryHints.HINT_READONLY, true)
					.getResultList();

			if (list.size() > 0) {
				response.setItems(list);
				return getSuccessResponse("Data found ", response);
			}

			return getSuccessResponse("Data Empty ");
		} catch (Exception e) {
			// TODO: handle exception
			return getErrorResponse("Data not found !!");
		}
	}

	public String findReportImg(String imageName) {
		try {
			Response res = findByAppImageInsubdir(imageName, "report_images");
			if (res.isSuccess() && res.getObj() != null) {
				return res.getObj().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String findReportHeaderImg() {
		return findReportImg("img_header.png");
	}

	public String findReportFooterImg() {
		return findReportImg("img_footer.png");
	}
	
	
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

	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ, CriteriaBuilder builder, Root root,
			Class clazz) {

		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();

		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();
			Field[] fields = getClassFields(clazz);

			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();

				String dataType = getClassFieldDataType(fields, pair.getKey());

				if (dataType != null && dataType.equals("class java.lang.String")) {
					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							CommonUtils.PERCENTAGE_SIGN + pair.getValue().toLowerCase() + CommonUtils.PERCENTAGE_SIGN));
				}

			}

		}

		return p;

	}
	

	public <T> List<Predicate> dataTablefilter(DataTableRequest<T> dataTableInRQ) {

		PaginationCriteria paginationCriteria = dataTableInRQ.getPaginationRequest();
		paginationCriteria.getFilterBy().getMapOfFilters();

		List<Predicate> p = new ArrayList<Predicate>();

		if (!paginationCriteria.isFilterByEmpty()) {
			Iterator<Entry<String, String>> fbit = paginationCriteria.getFilterBy().getMapOfFilters().entrySet()
					.iterator();

			while (fbit.hasNext()) {
				Map.Entry<String, String> pair = fbit.next();
				if (!pair.getKey().equals("ssModifiedOn")) {
					p.add(builder.like(builder.lower(root.get(pair.getKey())),
							CommonUtils.PERCENTAGE_SIGN + pair.getValue().toLowerCase() + CommonUtils.PERCENTAGE_SIGN));
				}

			}

		}

		return p;

	}
	
	
	

}
