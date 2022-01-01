package bloodbank.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

/**
 * The persistent class for the person database table.
 */

//TODO PR01 - add the missing annotations.
//TODO PR02 - do we need a mapped super class? which one?
@Entity(name = "Person")
@Table(name = "person")
@NamedQuery( name = "Person.findAll", query = "SELECT p FROM Person p")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Person extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO PR03 - add missing annotations.
	@Basic(optional = false)
	@Column(name = "first_name", length = 50)
	private String firstName;

	// TODO PR04 - add missing annotations.
	@Basic(optional = false)
	@Column(name = "last_name", length = 50)
	private String lastName;

	// TODO PR05 - add annotations for 1:M relation. insertable, updatable are
	// false. remove should not cascade.
	@OneToMany(mappedBy="owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@Column(insertable = false, updatable = false)
	private Set<DonationRecord> donations = new HashSet<>();

	// TODO PR06 - add annotations for 1:M relation. insertable, updatable are
	// false. remove should not cascade.
	@OneToMany(mappedBy="owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@Column(insertable = false, updatable = false)
	private Set<Contact> contacts = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<DonationRecord> getDonations() {
		return donations;
	}

	public void setDonations(Set<DonationRecord> donations) {
		this.donations = donations;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + Objects.hash(getFirstName(), getLastName());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(getClass() == obj.getClass() || Hibernate.getClass(obj) == getClass()))
			return false;
		Person other = (Person) obj;
		return Objects.equals(getFirstName(), other.getFirstName())
				&& Objects.equals(getLastName(), other.getLastName());
	}

}