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

package de.micromata.genome.chronos;

import de.micromata.genome.logging.LogAttribute;

/**
 * If the trigger is a chron expression, abort this job run with an error, but restart the job for the next regular time.
 * 
 * @author roger@micromata.de
 * 
 */
public class RetryNextRunException extends JobRetryException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6366441836855430698L;

  /**
   * Instantiates a new retry next run exception.
   */
  public RetryNextRunException()
  {
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param message the message
   * @param silent the silent
   */
  public RetryNextRunException(String message, boolean silent)
  {
    super(message, silent);
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param message the message
   * @param cause the cause
   * @param silent the silent
   */
  public RetryNextRunException(String message, Throwable cause, boolean silent)
  {
    super(message, cause, silent);
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public RetryNextRunException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param message the message
   */
  public RetryNextRunException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param message the message
   * @param attrs the attrs
   */
  public RetryNextRunException(String message, LogAttribute... attrs)
  {
    super(message, attrs);
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param message the message
   * @param cause the cause
   * @param captureLogContext the capture log context
   * @param attrs the attrs
   */
  public RetryNextRunException(String message, Throwable cause, boolean captureLogContext, LogAttribute... attrs)
  {
    super(message, cause, captureLogContext, attrs);
  }

  /**
   * Instantiates a new retry next run exception.
   *
   * @param cause the cause
   */
  public RetryNextRunException(Throwable cause)
  {
    super(cause);
  }

}
