package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.StickyModificationExtractor;

public class ModificationExtractorRegistryTest extends TestCase {

	private ModificationExtractorRegistry registry;

	@Override
	protected void setUp() throws Exception {
		registry = new ModificationExtractorRegistry();
	}

	public void testRegistry() throws Exception {
		try {
			assertNull(registry.getPluginModificationCalculator(ModificationExtractorType.STICKY));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No ModificationExtractor available for modification type STICKY", e.getMessage());
		}
		try {
			assertNull(registry.getPluginModificationCalculator("sticky"));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No ModificationExtractor available for modification type STICKY", e.getMessage());
		}

		StickyModificationExtractor stickyModificationExtractor = new StickyModificationExtractor();

		Map<ModificationExtractorType, ModificationExtractor> map = new HashMap<ModificationExtractorType, ModificationExtractor>();
		map.put(ModificationExtractorType.STICKY, stickyModificationExtractor);
		registry.setPluginModificationCalculators(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationExtractorType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}

	public void testRegistryWithStringMap() throws Exception {
		StickyModificationExtractor stickyModificationExtractor = new StickyModificationExtractor();

		Map<String, ModificationExtractor> map = new HashMap<String, ModificationExtractor>();
		map.put("sticky", stickyModificationExtractor);
		registry.setPluginModificationCalculatorMap(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationExtractorType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}
}
