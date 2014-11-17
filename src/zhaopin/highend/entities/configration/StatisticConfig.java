package zhaopin.highend.entities.configration;

import zhaopin.highend.entities.BaseEntity;
import zhaopin.highend.utilities.IdentityAnnotation;
import zhaopin.highend.utilities.PrimaryKeyAnnotation;

public class StatisticConfig   extends BaseEntity
{
	/**
	 * 统计ID
	 */
	@IdentityAnnotation
	@PrimaryKeyAnnotation
	public Integer StatisticID;
	
	/**
	 * 统计类别ID
	 */
	public Integer CategoryID;
	
	/**
	 * 统计数据库访问节点
	 */
	public String DBKey;
	
	/**
	 * 统计名称
	 */
	public String StatisticName;
	
	/**
	 * 统计Sql语句
	 */
	public String StatisticSQL;
	
	@Override
	public String getDBKey()
	{

		return "ZhaoPin.HighEnd.ConfigDB";
	}
}
