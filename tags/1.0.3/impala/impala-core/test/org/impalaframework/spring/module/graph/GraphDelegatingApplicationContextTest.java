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

package org.impalaframework.spring.module.graph;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;

public class GraphDelegatingApplicationContextTest extends TestCase {

    private ApplicationContext delegate;
    private ApplicationContext dependencyOne;
    private ApplicationContext dependencyTwo;
    private GraphDelegatingApplicationContext parent;
    private List<ApplicationContext> dependencies;

    @Override
    protected void setUp() throws Exception {
        
        delegate = createMock(ApplicationContext.class);
        dependencyOne = createMock(ApplicationContext.class);
        dependencyTwo = createMock(ApplicationContext.class);
        
        dependencies = new ArrayList<ApplicationContext>();
        dependencies.add(dependencyOne);
        dependencies.add(dependencyTwo);
        parent = new GraphDelegatingApplicationContext(delegate, dependencies, true);
    }
    
    public void testNoDefinition() throws Exception {
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(false);
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(false);
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(false);

        replay(delegate, dependencyOne, dependencyTwo);
        
        try {
            parent.getBean("bean");
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }

        try {
            parent.getBean("bean", String.class);
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }

        try {
            parent.getBean("bean", "arg1");
        } catch (NoSuchBeanDefinitionException e) {
        }

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testWithDependencies1() throws Exception {
	    
	    expect(delegate.containsBean("bean")).andReturn(false);
	    expect(dependencyOne.containsBeanDefinition("bean")).andReturn(true);
	    expect(dependencyOne.getBean("bean")).andReturn("value");
	    
	    replay(delegate, dependencyOne, dependencyTwo);
	    
	    parent.getBean("bean");
	
	    verify(delegate, dependencyOne, dependencyTwo);
	}

	public void testWithDependencies2() throws Exception {
	    
	    expect(delegate.containsBean("bean")).andReturn(false);
	    expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
	    expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(true);
	    expect(dependencyTwo.getBean("bean", String.class)).andReturn("value");
	
	    replay(delegate, dependencyOne, dependencyTwo);
	    
	    parent.getBean("bean", String.class);
	
	    verify(delegate, dependencyOne, dependencyTwo);
	}

	public void testWithDependencies3() throws Exception {

        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(true);
        expect(dependencyTwo.getBean("bean", "arg")).andReturn("value");

        replay(delegate, dependencyOne, dependencyTwo);
		parent.getBean("bean", "arg");

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testGetContainingApplicationContextFromParent() throws Exception {
        
        expect(delegate.containsBeanDefinition("beanName")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        parent.getContainingApplicationContext("beanName");

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testGetContainingApplicationContextFromDelegate() throws Exception {
        
        expect(delegate.containsBeanDefinition("beanName")).andReturn(false);
        expect(delegate.getParent()).andReturn(null);
        expect(dependencyOne.containsBeanDefinition("beanName")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("beanName")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        assertSame(dependencyTwo, parent.getContainingApplicationContext("beanName"));

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testGetContainingApplicationNotParent() throws Exception {
        
        parent = new GraphDelegatingApplicationContext(delegate, dependencies, false);
        
        expect(dependencyOne.containsBeanDefinition("beanName")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("beanName")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        assertSame(dependencyTwo, parent.getContainingApplicationContext("beanName"));

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testDelegatingGetBean1() throws Exception {

        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.getBean("bean")).andReturn("value");
        
        replay(delegate, dependencyOne, dependencyTwo);

        parent.getBean("bean");
        
        verify(delegate, dependencyOne, dependencyTwo);
	}
    
    public void testDelegatingGetBean2() throws Exception {
        
        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.getBean("bean", String.class)).andReturn("value");
        
        replay(delegate, dependencyOne, dependencyTwo);

        parent.getBean("bean", String.class);
        
        verify(delegate, dependencyOne, dependencyTwo);
	}
    
    public void testDelegatingGetBean3() throws Exception {

        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.getBean("bean", "arg1", "arg2")).andReturn("value");
        
        replay(delegate, dependencyOne, dependencyTwo);

        parent.getBean("bean", "arg1", "arg2");
        
        verify(delegate, dependencyOne, dependencyTwo);
	}
    
    public void testDelegatingContainsBean() throws Exception {

        expect(delegate.containsBean("bean")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);

        parent.containsBean("bean");
        
        verify(delegate, dependencyOne, dependencyTwo);
	}
    
    public void testDelegating4() throws Exception {

        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.containsBeanDefinition("bean")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);

        parent.containsBean("bean");
        parent.containsBeanDefinition("bean");
        
        verify(delegate, dependencyOne, dependencyTwo);
	}
    
    public void testDelegatingMethods() throws Exception {
        String[] args = new String[0];
        
        expect(delegate.containsLocalBean("bean")).andReturn(true);
        expect(delegate.getAliases("bean")).andReturn(args);
        expect(delegate.getAutowireCapableBeanFactory()).andReturn(null);
        expect(delegate.getBeanDefinitionCount()).andReturn(0);
        expect(delegate.getBeanDefinitionNames()).andReturn(null);
        expect(delegate.getBeanNamesForType(String.class)).andReturn(null);
        expect(delegate.getBeanNamesForType(String.class, true, false)).andReturn(null);
        expect(delegate.getClassLoader()).andReturn(null);
        expect(delegate.getMessage(isA(MessageSourceResolvable.class), eq(Locale.getDefault()))).andReturn(null);
        expect(delegate.getMessage(eq("code"), aryEq(args), eq(Locale.getDefault()))).andReturn(null);
        expect(delegate.getMessage(eq("code"), aryEq(args), eq("default"), eq(Locale.getDefault()))).andReturn(null);
        expect(delegate.getParentBeanFactory()).andReturn(null);
        expect(delegate.getResource("location")).andReturn(null);
        expect(delegate.getResources("locationPattern")).andReturn(null);
        expect(delegate.getType("type")).andReturn(null);
        expect(delegate.isPrototype("bean")).andReturn(true);
        expect(delegate.isSingleton("bean")).andReturn(true);
        expect(delegate.isTypeMatch("bean", String.class)).andReturn(true);
        delegate.publishEvent(isA(ApplicationEvent.class));
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        parent.containsLocalBean("bean");
        parent.getAliases("bean");
        parent.getAutowireCapableBeanFactory();
        parent.getBeanDefinitionCount();
        parent.getBeanDefinitionNames();
        parent.getBeanNamesForType(String.class);
        parent.getBeanNamesForType(String.class, true, false);
        parent.getClassLoader();
        parent.getDisplayName();
        parent.getMessage(createMock(MessageSourceResolvable.class), Locale.getDefault());
        parent.getMessage("code", args, Locale.getDefault());
        parent.getMessage("code", args, "default", Locale.getDefault());
        assertSame(delegate, parent.getParent());
        parent.getParentBeanFactory();
        parent.getResource("location");
        parent.getResources("locationPattern");
        parent.getType("type");
        parent.isPrototype("bean");
        parent.isSingleton("bean");
        parent.isTypeMatch("bean", String.class);
        parent.publishEvent(createMock(ApplicationEvent.class));
        
        assertNotNull(parent.getId());
        assertTrue(parent.getStartupDate() > 0);
        
        verify(delegate, dependencyOne, dependencyTwo);
    }
    
}
