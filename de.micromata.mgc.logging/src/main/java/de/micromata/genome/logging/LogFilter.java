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

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * Interface to filter a Log.
 * 
 * A filter can modify a LogWriteEntry (throw away attributes, etc.(
 * 
 * 
 * 
 * @author roger@micromata.de
 * 
 */
public abstract class LogFilter extends MatcherBase<LogEntry>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -146061547869821581L;

}
