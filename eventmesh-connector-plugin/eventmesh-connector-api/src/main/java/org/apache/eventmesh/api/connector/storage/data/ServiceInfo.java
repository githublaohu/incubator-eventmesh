/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.eventmesh.api.connector.storage.data;

/**
 * mysql 实例 是没有办法注册的。
 * 消费组订阅topic  大量存在注册中心
 * topic，nameservice  borkername， brokernameip
 * nameservice 不存在持久化
 * borker才有持久化
 * 存储的实例信息，是无法注册
 * 127.0.0.1:3306
 *
 * @author laohu
 * serviceInfo mysql地址。会去取元数据，表明，
 * 表明，直接读取mysql
 */
public class ServiceInfo {

}
