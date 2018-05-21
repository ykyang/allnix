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

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.QueryHelper;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.ColumnReference;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.io.csv.CsvReader;

/**
 *
 * @author Yi-Kun Yang ykyang@gmail.com
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TablesawTest {
    static private Logger logger = LoggerFactory.getLogger(TablesawTest.class);
    
    @Test
    public void writeCsvTest() throws IOException {
        Table df = Table.create("CSV");
        double[] a = {1., 2., 3.};
        double[] b = {10., 20., 30.};
        
        df.addColumn(new DoubleColumn("a", a));
        df.addColumn(new DoubleColumn("b", b));
        
        df.write().csv("CSV.csv");
    }
    
    @Test
    public void learn() throws IOException {
        Table df = Table.create("CSV");
        double[] a = {1., 2., 3.};
        double[] b = {10., 20., 30.};
        
        df.addColumn(new DoubleColumn("a", a));
        df.addColumn(new DoubleColumn("b", b));
        
        ColumnReference aref = QueryHelper.column("a");
        Table filtered = df.selectWhere(aref.isEqualTo(2.));
        int rowCount = filtered.rowCount();
        logger.info("row count: {}", rowCount);
        
        int colInd = filtered.columnIndex("b");
        DoubleColumn col = (DoubleColumn) filtered.column(colInd);
        logger.info("value: {}", col.get(0));
        
//        df.write().csv("CSV.csv");
    }
    
//    @Test
    public void testCsv() throws IOException {
        /*
        ColumnType[] columnTypes = {
            FLOAT,      // 0     Number      
            LOCAL_DATE, // 1     Date        
            LOCAL_TIME, // 2     Time        
            SHORT_INT,  // 3     Zone        
            CATEGORY,   // 4     State       
            FLOAT,      // 5     State No    
            FLOAT,      // 6     Scale       
            FLOAT,      // 7     Injuries    
            FLOAT,      // 8     Fatalities  
            FLOAT,      // 9     Loss        
            FLOAT,      // 10    Crop Loss   
            FLOAT,      // 11    Start Lat   
            FLOAT,      // 12    Start Lon   
            FLOAT,      // 13    End Lat     
            FLOAT,      // 14    End Lon     
            FLOAT,      // 15    Length      
            FLOAT,      // 16    Width       
        }
         */
        Table df = Table.read().csv("tornadoes_1950-2014.csv");
        Assertions.assertEquals(df.columns().size(),17);
        
        System.out.println(df.structure().print());
        
        
        String type = CsvReader.printColumnTypes("tornadoes_1950-2014.csv", true, ',');
        System.out.println(type);
        
        
        
        System.out.println(df.first(3).print());
        
    }
}