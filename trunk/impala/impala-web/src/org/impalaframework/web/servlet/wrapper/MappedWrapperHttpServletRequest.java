package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extension of {@link HttpServletRequestWrapper} which provides implementations for 
 * servlet path related methods. Note that if servlet path supplied in the constructor
 * is not valid (in the sense that, added to context path, it should be a substring of the request
 * URI), then the supplied servlet path value will be ignored.
 * 
 * @author Phil Zoio
 */
public class MappedWrapperHttpServletRequest extends
        HttpServletRequestWrapper {
    
    private static final Log logger = LogFactory.getLog(ModuleAwareWrapperHttpServletRequest.class);
    
    private ServletContext servletContext;
    
    private String servletPath;
    
    private String pathInfo;

    public MappedWrapperHttpServletRequest(HttpServletRequest request, ServletContext servletContext, String servletPath) {
        super(request);
        this.servletContext = servletContext;
        
        if (servletPath != null) {
            String contextPathPlusServletPath = request.getContextPath() + servletPath;
            String uri = request.getRequestURI();
            
            if (uri.startsWith(contextPathPlusServletPath)) {
                this.servletPath = servletPath;
                this.pathInfo = uri.substring(contextPathPlusServletPath.length());
            } else {
                logger.warn("URI does not start with context plus servlet path combination: " + contextPathPlusServletPath);
            }
        }
    }

    /**
     * If valid servlet path is provided, then this is returned. Otherwise, delegates to wrapped {@link #getServletPath()}
     */
    @Override
    public String getServletPath() {
        if (servletPath != null) {
            return servletPath;
        }
        return super.getServletPath();
    }
    
    /**
     * If valid servlet path is provided, the remainder of URI is returned. Otherwise, delegates to wrapped {@link #getPathInfo()}
     */
    @Override
    public String getPathInfo() {
        if (pathInfo != null) {
            return pathInfo;
        }
        return super.getPathInfo();
    }
    
    /**
     * If valid servlet path is provided, then real path associated with path info is returned. 
     * Otherwise, delegates to wrapped {@link #getPathInfo()}.
     * 
     * See {@link ServletContext#getRealPath(String)}
     */
    @Override
    public String getPathTranslated() {
        if (pathInfo != null) {
            return servletContext.getRealPath(pathInfo);
        }
        return super.getPathTranslated();
    }
 
}
