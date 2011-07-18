/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.jmx.bootstrap;

public interface JMXBootstrapProperties {

    String EXPOSE_JMX_OPERATIONS = "expose.jmx.operations";
    boolean EXPOSE_JMX_OPERATIONS_DEFAULT = true;
   
    String EXPOSE_MX4J_ADAPTOR = "expose.mx4j.adaptor";
    boolean EXPOSE_MX4J_ADAPTOR_DEFAULT = false;
    
    String JMX_ADAPTOR_PORT = "jmx.adaptor.port";
    int JMX_ADAPTOR_PORT_DEFAULT = 8001;
    
    String JMX_LOCATE_EXISTING_SERVER = "jmx.locate.existing.server";
    boolean JMX_LOCATE_EXISTING_SERVER_DEFAULT = false;

    String JMX_PREFER_PLATFORM_MBEAN_SERVER = "jmx.prefer.platform.mbean.server";
    boolean JMX_PREFER_PLATFORM_MBEAN_SERVER_DEFAULT = false;

}
