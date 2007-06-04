/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.rse.internal.persistence;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.persistence.IRSEPersistenceProvider;
import org.eclipse.rse.persistence.dom.RSEDOM;

class PFMetadataAnchor implements PFPersistenceAnchor {
	
	public IStatus deleteProfileLocation(String profileName, IProgressMonitor monitor) {
		IStatus result = Status.OK_STATUS;
		File profileFolder = getProfileFolder(profileName);
		if (profileFolder.exists()) {
			delete(profileFolder);
		}
		return result;
	}

	public PFPersistenceLocation getProfileLocation(String profileLocationName) {
		File profileFolder = getProfileFolder(profileLocationName);
		PFPersistenceLocation result = new PFMetadataLocation(profileFolder);
		return result;
	}

	public String[] getProfileLocationNames() {
		List names = new Vector(10);
		File providerFolder = getProviderFolder();
		File[] profileCandidates = providerFolder.listFiles();
		for (int i = 0; i < profileCandidates.length; i++) {
			File profileCandidate = profileCandidates[i];
			if (profileCandidate.isDirectory()) {
				String candidateName = profileCandidate.getName();
				if (candidateName.startsWith(PFConstants.AB_PROFILE)) {
					names.add(candidateName);
				}
			}
		}
		String[] result = new String[names.size()];
		names.toArray(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.rse.internal.persistence.PFPersistenceAnchor#makeSaveJob(org.eclipse.rse.persistence.dom.RSEDOM, org.eclipse.rse.persistence.IRSEPersistenceProvider)
	 */
	public Job makeSaveJob(RSEDOM dom, IRSEPersistenceProvider provider) {
		return new PFMetatdataJob(dom, provider);
	}

	/**
	 * @return the folder that acts as the parent for profile folders.
	 */
	private File getProviderFolder() {
		IPath statePath = RSECorePlugin.getDefault().getStateLocation();
		File stateFolder = new File(statePath.toOSString());
		File providerFolder = getFolder(stateFolder, "profiles"); //$NON-NLS-1$
		return providerFolder;
	}
	
	/**
	 * Returns the File (directory) in which a profile is stored. 
	 * @return The folder that was created or found.
	 */
	private File getProfileFolder(String profileLocationName) {
		File  providerFolder = getProviderFolder();
		File profileFolder = getFolder(providerFolder, profileLocationName);
		return profileFolder;
	}

	/**
	 * Returns the specified folder of the parent container. If the folder does
	 * not exist it creates it.
	 * @param parent the parent folder
	 * @param name the name of the folder to find or create
	 * @return the found or created folder
	 */
	private File getFolder(File parent, String name) {
		File folder = new File(parent, name);
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folder;
	}

	/**
	 * Delete a File resource. If the resource is a directory then 
	 * delete its children first.
	 * @param resource
	 */
	private void delete(File resource) {
		if (resource.isDirectory()) {
			File[] resources = resource.listFiles();
			for (int i = 0; i < resources.length; i++) {
				File child = resources[i];
				delete(child);
			}
		}
		resource.delete();
	}

}
