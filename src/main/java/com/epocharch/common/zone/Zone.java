
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
package com.epocharch.common.zone;

import com.epocharch.common.constants.DeployLevel;
import com.epocharch.common.constants.ClusterUsage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author archer
 *
 */
public class Zone implements Serializable {
	private static final long serialVersionUID = -3449736799079134998L;
	private String name;
	private String alias;
	private Map<ClusterUsage, String> zkClusterMap = new HashMap<ClusterUsage, String>();
	private Map<String,String> extZkClusterMap = new HashMap<String, String>();
	private double longtitude;
	private double latitude;
	private String platform;
	private String platformName;
	private long bandwidthIn;
	private long bandwidthOut;
	private String desc;
	private DeployLevel zoneLevel;

	public Zone() {
		super();
	}

	public Zone(String name, String alias, Map<ClusterUsage, String> zkClusterMap, double longtitude, double latitude,
			String platformName, long bandwidthIn, long bandwidthOut, String platform, String desc) {
		super();
		this.name = name;
		this.alias = alias;
		this.zkClusterMap = zkClusterMap;
		this.longtitude = longtitude;
		this.latitude = latitude;
		this.platform = platform;
		this.platformName = platformName;
		this.bandwidthIn = bandwidthIn;
		this.bandwidthOut = bandwidthOut;
		this.desc = desc;
	}

	public Zone(String name, String alias, String platformName, String platform, String desc) {
		super();
		this.name = name;
		this.alias = alias;
		this.platform = platform;
		this.platformName = platformName;
		this.desc = desc;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Map<ClusterUsage, String> getZkClusterMap() {
		return zkClusterMap;
	}

	public void setZkClusterMap(Map<ClusterUsage, String> zkClusterMap) {
		this.zkClusterMap = zkClusterMap;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public long getBandwidthIn() {
		return bandwidthIn;
	}

	public void setBandwidthIn(long bandwidthIn) {
		this.bandwidthIn = bandwidthIn;
	}

	public long getBandwidthOut() {
		return bandwidthOut;
	}

	public void setBandwidthOut(long bandwidthOut) {
		this.bandwidthOut = bandwidthOut;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getZkClusterByUsage(ClusterUsage usage) {
		return zkClusterMap.get(usage);
	}

	public String getZkClusterByUsageCode(String usageCode){
		String serverList = null;
		ClusterUsage usage = ClusterUsage.getByCode(usageCode);
		if(!usage.equals(ClusterUsage.UNKNOWN)){
			if(zkClusterMap!=null){
				serverList = zkClusterMap.get(usage);
			}
		}else{
			if(extZkClusterMap!=null){
				serverList = extZkClusterMap.get(usageCode);
			}
		}
		return serverList;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}


	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getExtZkClusterMap() {
		return extZkClusterMap;
	}

	public void setExtZkClusterMap(Map<String, String> extZkClusterMap) {
		this.extZkClusterMap = extZkClusterMap;
	}

	public DeployLevel getZoneLevel() {
		return zoneLevel;
	}

	public void setZoneLevel(DeployLevel zoneLevel) {
		this.zoneLevel = zoneLevel;
	}
}
