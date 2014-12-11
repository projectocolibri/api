/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.dma.eclipse.core.jobs.tasks.JobTask;
import org.dma.eclipse.core.jobs.tasks.JobUITask;

import org.eclipse.jface.action.Action;

import org.projectocolibri.rcp.birt.report.BIRTRender;
import org.projectocolibri.rcp.birt.report.BIRTRenderTask;
import org.projectocolibri.rcp.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.rcp.birt.report.BIRTReport.REPORT_FORMATS;
import org.projectocolibri.rcp.colibri.core.support.JobSupport;
import org.projectocolibri.rcp.colibri.core.vars.LabelVARS.LABELS;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentos;

public abstract class CustomEmitirRelatorioJob extends JobSupport {

	public abstract void done(BIRTRender render);

	private BIRTRender render;

	public CustomEmitirRelatorioJob(final Entidadesdocumentos documento, final REPORT_ACTIONS action) {
		super(LABELS.operacao_emitirrelatorio.singular());

		addTask(new JobTask(new Action() {
			public void run() {
				render=documento.createReport(action).
						process(REPORT_FORMATS.PDF, new BIRTRenderTask());
			}
		}));

		addTask(new JobUITask(new Action() {
			public void run() {
				done(render);
			}
		}));
	}


}
