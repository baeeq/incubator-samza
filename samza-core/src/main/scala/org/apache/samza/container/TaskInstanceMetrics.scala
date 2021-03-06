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

package org.apache.samza.container

import org.apache.samza.metrics.ReadableMetricsRegistry
import org.apache.samza.metrics.MetricsRegistryMap
import org.apache.samza.Partition
import org.apache.samza.metrics.MetricsHelper
import org.apache.samza.system.SystemStream
import org.apache.samza.metrics.Gauge

class TaskInstanceMetrics(
  val source: String = "unknown",
  val registry: ReadableMetricsRegistry = new MetricsRegistryMap) extends MetricsHelper {

  val commits = newCounter("commit-calls")
  val commitsSkipped = newCounter("commit-skipped")
  val windows = newCounter("window-calls")
  val windowsSkipped = newCounter("window-skipped")
  val processes = newCounter("process-calls")
  val sends = newCounter("send-calls")
  val sendsSkipped = newCounter("send-skipped")
  val messagesSent = newCounter("messages-sent")

  def addOffsetGauge(systemStream: SystemStream, getValue: () => String) {
    newGauge("%s-%s-offset" format (systemStream.getSystem, systemStream.getStream), getValue)
  }
}
