/*
 * Copyright 2017-2018 Yi-Kun Yang.
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
package org.allnix.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
public class AppConfig {
    private String host_port = "127.0.0.1:8017";
    
    @Primary
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(host_port);
    }
    
    /**
     * Do the following to use this bean.
     *  
     * <pre>
     * @Qualifier("person")
     * @Autowired
     * </pre>
     * @return
     */
    @Bean(name="person")
    public MongoTemplate personMongoTemplate() {
        MongoTemplate bean = new MongoTemplate(mongoClient(), "PersonDb");
        return bean;
    }
    
    @Bean(name="TechlogDb")
    public MongoTemplate techlogMongoTemplate() {
        MongoTemplate bean = new MongoTemplate(mongoClient(), "TechlogDb");
        return bean;
    }
    /*
     * Factory bean that creates the com.mongodb.MongoClient instance
     */
//     public @Bean MongoClientFactoryBean mongo() {
//          MongoClientFactoryBean mongo = new MongoClientFactoryBean();
//          mongo.setHost("localhost");
//          return mongo;
//     }
}
