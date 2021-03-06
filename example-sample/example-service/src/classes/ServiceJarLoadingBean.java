package classes;

import java.lang.reflect.Method;

import net.sf.ehcache.Cache;

import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Demonstrates the inheritance of ehcahe-0.6 from the module 'example-optional'
 * @author Phil Zoio
 */
public class ServiceJarLoadingBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
    
        Cache cache = new Cache("name", 100, true, true, 1000L, 1000L);
        System.out.println(cache);
        
        //this should fall over, as it is not present in local jar api
        final Method findMethod = ReflectionUtils.findMethod(cache.getClass(), "getCacheEventNotificationService", new Class[]{});
        try {
            Assert.isNull(findMethod, "Did not expect to find method 'getCacheEventNotificationService'");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

