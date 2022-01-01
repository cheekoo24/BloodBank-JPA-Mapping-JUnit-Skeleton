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

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;


import common.JUnitBase;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
public class TestCRUDPersonOriginal extends JUnitBase {
	
	private EntityManager em;
	private EntityTransaction et;
	
	private static Person person;
	private static final String FIRST_NAME = "Herman";
	private static final String LAST_NAME = "Redona";
	
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
		Root< Person> root = query.from( Person.class);
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
		person = new Person();
		person.setFirstName(FIRST_NAME);
		person.setLastName(LAST_NAME);
		em.persist(person);
		et.commit();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query = builder.createQuery( Long.class);
		// select count(a) from Address a where a.id=:id
		Root< Person> root = query.from( Person.class);
		query.select( builder.count( root));
		query.where( builder.equal( root.get( Person_.firstName), builder.parameter( String.class, "firstName")));
		// create query and set the parameter
		TypedQuery< Long> tq = em.createQuery( query);
		tq.setParameter( "firstName", person.getFirstName());
		// get the result as row count
		long result = tq.getSingleResult();
		
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}
	
	@Order(3)
	@Test
	void testCreateValid() {
		et.begin();
		Person per = new Person();
		per.setFirstName(null);
		per.setLastName(LAST_NAME);
		
		// first name is not nullable this should be error
		assertThrows( PersistenceException.class, () -> em.persist( per));
		et.commit();
	}
	
	@Order(4)
	@Test
	void testRead() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Person> query = builder.createQuery(Person.class);
		// select c from contact c
		Root<Person> root = query.from(Person.class);
		query.select( root);
		// create query and set the parameter
		TypedQuery<Person> tq = em.createQuery( query);
		// get the result as row count
		List< Person> add = tq.getResultList();
		
		assertThat( add, contains( equalTo( person)));
	}
	
	@Order(5)
	@Test
	void testUpdate() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Person> query = builder.createQuery( Person.class);
		// select a from Address a
		Root< Person> root = query.from( Person.class);
		query.select( root);
		query.where( builder.equal( root.get( Person_.firstName), builder.parameter( String.class, "firstName")));
		// create query and set the parameter
		TypedQuery< Person> tq = em.createQuery( query);
		tq.setParameter( "firstName", person.getFirstName());
		// get the result as row count
		Person returnPerson = tq.getSingleResult();
		
		String newLastName = "Rivera";
		
		et.begin();
		returnPerson.setLastName(newLastName);
		em.merge(returnPerson);
		et.commit();
		
		returnPerson = tq.getSingleResult();
		
		assertThat( returnPerson.getLastName(), equalTo(newLastName));
	}
	
	@Order(6)
	@Test
	void testDelete() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Address
		CriteriaQuery< Person> query = builder.createQuery( Person.class);
		// select a from Phone a
		Root< Person> root = query.from( Person.class);
		query.select( root);
		query.where( builder.equal( root.get( Person_.firstName), builder.parameter( String.class, "firstName")));
		// create query and set the parameter
		TypedQuery< Person> tq = em.createQuery( query);
		tq.setParameter( "firstName", person.getFirstName());
		// get the result as row count
		Person returnPerson = tq.getSingleResult();
		et.begin();
		em.remove(returnPerson);
		et.commit();
		
		CriteriaBuilder builder2 = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query2 = builder2.createQuery( Long.class);
		// select count(c) from Address c
		Root< Person> root2 = query2.from( Person.class);
		query2.select( builder2.count( root2));
		// create query and set the parameter
		TypedQuery< Long> tq2 = em.createQuery( query2);
		// get the result as row count
		long result = tq2.getSingleResult();

		assertThat( result, is( comparesEqualTo( 0L)));
	}
}




























