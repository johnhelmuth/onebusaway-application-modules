/**
 * Copyright (C) 2016 Cambridge Systematics, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.admin.service.bundle.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Status object for job GtfsFullValidationTaskJob
 */
public class GtfsFullValidationTaskJobResult {
  private boolean done = false;
  private String csvFileName = null;
  private String columnName = null;
  private List<String> errors = new ArrayList<String>();
  private long start;
  private long finish;
 
  public GtfsFullValidationTaskJobResult(String csvFileName, String columnNames) {
    this.start = System.currentTimeMillis();
    this.csvFileName = csvFileName;
    this.columnName = columnNames;
  }
  
  public void setDone() {
    done = true;
    this.finish = System.currentTimeMillis();
  }
  
  public long getRunTime() {
    return finish - start;
  }
  
  public boolean isDone() {
    return done;
  }
  public void setCsvFileName(String header) {
    this.csvFileName = header;
  }
  public String getCsvFileName() {
    return csvFileName;
  }
  public void setColumnName(String name) {
    columnName = name;
  }
  public String getColumnName() {
    return columnName;
  }
  public void addError(String msg) {
    errors.add(msg);
  }
  public List<String> getErrors() {
    return errors;
  }
    
}