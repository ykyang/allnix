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
package org.allnix.sql24.model;

import java.math.BigDecimal;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class Aircraft {
  private String aircraftCode;
  private String aircraftType;
  private BigDecimal freightOnly;
  private Integer seating;

  public String getAircraftCode() {
    return aircraftCode;
  }

  public void setAircraftCode(String aircraftCode) {
    this.aircraftCode = aircraftCode;
  }

  public String getAircraftType() {
    return aircraftType;
  }

  public void setAircraftType(String aircraftType) {
    this.aircraftType = aircraftType;
  }

  public BigDecimal getFreightOnly() {
    return freightOnly;
  }

  public void setFreightOnly(BigDecimal freightOnly) {
    this.freightOnly = freightOnly;
  }

  public Integer getSeating() {
    return seating;
  }

  public void setSeating(Integer seating) {
    this.seating = seating;
  }
}
