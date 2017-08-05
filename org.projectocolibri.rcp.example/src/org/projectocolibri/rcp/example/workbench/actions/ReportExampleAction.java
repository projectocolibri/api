/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.dma.eclipse.swt.dialogs.message.InformationDialog;
import org.dma.java.util.Debug;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.api.IconVARS;
import org.projectocolibri.api.birt.report.BIRTRender;
import org.projectocolibri.api.example.core.ReportExample;
import org.projectocolibri.rcp.colibri.RCPcolibri.PREFERENCES;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;
import org.projectocolibri.rcp.colibri.workbench.support.views.actions.OpenViewAction;
import org.projectocolibri.rcp.colibri.workbench.views.ReportView;

public class ReportExampleAction extends ColibriAction implements IWorkbenchAction {

	public ReportExampleAction() {
		setText("#report");
		setImageDescriptor(IconVARS.TOOLBAR_REPORT);
	}


	@Override
	public final void run() {

		new ReportExample() {
			@Override
			public void preview(BIRTRender render) {
				Debug.err("preview", render.getReportFile());
				if (!InformationDialog.open(render.errors())){
					new OpenViewAction(ReportView.ID, render).
						run(PREFERENCES.DETACH_REPORT_VIEW.value.getBoolean());
				}
			}
		}.run(true);

	}

	@Override
	public void dispose() {}


}
