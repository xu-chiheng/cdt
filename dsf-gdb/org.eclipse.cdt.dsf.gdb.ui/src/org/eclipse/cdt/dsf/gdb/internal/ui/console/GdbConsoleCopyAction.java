/*******************************************************************************
 * Copyright (c) 2016 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.cdt.dsf.gdb.internal.ui.console;

import org.eclipse.cdt.dsf.gdb.internal.ui.GdbUIPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.tm.internal.terminal.control.ITerminalViewControl;

/**
 * Action to copy the selected text from the associated terminal
 */
public class GdbConsoleCopyAction extends Action {

	private final ITerminalViewControl fTerminalCtrl;

	public GdbConsoleCopyAction(ITerminalViewControl terminalControl) {
		fTerminalCtrl = terminalControl;
		if (fTerminalCtrl == null || fTerminalCtrl.isDisposed()) {
			setEnabled(false);
		}
		setText(ConsoleMessages.ConsoleCopyAction_name);
		setToolTipText(ConsoleMessages.ConsoleCopyAction_description);
		setImageDescriptor(GdbUIPlugin.getImageDescriptor(IConsoleImagesConst.IMG_CONSOLE_COPY_ACTIVE_COLOR));
		setDisabledImageDescriptor(GdbUIPlugin.getImageDescriptor(IConsoleImagesConst.IMG_CONSOLE_COPY_DISABLED_COLOR));
	}

	@Override
	public void run() {
		if (fTerminalCtrl != null) {
			fTerminalCtrl.copy();
		}
	}
}
