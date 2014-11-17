package zhaopin.highend.entities;

import zhaopin.highend.utilities.IdentityAnnotation;
import zhaopin.highend.utilities.PrimaryKeyAnnotation;



public class EmailList extends BaseEntity
{

	@IdentityAnnotation
	@PrimaryKeyAnnotation
	public Integer ID;
	
	public String EmailAddress;
	
	public Integer SeekerUserID;
	
	public Short	EmailType;
	
	public Short EmailStatus;
	
	@Override
	public String getDBKey()
	{

		return "ZhaoPin.HighEnd.SeekerDB";
	}
}
