/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api;

import org.dma.utils.eclipse.jobs.CustomJob;
import org.dma.utils.eclipse.jobs.tasks.JobAction;
import org.dma.utils.eclipse.jobs.tasks.JobTask;
import org.eclipse.core.runtime.jobs.Job;

import rcpcolibri.core.BIRT.report.BIRTReport;
import rcpcolibri.core.mappers.filters.FilterMap;
import rcpcolibri.core.mappers.filters.FilterOperandMap;
import rcpcolibri.dao.model.classes.Entidadesdocumentos;
import rcpcolibri.ui.workbench.views.IView;
import rcpcolibri.vars.gui.LabelVARS;
import rcpcolibri.vars.model.TableVARS;

public class PrintExample {

	public PrintExample(){
	}


	/**
	 * Processa e imprime
	 */
	public void normalProcess(IView view, int numerodocumento) {
		try{

			BIRTReport report=initReport(numerodocumento);
			report.process();
			report.postProcess(view);

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * Processa em background e imprime
	 */
	public void backgroundProcess(final IView view, int numerodocumento) {
		try{

			final BIRTReport report=initReport(numerodocumento);

			CustomJob job=new CustomJob(LabelVARS.operacao_imprimirrelatorio[0],Job.LONG);
			job.addTask(new JobTask("Processamento", new JobAction() {
				public void task() {
					report.process();
				}
				public void UItask() {
					report.postProcess(view);
				}
			}));

			job.execute();

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * Inicializa o report
	 */
	public BIRTReport initReport(int numerodocumento) {
		try{

			return new BIRTReport("701", new FilterMap(TableVARS.entidadesdocumentos).
				addFieldRule(TableVARS.entidadesdocumentos_key,	FilterMap.OPERATORS.MATH.EQUALS_TO.symbol,
					new FilterOperandMap(Entidadesdocumentos.generateKey("CFA","2011",numerodocumento),
						FilterOperandMap.TYPE_VALUE)), BIRTReport.ACTION_PRINT);

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}


}
