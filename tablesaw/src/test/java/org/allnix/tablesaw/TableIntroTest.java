package org.allnix.tablesaw;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

/**
 * Learn Tablesaw Table
 * 
 * ./gradlew test --tests TableIntroTest
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TableIntroTest {
    static final private Logger logger = LoggerFactory.getLogger(TableIntroTest.class);
    
    @Test
    public void testReadCsv() throws IOException {
        Table t = Table.read().csv("../../tablesaw/data/baseball.csv");
        logger.info("Tornado Table:\n{}", t.print());
        
        
        logger.info("columnNames(): {}", StringUtils.join(t.columnNames(), ","));
        logger.info("structure():\n{}", t.structure().print());
        logger.info("shape(): {}", t.shape());
    }
    @Test
    public void testAddRemove() {
        logger.info("testAddRemove()");
        Table table = Table.create("Add & Remove");
        DoubleColumn nc;
        
        double[] numbers = {1, 2, 3, 4};
        logger.info("addColumns()");
        nc = DoubleColumn.create("One", numbers);
        table.addColumns(nc);
        
        nc = DoubleColumn.create("Two", numbers);
        table.addColumns(nc);
        
        nc = DoubleColumn.create("Three", numbers);
        table.addColumns(nc);
        logger.info("Table:\n{}", table.print());
        
        logger.info("removeColumns()");
        table.removeColumns("Two");
        logger.info("Table:\n{}", table.print());
        
        
        // retainColumns()
        
        
    }
    @Test
    public void testSelectReject() {
        
    }
}
