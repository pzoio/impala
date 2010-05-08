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

package org.impalaframework.jmx.spring.mx4j;

import mx4j.tools.adaptor.http.HttpAdaptor;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MX4JHttpAdaptorListener implements InitializingBean, DisposableBean {

    private HttpAdaptor httpAdaptor;
    
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(httpAdaptor, "httpAdaptor cannot be null");
        httpAdaptor.start();
    }

    public void destroy() throws Exception {
        httpAdaptor.stop();
    }

    public void setHttpAdaptor(HttpAdaptor httpAdaptor) {
        this.httpAdaptor = httpAdaptor;
    }

}
