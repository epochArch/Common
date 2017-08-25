
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

import com.epocharch.common.config.ProperitesContainer;
import com.epocharch.common.constants.DeployLevel;
import com.epocharch.common.constants.PropKey;
import com.epocharch.common.zone.Zone;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by archer on 06/02/2017.
 */
public class IDCContainer {
    private static Logger logger = LoggerFactory.getLogger(IDCContainer.class);
    private Map<String,String> zoneIdcMap;
    private Map<String,Idc> idcMap;
    private String localIdc;
    private Lock lock = new ReentrantLock();

    public IDCContainer(Collection<Zone> zones)
    {
        refresh(zones);
        localIdc = ProperitesContainer.getInstance().getProperty(PropKey.LOCAL_IDC_NAME);
    }

    public void refresh(Collection<Zone> zones){
        if(zones!=null){
            Map<String,String> newzoneIdcMap = new HashMap<String, String>();
            Map<String,Idc> newidcMap = new HashMap<String, Idc>();
            for(Zone zone:zones){
                String zoneId = zone.getName();
                String idcId = zone.getPlatform();
                newzoneIdcMap.put(zoneId,idcId);
                Idc idc;
                if(!newidcMap.containsKey(idcId))
                {
                    idc = new Idc(idcId);
                    newidcMap.put(idcId,idc);
                }else{
                    idc=newidcMap.get(idcId);
                }
                idc.putZone(zone);
                if(DeployLevel.IDC.equals(zone.getZoneLevel())){
                    idc.setZoneName(zone.getName());
                }
            }
            zoneIdcMap = newzoneIdcMap;
            idcMap = newidcMap;
        }
    }

    public Set<Zone> getIdcZones(String idcId){
        Set<Zone> zones = null;
        if(!StringUtils.isBlank(idcId)){
            Idc idc = idcMap.get(idcId);
            if(idc!=null){
                zones = idc.getZones();
            }
        }
        return zones;
    }

    public Set<Zone> getZoneBrothers(String zoneId){
        Set<Zone> zones = null;
        String idcId = zoneIdcMap.get(zoneId);
        if(!StringUtils.isBlank(idcId)){
            zones =getIdcZones(idcId);
        }
        return zones;
    }

    public Set<String> getAllIdc(){
        return idcMap.keySet();
    }

    public boolean isSameIdc(String zoneId1,String zoneId2){
        String idcId1 = zoneIdcMap.get(zoneId1);
        String idcId2 = zoneIdcMap.get(zoneId2);
        return idcId1.equals(idcId2);
    }

    public String getLocalIdc(){
        return localIdc;
    }

    public String getIdcZoneName(String idcId){
        String name = "";
        Idc idc = idcMap.get(idcId);
        if(idc!=null){
            name = idc.getZoneName();
        }
        return name;
    }
}
