/*******************************************************************************
 * Copyright (c) 2015, 2016 Institute for Software, HSR Hochschule fuer Technik
 * Rapperswil, University of applied sciences.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lukas Wegmann (IFS) - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.internal.core.index.composite.cpp;

import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPField;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPVariableInstance;
import org.eclipse.cdt.internal.core.index.IIndexFragmentBinding;
import org.eclipse.cdt.internal.core.index.composite.ICompositesFactory;

public class CompositeCPPFieldInstance extends CompositeCPPVariableInstance implements ICPPField {

	public CompositeCPPFieldInstance(ICompositesFactory cf, ICPPVariableInstance delegate) {
		super(cf, delegate);
	}

	@Override
	public ICompositeType getCompositeTypeOwner() {
		return getClassOwner();
	}

	@Override
	public int getVisibility() {
		return ((ICPPField) rbinding).getVisibility();
	}

	@Override
	public ICPPClassType getClassOwner() {
		return (ICPPClassType) cf.getCompositeBinding((IIndexFragmentBinding) ((ICPPField) rbinding).getClassOwner());
	}

	@Override
	public int getFieldPosition() {
		return ((ICPPField) rbinding).getFieldPosition();
	}
}
