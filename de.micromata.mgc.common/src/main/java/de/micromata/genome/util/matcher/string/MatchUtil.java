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

package de.micromata.genome.util.matcher.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.text.TextSplitterUtils;
import de.micromata.genome.util.types.Pair;

/**
 * Utility to matches rules.
 *
 * @author roger@micromata.de
 */
public class MatchUtil
{

  /**
   * Parses a +/- list.
   *
   * @param <T> the generic type
   * @param pattern the pattern
   * @param matchFactory the match factory
   * @return the list
   */
  public static <T> List<Pair<Boolean, Matcher<T>>> parseMatcherRuleList(String pattern, MatcherFactory<T> matchFactory)
  {
    List<String> rulezz = parseStringTokens(pattern, ", ", false);
    List<Pair<Boolean, Matcher<T>>> ret = new ArrayList<Pair<Boolean, Matcher<T>>>();
    for (String r : rulezz) {
      if (r.startsWith("+") == false && r.startsWith("-") == false) {
        throw new RuntimeException("A Match Rule has to start with + or -. Rules: " + pattern + "; Rule: " + r);
      }
      String p = r.substring(1);
      Matcher<T> matcher = matchFactory.createMatcher(p);
      ret.add(new Pair<Boolean, Matcher<T>>(r.startsWith("+"), matcher));
    }
    return ret;
  }

  /**
   * Parst die Regel.
   *
   * @param pattern the pattern
   * @return the list
   * @throws Exception falls die Regel nicht mit einem + oder - angangen
   */
  public static List<Pair<Boolean, Pattern>> parseRegexpRules(String pattern) throws Exception
  {
    if (StringUtils.isBlank(pattern) == true) {
      return new ArrayList<Pair<Boolean, Pattern>>(0);
    }

    List<String> rulezz = parseStringTokens(pattern, ", ", false);
    List<Pair<Boolean, Pattern>> pr = new ArrayList<Pair<Boolean, Pattern>>();
    for (String r : rulezz) {
      if (r.startsWith("+") == false && r.startsWith("-") == false) {
        throw new Exception("eine Regel muss mit + oder - anfangen. Regeln: " + pattern + "; Regel: " + r);
      }
      Pattern regExp = Pattern.compile(r.substring(1));
      pr.add(new Pair<Boolean, Pattern>(r.startsWith("+"), regExp));
    }
    return pr;
  }

  /**
   * Verwendet einen StringTokenizer und liefert das Ergebnis als Liste.
   *
   * @param text the text
   * @param delimiter the delimiter
   * @param returnDelimiter the return delimiter
   * @return the list
   */
  public static List<String> parseStringTokens(String text, String delimiter, boolean returnDelimiter)
  {
    List<String> result = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(text, delimiter, returnDelimiter);
    while (st.hasMoreTokens() == true) {
      result.add(st.nextToken());
    }
    return result;
  }

  /**
   * Parse String as wildcard rules.
   *
   * @param ruleString the rule string
   * @return List of rules
   * @throws RuntimeException if the string can not be parsed
   */
  @Deprecated
  // use Factory
  public static List<Pair<Boolean, String>> parseWildcardRules(String ruleString)
  {
    if (StringUtils.isBlank(ruleString) == true) {
      return Collections.emptyList();
    }

    List<String> rlist = TextSplitterUtils.parseStringTokens(ruleString, ", ", false);
    List<Pair<Boolean, String>> ret = new ArrayList<Pair<Boolean, String>>();

    for (String br : rlist) {
      if (br.length() < 2) {
        throw new RuntimeException("Invalid rule: " + br + " in rules: " + ruleString);
      }

      char c = br.charAt(0);
      Boolean b = null;
      if (c == '+') {
        b = Boolean.TRUE;
      } else if (c == '-') {
        b = Boolean.FALSE;
      } else {
        throw new RuntimeException("Invalid rule does not start with +/-: " + br + " in rules: " + ruleString);
      }
      ret.add(new Pair<Boolean, String>(b, br.substring(1)));
    }
    return ret;
  }

  /**
   * Matches wild card.
   *
   * @param ruleString the rule string
   * @param stringToMatch the string to match
   * @param defaultValue the default value
   * @return the boolean
   */
  public static Boolean matchesWildCard(String ruleString, String stringToMatch, Boolean defaultValue)
  {
    return matchesWildCard(parseWildcardRules(ruleString), stringToMatch, defaultValue);

  }

  /**
   * Matches wild card.
   *
   * @param rl the rl
   * @param stringToMatch the string to match
   * @param defaultValue the default value
   * @return the boolean
   */
  public static Boolean matchesWildCard(List<Pair<Boolean, String>> rl, String stringToMatch, Boolean defaultValue)
  {
    Boolean ret = defaultValue;
    for (Pair<Boolean, String> pl : rl) {
      if (FilenameUtils.wildcardMatch(stringToMatch, pl.getSecond()) == true) {
        ret = pl.getFirst();
      }
    }
    return ret;
  }
}
