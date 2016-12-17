/*
 * Copyright 2016 Yi-Kun Yang.
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
package org.allnix.restjson.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class Main {
  static public void main(String[] args) {
    RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    ObjectNode ans = template.getForObject("http://gturnquist-quoters.cfapps.io/api/random", 
      ObjectNode.class);
    System.out.println(ans.toString());
  }
}
