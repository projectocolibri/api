/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.eclipse.core.jobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dma.utils.eclipse.core.Debug;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;

public class JobSupport extends Job {

	/*
	 * MUTEX (mutual exclusion semaphore)
	 * Regra para evitar execucoes simultaneas
	 */
	public static final ISchedulingRule MUTEX_RULE=new MutexRule();

	/*
	 * On the Job: The Eclipse Jobs API
	 * http://www.eclipse.org/articles/Article-Concurrency/jobs-api.html
	 *
	 */

	private ILock lock;

	private final List<JobTask> tasks=new ArrayList();
	private final List<JobTask> uitasks=new ArrayList();
	private IAction exitAction;
	private boolean working=false;


	public JobSupport(String description, int priority) {
		super(description);
		setPriority(priority);

		/*
		 * Comentar a regra para testar operacoes simultaneas
		 */
		setRule(MUTEX_RULE);
	}


	//execute job
	public void execute() {

		Debug.info();

		//executa accao de saida, mesmo que tenha CANCELADO
		addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				Debug.info("### EXIT ACTION ###", getName());
				syncExec(exitAction);
			}
		});

		schedule();
	}




	//main method
	protected IStatus run(IProgressMonitor monitor) {

		try {
			Debug.info("### JOB STARTED ###", getName());

			lock = getJobManager().newLock();
			lock.acquire();
			monitor.beginTask("",IProgressMonitor.UNKNOWN);

			//tasks NAO graficas
			Iterator<JobTask> iterator=tasks.iterator();
			while(iterator.hasNext()) {

				JobTask jtask=iterator.next();

				monitor.setTaskName(jtask.getDescription());
				jtask.getAction().run();
			}

			//tasks graficas
			Iterator<JobTask> iterator2=uitasks.iterator();
			while(iterator2.hasNext()) {

				final JobTask jtask=iterator2.next();

				monitor.setTaskName("Updating Interface");
				syncExec(jtask.getAction());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{

			lock.release();
			monitor.done();
			Debug.info("### JOB FINISHED ###", getName());
		}

		return Status.OK_STATUS;
	}




	//sync method
	private void syncExec(final IAction action) {
		/*
		 * Causes the run() method of the runnable to be invoked by the user-interface
		 * thread at the next reasonable opportunity.
		 * The thread which calls this method is SUSPENDED until the runnable completes.
		 * Specifying null as the runnable simply wakes the user-interface thread.
		 */
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (action!=null)
					action.run();
			}
		});
	}




	//add and remove tasks
	public void addTask(JobTask action) {
		this.tasks.add(action);
	}

	public void addUITask(JobTask action) {
		this.uitasks.add(action);
	}

	public void removeTask(JobTask action) {
		this.tasks.remove(action);
	}

	public void removeUITask(JobTask action) {
		this.uitasks.remove(action);
	}



	//getters and setters
	public void setExitAction(IAction exitAction) {
		this.exitAction = exitAction;
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}


}
