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
