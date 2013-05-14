/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import org.dma.eclipse.core.jobs.CustomJob;
import org.dma.eclipse.core.jobs.JobGroup;
import org.dma.eclipse.core.jobs.JobManager;
import org.dma.eclipse.core.jobs.tasks.JobTask;
import org.dma.eclipse.core.jobs.tasks.JobUITask;
import org.dma.eclipse.swt.dialogs.message.InformationDialog;
import org.dma.java.utils.apache.EmailAddress;
import org.dma.java.utils.apache.EmailParameters;
import org.eclipse.jface.action.Action;

import rcp.birt.report.BIRTReport;
import rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import rcp.colibri.core.mappers.database.DatabaseFieldKey;
import rcp.colibri.core.mappers.database.DatabaseMap.TABLES;
import rcp.colibri.core.mappers.filters.FilterMap;
import rcp.colibri.core.mappers.filters.FilterMap.OPERATORS;
import rcp.colibri.core.mappers.filters.FilterOperandMap;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.colibri.vars.database.DatabaseVARS.FIELDS;
import rcp.colibri.vars.database.PopulateVARS.TEMPLATES;
import rcp.colibri.vars.gui.LabelVARS.LABELS;

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

		final CustomJob job=new CustomJob();

		switch(action){
			case PREVIEW: job.setName(LABELS.operacao_preverrelatorio.name(0)); break;
			case PRINT: job.setName(LABELS.operacao_imprimirrelatorio.name(0)); break;
			case EMAIL: job.setName(LABELS.operacao_enviarrelatorio.name(0)); break;
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
			public void jobGroupStart() {
			}
			public void jobGroupDone() {
				InformationDialog.open(job.getName(), LABELS.msgwin_info_operacaoconcluida.toString());
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
