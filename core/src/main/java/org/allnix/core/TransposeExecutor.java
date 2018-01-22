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
package org.allnix.core;

import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TransposeExecutor {
    static final Logger logger = LoggerFactory.getLogger(TransposeExecutor.class);
    /**
     * Transpose a comma separated file.
     * 
     * @param path Input file path
     * @return Output file path
     * @throws IOException 
     */
    String transpose(String path) throws IOException {
        String out = path + ".out";
        
        // datamash -t ',' transpose < csv > cvs
//        String format = "datamash -t ',' transpose < %s > %s";
        String format = "cp %s %s";
        String line = String.format(format, path, out);
        //line = "ls -l";
        logger.info("line: {}", line);
        CommandLine cmd = CommandLine.parse(line);
//        CommandLine cmd = new CommandLine("datamash");
//        cmd.addArgument("-t");
//        cmd.addArgument(",");
//        cmd.addArgument("transpose");
//        cmd.addArgument("<");
//        cmd.addArgument(path, false);
//        cmd.addArgument(">");
//        cmd.addArgument(out, true);
        logger.info(cmd.toString());
        DefaultExecutor executor = new DefaultExecutor();
        
        int exitValue = executor.execute(cmd);
        // TODO: Check exit value
        
        return out;
    }
}
