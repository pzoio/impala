package samples.teamplan.web;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.ApplicationContext;

import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.TemplateException;

/**
 * Extension of FreemarkerConfigurer which uses the {@link ApplicationContext}'s class loader
 * to load templates contained within the modules.
 * 
 * @author Phil Zoio
 */
public class FreeMarkerConfigurer extends
        org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
        implements BeanClassLoaderAware {

    private ClassLoader classLoader;
    private String prefix;

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.setPreTemplateLoaders(new TemplateLoader[] { 
                new ClassLoaderTemplateLoader(prefix != null ? prefix : "", classLoader),
                });
        super.afterPropertiesSet();
    }
    
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    class ClassLoaderTemplateLoader extends URLTemplateLoader {

        private ClassLoader classLoader;

        private String prefix;

        public ClassLoaderTemplateLoader(String prefix, ClassLoader classLoader) {
            super();
            this.prefix = canonicalizePrefix(prefix);
            this.classLoader = classLoader;
        }

        @Override
        protected URL getURL(String name) {
            String resource = prefix + name;    
            return classLoader.getResource(resource);
        }
    }   

}
