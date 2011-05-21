/*******************************************************************************
 * 2008-2010 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.java.object;

import java.util.ArrayList;
import java.util.List;

public final class ObjectUtils {


	public static boolean equals(Object a, Object b) {

		if(a==null && b==null)
			return true;

		if(a!=null && b!=null && a.getClass()==b.getClass()) {

			if(a instanceof Number) {
				if((((Number) a).doubleValue()==((Number) b).doubleValue()))
					return true;
			}else{
				return a.equals(b);
			}

		}

		return false;

	}


	public static List<Object> toList(Object[] array) {

		if (array==null)
			return null;

		List<Object> list=new ArrayList();

		for(int i=0;i<array.length;i++)
			list.add(array[i]);

		return list;

	}


	public static String toString(Object obj) {

		if (obj==null)
			return "";

		return obj.toString();

	}




}
