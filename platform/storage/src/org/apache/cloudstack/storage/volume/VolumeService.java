/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cloudstack.storage.volume;

import org.apache.cloudstack.storage.volume.type.VolumeType;

public interface VolumeService {

	/**
	 * 
	 */
	Volume allocateVolumeInDb(long size, VolumeType type);
	
    /**
     * Creates the volume based on the given criteria
     * 
     * @param cmd
     *            
     * @return the volume object
     */
    Volume createVolume(long volumeId);

    /**
     * Delete volume
     * @param volumeId
     * @return
     * @throws ConcurrentOperationException
     */
    boolean deleteVolume(long volumeId);
    
    /**
     * 
     */
    boolean cloneVolume(long volumeId, long baseVolId);
    
    /**
     * 
     */
    boolean createVolumeFromSnapshot(long volumeId, long snapshotId);
    
    /**
     * 
     */
    String grantAccess(long volumeId, long endpointId);
    
    /**
     * 
     */
    boolean rokeAccess(long volumeId, long endpointId);
}
