/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import org.dma.eclipse.core.jobs.JobGroup;
import org.dma.eclipse.core.jobs.JobManager;
import org.dma.eclipse.core.jobs.tasks.JobTask;
import org.dma.eclipse.core.jobs.tasks.JobUITask;
import org.dma.java.utils.email.EmailAddress;
import org.dma.java.utils.email.EmailParameters;
import org.eclipse.jface.action.Action;

import rcp.birt.report.BIRTReport;
import rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import rcp.colibri.core.jobs.ColibriJob;
import rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import rcp.colibri.core.vars.DatabaseVARS.TABLES;
import rcp.colibri.core.vars.LabelVARS.LABELS;
import rcp.colibri.core.vars.PopulateVARS.TEMPLATES;
import rcp.colibri.dao.database.filter.FilterMap;
import rcp.colibri.dao.database.filter.FilterOperandMap;
import rcp.colibri.dao.database.filter.FilterMap.OPERATORS;
import rcp.colibri.dao.database.mapper.DatabaseFieldKey;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;

public class ReportExample {

	public ReportExample(){
	}


	/**
	 * Processamento normal
	 * <p>
	 * Deve ser usado quando NAO existe workbench
	 * Evita que a aplicacao possa terminar enquanto o processamento decorre
	 */
	public void process(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		BIRTReport report=initReport(tipodocumento, serie, numerodocumento, action);
		String file=report.process();
		System.out.println("Output file: "+file);
		report.output();

	}




	/**
	 * Processamento em background
	 * <p>
	 * Deve ser usado apenas quando existe workbench
	 * Permite que a aplicacao continue a correr enquanto o processamento decorre
	 */
	public void backgroundProcess(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		final ColibriJob job=new ColibriJob();

		switch (action){
			case PREVIEW: job.setName(LABELS.operacao_preverrelatorio.singular()); break;
			case PRINT: job.setName(LABELS.operacao_imprimirrelatorio.singular()); break;
			case EMAIL: job.setName(LABELS.operacao_enviarrelatorio.singular()); break;
		}

		final BIRTReport report=initReport(tipodocumento, serie, numerodocumento, action);

		job.addTask(new JobTask("Processamento", new Action() {
			public void run() {
				report.process();
			}
		}));

		job.addTask(new JobUITask("Resultados", new Action() {
			public void run() {
				report.output();
			}
		}));

		JobGroup group=new JobGroup(){
			private static final long serialVersionUID = 1L;
			public void jobGroupStart() {
			}
			public void jobGroupDone() {
				job.showInfo(LABELS.msgwin_info_operacaoconcluida.toString());
			}
		};

		group.add(job);

		JobManager.schedule(group);

	}



	/**
	 * Inicializa o report
	 */
	public BIRTReport initReport(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		BIRTReport report=new BIRTReport(action, TEMPLATES.entidadesdocumentos_factura.codigo,
			new FilterMap(TABLES.entidadesdocumentos).
				addRule(new DatabaseFieldKey(FIELDS.entidadesdocumentos_key),
					OPERATORS.MATH.EQUAL, new FilterOperandMap(Entidadesdocumentos.generateKey(tipodocumento,serie,numerodocumento))),
			new EmailParameters(new EmailAddress[]{new EmailAddress("marcolopes@projectocolibri.com", "Marco Lopes")},
				"Envio de documento "+numerodocumento));

		return report;

	}


}
