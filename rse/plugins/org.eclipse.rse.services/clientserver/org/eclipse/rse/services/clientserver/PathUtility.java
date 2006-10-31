/********************************************************************************
 * Copyright (c) 2006 IBM Corporation and Wind River Systems, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Initial Contributors:
 * The following IBM employees contributed to the Remote System Explorer
 * component that contains this file: David McKnight, Kushal Munir, 
 * Michael Berger, David Dykstal, Phil Coulthard, Don Yantzi, Eric Simpson, 
 * Emily Bruner, Mazen Faraj, Adrian Storisteanu, Li Ding, and Kent Hawley.
 * 
 * Contributors:
 * Martin Oberhuber (Wind River) - Fix 161844 - regex matching backslashes
 * Martin Oberhuber (Wind River) - Fix 162781 - normalize without replaceAll()
 * Martin Oberhuber (Wind River) - Use pre-compiled regex Pattern
 ********************************************************************************/

package org.eclipse.rse.services.clientserver;

import java.util.regex.Pattern;

import org.eclipse.rse.services.clientserver.archiveutils.AbsoluteVirtualPath;
import org.eclipse.rse.services.clientserver.archiveutils.ArchiveHandlerManager;

public class PathUtility 
{
	//Regex pattern: / or \\
	private static Pattern badSlashPatternWin=Pattern.compile("/|\\\\\\\\"); //$NON-NLS-1$

	public static String normalizeWindows(String path)
	{
		if (path == null || path.length() < 2) {
			return path;
		}
		boolean endsWithSlash = (path.endsWith("\\") || path.endsWith("/"));
		if (badSlashPatternWin.matcher(path).find()) {
			//Replace /->\, then replace \\->\
			StringBuffer buf = new StringBuffer(path.length());
			boolean foundBackslash=false;
			for (int i=0; i<path.length(); i++) {
				char c = path.charAt(i);
				if (c=='/') {
					c='\\';
				}
				if (c=='\\') {
					if (!foundBackslash) {
						foundBackslash=true;
						buf.append(c);
					}
				} else {
					foundBackslash=false;
					buf.append(c);
				}
			}
			if (endsWithSlash && buf.length()!=3) {
				buf.deleteCharAt(buf.length()-1);
			}
			path = buf.toString();
		} else if (endsWithSlash && path.length()!=3) {
			//remove trailing slash only
			path = path.substring(0, path.length() - 1);			
		}
		return path;
	}

	//Regex pattern: \ or //
	private static Pattern badSlashPatternUnix=Pattern.compile("\\\\|//"); //$NON-NLS-1$

	public static String normalizeUnix(String path)
	{
		if (path == null || path.length() < 2) {
			return path;
		}
		boolean endsWithSlash = (path.endsWith("\\") || path.endsWith("/"));
		if (badSlashPatternUnix.matcher(path).find()) {
			//Replace \->/, then replace //->/
			StringBuffer buf = new StringBuffer(path.length());
			boolean foundSlash=false;
			for (int i=0; i<path.length(); i++) {
				char c = path.charAt(i);
				if (c=='\\') {
					c='/';
				}
				if (c=='/') {
					if (!foundSlash) {
						foundSlash=true;
						buf.append(c);
					}
				} else {
					foundSlash=false;
					buf.append(c);
				}
			}
			if (endsWithSlash && buf.length()!=1) {
				buf.deleteCharAt(buf.length()-1);
			}
			path = buf.toString();
		} else if (endsWithSlash && path.length()!=1) {
			//remove trailing slash only
			path = path.substring(0, path.length() - 1);			
		}
		return path;
	}
	
	public static String normalizeVirtualWindows(String path)
	{
		if (path == null || path.length() < 2) return path;
		AbsoluteVirtualPath avp = new AbsoluteVirtualPath(path);
		String realPart = avp.getContainingArchiveString();
		if (ArchiveHandlerManager.isVirtual(realPart))
			realPart = normalizeVirtualWindows(realPart);
		else realPart = normalizeWindows(realPart);
		return realPart + ArchiveHandlerManager.VIRTUAL_SEPARATOR + avp.getVirtualPart();
	}
	
	public static String normalizeVirtualUnix(String path)
	{
		if (path == null || path.length() < 2) return path;
		AbsoluteVirtualPath avp = new AbsoluteVirtualPath(path);
		String realPart = avp.getContainingArchiveString();
		if (ArchiveHandlerManager.isVirtual(realPart))
			realPart = normalizeVirtualUnix(realPart);
		else realPart = normalizeUnix(realPart);
		return realPart + ArchiveHandlerManager.VIRTUAL_SEPARATOR + avp.getVirtualPart();		
	}
	
	public static String normalizeUnknown(String path)
	{
		if (path == null || path.length() < 2) return path;
		if (path.charAt(1) == ':')
			if (path.indexOf(ArchiveHandlerManager.VIRTUAL_CANONICAL_SEPARATOR) == -1)
				return normalizeWindows(path);
			else return normalizeVirtualWindows(path);
		else if (path.charAt(0) == '/')
			if (path.indexOf(ArchiveHandlerManager.VIRTUAL_CANONICAL_SEPARATOR) == -1)
				return normalizeUnix(path);
			else return normalizeVirtualUnix(path);
		else return path;
	}
	
	public static String getSeparator(String path)
	{
		if (path.length() > 1 && path.charAt(1) == ':')
		{
			return "\\"; //$NON-NLS-1$
		}
		else
		{
			return "/"; //$NON-NLS-1$
		}
	}
}