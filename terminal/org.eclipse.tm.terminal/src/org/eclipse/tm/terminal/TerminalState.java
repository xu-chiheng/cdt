/*******************************************************************************
 * Copyright (c) 2006 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Michael Scharf (Wind River) - initial API and implementation
 * Martin Oberhuber (Wind River) - fixed copyright headers and beautified
 *******************************************************************************/
package org.eclipse.tm.terminal;

/**
 * Represent the sate of a terminal connection.
 * In java 1.5 this would be an enum.
 * @author Michael Scharf
 *
 */
public class TerminalState {
	/**
	 * The terminal is not connected.
	 */
	public final static TerminalState CLOSED=new TerminalState("CLOSED"); //$NON-NLS-1$

	/**
	 * TODO: Michael Scharf: it's not clear to me what the meaning of the open state is
	 */
	public final static TerminalState OPENED=new TerminalState("OPENED"); //$NON-NLS-1$

	/**
	 * The terminal is about to connect.
	 */
	public final static TerminalState CONNECTING=new TerminalState("CONNECTING..."); //$NON-NLS-1$

	/**
	 * The terminal is connected.
	 */
	public final static TerminalState CONNECTED=new TerminalState("CONNECTED"); //$NON-NLS-1$

	private final String fState;

	public TerminalState(String state) {
		fState = state;
	}

	public String toString() {
		return fState;
	}
}