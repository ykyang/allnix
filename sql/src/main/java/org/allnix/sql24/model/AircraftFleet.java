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

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class AircraftFleet {
   private int aircraftFleetId;
   private String aircraftCode;
   private String aircraftDesignator;
   private String status;
   private Integer homeAirportId;

  public int getAircraftFleetId() {
    return aircraftFleetId;
  }

  public void setAircraftFleetId(int aircraftFleetId) {
    this.aircraftFleetId = aircraftFleetId;
  }

  public String getAircraftCode() {
    return aircraftCode;
  }

  public void setAircraftCode(String aircraftCode) {
    this.aircraftCode = aircraftCode;
  }

  public String getAircraftDesignator() {
    return aircraftDesignator;
  }

  public void setAircraftDesignator(String aircraftDesignator) {
    this.aircraftDesignator = aircraftDesignator;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getHomeAirportId() {
    return homeAirportId;
  }

  public void setHomeAirportId(Integer homeAirportId) {
    this.homeAirportId = homeAirportId;
  }
}
