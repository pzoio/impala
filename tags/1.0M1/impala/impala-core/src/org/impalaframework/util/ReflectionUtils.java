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

package org.impalaframework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.impalaframework.exception.ExecutionException;
import org.springframework.util.Assert;

public class ReflectionUtils {

	public static Object invokeMethod(Object target, String methodName, Object... args) {

		Class<?>[] paramTypes = new Class[args.length];
		for (int i = 0; i < paramTypes.length; i++) {
			paramTypes[i] = args[i].getClass();
		}

		Method findMethod = findMethod(target.getClass(), methodName,
				paramTypes);
		if (findMethod == null) {
			throw new UnsupportedOperationException("No method compatible with method: " + methodName + ", args: "
					+ Arrays.toString(args));
		}

		return invokeMethod(findMethod, target, args);

	}

	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		}
		catch (InvocationTargetException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			}
			throw rethrow(e, method.getName(), args);
		}
		catch (Exception e) {
			throw rethrow(e, method.getName(), args);
		}
	}

	public static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName()) && (paramTypes.length == method.getParameterTypes().length)) {

					boolean found = true;
					Class<?>[] methodParameterTypes = method.getParameterTypes();

					for (int j = 0; j < methodParameterTypes.length; j++) {
						found = methodParameterTypes[j].isAssignableFrom(paramTypes[j]);
						if (!found)
							break;
					}

					if (found)
						return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	private static ExecutionException rethrow(Exception e, String methodName, Object... args) {
		return new ExecutionException("Unable to execute method: " + methodName + ", args: " + Arrays.toString(args), e);
	}
}
