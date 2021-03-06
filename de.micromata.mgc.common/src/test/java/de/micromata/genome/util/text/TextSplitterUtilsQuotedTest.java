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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author roger
 * @since 1.2.1
 */
public class TextSplitterUtilsQuotedTest
{
  @Test
  public void test10()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("'\\-a'", new String[] { ","}, false, '\'');
    Assert.assertEquals(1, ret.size());
    Assert.assertEquals("\\-a", ret.get(0));
  }

  @Test
  public void test9()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,b;'b,c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a", ret.get(0));
    Assert.assertEquals("b;b,c", ret.get(1));
  }

  @Test
  public void test8()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,b;b\\,c", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a", ret.get(0));
    Assert.assertEquals("b;b,c", ret.get(1));
  }

  @Test
  public void test7()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\\'n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a'n", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test6()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\\\\n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a\\n", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test5()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\\n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("an", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test4()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a\n,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("a\n", ret.get(0));
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test3()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,'b\\',c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("b',c", ret.get(1));
  }

  @Test
  public void test2()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,'b,c'", new String[] { ","}, true, '\'');
    Assert.assertEquals(3, ret.size());
    Assert.assertEquals("b,c", ret.get(2));
  }

  @Test
  public void testFirst()
  {
    List<String> ret = TextSplitterUtils.parseQuotedStringTokens("a,'b,c'", new String[] { ","}, false, '\'');
    Assert.assertEquals(2, ret.size());
    Assert.assertEquals("b,c", ret.get(1));
  }
}
