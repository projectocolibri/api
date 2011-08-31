/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import org.dma.utils.apache.mail.EmailAddress;
import org.dma.utils.eclipse.core.jobs.CustomJob;
import org.dma.utils.eclipse.core.jobs.IJobSupport;
import org.dma.utils.eclipse.core.jobs.JobManager;
import org.dma.utils.eclipse.core.jobs.tasks.JobAction;
import org.dma.utils.eclipse.core.jobs.tasks.JobTask;

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
		try{

			BIRTReport report=initReport(numerodocumento, action);
			report.process();
			report.postProcess();

		} catch (Exception e){
			e.printStackTrace();
		}
	}




	/**
	 * Processamento em background
	 * <p>
	 * Deve ser usado apenas quando existe workbench
	 * Permite que a aplicacao continue a correr enquanto o processamento decorre
	 */
	public void backgroundProcess(int numerodocumento, int action) {
		try{

			final BIRTReport report=initReport(numerodocumento, action);

			String description=null;
			switch(action){
				case BIRTReport.ACTION_PREVIEW: description=LabelVARS.operacao_preverrelatorio[0]; break;
				case BIRTReport.ACTION_PRINT: description=LabelVARS.operacao_imprimirrelatorio[0]; break;
				case BIRTReport.ACTION_EMAIL: description=LabelVARS.operacao_enviarrelatorio; break;
			}

			CustomJob job=new CustomJob(description);
			job.addTask(new JobTask("Processamento", new JobAction() {
				public void task() {
					report.process();
				}
				public void UItask() {
					report.postProcess();
				}
			}));

			/*
			 * Um JOB pode correr de forma imediata e independente, mas neste caso
			 * optamos por registar o job em JobManager.
			 * Desta forma temos um controle total sobre todos os jobs que possam
			 * estar agendados ou a correr.
			 * Tal e' importante para nao permitir que a aplicacao feche com jobs
			 * a decorrer, o que embora nao seja fatal, nao e' recomendado.
			 */
			//job.execute();

			JobManager.register(new IJobSupport(){
				public void setJobRunning(boolean jobRunning) {
				}
				public boolean isJobRunning() {
					return false;
				}
			}, job);

			JobManager.execute(job);

		} catch (Exception e){
			e.printStackTrace();
		}
	}



	/**
	 * Inicializa o report
	 */
	public BIRTReport initReport(int numerodocumento, int action) {
		try{

			String template="701"; //FACTURA

			BIRTReport report=new BIRTReport(action, template,
				new FilterMap(TableVARS.entidadesdocumentos).
					addFieldRule(TableVARS.entidadesdocumentos_key,	FilterMap.OPERATORS.MATH.EQUALS_TO.symbol,
					new FilterOperandMap(Entidadesdocumentos.generateKey("CFA","2011",numerodocumento),	FilterOperandMap.TYPE_VALUE)),
				new BIRTEmailParameters(new EmailAddress[]{new EmailAddress("marcolopes@projectocolibri.com", "Marco Lopes")},
					"Envio de documento "+numerodocumento));

			return report;

		} catch (Exception e){
			e.printStackTrace();
		}

		return null;
	}


}
