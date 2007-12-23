package org.impalaframework.module.operation;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.impalaframework.exception.NoServiceException;

import junit.framework.TestCase;

public class SimpleModuleOperationRegistryTest extends TestCase {

	private SimpleModuleOperationRegistry registry;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		registry = new SimpleModuleOperationRegistry();
	}
	
	public final void testPutOperation() {
		ModuleOperation moduleOp = newModuleOp();
		registry.putOperation("op1", moduleOp);
		
		assertSame(moduleOp, registry.getOperation("op1"));
	}
	
	public final void testUnknown() {		
		try {
			registry.getOperation("op1");
		}
		catch (NoServiceException e) {
			assertEquals("No instance of org.impalaframework.module.operation.ModuleOperation available for operation named 'op1'", e.getMessage());
		}
	}
	
	public void testContributions() throws Exception {

		ModuleOperation moduleOp1 = newModuleOp();
		registry.putOperation("op1", moduleOp1);
		
		Map<String, ModuleOperation> contributions = new HashMap<String, ModuleOperation>();
		ModuleOperation moduleOp2 = newModuleOp();
		contributions.put("op1", moduleOp2);
		
		registry.setContributedOperations(contributions);
		assertSame(moduleOp2, registry.getOperation("op1"));
	}
	
	private ModuleOperation newModuleOp() {
		ModuleOperation moduleOp = EasyMock.createMock(ModuleOperation.class);
		return moduleOp;
	}

	public final void testEmptyRegistry() {
		Map<String, ModuleOperation> operations = registry.getOperations();
		assertEquals(0, operations.size());
		
		try {
			operations.put("name", EasyMock.createMock(ModuleOperation.class));
			fail("Unmodifiable, so should not be supported");
		}
		catch (UnsupportedOperationException e) {
		}
	}

}
