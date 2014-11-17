package zhaopin.highend.entities;

import zhaopin.highend.utilities.DataRow;
import zhaopin.highend.utilities.IdentityAnnotation;
import zhaopin.highend.utilities.PageListRow;
import zhaopin.highend.utilities.PrimaryKeyAnnotation;
import zhaopin.highend.utilities.SqlParameter;
import zhaopin.highend.utilities.configAdapter;
import zhaopin.highend.utilities.orm;
import zhaopin.highend.utilities.ormAnnonationType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseEntity 
{
	/**
	 * Field����ʵ�
	 */
	static Map<String,Field[]> AnnonationFiledsDic=new  HashMap<String,Field[]>();
	
	/**
	 * ��������
	 */
	static Object _locakPad=new Object();
	
	/**
	 * ��ȡ��ǰDBKey
	 * @return ���ݿ����Keyֵ
	 */
	public String getDBKey()
	{

		return null;
	}
	
	/**
	 * ɾ��
	 */
	public void delete()
	{
		Field primaryKeyField=this.getPrimaryKeyFiledByCache();
		
		SqlParameter[] paramList=new SqlParameter[1];

		String sql="Delete from "+this.getClass().getSimpleName()+" where "+primaryKeyField.getName()+"=?";
		
		try 
		{
			Object paramValue=primaryKeyField.get(this);
			
			String paramName=getFieldTypeName(primaryKeyField);
			
			paramList[0]=new SqlParameter(paramName,paramValue);
			
		} catch (IllegalArgumentException | IllegalAccessException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String dbKey=this.getDBKey();
		
		zhaopin.highend.utilities.SqlAccess access=configAdapter.getAccessByDBKey(dbKey);
		
		access.executeUpdate(sql,paramList);
		
	}
	
	
	/**
	 * ����
	 */
	public void update()
	{
		Field primaryKeyField=this.getPrimaryKeyFiledByCache();
		
		SqlParameter[] paramList=this.getUpdateSQLParameters(primaryKeyField);

		String sql=this.getUpdateSql(primaryKeyField);
		
		String dbKey=this.getDBKey();
		
		zhaopin.highend.utilities.SqlAccess access=configAdapter.getAccessByDBKey(dbKey);
		
		access.executeUpdate(sql,paramList);
		
	}
	
	
	/**
	 *  ����
	 * @return ��������ID
	 */
	public Object insert()
	{
		Field indentityField=this.getIdentityFiledByCache();
		
		SqlParameter[] paramList=getInsertSQLParameters(indentityField);
		
		String sql=getInsertSql(indentityField);
		
		String dbKey=this.getDBKey();
		
		zhaopin.highend.utilities.SqlAccess access=configAdapter.getAccessByDBKey(dbKey);
		
		String typeName=getFieldTypeName(indentityField);
		
		Object id=access.executeScalarInsert(sql, paramList,typeName);
		
		try 
		{
			indentityField.set(this, id);
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
	}
	
	/**
	 * ��ѯ��ʵ��
	 * @param id ����
	 * @return ʵ�� 
	 */
	public BaseEntity query(long id)
	{
		Field primaryKeyFiled=getPrimaryKeyFiledByCache();
		
		BaseEntity entity=null;
		
		String dbKey=this.getDBKey();
		
		zhaopin.highend.utilities.SqlAccess access=configAdapter.getAccessByDBKey(dbKey);
		
		
		
		try 
		{
			//SQLParameter
			SqlParameter[] params=getQuerySQLParameters(primaryKeyFiled,id);
			
			//SQLCommand
			String sql=this.getQuerySql(primaryKeyFiled);
			
			List<DataRow> list=access.getDataTable(sql,params);
			
			DataRow dr=list.get(0);
			
			String s=this.getClass().getName();

			entity=(BaseEntity)orm.getInstanceByDataRow(dr,s);
		} 
		catch 
		(SQLException e) 
		{
			e.printStackTrace();
		}
		
		return entity;
	}
	
	
	/**
	 * �б��ѯ
	 * @param where
	 * @param paramArray
	 * @return
	 */
	public <T> List<T> queryList(String where,Object[] paramArray)
	{

		return queryList(where,paramArray,null);
	}
	
	
	/**
	 * �б��ѯ
	 * @param where
	 * @param paramArray
	 * @return
	 */
	public <T> List<T> queryList(String where,Object[] paramArray,String orderBy)
	{
		List<T> entityList=new ArrayList<T>();
		
		String dbKey=this.getDBKey();
		
		zhaopin.highend.utilities.SqlAccess access=configAdapter.getAccessByDBKey(dbKey);
		
		
		
		try 
		{
			//SQLParameter
			SqlParameter[] params=getSQLParameters(paramArray);
			
			String tableName=this.getClass().getSimpleName();
			
			
			
			//SQLCommand
			String sql="Select * from "+tableName+" where "+where;
			
			if(orderBy!=null&&orderBy!="")
			{
				sql+=" order by "+orderBy;
				
			}
			
			List<DataRow> list=access.getDataTable(sql,params);
			
			for(DataRow row:list)
			{
				String s=this.getClass().getName();

				@SuppressWarnings("unchecked")
				T entity=(T)orm.getInstanceByDataRow(row,s);
				
				entityList.add(entity);
				
			}
			
		} 
		catch 
		(SQLException e) 
		{
			e.printStackTrace();
		}
		
		
		return entityList;
	}

	
	
	/**
	 * ��ҳ��ѯ
	 * @param where
	 * @param paramArray
	 * @return
	 */
	public <T> List<T> queryPageList(PagerParameter pageParameter,String where,String orderByPart,Object[] paramArray)
	{
		
		
		List<T> entityList=new ArrayList<T>();
		
		String dbKey=this.getDBKey();
		
		zhaopin.highend.utilities.SqlAccess access=configAdapter.getAccessByDBKey(dbKey);
		
		
		
		//SQLParameter
		SqlParameter[] params=getSQLParameters(paramArray);
		
		String tableName=this.getClass().getSimpleName();
		
		//SQLCommand
		String sql="Select * from "+tableName+" where "+where;
		
		PageListRow pageRow=access.executeTableForPage(pageParameter.PageIndex,pageParameter.PageSize , sql, params, orderByPart);
		
		pageParameter.ItemCount=pageRow.ItemCount;
		
		for(DataRow row:pageRow.RowList)
		{
			String s=this.getClass().getName();

			@SuppressWarnings("unchecked")
			T entity=(T)orm.getInstanceByDataRow(row,s);
			
			entityList.add(entity);
			
		}
		
		
		return entityList;
	}
	
	/**
	 * ��ȡ��ѯSQL
	 * @param primaryKeyField ����Field
	 * @return ��ѯSQL���
	 */
	private String getQuerySql(Field primaryKeyField)
	{
		String sql=null;
		
		String filedName=primaryKeyField.getName();
		
		String tableName=this.getClass().getSimpleName();
		
		sql="Select * from "+tableName+" where "+filedName+"=?";
		
		return sql;
		
	}
	
	/**
	 * ��ȡ��ѯSQL
	 * @param primaryKeyField ����Field
	 * @return ��ѯSQL���
	 */
	private String getInsertSql(Field indentityField)
	{
		
		Class<?> c=this.getClass();
		
		Field[] fields=c.getFields();
		
		
		
		String valueText="";
		
		String columnText="";
		
		try
		{

			for(Field field:fields)
			{
				// check the field is identity
				boolean b=field.equals(indentityField);
				
				Object paramValue=field.get(this);
				
				if(!b&&paramValue!=null)
				{
					
					if(valueText=="")
					{
						valueText="?";
					}
					else
					{
						valueText+=",?";
					}
					
					if(columnText=="")
					{
						columnText=field.getName();
					}
					else
					{
						columnText+=","+field.getName();
					}
				}
			}
		}
		catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		

		
		String sqlText="Insert into "+c.getSimpleName()+" ("+columnText+") values ("+valueText+")";
		
		return sqlText;
		
	}
	
	/**
	 * ��ȡ��ѯSQL
	 * @param primaryKeyField ����Field
	 * @return ��ѯSQL���
	 */
	private String getUpdateSql(Field primaryKeyField)
	{
		
		Class<?> c=this.getClass();
		
		Field[] fields=c.getFields();
		
		String columnText="";
		
		
		try
		{
			for(Field field:fields)
			{

				// check the field is primaryKey
				boolean b=field.equals(primaryKeyField);
				
				Object paramValue=field.get(this);
				
				if(!b&&paramValue!=null)
				{
					if(columnText=="")
					{
						columnText=field.getName()+"=?";
					}
					else
					{
						columnText+=","+field.getName()+"=?";
					}
				}
			}
		}
		catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		

		
		String sqlText="Update "+c.getSimpleName()+" Set "+columnText+" where "+primaryKeyField.getName()+"=?";
		
		return sqlText;
		
	}
	
	
	/**
	 * ��ȡ��ѯ����
	 * @param primaryKeyField ����Field
	 * @param primaryKey ����ֵ
	 * @return
	 */
	private SqlParameter[] getQuerySQLParameters(Field primaryKeyField,Object primaryKey)
	{
		SqlParameter[] params=new SqlParameter[1];

		String outputParamaterName=getFieldTypeName(primaryKeyField);
		
		
		params[0]=new SqlParameter(outputParamaterName,primaryKey);
			
	
		return params;
	}
	
	/**
	 * ���������б��ȡ�����б�
	 * @param paramArray �����б�
	 * @return �����б�
	 */
	private SqlParameter[] getSQLParameters(Object[] paramArray)
	{
		
		SqlParameter[] params=new SqlParameter[paramArray.length];
		
		int j=0;
		
		for(Object obj:paramArray)
		{

			Class<? extends Object> c=obj.getClass();
			
			String fileTypeName=getFieldTypeName(c);
			
			params[j]=new SqlParameter(fileTypeName,obj);
			
			j++;
			
		}
		
		return params;
	}
	
	/**
	 * ��ȡ�ֶ���������
	 * @param field �ֶ�
	 * @return ��������
	 */
	private static String getFieldTypeName(Field field)
	{

		Class<?> c= field.getType();
		
		return getFieldTypeName(c);
	}
	
	/**
	 * ��ȡ�ֶ���������
	 * @param field �ֶ�
	 * @return ��������
	 */
	private static String getFieldTypeName(Class<?> c)
	{

		String paramaterName=c.getName();
		
		String outputParamaterName=null;
		
		switch (paramaterName)
		{
			case "java.lang.Integer":
				outputParamaterName="int";
			break;
			case "java.lang.Long":
				outputParamaterName="bigint";
			break;
			case "java.lang.Short":
				outputParamaterName="smallint";
			break;
			case "java.lang.String":
				outputParamaterName="string";
			break;
			case "java.util.Date":
				outputParamaterName="Date";
			break;
			default:
				outputParamaterName="int";
				break;
		}
		
		return outputParamaterName;
	}
	
	/**
	 * ��ȡ��ѯ����
	 * @param primaryKeyField ����Field
	 * @param primaryKey ����ֵ
	 * @return
	 */
	private SqlParameter[] getInsertSQLParameters(Field indentityField)
	{
		Class<?> c=this.getClass();
		
		Field[] fields=c.getFields();
		
		List<SqlParameter> paramList=new ArrayList<SqlParameter>();
		
		for(Field field:fields)
		{

			try 
			{
				Object paramValue=field.get(this);
				
				// check the field is identity
				boolean b=field.equals(indentityField);
				
				if(paramValue!=null&&!b)
				{
					String parameterName=getFieldTypeName(field);
					
					SqlParameter param=new SqlParameter(parameterName,paramValue);
					
					paramList.add(param);
					
				}
			} catch 
			(IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		SqlParameter[] paramArray=new SqlParameter[paramList.size()];
		
		paramList.toArray(paramArray);
		
		
		return paramArray;
	}
	
	/**
	 * ��ȡ���²���
	 * @param primaryKeyField ����Field
	 * @param primaryKey ����ֵ
	 * @return �����б�
	 */
	private SqlParameter[] getUpdateSQLParameters(Field primaryKeyField)
	{
		Class<?> c=this.getClass();
		
		Field[] fields=c.getFields();
		
		List<SqlParameter> paramList=new ArrayList<SqlParameter>();
		
		String primaryKeyName="";
		
		Object primaryKeyValue=null;
		
		for(Field field:fields)
		{

			try 
			{
				String parameterName=getFieldTypeName(field);
				
				Object paramValue=field.get(this);
				
				// check the field is identity
				boolean b=field.equals(primaryKeyField);

				SqlParameter param=new SqlParameter(parameterName,paramValue);
				
				if(b)
				{
					primaryKeyName=parameterName;
					
					primaryKeyValue=paramValue;
				}
				
				if(paramValue!=null&&!b)
				{
					paramList.add(param);
					
				}
			} catch 
			(IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		paramList.add(new SqlParameter(primaryKeyName,primaryKeyValue));
		
		SqlParameter[] paramArray=new SqlParameter[paramList.size()];
		
		paramList.toArray(paramArray);
		
		
		return paramArray;
	}
	
	
	/**
	 * ��ȡ��ǰʵ������Filed
	 * @return ����Filed
	 */
	private Field getIdentityFiled()
	{
		Field identityField=null;
		
		Field[] filedArray=this.getClass().getDeclaredFields();
		
		if(filedArray!=null&&filedArray.length>0)
		{
			for(int i=0;i<filedArray.length;i++)
			{

				Field currentFiled=filedArray[i];
				
				Annotation[] arr=currentFiled.getAnnotations();
				
				for(int j=0;j<arr.length;j++)
				{
					Annotation currentAnno=arr[j];
					
					if(currentAnno instanceof IdentityAnnotation)
					{
						return currentFiled;
						
					}
				}
			}
			
		}
		
		return identityField;
	}
	
	/**
	 * �ӻ����л�ȡ��ǰʵ������Filed
	 * @return ����Filed
	 */
	private Field getIdentityFiledByCache()
	{
		String className=this.getClass().getName();
		
		String identityFieldName=className+"_"+ormAnnonationType.Identity.toString();
		
		if(!AnnonationFiledsDic.containsKey(identityFieldName))
		{
			synchronized(_locakPad)
			{
				if(!AnnonationFiledsDic.containsKey(identityFieldName))
				{
					Field indentityField=this.getIdentityFiled();
					
					if(indentityField!=null)
					{
						Field[] fileds=new Field[1];
						
						fileds[0]=indentityField;
						
						AnnonationFiledsDic.put(identityFieldName, fileds);
						
					}
					
				}
				
			}
			
		}
		
		if(AnnonationFiledsDic.containsKey(identityFieldName))
		{
			return AnnonationFiledsDic.get(identityFieldName)[0];
		}
		
		return null;
		
	}

	/**
	 * ��ȡ��ǰʵ������Filed
	 * @return ����Filed
	 */
	private Field getPrimaryKeyFiled()
	{
		Field primaryKeyFiled=null;
		
		Field[] filedArray=this.getClass().getDeclaredFields();
		
		if(filedArray!=null&&filedArray.length>0)
		{
			for(int i=0;i<filedArray.length;i++)
			{

				Field currentFiled=filedArray[i];
				
				Annotation[] arr=currentFiled.getAnnotations();
				
				for(int j=0;j<arr.length;j++)
				{
					Annotation currentAnno=arr[j];
					
					if(currentAnno instanceof PrimaryKeyAnnotation)
					{
						System.out.println(currentFiled.getName());
						return currentFiled;
						
					}
				}
			}
			
		}
		
		return primaryKeyFiled;
	}
	

	/**
	 * �ӻ����л�ȡ��ǰʵ������Filed
	 * @return ����Filed
	 */
	private Field getPrimaryKeyFiledByCache()
	{
		String className=this.getClass().getName();
		
		String primaryKey=className+"_"+ormAnnonationType.PrimaryKey.toString();
		
		if(!AnnonationFiledsDic.containsKey(primaryKey))
		{
			synchronized(_locakPad)
			{
				if(!AnnonationFiledsDic.containsKey(primaryKey))
				{
					Field parimaryKeyFiled=getPrimaryKeyFiled();
					
					if(parimaryKeyFiled!=null)
					{
						Field[] fileds=new Field[1];
						
						fileds[0]=parimaryKeyFiled;
						
						AnnonationFiledsDic.put(primaryKey, fileds);
						
					}
					
				}
				
			}
			
		}
		
		if(AnnonationFiledsDic.containsKey(primaryKey))
		{
			return AnnonationFiledsDic.get(primaryKey)[0];
		}
		
		return null;
		
	}
	
	

}
