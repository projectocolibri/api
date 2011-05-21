/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.eclipse.core.jobs;

import org.eclipse.jface.action.IAction;

public class JobTask {

	private final String description;
	private final IAction action;

	public JobTask(IAction action) {
		this.action = action;
		this.description = "";
	}

	public JobTask(String description,IAction action) {
		this.action = action;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public IAction getAction() {
		return action;
	}


}

