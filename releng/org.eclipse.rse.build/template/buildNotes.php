<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="http://www.eclipse.org/default_style.css" type="text/css">
<title>Build Notes for TM @buildId@</title>
</head>

<body>
<table border="0" cellspacing="5" cellpadding="2" width="100%">
	<tr>
		<td align="LEFT" width="80%">
		<p><b><font class=indextop>Build Notes for TM @buildId@</font></b><br>
		@dateLong@ </p>
		</td>
	</tr>
</table>
<table border="0" cellspacing="5" cellpadding="2" width="100%">
	<tr>
		<td align="LEFT" valign="TOP" colspan="3" bgcolor="#0080C0"><b>
		<font face="Arial,Helvetica" color="#FFFFFF">New and Noteworthy</font></b></td>
	</tr>
</table>
<table><tbody><tr><td>
<ul>
<li>TM 2.0M5 <b>requires Eclipse 3.3M5</b>. Platform Runtime is the minimum
  requirement for dstore and ftp. Terminal-ssh and RSE-ssh also require the
  <b>CVS Client Runtime</b> (The dependency on CVS Client Runtime should go away
  again with M6 by resolving <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=175686">bug 175686</a>).</li>
<li>Added a <b>milestone update site</b> for TM 2.0 milestone builds at
  <a href="http://download.eclipse.org/dsdp/tm/updates/milestones">http://download.eclipse.org/dsdp/tm/updates/milestones</a>.
  "Check for updates" will automatically reference this milestone update site, so you can get milestone
  patches from there [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=175241">175241</a>].</li>
<li><b>Apache Commons.Net and ORO</b> are now distributed as single-jar bundles. The separate commons.net and ORO features 
have been removed. The bundles are now directly included in the RSE FTP feature only.</li>
<li><b>SystemRemoteResourceDialog.setPreSelection()</b> now works correctly [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=174944">174944</a>].
<li>Use 
  <a href="https://bugs.eclipse.org/bugs/buglist.cgi?query_format=advanced&classification=DSDP&product=Target+Management&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&resolution=FIXED&resolution=WONTFIX&resolution=INVALID&resolution=WORKSFORME&chfieldfrom=2007-02-25&chfieldto=2007-04-08&chfield=resolution&cmdtype=doit">
  <!-- <a href="https://bugs.eclipse.org/bugs/buglist.cgi?query_format=advanced&classification=DSDP&product=Target+Management&target_milestone=2.0+M6&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&resolution=FIXED&resolution=WONTFIX&resolution=INVALID&resolution=WORKSFORME&cmdtype=doit">  -->
  this query</a> to show the list of bugs fixed since the last milestone,
  <a href="http://download.eclipse.org/dsdp/tm/downloads/drops/S-2.0M5-200702240204/index.php">
  TM 2.0M5</a>
  [<a href="http://download.eclipse.org/dsdp/tm/downloads/drops/S-2.0M5-200702240204/buildNotes.php">build notes</a>].</li>
<li>For details on checkins, see the
  <a href="http://download.eclipse.org/dsdp/tm/downloads/drops/N-changelog/index.html">
  RSE CVS changelog</a>, and the
  <a href="http://download.eclipse.org/dsdp/tm/downloads/drops/N-changelog/core/index.html">
  TM Core CVS changelog</a>.</li>
<li>For other questions, please check the
  <a href="http://wiki.eclipse.org/index.php/TM_and_RSE_FAQ">TM and RSE FAQ</a>
  as well as the
  <a href="http://wiki.eclipse.org/index.php/TM_2.0_Known_Issues_and_Workarounds">
  TM 2.0 Known Issues and Workarounds</a>.</li>
</ul>
</td></tr></tbody></table>

<table border="0" cellspacing="5" cellpadding="2" width="100%">
	<tr>
		<td align="LEFT" valign="TOP" colspan="3" bgcolor="#0080C0"><b>
		<font face="Arial,Helvetica" color="#FFFFFF">Getting Started</font></b></td>
	</tr>
</table>
<table><tbody><tr><td>
<p>The RSE User Documentation now has a
<a href="http://dsdp.eclipse.org/help/latest/index.jsp?topic=/org.eclipse.rse.doc.user/gettingstarted/g_start.html">
Tutorial</a> that guides you through installation, first steps,
connection setup and important tasks.</p>
<p>
If you want to know more about future directions of the Target Management
Project, developer documents, architecture or how to get involved,<br/>
the online
<a href="http://www.eclipse.org/dsdp/tm/tutorial/index.php">Getting Started page</a>
as well as the
<a href="http://wiki.eclipse.org/index.php/TM_and_RSE_FAQ">TM and RSE FAQ</a>
are the best places for you to get started.
</p>
</td></tr></tbody></table>

<table border="0" cellspacing="5" cellpadding="2" width="100%">
	<tr>
		<td align="LEFT" valign="TOP" colspan="3" bgcolor="#0080C0"><b>
		<font face="Arial,Helvetica" color="#FFFFFF">API Status</font></b></td>
	</tr>
</table>
<table><tbody><tr><td>
<p>An important part of the <a href="http://www.eclipse.org/dsdp/tm/development/tm_project_plan_2_0.html">Target Management 2.0 Project Plan</a>
is to harden the APIs which were provisional by RSE 1.0. Naturally, this requires
API changes, and especially moving lots of classes which we cannot guarantee to 
support in the future into internal packages.</p> 
<p>We are committed to not introducing any incompatible
API changes on the RSE 1.0 maintenance stream (1.0.x), but we need to 
change the API for TM 2.0 in a not backward compatible way.<br/>
All such API changes are voted on
by committers on the <a href="https://dev.eclipse.org/mailman/listinfo/dsdp-tm-dev">
dsdp-tm-dev</a> developer mailing list, and documented in a migration guide
for future releases.</p>
<p>Currently, we see the following areas for more potential API changes:
<ul>
  <li>The <tt>IConnectorService</tt> interface may be slightly modified
   in order to allow for better UI / Non-UI separation.</li>
  <li>Some more RSE Model classes may be moved from the UI plugin to the 
   non-UI core plugin.</li>
</ul>
</td></tr></tbody></table>

<table border="0" cellspacing="5" cellpadding="2" width="100%">
	<tr>
		<td align="LEFT" valign="TOP" colspan="3" bgcolor="#808080"><b>
		<font face="Arial,Helvetica" color="#FFFFFF">API Changes since RSE 1.0.1 - newest changest first</font></b></td>
	</tr>
</table>
<table><tbody><tr><td>
The following lists those API changes that are not backward compatible and require
user attention. A short hint on what needs to change is given directly in the list.
More information can be found in the associated bugzilla items.
<ul><li>TM 2.0M6 API Changes
<ul>
<li><b>Cleaned up ISystemRegistry</b> - removed or changed signature of several methods [<a href="https://bugs.eclipse.org/bugs/showdependencytree.cgi?id=175680">175680</a>].</li>
<li><b>Renamed getAdapter(Object) -&gt; getViewAdapter(Object)</b> on several classes [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=175150">175150</a>].</li>
<li><b>Refactored IConnectorService</b> and moved to Core [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=168977">168977</a>].</li>
<li>[Just additions?] <b>Support Menu Configuration in RSESystemTypeAdapter</b> [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=161195">161195</a>].</li>
</ul></li>
<li>TM 2.0M5 API Changes
<ul>
<li><b>Modified Extension Point subsystemConfigurations</b> to reference system types by ID rather than
by name. Client code needs to use the <b><i>systemTypeIds</i></b> tag now rather than <samp>systemTypes</samp>
[<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=162081">162081</a>].</li>
<li><b>Removed Extension Points for Popup Menus and Property Pages:</b> The RSE-specific extension points
<samp>org.eclipse.rse.ui.propertyPages</samp> and <samp>org.eclipse.rse.ui.popupMenus</samp> were removed
because newer Eclipse versions have all required functionality in the base extension points already. 
Example code, tutorials and ISV docs were updated and have the required information for how to migrate
to the new extension points. [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=172651">172651</a>].</li>
<li><b>Moved extension point</b> <samp>org.eclipse.rse.ui.archiveHandlers</samp> to 
<samp><a href="http://dsdp.eclipse.org/help/latest/topic/org.eclipse.rse.doc.isv/reference/extension-points/org_eclipse_rse_services_archivehandlers.html">org.eclipse.rse.services.archiveHandlers</a></samp>
[<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=173871">173871</a>].
<li><b>newConnectionWizards:</b> The old <samp>newConnectionWizardDelegates</samp> extension point
 has been replaced by a new <samp><a href="http://dsdp.eclipse.org/help/latest/topic/org.eclipse.rse.doc.isv/reference/extension-points/org_eclipse_rse_ui_newConnectionWizards.html">newConnectionWizards</a></samp>
 extension point for more flexibility [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=173772">173772</a>].</li>
<li><b>Deep Filtering:</b> The SystemView now supports Context Information for queries of multiple filters to the same remote elements.
  That is, the SystemViewElementAdapter.getChildren() method can know the filer context in which a query is made.
  This is necessary in order to properly support "deep filtering" by file types. An API change was required 
  to get this implemented: <b>Existing code needs to change</b> two method signatures in classes deriving from
  AbstractSystemViewAdapter. For details, see [<a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=170627">170627</a>].</li>
<li><b>Making classes internal:</b> Many classes have been made internal, and PDE support for generating
  warnings of discourage access has been enabled. Client code should not have used the classes which are
  now internal anyways; but if you do, just running "organize imports" should make your code compile again.
  For details, see [<a href="https://bugs.eclipse.org/bugs/showdependencytree.cgi?id=170922">170922</a>].</li>
</ul></li>
</ul>

Use 
  <a href="https://bugs.eclipse.org/bugs/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=%5Bapi%5D&classification=DSDP&product=Target+Management&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&resolution=FIXED&resolution=WONTFIX&resolution=INVALID&resolution=WORKSFORME&chfieldfrom=2006-12-24&chfieldto=2007-07-01&chfield=resolution&cmdtype=doit">
  <!-- <a href="https://bugs.eclipse.org/bugs/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=%5Bapi%5D&classification=DSDP&product=Target+Management&target_milestone=2.0+M5&target_milestone=2.0+M6&target_milestone=2.0+M7&target_milestone=2.0+RC1&target_milestone=2.0&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&resolution=FIXED&resolution=WONTFIX&resolution=INVALID&resolution=WORKSFORME&cmdtype=doit">  -->
  this query</a> to show the full list of API changes since RSE 1.0.1.
</td></tr></tbody></table>

<table border="0" cellspacing="5" cellpadding="2" width="100%">
	<tr>
		<td align="LEFT" valign="TOP" colspan="3" bgcolor="#0080C0"><b>
		<font face="Arial,Helvetica" color="#FFFFFF">Known Problems and Workarounds</font></b></td>
	</tr>
</table>
<table><tbody><tr><td>
<!--
The following critical or major bugs are currently known.
We'll strive to fix these as soon as possible.
<ul>
  <li><a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=162993">bug 162993</a> - maj - ssh connection gets confused</li>
</ul>
-->
<p>No major or critical bugs are known at the time of release.
Use 
<a href="https://bugs.eclipse.org/bugs/buglist.cgi?query_format=advanced&classification=DSDP&product=Target+Management&bug_status=UNCONFIRMED&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&bug_severity=blocker&bug_severity=critical&bug_severity=major&cmdtype=doit">this query</a>
for an up-to-date list of major or critical bugs.</p>

<p>The 
<a href="http://wiki.eclipse.org/index.php/TM_2.0_Known_Issues_and_Workarounds">
TM 2.0 Known Issues and Workarounds</a> Wiki page gives an up-to-date list
of the most frequent and obvious problems, and describes workarounds for them.<br/>
If you have other questions regarding TM or RSE, please check the
<a href="http://wiki.eclipse.org/index.php/TM_and_RSE_FAQ">TM and RSE FAQ</a>
</p>

<p>Click 
<a href="https://bugs.eclipse.org/bugs/report.cgi?x_axis_field=bug_severity&y_axis_field=op_sys&z_axis_field=&query_format=report-table&classification=DSDP&product=Target+Management&bug_status=UNCONFIRMED&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&format=table&action=wrap">here</a>
for a complete up-to-date bugzilla status report, or
<a href="https://bugs.eclipse.org/bugs/report.cgi?x_axis_field=bug_severity&y_axis_field=op_sys&z_axis_field=&query_format=report-table&classification=DSDP&product=Target+Management&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&format=table&action=wrap">here</a>
for a report on bugs fixed so far.
</p>
</td></tr></tbody></table>

</body>
</html>
