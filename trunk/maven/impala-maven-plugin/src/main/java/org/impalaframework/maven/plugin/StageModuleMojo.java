package org.impalaframework.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Goal which makes module available to a pre-configured directory
 * 
 * @goal stage-module
 * 
 * @phase package
 * @requiresDependencyResolution
 * @requiresProject
 */
public class StageModuleMojo extends AbstractMojo {
    
    /** @parameter default-value="${project}" */
    private org.apache.maven.project.MavenProject project;
    
    /**
     * Location of the file.
     * @parameter expression="${module.staging.directory}"
     */
    private String moduleStagingDirectory;
    
    public void execute() throws MojoExecutionException {

        final Log log = getLog();
        
        if ("jar".equals(project.getPackaging())) {

            moduleStagingDirectory = MojoUtils.getModuleStagingDirectory(project, moduleStagingDirectory);

            //copying module to staging directory
            
            final File file = project.getArtifact().getFile();
            
            if (log.isInfoEnabled()) {
                log.info("Copying file " + file.getAbsolutePath() + " to module staging directory: " + moduleStagingDirectory);
            }
            
            final File targetDirectory = new File(moduleStagingDirectory);
            try {
                FileUtils.forceMkdir(targetDirectory);
            }
            catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            MojoUtils.copyFile(file, targetDirectory, file.getName());

        } else {
            System.out.println("Not a jar!!!!!!!");
        }
    }
}
