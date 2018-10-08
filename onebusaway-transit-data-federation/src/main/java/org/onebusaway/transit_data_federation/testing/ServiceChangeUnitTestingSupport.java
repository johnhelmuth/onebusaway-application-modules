/**
 * Copyright (C) 2018 Cambridge Systematics, Inc.
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
package org.onebusaway.transit_data_federation.testing;

import com.camsys.transit.servicechange.DateDescriptor;
import com.camsys.transit.servicechange.EntityDescriptor;
import com.camsys.transit.servicechange.ServiceChange;
import com.camsys.transit.servicechange.ServiceChangeType;
import com.camsys.transit.servicechange.Table;
import com.camsys.transit.servicechange.field_descriptors.AbstractFieldDescriptor;
import com.camsys.transit.servicechange.field_descriptors.ShapesFields;
import com.camsys.transit.servicechange.field_descriptors.StopTimesFields;
import com.camsys.transit.servicechange.field_descriptors.StopsFields;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceChangeUnitTestingSupport {

    public static EntityDescriptor stopTimeEntity(String tripId, String stopId) {
        EntityDescriptor desc = new EntityDescriptor();
        desc.setTripId(tripId);
        desc.setStopId(stopId);
        return desc;
    }

    public static EntityDescriptor tripEntity(String tripId) {
        EntityDescriptor desc = new EntityDescriptor();
        desc.setTripId(tripId);
        return desc;
    }

    public static EntityDescriptor stopEntity(String stopId) {
        EntityDescriptor desc = new EntityDescriptor();
        desc.setStopId(stopId);
        return desc;
    }

    public static List<DateDescriptor> dateDescriptors(LocalDate... dates) {
        List<DateDescriptor> descriptors = new ArrayList<>();
        for (LocalDate ld : dates) {
            DateDescriptor d = new DateDescriptor();
            d.setDate(ld);
            descriptors.add(d);
        }
        return descriptors;
    }

    public static List<DateDescriptor> dateDescriptorsRange(LocalDate... dates) {
        List<DateDescriptor> descriptors = new ArrayList<>();
        for (int i = 0; i < dates.length/2; i++) {
            LocalDate from = dates[i];
            LocalDate to = dates[i+1];
            DateDescriptor desc = new DateDescriptor();
            desc.setFrom(from);
            desc.setTo(to);
            descriptors.add(desc);
        }
        return descriptors;
    }

    public static StopTimesFields stopTimesFieldDescriptor(String tripId, Integer arrivalTime, Integer departureTime,
                                                           String stopId, Integer stopSequence, Integer pickupType, Integer dropOffType,
                                                           Double shapeDistTravelled, Integer timepoint) {
        StopTimesFields fields = new StopTimesFields();
        fields.setTripId(tripId);
        fields.setArrivalTime(arrivalTime == null ? -999 : arrivalTime);
        fields.setDepartureTime(departureTime == null ? -999 : departureTime);
        fields.setStopId(stopId);
        fields.setStopSequence(stopSequence);
        fields.setPickupType(pickupType);
        fields.setDropOffType(dropOffType);
        fields.setShapeDistTraveled(shapeDistTravelled);
        fields.setTimepoint(timepoint);
        return fields;
    }

    public static StopTimesFields stopTimesFieldDescriptor(String tripId, Integer arrivalTime, Integer departureTime,
                                                           String stopId, int stopSequence) {
        return stopTimesFieldDescriptor(tripId, arrivalTime, departureTime, stopId, stopSequence, null, null, null, null);
    }

    public static List<AbstractFieldDescriptor> stopTimesFieldsList(String tripId, Integer arrivalTime, Integer departureTime,
                                                            String stopId, int stopSequence) {
        return Collections.singletonList(stopTimesFieldDescriptor(tripId, arrivalTime, departureTime, stopId, stopSequence));
    }

    public static StopsFields stopsFields(String stopName, Double lat, Double lon) {
        StopsFields stop = new StopsFields();
        stop.setStopName(stopName);
        stop.setStopLat(lat);
        stop.setStopLon(lon);
        return stop;
    }

    public static List<AbstractFieldDescriptor> stopsFieldsList(String stopName, Double lat, Double lon) {
        return Collections.singletonList(stopsFields(stopName, lat, lon));
    }

    public static ShapesFields shapeFields(String shapeId, double lat, double lon, int sequence) {
        ShapesFields fields = new ShapesFields();
        fields.setShapeId(shapeId);
        fields.setShapePtLat(lat);
        fields.setShapePtLon(lon);
        fields.setShapePtSequence(sequence);
        return fields;
    }

    public static ServiceChange serviceChange(Table table, ServiceChangeType type, List<EntityDescriptor> entities,
                                               List<AbstractFieldDescriptor> descriptors, List<DateDescriptor> dates) {
        if (entities == null)
            entities = Collections.emptyList();
        if (descriptors == null)
            descriptors = Collections.emptyList();
        if (dates == null)
            dates = Collections.emptyList();
        ServiceChange change = new ServiceChange();
        change.setTable(table);
        change.setServiceChangeType(type);
        change.setAffectedEntity(entities);
        change.setAffectedField(descriptors);
        change.setAffectedDates(dates);
        return change;
    }

    public static int time(int hours, int minutes, int seconds) {
        return (hours * 3600) + (minutes * 60) + seconds;
    }
}
