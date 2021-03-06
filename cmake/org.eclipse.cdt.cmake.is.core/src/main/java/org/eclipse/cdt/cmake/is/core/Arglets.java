/*******************************************************************************
 * Copyright (c) 2015-2019 Martin Weber.
 *
 * Content is provided to you under the terms and conditions of the Eclipse Public License Version 2.0 "EPL".
 * A copy of the EPL is available at http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.cdt.cmake.is.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.cmake.is.core.IArglet.IParseContext;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.util.CDataUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Various Arglet implementation for parsing tool arguments.
 *
 * @author Martin Weber
 */
public class Arglets {
	private static final String EMPTY_STR = ""; //$NON-NLS-1$

	/** matches a macro name, with optional macro parameter list */
	@SuppressWarnings("nls")
	private static final String REGEX_MACRO_NAME = "([\\w$]+)(?:\\([\\w$, ]*?\\))?";
	/**
	 * matches a macro name, skipping leading whitespace. Name in matcher group 1
	 */
	@SuppressWarnings("nls")
	private static final String REGEX_MACRO_NAME_SKIP_LEADING_WS = "\\s*" + REGEX_MACRO_NAME;
	/**
	 * matches a macro argument in quotes, skipping leading whitespace. Quote
	 * character in matcher group 1, Name in matcher group 2
	 */
	@SuppressWarnings("nls")
	private static final String REGEX_MACRO_ARG_QUOTED__SKIP_LEADING_WS = "\\s*([\"'])" + REGEX_MACRO_NAME;
	/** matches an include path with quoted directory. Name in matcher group 2 */
	@SuppressWarnings("nls")
	private static final String REGEX_INCLUDEPATH_QUOTED_DIR = "\\s*([\"'])(.+?)\\1";
	/**
	 * matches an include path with unquoted directory. Name in matcher group 1
	 */
	@SuppressWarnings("nls")
	private static final String REGEX_INCLUDEPATH_UNQUOTED_DIR = "\\s*([^\\s]+)";

	/**
	 * nothing to instantiate
	 */
	private Arglets() {
	}

	////////////////////////////////////////////////////////////////////
	// Matchers for options
	////////////////////////////////////////////////////////////////////
	/**
	 * A matcher for option names. Includes information of the matcher groups that
	 * hold the option name.
	 *
	 * @author Martin Weber
	 */
	public static class NameOptionMatcher {
		final Matcher matcher;
		final int nameGroup;

		/**
		 * Constructor.
		 *
		 * @param pattern   - regular expression pattern being parsed by the parser.
		 * @param nameGroup - capturing group number defining name of an entry.
		 */
		public NameOptionMatcher(String pattern, int nameGroup) {
			this.matcher = Pattern.compile(pattern).matcher(EMPTY_STR);
			this.nameGroup = nameGroup;
		}

		@SuppressWarnings("nls")
		@Override
		public String toString() {
			return "NameOptionMatcher [matcher=" + this.matcher + ", nameGroup=" + this.nameGroup + "]";
		}
	}

	/**
	 * A matcher for preprocessor define options. Includes information of the
	 * matcher groups that hold the macro name and value.
	 *
	 * @author Martin Weber
	 */
	public static class NameValueOptionMatcher extends NameOptionMatcher {
		/**
		 * the number of the value group, or {@code -1} for a pattern that does not
		 * recognize a macro value
		 */
		private final int valueGroup;

		/**
		 * Constructor.
		 *
		 * @param pattern    - regular expression pattern being parsed by the parser.
		 * @param nameGroup  - capturing group number defining name of an entry.
		 * @param valueGroup - capturing group number defining value of an entry.
		 */
		/**
		 * @param pattern
		 * @param nameGroup
		 * @param valueGroup the number of the value group, or {@code -1} for a pattern
		 *                   that does not recognize a macro value
		 */
		public NameValueOptionMatcher(String pattern, int nameGroup, int valueGroup) {
			super(pattern, nameGroup);
			this.valueGroup = valueGroup;
		}

		@SuppressWarnings("nls")
		@Override
		public String toString() {
			return "NameValueOptionMatcher [matcher=" + this.matcher + ", nameGroup=" + this.nameGroup + ", valueGroup="
					+ this.valueGroup + "]";
		}
	}

	////////////////////////////////////////////////////////////////////
	// generic option parsers
	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a C-compiler macro definition
	 * argument.
	 */
	public static abstract class MacroDefineGeneric {

		protected final int processArgument(IParseContext parseContext, String args,
				NameValueOptionMatcher[] optionMatchers) {
			for (NameValueOptionMatcher oMatcher : optionMatchers) {
				final Matcher matcher = oMatcher.matcher;

				matcher.reset(args);
				if (matcher.lookingAt()) {
					final String name = matcher.group(oMatcher.nameGroup);
					final String value = oMatcher.valueGroup == -1 ? null : matcher.group(oMatcher.valueGroup);
					final ICLanguageSettingEntry entry = CDataUtil.createCMacroEntry(name, value,
							ICSettingEntry.READONLY);
					parseContext.addSettingEntry(entry);
					final int end = matcher.end();
					return end;
				}
			}
			return 0;// no input consumed
		}
	}

	/**
	 * A tool argument parser capable to parse a C-compiler macro cancel argument.
	 */
	public static class MacroUndefineGeneric {

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgument(java.util.List, java.lang.String)
		 */
		protected final int processArgument(IParseContext parseContext, String argsLine,
				NameOptionMatcher optionMatcher) {
			final Matcher oMatcher = optionMatcher.matcher;

			oMatcher.reset(argsLine);
			if (oMatcher.lookingAt()) {
				final String name = oMatcher.group(1);
				final ICLanguageSettingEntry entry = CDataUtil.createCMacroEntry(name, null,
						ICSettingEntry.UNDEFINED | ICSettingEntry.READONLY);
				parseContext.addSettingEntry(entry);
				final int end = oMatcher.end();
				return end;
			}
			return 0;// no input consumed
		}
	}

	/**
	 * A tool argument parser capable to parse a C-compiler include path argument.
	 */
	public static abstract class IncludePathGeneric {
		/**
		 * @param cwd the current working directory of the compiler at its invocation
		 * @see org.eclipse.cdt.cmake.is.core.IArglet#processArgument(IParseContext,
		 *      IPath, String)
		 */
		protected final int processArgument(IParseContext parseContext, IPath cwd, String argsLine,
				NameOptionMatcher[] optionMatchers) {
			for (NameOptionMatcher oMatcher : optionMatchers) {
				final Matcher matcher = oMatcher.matcher;

				matcher.reset(argsLine);
				if (matcher.lookingAt()) {
					String name = matcher.group(oMatcher.nameGroup);
					// workaround for relative path by cmake bug
					// https://gitlab.kitware.com/cmake/cmake/issues/13894 : prepend cwd
					IPath path = Path.fromOSString(name);
					if (!path.isAbsolute()) {
						// prepend CWD
						name = cwd.append(path).toOSString();
					}

					final ICLanguageSettingEntry entry = CDataUtil.createCIncludePathEntry(name,
							ICSettingEntry.READONLY);
					parseContext.addSettingEntry(entry);
					final int end = matcher.end();
					return end;
				}
			}
			return 0;// no input consumed
		}
	}

	////////////////////////////////////////////////////////////////////
	// POSIX compatible option parsers
	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a POSIX compatible C-compiler macro
	 * definition argument: {@code -DNAME=value}.
	 */
	public static class MacroDefine_C_POSIX extends MacroDefineGeneric implements IArglet {

		@SuppressWarnings("nls")
		private static final NameValueOptionMatcher[] optionMatchers = {
				/* string or char literal value, with whitespace in value and escaped quotes */
				new NameValueOptionMatcher("-D" + REGEX_MACRO_NAME_SKIP_LEADING_WS + "=(" + "([\"'])" // the quote char
																										// in group 3
						+ "(?:" // non-capturing
						+ "\\\\\\\\" // the escaped escape char
						+ "|" // OR
						+ "\\\\\\3" // the escaped quote char
						+ "|" // OR
						+ "(?!\\3)." // any character except the quote char
						+ ")*" // zero or more times
						+ "\\3" // the quote char
						+ ")", 1, 2),
				/* macro name only, w/ optional macro arglist */
				new NameValueOptionMatcher("-D" + REGEX_MACRO_NAME_SKIP_LEADING_WS + "=((\\S+))", 1, 3),
				/* separated, quoted name-value arg, whitespace in value */
				new NameValueOptionMatcher("-D" + REGEX_MACRO_ARG_QUOTED__SKIP_LEADING_WS + "=((.+?))\\1", 2, 4),
				/* macro name only */
				new NameValueOptionMatcher("-D" + REGEX_MACRO_NAME_SKIP_LEADING_WS, 1, -1), };

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgs(java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, argsLine, optionMatchers);
		}

	}

	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a POSIX compatible C-compiler macro
	 * cancel argument: {@code -UNAME}.
	 */
	public static class MacroUndefine_C_POSIX extends MacroUndefineGeneric implements IArglet {

		@SuppressWarnings("nls")
		private static final NameOptionMatcher optionMatcher = new NameOptionMatcher(
				"-U" + REGEX_MACRO_NAME_SKIP_LEADING_WS, 1);

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgument(java.util.List, java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, argsLine, optionMatcher);
		}
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a POSIX compatible C-compiler include
	 * path argument: {@code -Ipath}.
	 */
	public static class IncludePath_C_POSIX extends IncludePathGeneric implements IArglet {
		@SuppressWarnings("nls")
		private static final NameOptionMatcher[] optionMatchers = {
				/* quoted directory */
				new NameOptionMatcher("-I" + REGEX_INCLUDEPATH_QUOTED_DIR, 2),
				/* unquoted directory */
				new NameOptionMatcher("-I" + REGEX_INCLUDEPATH_UNQUOTED_DIR, 1) };

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgs(java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, cwd, argsLine, optionMatchers);
		}
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a C-compiler system include path
	 * argument: {@code -system path}.
	 */
	public static class SystemIncludePath_C extends IncludePathGeneric implements IArglet {
		@SuppressWarnings("nls")
		static final NameOptionMatcher[] optionMatchers = {
				/* quoted directory */
				new NameOptionMatcher("-isystem" + REGEX_INCLUDEPATH_QUOTED_DIR, 2),
				/* unquoted directory */
				new NameOptionMatcher("-isystem" + REGEX_INCLUDEPATH_UNQUOTED_DIR, 1), };

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgs(java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, cwd, argsLine, optionMatchers);
		}
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a armcc-compiler system include path
	 * argument: {@code -Jdir}.
	 */
	public static class SystemIncludePath_armcc extends IncludePathGeneric implements IArglet {
		@SuppressWarnings("nls")
		static final NameOptionMatcher[] optionMatchers = {
				/* quoted directory */
				new NameOptionMatcher("-J" + "([\"'])(.+?)\\1", 2),
				/* unquoted directory */
				new NameOptionMatcher("-J" + "([^\\s]+)", 1), };

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgs(java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, cwd, argsLine, optionMatchers);
		}
	}

	////////////////////////////////////////////////////////////////////
	// POSIX compatible option parsers
	////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// compiler built-ins detection
	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse arguments from the command-line that
	 * affect built-in detection.
	 */
	public static abstract class BuiltinDetctionArgsGeneric {
		/**
		 * @see org.eclipse.cdt.cmake.is.core.IArglet#processArgument(IParseContext,
		 *      IPath, String)
		 */
		protected final int processArgument(IParseContext parseContext, String argsLine, Matcher[] optionMatchers) {
			for (Matcher matcher : optionMatchers) {
				matcher.reset(argsLine);
				if (matcher.lookingAt()) {
					parseContext.addBuiltinDetectionArgument(matcher.group());
					return matcher.end();
				}
			}
			return 0;// no input consumed
		}
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a GCC option to specify paths
	 * {@code --sysrooot}.
	 */
	public static class Sysroot_GCC extends BuiltinDetctionArgsGeneric implements IArglet {
		@SuppressWarnings("nls")
		private static final Matcher[] optionMatchers = {
				/* "--sysroot=" quoted directory */
				Pattern.compile("--sysroot=" + REGEX_INCLUDEPATH_QUOTED_DIR).matcher(EMPTY_STR),
				/* "--sysroot=" unquoted directory */
				Pattern.compile("--sysroot=" + REGEX_INCLUDEPATH_UNQUOTED_DIR).matcher(EMPTY_STR),
				/* "-isysroot=" quoted directory */
				Pattern.compile("-isysroot=" + REGEX_INCLUDEPATH_QUOTED_DIR).matcher(EMPTY_STR),
				/* "-isysroot=" unquoted directory */
				Pattern.compile("-isysroot=" + REGEX_INCLUDEPATH_UNQUOTED_DIR).matcher(EMPTY_STR),
				/* "--no-sysroot-prefix" */
				Pattern.compile("--no-sysroot-prefix").matcher(EMPTY_STR) };

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgs(java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, argsLine, optionMatchers);
		}
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * A tool argument parser capable to parse a GCC option to specify the language
	 * standard {@code -std=xxx}.
	 */
	public static class LangStd_GCC extends BuiltinDetctionArgsGeneric implements IArglet {
		@SuppressWarnings("nls")
		private static final Matcher[] optionMatchers = { Pattern.compile("-std=\\S+").matcher(EMPTY_STR),
				Pattern.compile("-ansi").matcher(EMPTY_STR), };

		/*-
		 * @see org.eclipse.cdt.cmake.is.IArglet#processArgs(java.lang.String)
		 */
		@Override
		public int processArgument(IParseContext parseContext, IPath cwd, String argsLine) {
			return processArgument(parseContext, argsLine, optionMatchers);
		}
	}

	////////////////////////////////////////////////////////////////////
}
