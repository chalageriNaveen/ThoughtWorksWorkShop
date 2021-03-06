package com.library.inventoryManagement.IVM;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class InventoryManagementDAO {
	public void addInventoryManagement(InventoryManagement bean) {
		Session session = SessionUtil.getSession();
		Transaction tx = session.beginTransaction();
		addInventoryManagement(session, bean);
		tx.commit();
		session.close();
	}

	private void addInventoryManagement(Session session, InventoryManagement bean) {
		InventoryManagement ivmbean = new InventoryManagement();
		ivmbean.setIsbn(bean.getIsbn());
		ivmbean.setShelfNumber(bean.getShelfNumber());
		ivmbean.setQuantity(bean.getQuantity());
		session.save(ivmbean);
	}

	public InventoryManagement serachInventory(String isbn) {

		Session session = SessionUtil.getSession();
		Query query = session.createQuery("from InventoryManagement where isbn = :isbn ");
		query.setString("isbn", isbn);
		List<InventoryManagement> books = query.list();
		if (books.size() == 0) {
			return null;
		}
		session.close();
		return books.get(0);
	}

	public int updateInventoryManagement(String isbn, String status) {
		int quantity = 0;
		Session session = SessionUtil.getSession();
		Query getQuery = session.createQuery("from InventoryManagement where isbn = :isbn ");
		getQuery.setString("isbn", isbn);
		InventoryManagement ivm = (InventoryManagement) getQuery.list().get(0);
		quantity = ivm.getQuantity();
		if (status != null && status.equals("book")) {
			quantity = quantity - 1;
		} else if (status != null && status.equals("return")) {
			quantity = quantity + 1;
		}
		Transaction tx = session.beginTransaction();
		String hql = "update InventoryManagement set quantity = :quantity where isbn = :isbn";
		Query updateQuery = session.createQuery(hql);
		updateQuery.setInteger("quantity", quantity);
		updateQuery.setString("isbn", isbn);
		int rowCount = updateQuery.executeUpdate();
		System.out.println("Rows affected: " + rowCount);
		tx.commit();
		session.close();
		return rowCount;
	}

	public int deleteInventoryManagement(String isbn) {
		Session session = SessionUtil.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete from InventoryManagement where isbn = :isbn";
		Query query = session.createQuery(hql);
		query.setString("isbn", isbn);
		int rowCount = query.executeUpdate();
		System.out.println("Rows affected: " + rowCount);
		tx.commit();
		session.close();
		return rowCount;
	}
}
