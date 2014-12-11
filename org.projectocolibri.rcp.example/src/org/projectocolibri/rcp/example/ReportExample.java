/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import org.dma.eclipse.core.jobs.JobBatch;
import org.dma.eclipse.core.jobs.tasks.JobTask;
import org.dma.java.utils.email.EmailAddress;

import org.eclipse.jface.action.Action;

import org.projectocolibri.rcp.birt.report.BIRTRender;
import org.projectocolibri.rcp.birt.report.BIRTReport;
import org.projectocolibri.rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.rcp.colibri.core.support.EmailParameters;
import org.projectocolibri.rcp.colibri.core.support.JobSupport;
import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.TABLES;
import org.projectocolibri.rcp.colibri.core.vars.LabelVARS.LABELS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.filter.FilterMap;
import org.projectocolibri.rcp.colibri.dao.database.filter.FilterMap.OPERATORS;
import org.projectocolibri.rcp.colibri.dao.database.filter.FilterOperandMap;
import org.projectocolibri.rcp.colibri.dao.database.mapper.DatabaseFieldKey;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.TemplatesPopulate;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentos;

public class ReportExample {

	private BIRTRender render;

	public ReportExample() {}


	/*
	 * Deve ser usado apenas quando existe workbench;
	 * NAO bloqueia a aplicacao enquanto o processamento decorre
	 */
	/** Processamento em background */
	public void backgroundProcess(final BIRTReport report, String name) {

		final JobSupport job=new JobSupport(name);

		job.addTask(new JobTask("Processamento", new Action() {
			public void run() {
				render=report.process();
			}
		}));

		@SuppressWarnings("serial")
		JobBatch batch=new JobBatch(){
			public void start() {
			}
			public void done() {
				if (!job.showError(render.output().getErrors())){
					job.showInfo(LABELS.msgwin_info_operacaoconcluida.singular());
				}
			}
		};
		batch.add(job);
		batch.schedule();

	}


	/*
	 * Caso seja utilizada a inicializacao a partir do documento
	 * carregado (ver exemplos de processamento), os parametros de
	 * EMAIL utilizados sao os da entidade associada ao documento.
	 */
	/** Documentos de entidades */
	private BIRTReport createReport(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		//FACULTATIVO: inicializa o report manualmente
		return new BIRTReport(action, TemplatesPopulate.RECORDS.entidadesdocumentos_factura.codigo,
				new FilterMap(TABLES.entidadesdocumentos).
					addRule(new DatabaseFieldKey(FIELDS.entidadesdocumentos_key),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, Entidadesdocumentos.generateKey(tipodocumento,serie,numerodocumento))),
				new EmailParameters("Envio de documento "+numerodocumento,
					new EmailAddress("marcolopes@projectocolibri.com", "Marco Lopes")));

	}


	public void backgroundProcess(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		//carrega o documento
		Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(tipodocumento, serie, numerodocumento);
		//inicializa o report
		BIRTReport report=documento.createReport(action);
		//processa o report
		switch(action){
			case PREVIEW: backgroundProcess(report, LABELS.operacao_emitirrelatorio.singular()); break;
			case PRINT: backgroundProcess(report, LABELS.operacao_imprimirrelatorio.singular()); break;
			case EMAIL: backgroundProcess(report, LABELS.operacao_emailrelatorio.singular()); break;
		}

	}


	public void process(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		//carrega o documento
		Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(tipodocumento, serie, numerodocumento);
		//inicializa o report
		BIRTReport report=documento.createReport(action);
		//processa o report
		BIRTRender render=report.process();
		System.out.println("Output file: "+render.getReportFile());
		//imprime possiveis erros
		render.output().print();

	}


	/** Listagens de entidades */
	public void backgroundProcess(String tipoentidade, Integer numero, String template) {

		final BIRTReport report=new BIRTReport(REPORT_ACTIONS.PREVIEW, template,
				new FilterMap(TABLES.entidadesmovimentos).
				addRule(new DatabaseFieldKey(FIELDS.entidadesmovimentos_tipoentidade,FIELDS.entidadestipos_codigo),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, tipoentidade)).
				addRule(new DatabaseFieldKey(FIELDS.entidadesmovimentos_entidade,FIELDS.entidades_numero),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, numero)));

		backgroundProcess(report, LABELS.operacao_emitirrelatorio.singular());

	}


}
