// eclox : Doxygen plugin for Eclipse.
// Copyright (C) 2003-2006 Guillaume Brocker
//
// This file is part of eclox.
//
// eclox is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// eclox is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with eclox; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA	

package eclox.ui.editor.form.settings.editors;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class PathEditor extends TextEditor {
	
	/**
	 * the push button for browsing the file system
	 */
	private Button	browseFileSystemButton;
	
	/**
	 * the push button for browsing eclipse's workspace
	 */
	private Button	browseWorkspaceButton;
	
	/**
	 * Implements the selection listener attached to the push buttons.
	 */
	class MySelectionListener implements SelectionListener {

		/**
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			if( e.widget == browseFileSystemButton ) {
				browseFileSystem();
			}
			else if( e.widget == browseWorkspaceButton ) {
				browseWorkspace();
			}
		}

		/**
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			if( e.widget == browseFileSystemButton ) {
				browseFileSystem();
			}
			else if( e.widget == browseWorkspaceButton ) {
				browseWorkspace();
			}
		}
		
	}
	
	public void createContent(Composite parent, FormToolkit formToolkit) {
		super.createContent(parent, formToolkit);
		
		// Create controls and their associated layout data.
		FormData				formData;
		SelectionListener	selectionListener = new MySelectionListener();
		
		browseWorkspaceButton = formToolkit.createButton( parent, "Browse Workspace", 0 );
		formData = new FormData();
		formData.top = new FormAttachment( super.text, 0, SWT.BOTTOM );
		formData.left = new FormAttachment( 0, 0 );
		browseWorkspaceButton.setLayoutData( formData );
		browseWorkspaceButton.addSelectionListener( selectionListener );
		
		browseFileSystemButton = formToolkit.createButton( parent, "Browse File System", 0 );
		formData = new FormData();
		formData.top = new FormAttachment( super.text, 0, SWT.BOTTOM );
		formData.left = new FormAttachment( browseWorkspaceButton, 0, SWT.RIGHT );
		browseFileSystemButton.setLayoutData( formData );
		browseFileSystemButton.addSelectionListener( selectionListener );
	}

	/**
	 * @see eclox.ui.editor.form.settings.editors.TextEditor#dispose()
	 */
	public void dispose() {
		// Local cleaning.
		browseFileSystemButton.dispose();
		browseWorkspaceButton.dispose();
		
		// Default cleaning.
		super.dispose();
	}
	
	/**
	 * Browses the workspace for a path and updates the managed text field.
	 */
	private void browseWorkspace() {
		IWorkspaceRoot				root = ResourcesPlugin.getWorkspace().getRoot();
		ContainerSelectionDialog		dialog = new ContainerSelectionDialog( super.text.getShell(), root, false, "Please, select a folder." );
		
		Object[]		result;
		dialog.open();
		result = dialog.getResult();
		if( result != null && result.length >= 1 ) {
			Path	 		path = (Path) result[0];
			IContainer	container = (IContainer) root.findMember( path );
			super.text.setText( container.getLocation().toOSString() );
		}
	}

	/**
	 * Browses the file system for a path and updates the managed text field.
	 */
	private void browseFileSystem() {
		DirectoryDialog	dialog = new DirectoryDialog( super.text.getShell() );
		String			path;
		
		path = dialog.open();
		if( path != null ) {
			super.text.setText( path );
		}
	}
}
