/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.core;

import org.dma.eclipse.core.jobs.CustomJob;
import org.dma.eclipse.core.jobs.tasks.JobTask;
import org.dma.eclipse.core.jobs.tasks.JobUITask;
import org.dma.java.email.EmailAddress;
import org.dma.java.util.CollectionUtils;

import org.eclipse.jface.action.Action;

import org.projectocolibri.api.birt.report.BIRTRender;
import org.projectocolibri.api.birt.report.BIRTReport;
import org.projectocolibri.api.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.api.core.support.EmailParameters;
import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.api.database.filter.FilterMap;
import org.projectocolibri.api.database.filter.FilterMap.OPERATORS;
import org.projectocolibri.api.database.filter.FilterOperandMap;
import org.projectocolibri.api.database.mapper.TableFieldKey;
import org.projectocolibri.api.database.mapper.TableMap.FIELDS;
import org.projectocolibri.api.database.mapper.TableMap.TABLES;
import org.projectocolibri.api.database.model.Documentosseries;
import org.projectocolibri.api.database.model.Documentostipos;
import org.projectocolibri.api.database.model.Entidadesdocumentos;
import org.projectocolibri.api.database.model.Entidadestipos;
import org.projectocolibri.api.database.model.Templates;

public abstract class ReportExample {

	public abstract void preview(BIRTRender render);

	public class ReportProcessJob extends CustomJob {

		private BIRTRender render;

		public ReportProcessJob(final BIRTReport report) {
			super(report.action.label.name(0));

			addTask(new JobTask("Processing...", new Action() {
				@Override
				public void run() {
					render=report.process();
				}
			}));

			addTask(new JobUITask(new Action() {
				@Override
				public void run() {
					output(render, report.action);
				}
			}));

			setRule(null); // imediato;
		}

	}

	public void run(boolean backgound) {

		try{
			//factura de clientes
			String tipodocumento=Documentostipos.RECORDS.clientes_factura.codigo;
			//primeira serie do documento
			Documentosseries serie=ColibriDatabase.loadDocumentostipos(tipodocumento).getSeries().iterator().next();

			if (backgound){
				//Exemplos em BACKGROUND (documentos)
				Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(tipodocumento, serie.getSerie(), serie.getNumero());
				new ReportProcessJob(documento.createReport(REPORT_ACTIONS.PREVIEW)).schedule();
				new ReportProcessJob(documento.createReport(REPORT_ACTIONS.PRINT)).schedule();
				new ReportProcessJob(documento.createReport(REPORT_ACTIONS.EMAIL)).schedule();

				new ReportProcessJob(createReport(tipodocumento, serie.getSerie(), serie.getNumero(), REPORT_ACTIONS.PREVIEW)).schedule();
				new ReportProcessJob(createReport(tipodocumento, serie.getSerie(), serie.getNumero(), REPORT_ACTIONS.PRINT)).schedule();
				new ReportProcessJob(createReport(tipodocumento, serie.getSerie(), serie.getNumero(), REPORT_ACTIONS.EMAIL)).schedule();

				//Exemplos em BACKGROUND (movimentos)
				String template=CollectionUtils.random(ColibriDatabase.getTemplatesCollection(Templates.tabela_entidadesmovimentos)).getCodigo();
				new ReportProcessJob(createReport(Entidadestipos.RECORDS.cliente.codigo, 1, template, REPORT_ACTIONS.PREVIEW)).schedule();
				new ReportProcessJob(createReport(Entidadestipos.RECORDS.cliente.codigo, 1, template, REPORT_ACTIONS.PRINT)).schedule();
				new ReportProcessJob(createReport(Entidadestipos.RECORDS.cliente.codigo, 1, template, REPORT_ACTIONS.EMAIL)).schedule();
			}else{
				//Exemplos em tempo real (documentos)
				process(createReport(tipodocumento, serie.getSerie(), serie.getNumero(), REPORT_ACTIONS.PREVIEW));
				process(createReport(tipodocumento, serie.getSerie(), serie.getNumero(), REPORT_ACTIONS.PRINT));
				process(createReport(tipodocumento, serie.getSerie(), serie.getNumero(), REPORT_ACTIONS.EMAIL));
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/** Processamento em tempo real */
	public void process(BIRTReport report) {

		//processa o report
		BIRTRender render=report.process();

		output(render, report.action);

	}


	public void output(BIRTRender render, REPORT_ACTIONS action) {

		System.out.println("REPORT: "+render.getReportFile());

		//imprime possiveis erros
		render.output().print();

		if (action==REPORT_ACTIONS.PREVIEW) preview(render);

	}


	/** Documentos de entidades */
	private BIRTReport createReport(String tipodocumento, String serie, int numerodocumento, REPORT_ACTIONS action) {

		return new BIRTReport(action, Templates.RECORDS.entidadesdocumentos_factura.codigo,
				new FilterMap(TABLES.entidadesdocumentos).
					addRule(new TableFieldKey(FIELDS.entidadesdocumentos_key),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, Entidadesdocumentos.generateKey(tipodocumento,serie,numerodocumento))),
				//parametros de email manuais
				new EmailParameters("Envio de documento "+numerodocumento,
					new EmailAddress("marcolopes@projectocolibri.com", "Marco Lopes")));

	}


	/** Movimentos de entidades */
	public BIRTReport createReport(String tipoentidade, Integer numero, String template, REPORT_ACTIONS action) {

		return new BIRTReport(action, template,
				new FilterMap(TABLES.entidadesmovimentos).
				addRule(new TableFieldKey(FIELDS.entidadesmovimentos_tipodocumento,FIELDS.documentostipos_tipoentidade,FIELDS.entidadestipos_codigo),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, tipoentidade)).
				addRule(new TableFieldKey(FIELDS.entidadesmovimentos_entidade,FIELDS.entidades_numero),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, numero)));

	}


}