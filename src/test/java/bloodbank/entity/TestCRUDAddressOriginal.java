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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
public class TestCRUDAddressOriginal extends JUnitBase {
	
	private EntityManager em;
	private EntityTransaction et;
	
	private static Address address;
	private static final String STREET_NUMBER = "123";
	private static final String STREET = "Rue Chambly";
	private static final String CITY = "Montreal";
	private static final String PROVINCE = "Quebec";
	private static final String COUNTRY = "Canada";
	private static final String ZIPCODE = "H2E 2S1";
	
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
		Root< Address> root = query.from( Address.class);
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
		address = new Address();
		address.setStreetNumber(STREET_NUMBER);
		address.setStreet(STREET);
		address.setCity(CITY);
		address.setProvince(PROVINCE);
		address.setCountry(COUNTRY);
		address.setZipcode(ZIPCODE);
		em.persist(address);
		et.commit();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query = builder.createQuery( Long.class);
		// select count(a) from Address a where a.id=:id
		Root< Address> root = query.from( Address.class);
		query.select( builder.count( root));
		query.where( builder.equal( root.get( Address_.streetNumber), builder.parameter( String.class, "streetNumber")));
		// create query and set the parameter
		TypedQuery< Long> tq = em.createQuery( query);
		tq.setParameter( "streetNumber", address.getStreetNumber());
		// get the result as row count
		long result = tq.getSingleResult();

		
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}
	
	@Order(3)
	@Test
	void testCreateValid() {
		et.begin();
		Address add = new Address();
		add.setStreetNumber("12345678901231");
		add.setStreet(STREET);
		add.setCity(CITY);
		add.setProvince(PROVINCE);
		add.setCountry(COUNTRY);
		add.setZipcode(ZIPCODE);
		// expected to be error because street_number can only take 10 characters
		assertThrows( PersistenceException.class, () -> em.persist( add));
		et.commit();
	}
	
	@Order(4)
	@Test
	void testRead() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Address> query = builder.createQuery(Address.class);
		// select c from contact c
		Root<Address> root = query.from(Address.class);
		query.select( root);
		// create query and set the parameter
		TypedQuery<Address> tq = em.createQuery( query);
		// get the result as row count
		List< Address> add = tq.getResultList();
		
		assertThat( add, contains( equalTo( address)));
	}
	
	@Order(5)
	@Test
	void testUpdate() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Address> query = builder.createQuery( Address.class);
		// select a from Address a
		Root< Address> root = query.from( Address.class);
		query.select( root);
		query.where( builder.equal( root.get( Address_.streetNumber), builder.parameter( String.class, "streetNumber")));
		// create query and set the parameter
		TypedQuery< Address> tq = em.createQuery( query);
		tq.setParameter( "streetNumber", address.getStreetNumber());
		// get the result as row count
		Address returnAddress = tq.getSingleResult();
		
		String newStreet = "Rue Allard";
		String newCity = "Toronto";
		
		et.begin();
		returnAddress.setStreet(newStreet);
		returnAddress.setCity(newCity);
		em.merge(returnAddress);
		et.commit();
		
		returnAddress = tq.getSingleResult();

		assertThat( returnAddress.getStreet(), equalTo(newStreet));
		assertThat( returnAddress.getCity(), equalTo(newCity));
	}
	
	@Order(6)
	@Test
	void testDelete() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Address> query = builder.createQuery( Address.class);
		// select a from Address a
		Root< Address> root = query.from( Address.class);
		query.select( root);
		query.where( builder.equal( root.get( Address_.streetNumber), builder.parameter( String.class, "streetNumber")));
		// create query and set the parameter
		TypedQuery< Address> tq = em.createQuery( query);
		tq.setParameter( "streetNumber", address.getStreetNumber());
		// get the result as row count
		Address returnAddress = tq.getSingleResult();
		et.begin();
		em.remove(returnAddress);
		et.commit();
		
		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query2 = builder2.createQuery( Long.class);
		// select count(c) from Address c
		Root< Address> root2 = query2.from( Address.class);
		query2.select( builder2.count( root2));
		// create query and set the parameter
		TypedQuery< Long> tq2 = em.createQuery( query2);
		// get the result as row count
		long result = tq2.getSingleResult();

		assertThat( result, is( comparesEqualTo( 0L)));
		
		
	}

}

























