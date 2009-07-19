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

package org.impalaframework.web.spring.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.impalaframework.web.servlet.invocation.ModuleInvokerContributor;
import org.impalaframework.web.spring.integration.ModuleUrlPrefixContributor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Responsible for handling module URL mappings, both using the 'prefix' element, which maps
 * URLs by prefix to this module, and the 'suffix' element, which maps requests directed to this module
 * to combinations of filters and/or servlets.
 * @author Phil Zoio
 */
public class WebMappingBeanDefinitionParser implements BeanDefinitionParser {
    
    private static final String SUFFIX_ELEMENT = "suffix";
    private static final String PREFIX_ELEMENT = "prefix";

    /**
     * Path used to map an entry in a 'prefix' element to the current module
     */
    private static final String PATH_ATTRIBUTE = "path";
    
    /**
     * Whether to set the servlet path in the request passed to servlets or filters in this module. If 
     * true, and 'servletPath' attribute is not specified, then uses the mapping specified using the 'path'
     * attribute. If 'servletPath' attribute is specified, then uses this value.
     */
    private static final String SET_SERVLET_PATH_ATTRIBUTE = "setServletPath";
    
    /**
     * If a value is set for this optional attribute, 
     * then as long as 'setServletPath' is not set with a value of false, then sets 
     * the servlet path in the request passed to servlets or filters in this module.
     */
    private static final String SERVLET_PATH_ATTRIBUTE = "servletPath";
    
    /**
     * Used in the 'suffix' element to map an extension to a servlet and/or list of filters. Special values include:
     * <ul>
     * <li>* - denotes all extensions
     * </ul>[none] - denotes a path without an extension
     */
    private static final String EXTENSION_ATTRIBUTE = "extension";
    
    /**
     * Used in the 'suffix' element to name the servlet to which requests with URI extensions as specified using the
     * 'extension' attribute are mapped. Only a single named servlet may be specified.
     */
    private static final String SERVLET_NAME_ATTTRIBUTE = "servletName";
    
    /**
     * Used in the 'suffix' element to name the filters to which requests with URI extensions as specified using the
     * 'extension' attribute are mapped. A list of named filters may be specified. During invocation of the request,
     * each filter's {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
     * method is invoked in the order specified in the 'filterNames' attribute declaration.
     */
    private static final String FILTER_NAMES = "filterNames";
    
    /**
     * Refers to {@link ModuleUrlPrefixContributor#setPrefixMap(Map)}
     */
    private static final String PREFIX_MAP_PROPERTY = "prefixMap";

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        handlePrefixes(element, parserContext);
        handleSuffixes(element, parserContext);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void handlePrefixes(Element element, ParserContext parserContext) {
        List<Element> prefixes = DomUtils.getChildElementsByTagName(element, PREFIX_ELEMENT);
        
        Map<String,String> prefixMap = new LinkedHashMap<String, String>();
        
        for (Element prefixElement : prefixes) {
            String pathAttribute = prefixElement.getAttribute(PATH_ATTRIBUTE);
            String setServletPathAttribute = prefixElement.getAttribute(SET_SERVLET_PATH_ATTRIBUTE);
            String servletPathAttribute = prefixElement.getAttribute(SERVLET_PATH_ATTRIBUTE);
            
            String servletPath = getServletPath(pathAttribute, setServletPathAttribute, servletPathAttribute);
            
            prefixMap.put(pathAttribute.trim(), servletPath != null ? servletPath.trim() : null);
        }

        RootBeanDefinition definition = newContributorDefinition(prefixMap);
        registerDefinition(parserContext, definition);
    }

    String getServletPath(String pathAttribute, String setServletPathAttribute, String servletPathAttribute) {
        
        String servletPath = null;
        if (StringUtils.hasText(setServletPathAttribute)) {
            boolean setServletPath = Boolean.parseBoolean(setServletPathAttribute);
            if (setServletPath) {
                servletPath = (StringUtils.hasText(servletPathAttribute) ? servletPathAttribute : pathAttribute);
            }
        } else {
            boolean setServletPath = StringUtils.hasText(servletPathAttribute);
            if (setServletPath) {
                servletPath = servletPathAttribute;
            }
        }
        return servletPath;
    }

    RootBeanDefinition newContributorDefinition(Map<String, String> prefixMap) {
        RootBeanDefinition definition = new RootBeanDefinition(ModuleUrlPrefixContributor.class);
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue(PREFIX_MAP_PROPERTY, prefixMap);
        return definition;
    }

    @SuppressWarnings("unchecked")
    private void handleSuffixes(Element element, ParserContext parserContext) {
        
        List<Element> suffixes = DomUtils.getChildElementsByTagName(element, SUFFIX_ELEMENT);
        
        for (Element suffixElement : suffixes) {
            String extensionAttribute = suffixElement.getAttribute(EXTENSION_ATTRIBUTE);
            String servletNameAttribute = suffixElement.getAttribute(SERVLET_NAME_ATTTRIBUTE);
            String filterNamesAttribute = suffixElement.getAttribute(FILTER_NAMES);
           
            String[] filterNames = 
                StringUtils.hasText(filterNamesAttribute) ? 
                StringUtils.tokenizeToStringArray(filterNamesAttribute, " ,") :
                null;
            
            String servletName = StringUtils.hasText(servletNameAttribute) ? 
                    servletNameAttribute.trim() :
                    null;

            RootBeanDefinition definition = new RootBeanDefinition(ModuleInvokerContributor.class);
            MutablePropertyValues propertyValues = definition.getPropertyValues();
            propertyValues.addPropertyValue("suffix", extensionAttribute);
            propertyValues.addPropertyValue("servletName", servletName);
            propertyValues.addPropertyValue("filterNames", filterNames);
            
            registerDefinition(parserContext, definition);
        }
    }

    private void registerDefinition(ParserContext parserContext,
            RootBeanDefinition definition) {
        String beanName = parserContext.getReaderContext().generateBeanName(definition);
        parserContext.getRegistry().registerBeanDefinition(beanName, definition);
    }
}
