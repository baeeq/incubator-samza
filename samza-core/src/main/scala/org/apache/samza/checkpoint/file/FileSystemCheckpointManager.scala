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

package org.apache.samza.checkpoint.file

import java.io.File
import java.io.FileOutputStream
import scala.collection.JavaConversions._
import scala.io.Source
import org.apache.samza.SamzaException
import org.apache.samza.serializers.CheckpointSerde
import org.apache.samza.metrics.MetricsRegistry
import org.apache.samza.config.Config
import org.apache.samza.Partition
import org.apache.samza.config.JobConfig.Config2Job
import org.apache.samza.config.FileSystemCheckpointManagerConfig.Config2FSCP
import org.apache.samza.checkpoint.CheckpointManagerFactory
import org.apache.samza.checkpoint.CheckpointManager
import org.apache.samza.checkpoint.Checkpoint
import java.io.FileNotFoundException

class FileSystemCheckpointManager(
  jobName: String,
  root: File,
  serde: CheckpointSerde = new CheckpointSerde) extends CheckpointManager {

  def register(partition: Partition) {
  }

  def writeCheckpoint(partition: Partition, checkpoint: Checkpoint) {
    val bytes = serde.toBytes(checkpoint)
    val fos = new FileOutputStream(getFile(jobName, partition))

    fos.write(bytes)
    fos.close
  }

  def readLastCheckpoint(partition: Partition): Checkpoint = {
    try {
      val bytes = Source.fromFile(getFile(jobName, partition)).map(_.toByte).toArray

      serde.fromBytes(bytes)
    } catch {
      case e: FileNotFoundException => null
    }
  }

  def start {
    if (!root.exists) {
      throw new SamzaException("Root directory for file system checkpoint manager does not exist: %s" format root)
    }
  }

  def stop {}

  private def getFile(jobName: String, partition: Partition) =
    new File(root, "%s-%d" format (jobName, partition.getPartitionId))
}

class FileSystemCheckpointManagerFactory extends CheckpointManagerFactory {
  def getCheckpointManager(config: Config, registry: MetricsRegistry) = {
    val name = config
      .getName
      .getOrElse(throw new SamzaException("Missing job name in configs"))
    val root = config
      .getFileSystemCheckpointRoot
      .getOrElse(throw new SamzaException("Missing checkpoint root in configs"))
    new FileSystemCheckpointManager(name, new File(root))
  }
}
