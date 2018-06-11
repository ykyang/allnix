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
package org.allnix.tablesaw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.opencsv.CSVWriter;

import tech.tablesaw.api.Table;

/**
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class CsvWriter {
    static public void write(Table table, Writer writer, boolean header) throws IOException {
        if (header) {
            tech.tablesaw.io.csv.CsvWriter.write(table, writer);
            return;
        }
        
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            for (int r = 0; r < table.rowCount(); r++) {
                String[] entries = new String[table.columnCount()];
                for (int c = 0; c < table.columnCount(); c++) {
                    entries[c] = table.get(r, c);
                }
                csvWriter.writeNext(entries, false);
            }
        }
    }
    
    static public void write(Table table, File file, boolean header) throws IOException {
        write(table, new FileWriter(file), header);    
     }
    
    public static void write(Table table, String fileName, boolean header) throws IOException {
        write(table, new File(fileName), header);    
     }
}
