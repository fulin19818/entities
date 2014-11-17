package zhaopin.highend.entities.log;

import java.math.BigDecimal;
import java.util.Date;

import zhaopin.highend.entities.BaseEntity;
import zhaopin.highend.utilities.IdentityAnnotation;
import zhaopin.highend.utilities.PrimaryKeyAnnotation;


public class StatisticLog  extends BaseEntity
{
	/**
	 * 自增ID
	 */
	@IdentityAnnotation
	@PrimaryKeyAnnotation
	public Integer ID;
	
	/**
	 * 统计类别ID
	 */
	public Integer StatisticID;
	
	/**
	 * 数值
	 */
	public BigDecimal	DataCount;
	
	/**
	 * 统计日期
	 */
	public Date	StatisticDate;
	
	/**
	 * 数据生成时间
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
