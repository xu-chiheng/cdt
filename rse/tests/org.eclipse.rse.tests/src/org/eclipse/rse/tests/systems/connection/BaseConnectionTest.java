/* *******************************************************************************
 * Copyright (c) 2006 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Don Yantzi (IBM) - initial contribution.
 * David Dykstal (IBM) - initial contribution.
 * *******************************************************************************/
package org.eclipse.rse.tests.systems.connection;

import java.text.MessageFormat;
import java.util.Properties;

import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemProfile;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.subsystems.IServiceSubSystemConfiguration;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.core.subsystems.ISubSystemConfiguration;
import org.eclipse.rse.subsystems.files.core.model.RemoteFileUtility;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.FileServiceSubSystem;
import org.eclipse.rse.subsystems.shells.core.subsystems.servicesubsystem.IShellServiceSubSystem;
import org.eclipse.rse.tests.framework.AnnotatingTestCase;
import org.eclipse.rse.tests.systems.core.SystemTestPlugin;
import org.eclipse.rse.tests.systems.core.SystemTestUtil;
import org.eclipse.rse.ui.RSEUIPlugin;

/**
 * Abstract superclass for JUnit PDE test cases that require an IHost.
 * This superclass creates a single RSE IHost that can
 * be reused by multiple testcases run during the same PDE invocation.
 * 
 * @author yantzi
 */
public class BaseConnectionTest extends AnnotatingTestCase {

	private IHost host = null;
	private Properties properties = null;

	/**
	 * Constructor for ConnectionTest.
	 * 
	 * @param name
	 *            Name of current test being run.
	 */
	public BaseConnectionTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		properties = SystemTestUtil.loadProperties(SystemTestPlugin.getDefault().getBundle(), "SystemConnectionInfo.properties");
		host = getHost();
	}
	
	protected void tearDown() throws Exception {
		host = null;
		properties = null;
		super.tearDown();
	}

	/**
	 * Load strings from the SystemConnectionTests.properties file. Can be used to retrieve
	 * properties that influence the running of the testcase.
	 * @param key the key of the string in the properties file. 
	 * @return the value of the property
	 */
	public String getString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Find the host used by all subclasses for this
	 * invocation of the runtime workbench. If not found, then 
	 * create it using the associated getters to
	 * change the default connection name, hostname, user ID or password (as
	 * specified in SystemConnectionTests.properties).
	 * 
	 * @return the new connection (host)
	 * @throws Exception if there is a problem
	 * 
	 * @see BaseConnectionTest#getHostName()
	 * @see BaseConnectionTest#getHostAddress()
	 * @see BaseConnectionTest#getSystemType()
	 * @see BaseConnectionTest#getUserID()
	 * @see BaseConnectionTest#getPassword()
	 */
	protected IHost getHost() throws Exception {
		if (host == null) {
			String profileName = getProfileName();
			assertNotSame("need to change the profile name in SystemConnectionInfo.properties", "unknown", profileName);
			ISystemProfile profile = SystemTestUtil.findProfile(profileName);
			if (profile == null) {
				profile = SystemTestUtil.createProfile(profileName);
			}
			String hostName = getHostName();
			assertNotSame("need to change the host name in SystemConnectionInfo.properties", "unknown", hostName);
			host = SystemTestUtil.findHost(profileName, hostName);
			if (host == null) {
				String userID = getUserID();
				assertNotSame("need to change the user id in SystemConnectionInfo.properties", "unknown", userID);
				String password = getPassword();
				assertNotSame("need to change the password in SystemConnectionInfo.properties", "unknown", password);
				String hostAddress = getHostAddress();
				assertNotSame("need to change the host address in SystemConnectionInfo.properties", "unknown", hostAddress);
				host = SystemTestUtil.createHost(profileName, hostName, hostAddress, getSystemType(), userID, password);
			}
		}
		return host;
	}

	/**
	 * Get the file subsystem for default connection.
	 * Returns null if there is no subsystem with the given configuration id in this connection.
	 * @param desiredConfigurationId the subsystem configuration id of the desired subsystem. 
	 * @return the file subsystem
	 * @throws Exception if there is a problem
	 */
	protected ISubSystem getFileSubSystem(String desiredConfigurationId) throws Exception {
		FileServiceSubSystem subsystem = (FileServiceSubSystem) RemoteFileUtility.getFileSubSystem(getHost());
		ISubSystemConfiguration config = subsystem.getSubSystemConfiguration();
		String activeId = config.getId();
		if (!activeId.equals(desiredConfigurationId)) {
			if (subsystem.isConnected()) {
				throw new RuntimeException(MessageFormat.format("The subsystem is connected as {}. Disconnect before changing.", new Object[] {activeId}));
			} else {
				ISystemRegistry registry = RSECorePlugin.getDefault().getSystemRegistry();
				ISubSystemConfiguration desiredConfiguration = registry.getSubSystemConfiguration(desiredConfigurationId);
				if (desiredConfiguration instanceof IServiceSubSystemConfiguration) {
					IServiceSubSystemConfiguration t = (IServiceSubSystemConfiguration) desiredConfiguration;
					subsystem.switchServiceFactory(t);
				}
			}
		}
		return subsystem;
	}

	/**
	 * Get the shell subsystem for default connection
	 * @return the shell subsystem
	 * @throws Exception if there is a problem
	 */
	protected ISubSystem getShellSubSystem() throws Exception {
		ISystemRegistry registry = RSEUIPlugin.getTheSystemRegistry();
		ISubSystem[] subSystems = registry.getSubSystems(getHost());
		for (int i = 0; i < subSystems.length; i++) {
			ISubSystem subSystem = subSystems[i];
			if (subSystem instanceof IShellServiceSubSystem) {
				IShellServiceSubSystem shellSubSystem = (IShellServiceSubSystem) subSystem;
				return shellSubSystem;
			}
		}
		return null;
	}
	
	/**
	 * Retrieve the name for the default RSE profile used by subclasses.
	 * Subclasses should override this method to override the profile name,
	 * otherwise the default system type from SystemConnectionTests.properties
	 * is used.
	 * @return the name of the profile
	 */
	protected String getProfileName() {
		return getString(ConnectionConstants.DEFAULT_PROFILE_NAME);
	}

	/**
	 * Retrieve the name for the default RSE connection used by subclasses.
	 * Subclasses should override this method to override the connection name,
	 * otherwise the default system type from SystemConnectionTests.properties
	 * is used.
	 * @return the name of the host
	 */
	protected String getHostName() {
		return getString(ConnectionConstants.DEFAULT_HOST_NAME);
	}

	/**
	 * Retrieve the hostname for the default RSE connection used by subclasses.
	 * Subclasses should override this method to override the hostname,
	 * otherwise the default hostname is used.
	 * @return the host address information
	 */
	protected String getHostAddress() {
		return getString(ConnectionConstants.DEFAULT_IP_NAME);
	}

	/**
	 * Retrieve the system type for the default RSE connection used by
	 * subclasses. Subclasses should override this method to override the system
	 * type, otherwise the default system type from
	 * SystemConnectionTests.properties is used.
	 * @return the system type
	 */
	protected String getSystemType() {
		return getString(ConnectionConstants.DEFAULT_SYSTEM_TYPE);
	}

	/**
	 * Retrieve the user ID for the default RSE connection used by subclasses.
	 * Subclasses should override this method to override the user ID used for
	 * the connection, otherwise the default system type from
	 * SystemConnectionTests.properties is used.
	 * @return the user id the connection will use
	 */
	protected String getUserID() {
		return getString(ConnectionConstants.DEFAULT_USERID);
	}

	/**
	 * Retrieve the password for the default RSE connection used by subclasses.
	 * Subclasses should override this method to override the password used for
	 * the associated hostname and user ID, otherwise the default system type
	 * from SystemConnectionTests.properties is used.
	 * @return the password used to establish the connection
	 */
	protected String getPassword() {
		return getString(ConnectionConstants.DEFAULT_PASSWORD);
	}
	
}