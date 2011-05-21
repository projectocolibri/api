/*******************************************************************************
 * 2008-2010 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.eclipse.swt;

import org.dma.utils.java.array.ArrayUtils;
import org.dma.utils.java.collection.CollectionUtils;
import org.eclipse.swt.widgets.List;

public final class ListUtils {

	public static void insert(List swtList, String[] array, int index) {

		java.util.List<String> list=ArrayUtils.toList(swtList.getItems());
		list.addAll(index, ArrayUtils.toList(array));
		swtList.setItems(CollectionUtils.toArray(list));

	}


	public static void remove(List swtList, int[] indexes) {

		java.util.List<String> list=ArrayUtils.toList(swtList.getItems());
		CollectionUtils.remove(list, indexes);
		swtList.setItems(CollectionUtils.toArray(list));

	}


	public static void moveDown(List swtList, int[] indexes, int position) {

		java.util.List<String> list=ArrayUtils.toList(swtList.getItems());
		CollectionUtils.moveDown(list, indexes, position);
		swtList.setItems(CollectionUtils.toArray(list));

	}


	public static void moveUp(List swtList, int[] indexes, int position) {

		java.util.List<String> list=ArrayUtils.toList(swtList.getItems());
		CollectionUtils.moveUp(list, indexes, position);
		swtList.setItems(CollectionUtils.toArray(list));

	}


}
