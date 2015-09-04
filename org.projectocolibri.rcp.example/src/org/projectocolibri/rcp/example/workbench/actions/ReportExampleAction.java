/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.Application;
import org.projectocolibri.rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.DocumentostiposPopulate;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.EntidadestiposPopulate;
import org.projectocolibri.rcp.example.ReportExample;

public class ReportExampleAction extends Action implements IWorkbenchAction {

	public ReportExampleAction() {
		setText("#report");
	}


	@Override
	public final void run() {

		ReportExample reportExample=new ReportExample();

		//factura de clientes
		String tipodocumento=DocumentostiposPopulate.RECORDS.clientes_factura.codigo;
		//primeira serie do documento
		String serie=ColibriDatabase.loadDocumentosseries(tipodocumento,0).getSerie();

		if (Application.CREATE_WORKBENCH){
			//Exemplos que NAO podem correr SEM workbench
			reportExample.backgroundProcess(tipodocumento, serie, 1, REPORT_ACTIONS.PREVIEW);
			reportExample.backgroundProcess(tipodocumento, serie, 1, REPORT_ACTIONS.PRINT);
			reportExample.backgroundProcess(tipodocumento, serie, 1, REPORT_ACTIONS.EMAIL);
			//Clientes - Extracto de Conta
			reportExample.backgroundProcess(EntidadestiposPopulate.RECORDS.cliente.codigo, 1, "601");

		}else{
			 //Exemplos que podem correr SEM workbench
			reportExample.process(tipodocumento, serie, 1, REPORT_ACTIONS.PRINT);
			reportExample.process(tipodocumento, serie, 1, REPORT_ACTIONS.EMAIL);

		}

	}

	@Override
	public void dispose() {}


}
