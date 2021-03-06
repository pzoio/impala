/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.impalaframework.spring.resource;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * {@link ResourceLoader} implementation that resolves plain paths as file
 * system resources. This implementation is based on the Spring
 * <code>FileSystemResourceLoader</code> by Juergen Hoeller.
 * 
 * <p>
 * <b>NOTE:</b> Plain paths will always be interpreted as relative to the
 * current VM working directory, even if they start with a slash. (This is
 * consistent with the semantics in a Servlet container.) <b>Use an explicit
 * "file:" prefix to enforce an absolute file path.</b>
 * <p>
 * 
 * @author Juergen Hoeller
 * @author Phil Zoio
 */

public class FileSystemResourceLoader extends PathBasedResourceLoader {

    @Override
    protected Resource getResourceForPath(String prefix, String location, ClassLoader classLoader) {
        return getResourceByPath(prefix + location);
    }

    /**
     * Resolve resource paths as file system paths.
     * <p>
     * Note: Even if a given path starts with a slash, it will get interpreted
     * as relative to the current VM working directory.
     * @param path path to the resource
     * @return Resource handle
     * @see FileSystemResource
     * @see org.springframework.web.context.support.ServletContextResourceLoader#getResourceByPath
     */
    protected Resource getResourceByPath(String path) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        return new FileSystemResource(path);
    }

}
