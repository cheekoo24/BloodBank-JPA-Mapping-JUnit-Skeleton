package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-11-11T16:07:29.792-0500")
@StaticMetamodel(BloodBank.class)
public class BloodBank_ extends PojoBase_ {
	public static volatile SingularAttribute<BloodBank, String> name;
	public static volatile SetAttribute<BloodBank, BloodDonation> donations;
}
