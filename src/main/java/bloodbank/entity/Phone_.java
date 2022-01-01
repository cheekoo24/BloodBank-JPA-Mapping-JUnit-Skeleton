package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-11-11T12:02:07.584-0500")
@StaticMetamodel(Phone.class)
public class Phone_ extends PojoBase_ {
	public static volatile SingularAttribute<Phone, String> areaCode;
	public static volatile SingularAttribute<Phone, String> countryCode;
	public static volatile SingularAttribute<Phone, String> number;
	public static volatile SetAttribute<Phone, Contact> contacts;
}
