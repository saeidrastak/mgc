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


/**
 * Um die {@link LogAttribute} zu rendern.
 *
 * @author lado
 */
public interface LogAttributeRenderer
{

  /**
   * Rendert eine HTML Darestellung des {@link LogAttribute} Wertes. Optional kann {@link HttpContext} übergeben werden,
   * z.B, richige Links zu rendern, etc.
   * 
   * @param attr {@link LogAttribute}
   * @param ctx HttpContext optionales Element. Die Implementierung sorgt dafür ob die es optinal braucht oder
   *          pflicht ist
   * @return {@link String} Html Representation des {@link LogAttribute} Wert
   */
  public String renderHtml(LogAttribute attr);

  /**
   * Liefert die Text Representation eines {@link LogAttribute} Wertes.
   *
   * @param attr {@link LogAttribute}
   * @return {@link String} Text Representation des {@link LogAttribute} Wertes
   */
  public String renderText(LogAttribute attr);

}
