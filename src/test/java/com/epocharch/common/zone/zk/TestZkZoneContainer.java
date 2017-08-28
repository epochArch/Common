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

import com.epocharch.common.constants.ClusterUsage;
import com.epocharch.common.zone.zk.ZkZoneContainer;
import com.epocharch.zkclient.ZkClient;
import org.junit.Test;

/**
 * Created by archer on 24/08/2017.
 */
public class TestZkZoneContainer {

	@Test
	public void testZoneCreate(){
		ZkClient zk = ZkZoneContainer.getInstance().getLocalZkClient(ClusterUsage.SOA);
		System.out.println("level:"+ ZkZoneContainer.getInstance().getLevel());
	}
}
