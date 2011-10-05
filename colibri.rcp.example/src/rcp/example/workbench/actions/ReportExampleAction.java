/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.Application;
import rcp.colibri.core.BIRT.report.BIRTReport;
import rcp.example.ReportExample;

public class ReportExampleAction extends Action implements IWorkbenchAction {

	public ReportExampleAction() {
		setText("#report");
	}


	public final void run(){

		try{
			ReportExample reportExample=new ReportExample();

			if (Application.CREATE_WORKBENCH){
				/*
				 * Exemplos que NAO podem correr SEM workbench
				 */
				reportExample.backgroundProcess(1, BIRTReport.ACTION_PREVIEW);
				reportExample.backgroundProcess(1, BIRTReport.ACTION_PRINT);
				//reportExample.backgroundProcess(1, BIRTReport.ACTION_EMAIL);

			}else{
				/*
				 * Exemplos que podem correr SEM workbench
				 */
				reportExample.process(1, BIRTReport.ACTION_EMAIL);

			}

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public void dispose() {}


}
