/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.Application;
import rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import rcp.colibri.core.vars.PopulateVARS.DOCUMENTOSTIPOS;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.example.ReportExample;

public class ReportExampleAction extends Action implements IWorkbenchAction {

	public ReportExampleAction() {
		setText("#report");
	}


	public final void run(){

		ReportExample reportExample=new ReportExample();

		//factura de clientes
		String tipodocumento=DOCUMENTOSTIPOS.clientes_factura.codigo;
		//primeira serie do documento
		String serie=ColibriDatabase.loadDocumentosseries(tipodocumento,0).getSerie();

		if (Application.CREATE_WORKBENCH){
			/*
			 * Exemplos que NAO podem correr SEM workbench
			 */
			reportExample.backgroundProcess(tipodocumento, serie, 1, REPORT_ACTIONS.PREVIEW);
			reportExample.backgroundProcess(tipodocumento, serie, 1, REPORT_ACTIONS.PRINT);
			reportExample.backgroundProcess(tipodocumento, serie, 1, REPORT_ACTIONS.EMAIL);

		}else{
			/*
			 * Exemplos que podem correr SEM workbench
			 */
			reportExample.process(tipodocumento, serie, 1, REPORT_ACTIONS.EMAIL);

		}

	}

	public void dispose() {}


}
