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

package de.micromata.genome.chronos.manager;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;

/**
 * A factory for creating GenomeClassJob objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class GenomeClassJobFactory implements JobDefinition
{

  /**
   * The class job definition.
   */
  ClassJobDefinition classJobDefinition;

  /**
   * The bean properties.
   */
  private Map<String, Object> beanProperties = null;

  /**
   * Instantiates a new genome class job factory.
   *
   * @param job the job
   */
  public GenomeClassJobFactory(Class< ? extends FutureJob> job)
  {
    classJobDefinition = new ClassJobDefinition(job);
  }

  /**
   * Instantiates a new genome class job factory.
   *
   * @param classNameToStart the class name to start
   */
  @SuppressWarnings("unchecked")
  public GenomeClassJobFactory(String classNameToStart)
  {
    try {
      Class< ? extends FutureJob> cls = (Class< ? extends FutureJob>) Thread.currentThread().getContextClassLoader().loadClass(
          classNameToStart);
      classJobDefinition = new ClassJobDefinition(cls);
    } catch (Exception ex) {
      /**
       * @logging
       * @reason Die JobFactory konnte nicht initialisiert werden
       * @action TechAdmin kontaktieren
       */
      throw new LoggedRuntimeException(ex, LogLevel.Error, GenomeLogCategory.Scheduler, "Cannot initialize GenomeClassJobFactory for: "
          + classNameToStart
          + ": "
          + ex.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public FutureJob getInstance()
  {
    FutureJob fj = classJobDefinition.getInstance();
    try {
      if (beanProperties != null) {
        BeanUtils.populate(fj, beanProperties);
      }
    } catch (Exception ex) {
      /**
       * @logging
       * @reason Die JobFactory konnte die bean properties nicht setzen
       * @action TechAdmin kontaktieren
       */
      throw new LoggedRuntimeException(ex, LogLevel.Error, GenomeLogCategory.Scheduler, "Cannot populate properties for bean: "
          + fj.getClass().getName()
          + ": "
          + ex.getMessage());
    }
    return fj;
  }

  /**
   * Gets the bean properties.
   *
   * @return the bean properties
   */
  public Map<String, Object> getBeanProperties()
  {
    return beanProperties;
  }

  /**
   * Sets the bean properties.
   *
   * @param beanProperties the bean properties
   */
  public void setBeanProperties(Map<String, Object> beanProperties)
  {
    this.beanProperties = beanProperties;
  }

}
