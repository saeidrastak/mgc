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

package de.micromata.genome.util.matcher;

import org.junit.Test;

import de.micromata.genome.util.runtime.RuntimeCallable;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class CachedMatcherFactoryTest
{
  BooleanListRulesFactory<String> uncachedFactory = new BooleanListRulesFactory<String>();

  CachedMatcherFactory<String> cachedFactory = new CachedMatcherFactory<String>(new BooleanListRulesFactory<String>());

  public void doAccess(MatcherFactory<String> factory)
  {
    factory.createMatcher("A || B && (*C)");
    factory.createMatcher("${arg.lengt == 0}");
    factory.createMatcher("~all\\.\\+");
  }

  @Test
  public void testSinge()
  {
    doAccess(uncachedFactory);
  }

  @Test
  public void test1tread()
  {
    System.out.println("Uncached Matcherfactory");
    new GenomeCommonsThreadedRunner(200, 1).run(new RuntimeCallable() {

      @Override
      public void call()
      {
        doAccess(uncachedFactory);

      }
    });
    System.out.println("Cached Matcherfactory");
    cachedFactory.clearCache();
    new GenomeCommonsThreadedRunner(200, 1).run(new RuntimeCallable() {

      @Override
      public void call()
      {
        doAccess(cachedFactory);

      }
    });
  }

  @Test
  public void test5tread()
  {
    System.out.println("Uncached Matcherfactory");
    new GenomeCommonsThreadedRunner(500, 5).run(new RuntimeCallable() {

      @Override
      public void call()
      {
        doAccess(uncachedFactory);

      }
    });
    System.out.println("Cached Matcherfactory");
    cachedFactory.clearCache();
    new GenomeCommonsThreadedRunner(500, 5).run(new RuntimeCallable() {

      @Override
      public void call()
      {
        doAccess(cachedFactory);

      }
    });
  }
}
