
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
package com.epocharch.common.constants;

/**
 * @author archer
 *
 */
public interface ZoneConstants {

	public static final String ZONE_ROOT = "/EpochArch/ZoneMeta";
	public static final String ZK_USAGE_SOA = "SOA";
	public static final String ZK_USAGE_MQ = "MQ";
	public static final String ZK_USAGE_CACHE = "CACHE";
	public static final String ZK_USAGE_DAL = "DAL";
	public static final String ZK_USAGE_SCHEDULER = "SCHEDULER";
	public static final String ZK_USAGE_UNKNOWN = "UNKNOWN";
	public static final String ZK_USAGE_OPS = "OPS";
	public static final String ZK_USAGE_EXT1 = "EXT1";
	public static final String ZK_USAGE_EXT2 = "EXT2";
	public static final int ROUTE_PRIORITY_PRIMARY = 10;
	public static final int ROUTE_PRIORITY_DEFAULT = 5;
	public static final int ROUTE_PRIORITY_BACKUP = 1;
	public static final int ROUTE_PRIORITY_NONE = 0;

	public static final int ZK_SESSION_TIMEOUT = 15000;

	public static final String ZONE_PATH = "/EpochArch/ZoneMeta/zones";
	public static final String IDC_ZONE_PATH = "/EpochArch/ZoneMeta/zones";

}
