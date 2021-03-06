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

package de.micromata.genome.stats;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO for holding a stats entry.
 * 
 * @author roger@micromata.de
 * 
 */
public class TypeStatsDO implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7128205357814467249L;

  /**
   * The first date.
   */
  private Date firstDate = new Date();

  /**
   * The last date.
   */
  private Date lastDate = new Date();

  public Date getFirstDate()
  {
    return firstDate;
  }

  public void setFirstDate(Date firstDate)
  {
    this.firstDate = firstDate;
  }

  public Date getLastDate()
  {
    return lastDate;
  }

  public void setLastDate(Date lastDate)
  {
    this.lastDate = lastDate;
  }
}
