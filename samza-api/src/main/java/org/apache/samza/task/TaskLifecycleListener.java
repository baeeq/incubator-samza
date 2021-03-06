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

package org.apache.samza.task;

import org.apache.samza.config.Config;
import org.apache.samza.system.IncomingMessageEnvelope;

/**
 * Used to get before/after notifications before initializing/closing all tasks
 * in a given container (JVM/process).
 */
public interface TaskLifecycleListener {
  /**
   * Called before all tasks in TaskRunner are initialized.
   */
  void beforeInit(Config config, TaskContext context);

  /**
   * Called after all tasks in TaskRunner are initialized.
   */
  void afterInit(Config config, TaskContext context);

  /**
   * Called before a message is processed by a task.
   */
  void beforeProcess(IncomingMessageEnvelope envelope, Config config, TaskContext context);

  /**
   * Called after a message is processed by a task.
   */
  void afterProcess(IncomingMessageEnvelope envelope, Config config, TaskContext context);

  /**
   * Called before all tasks in TaskRunner are closed.
   */
  void beforeClose(Config config, TaskContext context);

  /**
   * Called after all tasks in TaskRunner are closed.
   */
  void afterClose(Config config, TaskContext context);
}
