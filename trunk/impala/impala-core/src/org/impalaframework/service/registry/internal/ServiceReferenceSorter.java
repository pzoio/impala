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

package org.impalaframework.service.registry.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.impalaframework.service.ServiceRegistryReference;
import org.springframework.util.Assert;

/**
 * Sorts {@link ServiceRegistryReference} instance using the attribute 'service.ranking'. 
 * @author Phil Zoio
 */
public class ServiceReferenceSorter {
    
    private Comparator<ServiceRegistryReference> comparator = new ServiceReferenceComparator();
    
    public List<ServiceRegistryReference> sort(List<ServiceRegistryReference> references) {
        return sort(references, false);
        //FIXME document
    }
    
    public List<ServiceRegistryReference> sort(List<ServiceRegistryReference> references, boolean reuseList) {
        Assert.notNull(references);
        //no point sorting if size < 2
        if (references.size() < 2) {
            return references;
        }
        List<ServiceRegistryReference> list = reuseList ? references : new ArrayList<ServiceRegistryReference>(references);
        Collections.sort(list, comparator);
        return list;
    }

    static class ServiceReferenceComparator implements Comparator<ServiceRegistryReference> {

        public int compare(ServiceRegistryReference o1, ServiceRegistryReference o2) {
            int o2ranking = getServiceRanking(o2);
            int o1ranking = getServiceRanking(o1);
            if (o2ranking > o1ranking) return 1;
            if (o1ranking < o2ranking) return -1;
            return 0;
        }

        int getServiceRanking(ServiceRegistryReference reference) {
            Map<String, ?> attributes = reference.getAttributes();
            
            Integer ranking;
            
            Object rankingObject = attributes.get("service.ranking");
            if (rankingObject instanceof Integer) {
                ranking = (Integer) rankingObject;
            } else if  (rankingObject instanceof Number) {
                Long longVal = ((Number) rankingObject).longValue();
                if (longVal >= Integer.MAX_VALUE) {
                    ranking = Integer.MAX_VALUE;
                }
                else {
                    ranking = longVal.intValue();
                }
            } else if (rankingObject instanceof String) {
                try {
                    ranking = Integer.valueOf(rankingObject.toString());
                }
                catch (NumberFormatException e) {
                    ranking = 0;
                }
            } else {
                ranking = 0;
            }
            return ranking;
        }
        
    }
    
}