/*******************************************************************************
 * Copyright (c) 2010, 2010 Andrew Gvozdev and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrew Gvozdev (Quoin Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.ui.actions;

import java.util.ArrayList;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.ide.actions.BuildUtilities;

/**
 * Action which cleans all configurations of the selected projects
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * 
 * @since 7.0
 */
public class CleanAllConfigurationsAction implements IObjectActionDelegate {
	private ArrayList<IProject> projects = null;

	public void selectionChanged(IAction action, ISelection selection) {
		projects = CleanAndBuildAction.getSelectedCdtProjects(selection);
		action.setEnabled(projects.size() > 0);
	}

	public void run(IAction action) {
		for (IProject project : projects) {
			ICProjectDescription prjd = CoreModel.getDefault().getProjectDescription(project, false);
			if (prjd != null) {
				ICConfigurationDescription[] cfgds = prjd.getConfigurations();
				if (cfgds != null && cfgds.length > 0) {
					// save all dirty editors
					BuildUtilities.saveEditors(null);
					
					Job buildFilesJob = new BuildConfigurationsJob(cfgds, IncrementalProjectBuilder.CLEAN_BUILD, 0);
					buildFilesJob.schedule();
				}
			}

		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
}
