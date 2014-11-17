package zhaopin.highend.entities.configration;

import zhaopin.highend.entities.BaseEntity;
import zhaopin.highend.utilities.IdentityAnnotation;
import zhaopin.highend.utilities.PrimaryKeyAnnotation;

public class StatisticConfig   extends BaseEntity
{
	/**
	 * ͳ��ID
	 */
	@IdentityAnnotation
	@PrimaryKeyAnnotation
	public Integer StatisticID;
	
	/**
	 * ͳ�����ID
	 */
	public Integer CategoryID;
	
	/**
	 * ͳ�����ݿ���ʽڵ�
	 */
	public String DBKey;
	
	/**
	 * ͳ������
	 */
	public String StatisticName;
	
	/**
	 * ͳ��Sql���
	 */
	public String StatisticSQL;
	
	@Override
	public String getDBKey()
	{

		return "ZhaoPin.HighEnd.ConfigDB";
	}
}
