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
package org.allnix.core;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Store JSON as files
 *
 * The table name is used as the file extension and the ID is the file name.
 * The table name is case insensitive in order to conform to SQL implementation.
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class FileJsonDao implements JsonDao {

  static private final Logger logger = LoggerFactory.getLogger(FileJsonDao.class);

  private String databaseFolder;
//  private final ObjectMapper mapper;

  public void setDatabaseFolder(String databaseFolder) {
    this.databaseFolder = databaseFolder;
  }

  public FileJsonDao() {
//    mapper = new ObjectMapper();
  }

  /**
   * Calculate the file name based on ID and table name.
   *
   * @param tableName Table name will be converted to all lower cases
   * @param id
   * @return File name
   */
  private String fileName(String tableName, String id) {
    return id + "." + tableName.toLowerCase();
  }

  @Override
  public boolean create(String tableName, String id, String json) {
    Path path = Paths.get(databaseFolder, fileName(tableName, id));

    if (Files.exists(path)) {
      return false;
    }

    try {
      FileUtils.writeStringToFile(path.toFile(), json, StandardCharsets.UTF_8);
//      mapper.writeValue(path.toFile(), json);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }

    return true;
  }

  @Override
  public String read(String tableName, String id) {
    Path path = Paths.get(databaseFolder, fileName(tableName, id));

    if (!Files.isRegularFile(path)) {
      return null;
    }

    try {
      String json = FileUtils.readFileToString(path.toFile(),
        StandardCharsets.UTF_8);
      return json;
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  @Override
  public boolean update(String tableName, String id, String json) {
    if (!hasId(tableName, id)) {
      return false;
    }

    Path path = Paths.get(databaseFolder, fileName(tableName, id));

    try {
      FileUtils.writeStringToFile(path.toFile(), json, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }

    return true;
  }

  @Override
  public boolean delete(String tableName, String id) {
    if (!hasId(tableName, id)) {
      return false;
    }
    
    Path path = Paths.get(databaseFolder, fileName(tableName, id));
    
    try {
      Files.delete(path);
      return true;
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public boolean hasId(String tableName, String id) {
    Path path = Paths.get(databaseFolder, fileName(tableName, id));

    if (Files.isRegularFile(path)) {
      return true;
    } else {
      return false;
    }
  }

}
