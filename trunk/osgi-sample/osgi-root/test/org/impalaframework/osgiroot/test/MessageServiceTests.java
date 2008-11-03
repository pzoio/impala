package org.impalaframework.osgiroot.test;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.osgiroot.MessageService;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.provisioning.ArtifactLocator;

public class MessageServiceTests extends AbstractConfigurableBundleCreatorTests {

	private ArtifactLocator locator;

	public MessageServiceTests() {
		super();
		locator = new RepositoryArtifactLocator();
	}

	public void testMessageService() throws Exception {
		
		ServiceReference[] serviceReference = bundleContext.getServiceReferences(ApplicationContext.class.getName(), "(org.springframework.context.service.name=org.impalaframework.osgiroot)");	
		Object service = bundleContext.getService(serviceReference[0]);
		ApplicationContext context = (ApplicationContext) service;
		MessageService messageService = (MessageService) context.getBean("messageService");
		System.out.println(messageService.getMessage());
	}
	
	/* ********************** Test bundle names ********************* */
	
	@Override
	protected Resource[] getTestBundles() {
		File osgiDirectory = new File("../osgi-repository/osgi");
		File[] thirdPartyBundles = osgiDirectory.listFiles(new BundleFileFilter());
		
		File mainDirectory = new File("../osgi-repository/main");
		File[] mainBundles = mainDirectory.listFiles(new BundleFileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (!super.accept(pathname)) return false;
				if (pathname.getName().contains("build")) return false;
				if (pathname.getName().contains("jmx")) return false;
				if (!pathname.getName().contains("impala")) return false;
				
				return true;
			}
		});

		//FIXME dist directory is only created by running ant dist
		File distDirectory = new File("../osgi-repository/dist");
		File[] distBundles = distDirectory.listFiles(new BundleFileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (!super.accept(pathname)) return false;
				return pathname.getName().contains("extender");
			}
			
		});

		List<Resource> resources = new ArrayList<Resource>();
		addResources(resources, thirdPartyBundles);
		addResources(resources, mainBundles);
		return addResources(resources, distBundles);
	}	
	
	/* ********************** Helper methods ********************* */

	private Resource[] addResources(List<Resource> resources, File[] listFiles) {
		for (int i = 0; i < listFiles.length; i++) {
			resources.add(new FileSystemResource(listFiles[i]));
		}
		
		return resources.toArray(new Resource[0]);
	}

	protected String[] getTestFrameworkBundlesNames() {
		String[] testFrameworkBundlesNames = super.getTestFrameworkBundlesNames();
		for (int i = 0; i < testFrameworkBundlesNames.length; i++) {
			String bundle = testFrameworkBundlesNames[i];
			System.out.println(bundle);
			if (bundle.equals("org.springframework.osgi,log4j.osgi,1.2.15-SNAPSHOT")) {
				bundle = bundle.replace("log4j.osgi,1.2.15-SNAPSHOT", "com.springsource.org.apache.log4j,1.2.15");
				testFrameworkBundlesNames[i] = bundle;
			}
			
		}
		return testFrameworkBundlesNames;
	}
	
	protected ArtifactLocator getLocator() {
		return locator;
	}
	
}

class BundleFileFilter implements FileFilter {
	public boolean accept(File pathname) {
		if (pathname.getName().contains("sources")) return false;
		if (!pathname.getName().endsWith(".jar")) return false;
		return true;
	}
}

class RepositoryArtifactLocator implements ArtifactLocator {

	public Resource locateArtifact(String group, String id, String version) {
		String directory = "../osgi-repository/osgi/";
		FileSystemResource resource = findBundleResource(directory, id, version);
		return resource;
	}

	private FileSystemResource findBundleResource(String directory, String id,
			String version) {
		FileSystemResource resource = new FileSystemResource(directory + id + "-" + version + ".jar");
		System.out.println(resource + ": " + resource.exists());
		return resource;
	}

	public Resource locateArtifact(String group, String id, String version,
			String type) {
		String directory = "../osgi-repository/osgi/";
		FileSystemResource resource = new FileSystemResource(directory + id + "-" + version + "-" + type + ".jar");
		System.out.println(resource + ": " + resource.exists());
		return resource;
	}
	
}
