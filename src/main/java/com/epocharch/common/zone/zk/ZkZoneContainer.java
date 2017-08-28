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

package com.epocharch.common.zone.zk;

import com.epocharch.common.config.PropertiesContainer;
import com.epocharch.common.constants.DeployLevel;
import com.epocharch.common.constants.PropKey;
import com.epocharch.common.constants.ClusterUsage;
import com.epocharch.common.constants.ZoneConstants;
import com.epocharch.common.idc.IDCContainer;
import com.epocharch.common.zone.Zone;
import com.epocharch.common.zone.ZoneCalcHelper;
import com.epocharch.zkclient.IZkChildListener;
import com.epocharch.zkclient.IZkDataListener;
import com.epocharch.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author archer
 *
 */
public class ZkZoneContainer {

    private static Logger logger = LoggerFactory.getLogger(ZkZoneContainer.class);
    private static Map<String, Zone> zones = new HashMap<String, Zone>();
    private static Map<String, ZkClient> zkClients;
    private static Map<String, Double> distances;
    private static Map<String, Long> bandwidthMap;
    private String localZone = "UnknownZone";
    private Lock lock = new ReentrantLock();
    private IDCContainer idcContainer;
    private DeployLevel level = DeployLevel.ZONE;
    private static ZkZoneContainer container = new ZkZoneContainer();
    private String zonePath = ZoneConstants.IDC_ZONE_PATH;
    private String localZonePath = ZoneConstants.ZONE_ROOT;

    public static ZkZoneContainer getInstance() {

        return container;
    }

    private ZkZoneContainer() {
        super();
        zkClients = new HashMap<String, ZkClient>();
        distances = new HashMap<String, Double>();
        String tmplevel = PropertiesContainer.getInstance().getProperty(PropKey.DEPLOY_LEVEL);
		level = DeployLevel.getByCode(tmplevel);
		localZone = PropertiesContainer.getInstance().getProperty(PropKey.LOCAL_ZONE_NAME);
		if(DeployLevel.IDC.equals(level)){
			localZone = "IDC_"+localZone;
		}
        initZones();
        Collection<Zone> zoneCollection=null;
        if(zones!=null){
            zoneCollection = zones.values();
        }
        idcContainer = new IDCContainer(zoneCollection);
        watchZoneChange();
        if (logger.isDebugEnabled()) {
            logger.info("\n###---" + ZkZoneContainer.class.getName() + " init SUCCESS!!---###");
        }
    }

    public IDCContainer getIdcContainer(){
        return idcContainer;
    }

    public ZkClient getLocalZkClient(ClusterUsage usage) {
        return getZkClient(localZone, usage.getCode());
    }

    public ZkClient getLocalZkClient(String usageCode) {
        return getZkClient(localZone,usageCode);
    }

    public String getLocalZoneName() {
        return localZone;
    }

    public boolean hasZone(String zoneName) {
        return zones.containsKey(zoneName);
    }

    private void watchZoneChange() {
        try {
            ZkClient localZk = getLocalZkClient(ClusterUsage.SOA);
            localZk.subscribeChildChanges(zonePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    initZones();
                    if (logger.isDebugEnabled()) {
                        logger.debug("####-----ZkZoneContainer.watchZoneChange()_handleChildChange:initZones=" + getAllZoneName());
                    }
                }
            });
            localZk.subscribeDataChanges(zonePath, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    initZones();
                    if (logger.isDebugEnabled()) {
                        logger.debug("####-----ZkZoneContainer.watchZoneChange()_handleDataChange:initZones=" + getAllZoneName());
                    }
                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    initZones();
                    if (logger.isDebugEnabled()) {
                        logger.debug("####-----ZkZoneContainer.watchZoneChange()_handleDataDeleted:initZones=" + getAllZoneName());
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public void initZones() {
        lock.lock();
        try {
            Map<String, Zone> zoneMap = new HashMap<String, Zone>();
            try {
				String localCluster = PropertiesContainer.getInstance().getProperty(PropKey.REGISTE_CENTER);
                ZkClient localZk = new ZkClient(localCluster, ZoneConstants.ZK_SESSION_TIMEOUT, Integer.MAX_VALUE);
                if (!localZk.exists(zonePath)) {
                    localZk.createPersistent(zonePath, true);
                }
                String currentZone = localZk.readData(localZonePath, true);
                if (StringUtils.isBlank(currentZone)) {
                    if (!StringUtils.isBlank(localZone)) {
                        if (localZk.exists(localZonePath)) {
                            localZk.writeData(localZonePath, localZone);
                        } else {
                            localZk.createPersistent(localZonePath, localZone);
                        }
						Zone z = new Zone();
						z.setName(localZone);
						z.setZoneLevel(level);
						Map<ClusterUsage, String> m = new HashMap<ClusterUsage, String>();
						m.put(ClusterUsage.SOA, localCluster);
						z.setZkClusterMap(m);
						localZk.createPersistent(zonePath + "/" + localZone, z);
                    } else {
                        logger.error("Can't find local zone name in epocharch.properties");
                        System.exit(-1);

                    }
                }
                String key = genkey(localZone, ClusterUsage.SOA.getCode());
                zkClients.put(key, localZk);
                List<String> zoneList = localZk.getChildren(zonePath);
                if (zoneList != null) {
                    for (String zoneName : zoneList) {
                        String dataPath = zonePath + "/" + zoneName;
                        try {
                            Zone z = localZk.readData(dataPath);
                            if (z != null) {
                                zoneMap.put(z.getName(), z);
                                if(localZone.equals(z.getName()) && z.getZoneLevel()==null){
                                    z.setZoneLevel(level);
                                    localZk.writeData(dataPath,z);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Read zone:" + zoneName + " data error!!!", e);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
            distances = ZoneCalcHelper.calcDistences(zoneMap);
            bandwidthMap = ZoneCalcHelper.calcBandwidth(zoneMap);
            zones = zoneMap;
        } finally {
            if(idcContainer!=null){
                idcContainer.refresh(zones.values());
            }
            lock.unlock();
        }
    }


    public Double getDistence(String srcZone, String destZone) {
        String key = ZoneCalcHelper.genKey(srcZone, destZone);
        return distances.get(key);
    }

    public Zone getZone(String zone) {
        return zones.get(zone);
    }

    public ZkClient getZkClient(String zone, ClusterUsage usage) {
        return getZkClient(zone, usage.getCode());
    }

    public ZkClient getZkClient(String zone, String usageCode) {
        ZkClient cli = null;
        String key = genkey(zone, usageCode);
        if (!zkClients.containsKey(key)) {
            lock.lock();
            try {
                if (!zkClients.containsKey(key)) {
                    Zone z = zones.get(zone);
                    if (z != null) {
                        String clusterString = z.getZkClusterByUsageCode(usageCode);
                        if (!StringUtils.isBlank(clusterString)) {
                            ZkClient client = new ZkClient(clusterString, ZoneConstants.ZK_SESSION_TIMEOUT, Integer.MAX_VALUE);
                            zkClients.put(key, client);
                        } else {
                            throw new RuntimeException("Zone:" + zone + " zk cluster for " + usageCode + " is not exist!!!");
                        }
                    } else {
                        throw new RuntimeException("Zone:" + zone + " is not exist!!!");
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        cli = zkClients.get(key);
        return cli;
    }


    private String genkey(String zone, String usageCode) {
        String key = null;
        if (StringUtils.isBlank(zone)) {
            throw new IllegalArgumentException("Zone name must not null!!!");
        }
        if (usageCode == null) {
            throw new IllegalArgumentException("ZkCluster usage must not null!!!");
        }
        key = zone + "_" + usageCode;
        return key;
    }

    public Set<String> getAllZoneName() {
        Set<String> az = new HashSet<String>();
        if (zones != null) {
            for (String zn : zones.keySet()) {
                az.add(zn);
            }
        }
        return az;
    }

    public Set<Zone> getAllZone() {
        return new HashSet<Zone>(zones.values());
    }

    public Long getBandwidth(String srcZone, String destZone) {
        String key = ZoneCalcHelper.genKey(srcZone, destZone);
        return bandwidthMap.get(key);
    }

    public Map<String, Long> getBandwidthMap() {
        return new HashMap<String, Long>(bandwidthMap);
    }

    public DeployLevel getLevel(){
        return level;
    }

}
