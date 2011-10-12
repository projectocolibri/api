/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import org.dma.utils.apache.mail.EmailAddress;
import org.dma.utils.eclipse.core.jobs.CustomJob;
import org.dma.utils.eclipse.core.jobs.IJobSupport;
import org.dma.utils.eclipse.core.jobs.JobManager;
import org.dma.utils.eclipse.core.jobs.tasks.JobTask;
import org.dma.utils.eclipse.core.jobs.tasks.JobUITask;
import org.dma.utils.eclipse.swt.DialogHandler;
import org.eclipse.jface.action.Action;

import rcp.colibri.core.BIRT.report.BIRTEmailParameters;
import rcp.colibri.core.BIRT.report.BIRTReport;
import rcp.colibri.core.mappers.filters.FilterMap;
import rcp.colibri.core.mappers.filters.FilterOperandMap;
import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.colibri.core.vars.model.TableVARS;
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
	public void process(int numerodocumento, int action) {

		BIRTReport report=initReport(numerodocumento, action);
		report.process();
		report.postProcess();

	}




	/**
	 * Processamento em background
	 * <p>
	 * Deve ser usado apenas quando existe workbench
	 * Permite que a aplicacao continue a correr enquanto o processamento decorre
	 */
	public void backgroundProcess(int numerodocumento, int action) {

		final CustomJob job=new CustomJob();

		switch(action){
			case BIRTReport.ACTION_PREVIEW: job.setName(LabelVARS.operacao_preverrelatorio[0]); break;
			case BIRTReport.ACTION_PRINT: job.setName(LabelVARS.operacao_imprimirrelatorio[0]); break;
			case BIRTReport.ACTION_EMAIL: job.setName(LabelVARS.operacao_enviarrelatorio); break;
		}

		final BIRTReport report=initReport(numerodocumento, action);

		job.addTask(new JobTask("Processamento", new Action() {
			public void run() {
				report.process();
			}
		}));

		job.addTask(new JobUITask("Resultados", new Action() {
			public void run() {
				report.postProcess();
			}
		}));

		/*
		 * O registo do JOB em JobManager permite um controle
		 * total sobre todos os jobs agendados ou em execucao.
		 */
		JobManager.register(new IJobSupport(){
			public void jobStarting() {
			}
			public void jobDone() {
				DialogHandler.information(job.getName(), LabelVARS.msgwin_info_operacaoconcluida);
			}
		}, job);

		job.execute();

	}



	/**
	 * Inicializa o report
	 */
	public BIRTReport initReport(int numerodocumento, int action) {

		String template="701"; //FACTURA

		BIRTReport report=new BIRTReport(action, template,
			new FilterMap(TableVARS.entidadesdocumentos).
				addFieldRule(TableVARS.entidadesdocumentos_key,	FilterMap.OPERATORS.MATH.EQUALS_TO.symbol,
				new FilterOperandMap(Entidadesdocumentos.generateKey("CFA","2011",numerodocumento),	FilterOperandMap.TYPE_VALUE)),
			new BIRTEmailParameters(new EmailAddress[]{new EmailAddress("marcolopes@projectocolibri.com", "Marco Lopes")},
				"Envio de documento "+numerodocumento));

		return report;

	}


}
