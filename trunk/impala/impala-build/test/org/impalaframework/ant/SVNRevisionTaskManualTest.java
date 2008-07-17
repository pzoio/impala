package org.impalaframework.ant;

import org.apache.tools.ant.Project;

import junit.framework.TestCase;

public class SVNRevisionTaskManualTest extends TestCase {

	public void testExecute() {
		SVNRevisionTask task = new SVNRevisionTask();
		task.setUrl("http://impala.googlecode.com/svn");

		Project project = new Project();
		task.setProject(project);
		
		task.execute();
		System.out.println(project.getProperty(SVNRevisionTask.DEFAULT_REVISION_PROPERTY));
	}

}
