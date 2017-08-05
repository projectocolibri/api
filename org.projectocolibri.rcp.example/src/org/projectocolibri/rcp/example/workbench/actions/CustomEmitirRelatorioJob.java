/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.dma.eclipse.core.jobs.tasks.JobTask;
import org.dma.eclipse.core.jobs.tasks.JobUITask;

import org.eclipse.jface.action.Action;

import org.projectocolibri.api.Language.LABELS;
import org.projectocolibri.api.birt.report.BIRTRender;
import org.projectocolibri.api.birt.report.BIRTRenderTask;
import org.projectocolibri.api.birt.report.BIRTReport.REPORT_ACTIONS;
import org.projectocolibri.api.birt.report.BIRTReport.REPORT_FORMATS;
import org.projectocolibri.api.database.model.Entidadesdocumentos;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriJob;

public abstract class CustomEmitirRelatorioJob extends ColibriJob {

	public abstract void done(BIRTRender render);

	private BIRTRender render;

	public CustomEmitirRelatorioJob(final Entidadesdocumentos documento, final REPORT_ACTIONS action) {
		super(LABELS.operacao_emitirrelatorio.name(0));

		addTask(new JobTask(new Action() {
			@Override
			public void run() {
				render=documento.createReport(action).
						process(REPORT_FORMATS.PDF, new BIRTRenderTask());
			}
		}));

		addTask(new JobUITask(new Action() {
			@Override
			public void run() {
				done(render);
			}
		}));
	}


}
