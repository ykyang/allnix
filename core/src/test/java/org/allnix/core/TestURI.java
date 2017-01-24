/*
 * Copyright 2017 Yi-Kun Yang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.allnix.core;

import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This is more of learning than testing
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TestURI {
  static private final Logger logger = LoggerFactory.getLogger(TestURI.class);
  
  @Test
  public void testURIResolve() throws URISyntaxException {
    URI base = new URI("http", null, "localhost", 12345, "/job", null, null);
    Assert.assertEquals(base.toString(),"http://localhost:12345/job");
    
    URI idbase = new URI("http", null, "localhost", 12345, "/job/", null, null);
    Assert.assertEquals(idbase.toString(),"http://localhost:12345/job/");
    
    URI idquery = idbase.resolve("23456");
    Assert.assertEquals(idquery.toString(),"http://localhost:12345/job/23456");
  }
}
