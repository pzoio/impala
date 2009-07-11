/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.servlet.invocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.integration.ModuleProxyUtils;
import org.impalaframework.web.servlet.invoker.HttpServiceInvoker;

/**
 * Implementation of {@link HttpServiceInvoker} responsible for invoking filters or servlets registered within the module.
 * Registration of filters or servlets within the module is based on suffix.
 * 
 * @author Phil Zoio
 */
public class ModuleHttpServiceInvoker implements HttpServiceInvoker {
    
    public static final String DEFAULT_SUFFIX = ModuleHttpServiceInvoker.class.getName() + ".DEFAULT_SUFFIX";

    /**
     * Mapping of extensions to filters. Note that filters are listed in invocation order
     */
    private Map<String,List<Filter>> filters = new LinkedHashMap<String, List<Filter>>();
    
    /**
     * Mapping of extensions to servlet. Only a single servlet can be mapped to an extension within 
     * a module
     */
    private Map<String,Servlet> servlets = new LinkedHashMap<String, Servlet>();
    
    /**
     * Determines suffix for incoming request. Then builds an invocation chain using the filters and servlets
     * which are mapped to the suffix for the module. Invokes this chain.
     *
     * If processing is complete, either through the request being handled by a servlet, or one of the filters
     * in the chain not invoking {@link FilterChain#doFilter(ServletRequest, ServletResponse)}, the method simply
     * returns after invoking the chain.
     * 
     * However, is processing is not complete (through absence of both the above conditions), then if a
     * {@link FilterChain} argument is supplied, this will be invoked.
     * 
     * In other words, if the request is not fully processed by the module, it is still possible for another
     * filter or even servlet to process the request. This however, is likely to be an unusual case: for the most
     * part, if a request directed to the module, it will be processed within that module.
     */
    public void invoke(
            HttpServletRequest request,
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        //check suffix of request
        String uri = request.getRequestURI();
        String suffix = ModuleProxyUtils.getSuffix(uri);
        if (suffix == null) {
            suffix = DEFAULT_SUFFIX;
        }
        
        //create invocation chain with 
        List<Filter> invocationFilters = filters.get(suffix);
        Servlet invocationServlet = servlets.get(suffix);
        InvocationChain chain = new InvocationChain(invocationFilters, invocationServlet);
        
        chain.invoke(request, response, filterChain);
        
        if (filterChain != null) {
            if (!chain.isComplete()) {
                filterChain.doFilter(request, response);
            }
        }
    }
    
    Map<String, List<Filter>> getFilters() {
        return Collections.unmodifiableMap(filters);
    }
    
    Map<String, Servlet> getServlets() {
        return Collections.unmodifiableMap(servlets);
    }
    
    public void setFilters(Map<String, List<Filter>> filters) {
        this.filters = filters;
    }
    
    public void setServlets(Map<String, Servlet> servlets) {
        this.servlets = servlets;
    }

}

/**
 * Implements Filter and Servlet invocation chain. 
 * Filters invoked in order (as long as each filter calls {@link FilterChain#doFilter(ServletRequest, ServletResponse)}.
 * Finally, {@link Servlet#service(ServletRequest, ServletResponse)} is invoked.
 * 
 * This allows for the same filter/servlet invocation orders as occurs in a web container.
 * The main difference, however, is that only filters/servlets defined within the module are invoked.
 * 
 * @author Phil Zoio
 */
class InvocationChain implements HttpServiceInvoker, FilterChain {
    
    private final List<Filter> filters = new ArrayList<Filter>();
    private final Servlet servlet;
    private int filterCount;
    
    private boolean incomplete;
    
    /**
     * Invocation chain consisting of zero or more filters, plus optional servlet
     * @param filters
     * @param servlet
     */
    public InvocationChain(
            List<Filter> filters, 
            Servlet servlet) {
        super();
        if (filters != null) {
            this.filters.addAll(filters);
        }
        this.servlet = servlet;
    }

    public void invoke(
            HttpServletRequest request,
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        if (filterCount < filters.size()) {
            int currentCount = filterCount;
            filterCount++;
            
            System.out.println("Getting filter for " + currentCount);
            Filter filter = filters.get(currentCount);
            filter.doFilter(request, response, this);
        }
        else if (servlet != null) {
            servlet.service(request, response);
        } else {
            incomplete = true;
        }
    }    
    
    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        
        invoke((HttpServletRequest)request, (HttpServletResponse)response, this);
        
    }
    
    public boolean isComplete() {
        return !incomplete;
    }
    
}
