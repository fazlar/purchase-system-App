package com.purchaseSystem.itemDtl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.purchaseSystem.base.BaseRepository;
import com.purchaseSystem.util.Def;
import com.purchaseSystem.util.Response;

@Repository
@Transactional
public class PurItemdtlRepository extends BaseRepository {

	public static String getStockQtyStaement(Long Id) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SUM(Qty) AS stockQty FROM pur_item_dtl ");
		sql.append(" WHERE item_id =" + Id + "");
System.out.println(sql.toString());
		return sql.toString();

	}

	public Response getStockQty(String reobj) throws SQLException {
		System.out.println(reobj);
		JSONObject json = new JSONObject(reobj);
		Long id = Def.getLong(json, "id");
		Response res = new Response();
		Connection con = null;
		ResultSet rs = null;
		Statement stm = null;
		Long total = 0l;
		con = getOraConnection();

		try {
			stm = con.createStatement();
			rs = stm.executeQuery(getStockQtyStaement(id));

			while (rs.next()) {
				System.out.println("true");
				total = rs.getLong("stockQty");
				System.out.println("true");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stm != null) {
				stm.close();
			}
			if (con != null) {
				con.close();
			}
		}
		res.setId(total);
		return getSuccessResponse("Medicine List Found", res);
	}

	public Response save(String reqObj) {
		PurItemDtlEntity PurItemDtlEntity = objectMapperReadValue(reqObj, PurItemDtlEntity.class);

		PurItemDtlEntity lastSavedGrade = new PurItemDtlEntity();

		for (int i = 6; i >= 1; i--) {

			PurItemDtlEntity grade = new PurItemDtlEntity();
//			Double basicSalary = i == 6 ? PurItemDtlEntity.getBasic() : lastSavedGrade.getBasic() + ADD_AMOUNT;
//			Long maxPost = (i == 1 || i == 2 ? 1l : 2l);
//
//			grade.setMaxPost(maxPost);
//			grade.setBasic(basicSalary);
//			grade.setName("Grade - " + i);
//
//			Double houseRent = (basicSalary / 100) * HOUSE_RENT;
//			Double medical = (basicSalary / 100) * MEDICAL_ALLOWANCE;
//			Double salary = basicSalary + houseRent + medical;

//			grade.setHouseRent(houseRent);
//			grade.setMedical(medical);
//			grade.setSalary(salary);

			Response res = baseOnlySave(grade);

			lastSavedGrade = getValueFromObject(res.getObj(), PurItemDtlEntity.class);

		}

		return getSuccessResponse("Update Successfully");

	}

	public Response list(String reqObj) {
		PurItemDtlEntity billsBToBLabPolicyObj = null;
		if (null != reqObj) {
			billsBToBLabPolicyObj = objectMapperReadValue(reqObj, PurItemDtlEntity.class);
		}

		Response res = baseList(criteriaQuery(billsBToBLabPolicyObj));

//		if (res.isSuccess() && !CollectionUtils.isEmpty(res.getItems())) {
//			List<PurItemDtlEntity> grades = (List<PurItemDtlEntity>) getValueFromObject(res.getItems(), PurItemDtlEntity.class);
//			Collections.sort(grades, (o1, o2) -> o1.getId().compareTo(o2.getId()));
//			Response finalRes = new Response();
//			finalRes.setItems(grades);
//			return getSuccessResponse("Data Found !.", finalRes);
//		}

		return getErrorResponse("Data Not Found!");
	}

	public Response getAll() {
		PurItemDtlEntity billsBToBLabPolicyObj = new PurItemDtlEntity();
		return baseList(criteriaQuery(billsBToBLabPolicyObj));
	}

	public Response delete(Long id) {

		PurItemDtlEntity billsBToBLabPolicyObj = findById(id);
		if (billsBToBLabPolicyObj == null) {
			return getErrorResponse("Record not found!");
		}
		return baseDelete(billsBToBLabPolicyObj);

	}

	public PurItemDtlEntity findById(Long id) {
		PurItemDtlEntity PurItemDtlEntity = new PurItemDtlEntity();
		PurItemDtlEntity.setId(id);
		Response response = baseFindById(criteriaQuery(PurItemDtlEntity));
		if (response.isSuccess()) {
			return getValueFromObject(response.getObj(), PurItemDtlEntity.class);
		}
		return null;
	}

	// Non API
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private CriteriaQuery criteriaQuery(PurItemDtlEntity filter) {
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
	private List<Predicate> criteriaCondition(PurItemDtlEntity filter, CriteriaBuilder builder,
			Root<PurItemDtlEntity> root) {
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

		}
		return p;
	}

	@SuppressWarnings("rawtypes")
	private void init() {
		initEntityManagerBuilderCriteriaQueryRoot(PurItemDtlEntity.class);
		@SuppressWarnings("unused")
		CriteriaBuilder builder = super.builder;
		@SuppressWarnings("unused")
		CriteriaQuery criteria = super.criteria;
		@SuppressWarnings("unused")
		Root root = super.root;
	}

}
