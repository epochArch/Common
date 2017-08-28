
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

public enum ClusterUsage {

	UNKNOWN(ZoneConstants.ZK_USAGE_UNKNOWN), SOA(ZoneConstants.ZK_USAGE_SOA), MQ(ZoneConstants.ZK_USAGE_MQ),
	CACHE(ZoneConstants.ZK_USAGE_CACHE), DAL(ZoneConstants.ZK_USAGE_DAL), SCHEDULER(ZoneConstants.ZK_USAGE_SCHEDULER),
	OPS(ZoneConstants.ZK_USAGE_OPS),EXT1(ZoneConstants.ZK_USAGE_EXT1),EXT2(ZoneConstants.ZK_USAGE_EXT2);

	private String code;

	private ClusterUsage(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static ClusterUsage getByCode(String c) {
		for (ClusterUsage usage : ClusterUsage.values()) {
			if (usage.getCode().equals(c)) {
				return usage;
			}
		}
		return UNKNOWN;
	}
}
