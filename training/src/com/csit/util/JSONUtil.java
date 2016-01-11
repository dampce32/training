package com.csit.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @Description: 处理json数据的工具类
 * @Copyright: 福州骏华信息有限公司 (c)2012
 * @Created Date : 2012-10-27
 * @author lys
 * @vesion 1.0
 */
public class JSONUtil{
	public static final String EMPTYJSON = "{\"rows\":[],\"total\":0}";
	public static final String EMPTY_COMBOBOX_JSON = "[]";
	/**
	 * @Description: 将List型数据转化成Json数据
	 * @Create: 2012-10-27 上午10:26:14
	 * @author lys
	 * @update logs
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String toJson(List list){
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}
	
	/**
	 * @Description: 将List型数据转化成Json数据,并指定要选取的属性 
	 * @Create: 2012-10-27 上午10:48:13
	 * @author lys
	 * @update logs
	 * @param list
	 * @param filterList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String toJsonWithoutRows(List list,List<String> propertyList){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new JSONPropertyFilter(propertyList));
		JSONArray jsonArray = JSONArray.fromObject(list, jsonConfig);
		String result = jsonArray.toString();
		return result;
	}
	/**
	 * @Description: 将List型数据转化成Json数据,并指定要选取的属性 
	 * @Create: 2012-10-27 上午10:48:13
	 * @author lys
	 * @update logs
	 * @param list
	 * @param filterList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String toJson(List list,List<String> propertyList){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new JSONPropertyFilter(propertyList));
		JSONArray jsonArray = JSONArray.fromObject(list, jsonConfig);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", jsonArray);
		String result = JSONObject.fromObject(map).toString();
		return result;
	}
	
	
	/**
	 * @Description:  将List型数据转化成Json数据,并指定要选取的属性 ( easyui datagrid中使用)
	 * @Create: 2012-10-27 上午10:50:45
	 * @author lys
	 * @update logs
	 * @param list
	 * @param propertyList
	 * @param total
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String toJson(List list,List<String> propertyList,Long total){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new JSONPropertyFilter(propertyList));
		JSONArray jsonArray = JSONArray.fromObject(list, jsonConfig);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", jsonArray);
		String result = JSONObject.fromObject(map).toString();
		return result;
	}
	/**
	 * @Description: 将List型数据转化成Json数据,并指定要选取的属性 ( easyui datagrid中使用)
	 * @Create: 2012-10-28 上午9:25:48
	 * @author lys
	 * @update logs
	 * @param list
	 * @param total
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String toJson(List list,Long total){
		JSONArray jsonArray = JSONArray.fromObject(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", jsonArray);
		String result = JSONObject.fromObject(map).toString();
		return result;
	}
	/**
	 * @Description: 取得list的json字符串(带rows)
	 * @Create: 2012-12-29 下午6:23:29
	 * @author lys
	 * @update logs
	 * @param list
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toJson(List list,String[] properties){
		JSONObject object = new JSONObject();
		object.put("rows", toJsonWithoutRows(list,properties));
		return object.toString();
	}
	/**
	 * @Description: 取得list的json字符串(不带rows)
	 * @Create: 2012-12-29 下午6:24:53
	 * @author lys
	 * @update logs
	 * @param list
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toJsonWithoutRows(List list,String[] properties){
		JSONArray array = new JSONArray();
		for (Object t : list) {
			array.add(toJSONObject(t, properties));
		}
		return array.toString();
	}
	/**
	 * @Description: 取得list的json字符串(带rows+total)
	 * @Create: 2012-12-29 下午6:26:43
	 * @author lys
	 * @update logs
	 * @param list
	 * @param properties
	 * @param total
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String toJson(List list, String[] properties,
			Long total) {
		JSONObject object = new JSONObject();
		String rows = JSONUtil.toJsonWithoutRows(list, properties);
		object.put("rows", rows);
		object.put("total", total);
		return object.toString();
	}
	/**
	 * @Description: 取得对象的json字符串
	 * @Create: 2013-1-8 下午10:35:55
	 * @author lys
	 * @update logs
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	public static String toJson(Object object,String[] properties){
		return toJSONObject(object,properties).toString();
	}
	/**
	 * @Description: 转化成JSONObject
	 * @Create: 2013-1-8 下午11:07:46
	 * @author lys
	 * @update logs
	 * @param object
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static JSONObject toJSONObject(Object object,String[] properties){
		JSONObject item = new JSONObject();
		try {
			for(String property : properties){
				/*
				 * 处理property 
				 * property的规则是:
				 * 如果包含有：这说明指定了key的别名
				 * 
				 * 如果没有则key的名称，要最后一个.后的名称为key
				 */
				String key = null;
				if(property.contains(":")){
					 key = StringUtils.substringAfter(property, ":");
					 property =  StringUtils.substringBefore(property, ":");
				}else{
					if(property.contains(".")){
						 key =  StringUtils.substringAfterLast(property, ".");
					}else{
						key = property;
					}
				}
				if(property.contains(".")){
					String propertyPrefix = "";
					String propertySuffix = property;
					
					boolean canGetProperty = true;
					/*
					 * 判读.前的属性是否为null，如果为null，则不能读取该属性
					 * 如果不为null，则需截取.后的属性，继续相同判断，直到最后截取的属性中不含有.
					 */
					String haveTestStr = "";
					while(propertySuffix.contains(".")){
						propertyPrefix = StringUtils.substringBefore(propertySuffix, ".");
						if(BeanUtils.getProperty(object, haveTestStr+propertyPrefix)==null){
							canGetProperty = false;
							break;
						}
						haveTestStr+=propertyPrefix+".";;
						propertySuffix = StringUtils.substringAfter(propertySuffix, ".");
					}
					if(canGetProperty){
						item.put(key, BeanUtils.getProperty(object, property));
					}
				}else{
					item.put(key, BeanUtils.getProperty(object, property));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * @Description: 将List<Map>数据转化成json数据
	 * @Create: 2013-1-11 下午11:04:41
	 * @author lys
	 * @update logs
	 * @param listMap
	 * @return
	 */
	public static String toJsonFromListMap(List<Map<String, Object>> listMap) {
		if(listMap==null||listMap.size()==0){
			return EMPTYJSON;
		}
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject item = null;
		Set<String> keySet = listMap.get(0).keySet();
		try {
			for (Map<String,Object> map : listMap) {
				item = new JSONObject();
				Object value = null;
				for(String key : keySet){
					value = map.get(key);
					item.put(key, value);
				}
				array.add(item);
			}
			object.put("rows", array);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString();
	}
	/**
	 * @Description: 将List<Map>数据转化成json数据(不带rows)
	 * @Create: 2013-1-11 下午11:04:41
	 * @author lys
	 * @update logs
	 * @param listMap
	 * @return
	 */
	public static String toJsonFromListMapWithOutRows(List<Map<String, Object>> listMap) {
		if(listMap==null||listMap.size()==0){
			return EMPTYJSON;
		}
		JSONArray array = new JSONArray();
		JSONObject item = null;
		Set<String> keySet = listMap.get(0).keySet();
		try {
			for (Map<String,Object> map : listMap) {
				item = new JSONObject();
				Object value = null;
				for(String key : keySet){
					value = map.get(key);
					item.put(key, value);
				}
				array.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array.toString();
	}
	
	/**
	 * @Description: 将List<Map> total数据转化成json数据
	 * @Create: 2013-1-11 下午11:04:41
	 * @author lys
	 * @update logs
	 * @param listMap
	 * @return
	 */
	public static String toJsonFromListMap(List<Map<String, Object>> listMap,Long total) {
		if(listMap==null||listMap.size()==0){
			return EMPTYJSON;
		}
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject item = null;
		Set<String> keySet = listMap.get(0).keySet();
		try {
			for (Map<String,Object> map : listMap) {
				item = new JSONObject();
				Object value = null;
				for(String key : keySet){
					value = map.get(key);
					if(value instanceof Date){
						item.put(key, value.toString());
					}else{
						item.put(key, value);
					}
				}
				array.add(item);
			}
			object.put("rows", array);
			object.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString();
	}
	
}
