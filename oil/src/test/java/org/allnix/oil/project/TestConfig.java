/*
 * Copyright 2018 Yi-Kun Yang.
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
package org.allnix.oil.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


//@EnableJpaRepositories("org.allnix.oil")
//@EntityScan(basePackages = {"org.allnix.oil"})
@Profile("int-test")
@Configuration("ProjectTestConfig")
public class TestConfig {
    @Bean
    public DataSource dataSource() throws IOException {
        
        // - database path - //
        Path path = Paths.get(TestConfig.class.getName()+"DB").toAbsolutePath();
        Path h2DbPath = Paths.get(TestConfig.class.getName()+"DB.mv.db").toAbsolutePath();
        Files.deleteIfExists(h2DbPath);
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:" //
            + path.toString() //
             );
//      dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }
    
    @Bean
    public ProjectLoader projectLoader() {
        ProjectLoader bean = new ProjectLoader();
        return bean;
    }
}
