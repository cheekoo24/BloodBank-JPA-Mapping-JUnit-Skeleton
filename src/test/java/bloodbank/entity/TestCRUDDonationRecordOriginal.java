package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;


import common.JUnitBase;

@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
public class TestCRUDDonationRecordOriginal extends JUnitBase {
	
	private EntityManager em;
	private EntityTransaction et;
	
	private static DonationRecord record;
	private static Person person;
	private static BloodDonation donation;
	private static BloodBank bank;
	private static BloodType type;
	
	@BeforeAll
	static void setupAllInit() {
		person = new Person();
		person.setFullName("Herman" , "Redona");
		
		bank = new PrivateBloodBank();
		bank.setName("Private Blood Bank");
		
		type = new BloodType();
		type.setType("A", "+");
		
		donation = new BloodDonation();
		donation.setBloodType(type);
		donation.setMilliliters(10);
		donation.setBank(bank);
	}
	
	@BeforeEach
	void setup() {
		em = getEntityManager();
		et = em.getTransaction();
	}
	
	@AfterEach
	void tearDown() {
		em.clear();
	}
	
	@Order( 1)
	@Test
	void testEmpty() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query = builder.createQuery( Long.class);
		// select count(c) from Contact c
		Root< DonationRecord> root = query.from( DonationRecord.class);
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
		record = new DonationRecord();
		record.setOwner(person);
		record.setTested(true);
		record.setDonation(donation);
		em.persist(record);
		et.commit();
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for long as we need the number of found rows
		CriteriaQuery< Long> query = builder.createQuery( Long.class);
		// select count(c) from Contact c where c.id=:id
		Root< DonationRecord> root = query.from( DonationRecord.class);
		query.select( builder.count( root));
		query.where( builder.equal( root.get( DonationRecord_.donation), builder.parameter( BloodDonation.class, "donation")));
		// create query and set the parameter
		TypedQuery< Long> tq = em.createQuery( query);
		tq.setParameter( "donation", record.getDonation());
		// get the result as row count
		long result = tq.getSingleResult();

		// there should only be one row in the DB
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}

}














