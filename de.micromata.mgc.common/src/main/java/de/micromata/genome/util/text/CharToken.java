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

package de.micromata.genome.util.text;

/**
 * The Class CharToken.
 *
 * @author roger
 */
public class CharToken extends TokenBase
{

  /**
   * The character.
   */
  private char character;

  /**
   * Instantiates a new char token.
   *
   * @param tokenType the token type
   * @param character the character
   */
  public CharToken(int tokenType, char character)
  {
    super(tokenType);
    this.character = character;
  }

  /**
   * Instantiates a new char token.
   */
  public CharToken()
  {

  }

  @Override
  public TokenResult consume(String text, char escapeChar)
  {
    if (text.length() > 0 && text.charAt(0) == character) {
      return new CharTokenResult(this);
    }
    return null;
  }

  @Override
  public boolean match(String text)
  {
    return text.length() > 0 && text.charAt(0) == character;
  }

  public char getCharacter()
  {
    return character;
  }

  public void setCharacter(char character)
  {
    this.character = character;
  }

}
