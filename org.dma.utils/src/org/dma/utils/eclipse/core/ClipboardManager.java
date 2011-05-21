/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 * Paulo Silva (wickay@hotmail.com)
 *******************************************************************************/
package org.dma.utils.eclipse.core;

import java.util.ArrayList;
import java.util.List;

import org.dma.utils.java.object.ObjectUtils;

public class ClipboardManager {

	private Class objectClass;
	private List<Object> clipboard=new ArrayList();

	public ClipboardManager() throws Exception {
		Debug.info();
	}

	public void transferToClipboard(Object[] objectArray) {

		if(objectArray!=null && objectArray.length>0) {

			clearClipboard();

			clipboard=ObjectUtils.toList(objectArray);

			if(clipboard!=null)
				objectClass=clipboard.get(0).getClass();

		}

	}


	public void clearClipboard() {
		clipboard.clear();
	}


	public List<Object> retrieveCollection(Class cl) {
		if(!cl.equals(objectClass))
			return null;

		return clipboard;
	}


	public Object retrieveObject(Class cl) {
		if(clipboard==null || clipboard.size()==0 || !cl.equals(objectClass))
			return null;

		return clipboard.iterator().next();
	}


	public boolean hasObject(Class cl) {
		return cl.equals(objectClass);
	}


	public boolean hasObject() {
		return clipboard.size()>0;
	}






	//getters and setters
	public Class getObjectClass() {
		return objectClass;
	}

	public List<Object> getObjects() {
		return clipboard;
	}


}
