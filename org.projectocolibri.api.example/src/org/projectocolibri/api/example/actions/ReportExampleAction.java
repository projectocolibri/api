/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.actions;

import org.eclipse.jface.action.Action;

import org.projectocolibri.api.birt.report.BIRTRender;
import org.projectocolibri.api.example.core.ReportExample;

public class ReportExampleAction extends Action {

	public ReportExampleAction() {
		setText("#report");
	}


	@Override
	public final void run() {

		new ReportExample() {
			@Override
			public void preview(BIRTRender render) {}
		}.run(false);

	}


}
