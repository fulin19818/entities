package zhaopin.highend.entities.log;

import java.math.BigDecimal;
import java.util.Date;

import zhaopin.highend.entities.BaseEntity;
import zhaopin.highend.utilities.IdentityAnnotation;
import zhaopin.highend.utilities.PrimaryKeyAnnotation;


public class StatisticLog  extends BaseEntity
{
	/**
	 * ����ID
	 */
	@IdentityAnnotation
	@PrimaryKeyAnnotation
	public Integer ID;
	
	/**
	 * ͳ�����ID
	 */
	public Integer StatisticID;
	
	/**
	 * ��ֵ
	 */
	public BigDecimal	DataCount;
	
	/**
	 * ͳ������
	 */
	public Date	StatisticDate;
	
	/**
	 * ��������ʱ��
	 */
	public Date LogTime;
	
	/**
	 * SID
	 */
	public String SID;
	
	/**
	 * Site
	 */
	public String SITE;
	
	@Override
	public String getDBKey()
	{

		return "ZhaoPin.HighEnd.LogDB";
	}
}
