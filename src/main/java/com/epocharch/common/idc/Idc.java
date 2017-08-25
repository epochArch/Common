
/*
 * Copyright 2017 EpochArch.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epocharch.common.idc;


import com.epocharch.common.zone.Zone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by archer on 06/02/2017.
 */
public class Idc {
    private String name;
    private String zoneName;
    private Map<String,Zone> zoneMap= new HashMap<String,Zone>();


    public Idc(String idcId) {
        name = idcId;
    }

    public void putZone(Zone zone){
        zoneMap.put(zone.getName(),zone);
    }

    public Set<Zone> getZones(){
        return new HashSet<Zone>(zoneMap.values());
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
