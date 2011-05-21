/*******************************************************************************
 * 2008-2011 Public Domain
 * Contributors
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package org.dma.utils.eclipse.core.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dma.utils.eclipse.core.Debug;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class JobManager {

	private final Map<IViewJob, List<JobSupport>> jobMap=new HashMap();

	public JobManager() throws Exception {
		Debug.info();
	}


	//exit task class
	public class ExitTask extends Action implements IWorkbenchAction {

		public final String ACTION_ID = getClass().getName();
		private final JobSupport job;

		public ExitTask(JobSupport job) {
			setId(ACTION_ID);
			this.job=job;
		}

		public final void run(){
			try {
				IViewJob view=findJobView(job);

				remove(job);

				if (getQueuedJobs(view)==0)
					view.setJobRunning(false);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void dispose() {}
	}



	//add
	public void register(IViewJob view, JobSupport job) {

		job.setExitAction(new ExitTask(job));

		if(!jobMap.containsKey(view))
			jobMap.put(view, new ArrayList());

		jobMap.get(view).add(job);

		debug();

	}



	//remove
	public void remove(JobSupport job) {

		IViewJob view=findJobView(job);
		if (view!=null)
			jobMap.get(view).remove(job);
		else
			Debug.warning("JOB NOT FOUND", job.getName());

	}


	public void clean() {

		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext()){
			if(jobMap.get(iterator.next()).size()==0)
				iterator.remove();
		}

	}



	//execute
	public void execute(JobSupport job) {

		IViewJob view=findJobView(job);
		execute(view);

	}


	public void execute(IViewJob view) {

		List<JobSupport> jobs=jobMap.get(view);

		for(int i=0; i<jobs.size(); i++){

			JobSupport job=jobs.get(i);
			if (!job.isWorking()){

				job.setWorking(true);

				//e' o primeiro JOB?
				if (getPendingJobs(view)==0 && getRunningJobs(view)==0)
					view.setJobRunning(true);

				job.execute();
			}

		}

	}



	//cancel
	public boolean cancelJobs(IViewJob view) {

		boolean result=true;
		List<JobSupport> jobs=jobMap.get(view);
		/*
		 * Deve comecar pelo ultimo elemento para prevenir a remocao
		 * automatica de JOBS da lista efectuada pelo metodo de SAIDA
		 */
		for(int i=jobs.size()-1; i>=0; i--){
			JobSupport job=jobs.get(i);
			if (!job.cancel())
				result=false;
		}

		return result;

	}


	public boolean cancelJobs() {

		boolean result=true;
		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext()) {
			IViewJob view=iterator.next();
			if (!cancelJobs(view))
				result=false;
		}

		debug();

		return result;

	}



	public IViewJob findJobView(JobSupport job) {

		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext()) {
			IViewJob view=iterator.next();
			if(jobMap.get(view).contains(job))
				return view;
		}

		return null;
	}



	public void debug() {

		Debug.info("QUEUED", getQueuedJobs());
		Debug.info("PENDING", getPendingJobs());
		Debug.info("RUNNING", getRunningJobs());

		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext()) {

			IViewJob view=iterator.next();
			Debug.info("view",view);

			List<JobSupport> jobList=jobMap.get(view);
			for(int i=0; i<jobList.size(); i++){
				JobSupport job=jobList.get(i);
				Debug.info(job.getName()+": "+getStateName(job));
			}

		}

	}





	//getters and setters
	public String getStateName(JobSupport job) {

		String state="NONE";
		switch (job.getState()){
			case Job.RUNNING: state="RUNNING"; break;
			case Job.WAITING: state="WAITING"; break;
			case Job.SLEEPING: state="SLEEPING"; break;
		}

		return state;

	}


	public int getQueuedJobs(IViewJob view) {

		List<JobSupport> jobs=jobMap.get(view);
		return jobs==null ? 0 : jobs.size();

	}


	public int getQueuedJobs() {

		int n=0;
		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext())
			n+=getQueuedJobs(iterator.next());

		return n;

	}


	public int getPendingJobs(IViewJob view) {

		int n=0;
		List<JobSupport> jobs=jobMap.get(view);
		for(int i=0; i<jobs.size(); i++){
			JobSupport job=jobs.get(i);
			if (job.getState()==Job.WAITING) ++n;
		}

		return n;

	}


	public int getPendingJobs() {

		int n=0;
		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext())
			n+=getPendingJobs(iterator.next());

		return n;

	}


	public int getRunningJobs(IViewJob view) {

		int n=0;
		List<JobSupport> jobs=jobMap.get(view);
		for(int i=0; i<jobs.size(); i++){
			JobSupport job=jobs.get(i);
			if (job.getState()==Job.RUNNING) ++n;
		}

		return n;

	}


	public int getRunningJobs() {

		int n=0;
		Iterator<IViewJob> iterator=jobMap.keySet().iterator();
		while(iterator.hasNext())
			n+=getRunningJobs(iterator.next());

		return n;

	}


}
