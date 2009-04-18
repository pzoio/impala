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

package org.impalaframework.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.FileCopyUtils;

public class ClassLoaderTestUtils {

    static BaseURLClassLoader getLoader(String location) {
        File file = new File(location);
        return new ModuleClassLoader(new File[] { file });
    }

    static String readResource(ClassLoader location1Loader, String resourceName) throws IOException {
        InputStream resource = location1Loader.getResourceAsStream(resourceName);
        String result = FileCopyUtils.copyToString(new InputStreamReader(resource));
        return result;
    }

}
