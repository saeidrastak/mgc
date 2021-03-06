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

package de.micromata.genome.util.validation;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Mesasge with a list of message.
 * 
 * NOTE: the List must never by empty.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValMessagesException extends ValStatusException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 8970392691691721620L;

  /**
   * The val messages.
   */
  private List<ValMessage> valMessages;

  /**
   * Instantiates a new val messages exception.
   *
   * @param message the message
   * @param valMessages the val messages
   */
  public ValMessagesException(String message, List<ValMessage> valMessages)
  {
    super(message);
    this.valMessages = valMessages;
    Validate.isTrue(valMessages.isEmpty() == false);
  }

  public ValMessagesException(List<ValMessage> valMessages)
  {
    this("", valMessages);
  }

  @Override
  public ValMessage getWorstMessage()
  {
    ValMessage ret = null;
    for (ValMessage msg : valMessages) {
      if (ret == null) {
        ret = msg;
        continue;
      }
      if (msg.getValState().isWorse(ret.getValState()) == true) {
        ret = msg;
      }
    }
    return ret;
  }

}
