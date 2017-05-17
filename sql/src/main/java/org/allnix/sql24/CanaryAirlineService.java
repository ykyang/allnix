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
package org.allnix.sql24;

import org.allnix.sql24.model.Aircraft;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class CanaryAirlineService {
  private AircraftDao aircraftDao;

  public void setAircraftDao(AircraftDao aircraftDao) {
    this.aircraftDao = aircraftDao;
  }
  
  @Transactional(readOnly = true)
  public Aircraft findOneAircraft(String aircraftCode) {
    return aircraftDao.findOne(aircraftCode);
  }
  
  @Transactional(readOnly = false)
  public void deleteAircraft(String aircraftCode) {
    aircraftDao.deleteAircraft(aircraftCode);
  }
}
