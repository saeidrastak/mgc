//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.logging;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * In select logs, collect selected logs.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class CollectLogEntryCallback implements LogEntryCallback
{

  /**
   * The entries.
   */
  public List<LogEntry> entries = new ArrayList<LogEntry>();

  /**
   * The node postfix.
   */
  private final String nodePostfix;

  /**
   * Default cons.
   */
  public CollectLogEntryCallback()
  {
    this(null);
  }

  /**
   * Instantiates a new collect log entry callback.
   *
   * @param nodePostfix the node postfix
   */
  public CollectLogEntryCallback(String nodePostfix)
  {
    this.nodePostfix = StringUtils.trimToNull(nodePostfix);
  }

  @Override
  public void onRow(LogEntry le)
  {
    if (nodePostfix != null) {
      LogAttribute attr = le.getAttributeByType(GenomeAttributeType.NodeName);
      if (attr != null && StringUtils.endsWith(attr.getValue(), nodePostfix) == false) {
        // ignore log entry
        return;
      }
    }
    entries.add(le);
  }

  @Override
  public LogEntry createLogEntry(LogEntry le)
  {
    return le;
  }

  public List<LogEntry> getEntries()
  {
    return entries;
  }

  public void setEntries(List<LogEntry> entries)
  {
    this.entries = entries;
  }

}
