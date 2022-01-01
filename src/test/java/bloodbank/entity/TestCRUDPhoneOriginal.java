package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterEach;

import common.JUnitBase;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
public class TestCRUDPhoneOriginal extends JUnitBase {
	
	private EntityManager em;
	private EntityTransaction et;
	
	private static Phone phone;
	private static final String COUNTRY_CODE = "1";
	private static final String AREA_CODE = "438";
	private static final String NUMBER = "231-2314";

	@BeforeEach
	void setupAllInit() {
		em = getEntityManager();
		et = em.getTransaction();
	}
	
	@AfterEach
	void tearDown() {
		em.close();
	}
	
	@Order(1)
	@Test
	void testEmpty() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query = builder.createQuery( Long.class);
		// select count(c) from Address c
		Root< Phone> root = query.from( Phone.class);
		query.select( builder.count( root));
		// create query and set the parameter
		TypedQuery< Long> tq = em.createQuery( query);
		// get the result as row count
		long result = tq.getSingleResult();

		assertThat( result, is( comparesEqualTo( 0L)));
	}
	
	@Order(2)
	@Test
	void testCreate() {
		et.begin();
		phone = new Phone();
		phone.setCountryCode(COUNTRY_CODE);
		phone.setAreaCode(AREA_CODE);
		phone.setNumber(NUMBER);
		em.persist(phone);
		et.commit();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query = builder.createQuery( Long.class);
		// select count(a) from Phone p where p.id=:id
		Root< Phone> root = query.from( Phone.class);
		query.select( builder.count( root));
		query.where( builder.equal( root.get( Phone_.areaCode), builder.parameter( String.class, "areaCode")));
		// create query and set the parameter
		TypedQuery< Long> tq = em.createQuery( query);
		tq.setParameter( "areaCode", phone.getAreaCode());
		// get the result as row count
		long result = tq.getSingleResult();

		
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}
	
	@Order(3)
	@Test
	void testCreateValid() {
		et.begin();
		Phone cp = new Phone();
		cp.setCountryCode("12345678901010");
		cp.setAreaCode(AREA_CODE);
		cp.setNumber(NUMBER);
		// should be error since country code characters should be only 10
		assertThrows( PersistenceException.class, () -> em.persist( cp));
		et.commit();
	}
	
	@Order(4)
	@Test
	void testRead() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Phone> query = builder.createQuery(Phone.class);
		// select c from contact c
		Root<Phone> root = query.from(Phone.class);
		query.select( root);
		// create query and set the parameter
		TypedQuery<Phone> tq = em.createQuery( query);
		// get the result as row count
		List< Phone> cp = tq.getResultList();
		
		assertThat( cp, contains( equalTo( phone)));
	}
	
	@Order(5)
	@Test
	void testUpdate() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Phone
		CriteriaQuery< Phone> query = builder.createQuery( Phone.class);
		// select p from Phone p
		Root< Phone> root = query.from( Phone.class);
		query.select( root);
		query.where( builder.equal( root.get( Phone_.areaCode), builder.parameter( String.class, "areaCode")));
		// create query and set the parameter
		TypedQuery< Phone> tq = em.createQuery( query);
		tq.setParameter( "areaCode", phone.getAreaCode());
		// get the result as row count
		Phone returnPhone = tq.getSingleResult();
		
		String newCountryCode = "5";
		String newNumber = "555-5555";
		
		et.begin();
		returnPhone.setCountryCode(newCountryCode);
		returnPhone.setNumber(newNumber);
		em.merge(returnPhone);
		et.commit();
		
		returnPhone = tq.getSingleResult();

		assertThat( returnPhone.getCountryCode(), equalTo(newCountryCode));
		assertThat( returnPhone.getNumber(), equalTo(newNumber));
	
	}
	
	@Order(6)
	@Test
	void testDelete() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Phone
		CriteriaQuery< Phone> query = builder.createQuery( Phone.class);
		// select p from Phone p
		Root< Phone> root = query.from( Phone.class);
		query.select( root);
		query.where( builder.equal( root.get( Phone_.areaCode), builder.parameter( String.class, "areaCode")));
		// create query and set the parameter
		TypedQuery< Phone> tq = em.createQuery( query);
		tq.setParameter( "areaCode", phone.getAreaCode());
		// get the result as row count
		Phone returnPhone = tq.getSingleResult();
		et.begin();
		em.remove(returnPhone);
		et.commit();
		
		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query2 = builder2.createQuery( Long.class);
		// select count(c) from Address c
		Root< Phone> root2 = query2.from( Phone.class);
		query2.select( builder2.count( root2));
		// create query and set the parameter
		TypedQuery< Long> tq2 = em.createQuery( query2);
		// get the result as row count
		long result = tq2.getSingleResult();

		assertThat( result, is( comparesEqualTo( 0L)));
	}
}

























