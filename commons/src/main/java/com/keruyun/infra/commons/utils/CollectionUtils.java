/**
 * company: 北京时时客科技有限公司
 */
package com.keruyun.infra.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author liuc@shishike.com
 * 
 * @date 2014年9月22日 上午10:11:29
 */
public class CollectionUtils {

	/**
	 * 用于判断map、list、数组是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		boolean flag = false;

		if (obj == null) {
			flag = true;
		} else {
			if (obj instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) obj;
				if (map.size() == 0) {
					flag = true;
				}
			} else if (obj instanceof List) {
				List<?> list = (List<?>) obj;
				if (list.size() == 0) {
					flag = true;
				}
			} else if (obj.getClass().isArray()) {
				int length = Array.getLength(obj);
				if (length == 0) {
					flag = true;
				}

			}
		}

		return flag;
	}

	/**
	 * 返回2个集合的交集，并去除重复
	 * 
	 * @param firstColl
	 * @param secendColl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> innerJoin(Collection<T> firstColl, Collection<T> secendColl) {
		if (firstColl == null || secendColl == null) {
			return Collections.EMPTY_SET;
		}
		Set<T> result = new HashSet<T>();
		Iterator<T> it = firstColl.iterator();
		while (it.hasNext()) {
			T t = it.next();
			if (t == null) {
				continue;
			}
			if (secendColl.contains(t)) {
				result.add(t);
			}
		}
		return result;
	}

	public static <T> Map<String, T> getMapByUniqueProperty(List<T> list, String uniquePropertyName) {
		try {
			Map<String, T> map = new HashMap<String, T>();
			String methodName = "get" + StringUtils.capitalize(uniquePropertyName);
			for (T t : list) {
				if (t != null) {
					Method method = t.getClass().getMethod(methodName);
					Object value = method.invoke(t);
					if(value==null) {
						continue;
					}
					map.put(value.toString(), t);
				}
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 筛选两个列表中的相同元素
	 * @param baseList
	 * @param derivedList
	 * @return List<?>
	 */
    public static List<?> intersect(List<?> baseList, List<?> derivedList) {
        List<Object> resultList = new ArrayList<Object>();
	    if (!isNullOrEmpty(baseList) && !isNullOrEmpty(derivedList)) {
	        Iterator<?> it = derivedList.iterator();
	        while (it.hasNext()) {
	            Object element = it.next();
	            if (baseList.contains(element)) {
	                if (!resultList.contains(element)) {
	                    resultList.add(element);
	                }
	            }
	        }
	        
	    }
	    
	    return resultList;
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();
		list.add("");
		int[] aa = new int[] { 1 };

		boolean arrayFlag = isNullOrEmpty(aa);
		boolean listFlag = isNullOrEmpty(list);
		boolean mapFlag = isNullOrEmpty(map);
		System.out.println(arrayFlag);
		System.out.println(listFlag);
		System.out.println(mapFlag);

	}

}
