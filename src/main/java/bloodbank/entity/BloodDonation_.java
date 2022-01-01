package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-11-11T15:59:47.760-0500")
@StaticMetamodel(BloodDonation.class)
public class BloodDonation_ extends PojoBase_ {
	public static volatile SingularAttribute<BloodDonation, BloodBank> bank;
	public static volatile SingularAttribute<BloodDonation, DonationRecord> record;
	public static volatile SingularAttribute<BloodDonation, Integer> milliliters;
	public static volatile SingularAttribute<BloodDonation, BloodType> bloodType;
}
