/**
 * Copyright (C) 2011 Google, Inc.
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
package org.onebusaway.transit_data_federation.impl.realtime.gtfs_realtime;

import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.transit_data_federation.impl.service_alerts.ServiceAlertLibrary;
import org.onebusaway.transit_data_federation.services.EntityIdService;
import org.onebusaway.transit_data_federation.services.service_alerts.ServiceAlerts;
import org.onebusaway.transit_data_federation.services.service_alerts.ServiceAlerts.Affects;
import org.onebusaway.transit_data_federation.services.service_alerts.ServiceAlerts.Consequence;
import org.onebusaway.transit_data_federation.services.service_alerts.ServiceAlerts.Id;
import org.onebusaway.transit_data_federation.services.service_alerts.ServiceAlerts.ServiceAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.EntitySelector;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;

import java.util.List;

import static org.onebusaway.transit_data_federation.impl.service_alerts.ServiceAlertLibrary.id;

class GtfsRealtimeAlertLibrary {

  private static final Logger _log = LoggerFactory.getLogger(GtfsRealtimeAlertLibrary.class);

  private EntityIdService _entityIdService;

  private String[] _agencyIds = {};
  void setAgencyIds(List<String> agencies) {
    if (agencies != null) {
      _agencyIds = agencies.toArray(_agencyIds);
    }
  }
  private boolean _stripAgencyPrefix = true;
  public void setStripAgencyPrefix(boolean remove) {
    _stripAgencyPrefix = remove;
  }

  public void setEntitySource(EntityIdService entitySource) {
    _entityIdService = entitySource;
  }

  public ServiceAlert.Builder getAlertAsServiceAlert(AgencyAndId id, Alert alert) {
    ServiceAlert.Builder b = ServiceAlert.newBuilder();
    b.setCreationTime(System.currentTimeMillis());
    b.setModifiedTime(System.currentTimeMillis());
    b.setId(ServiceAlertLibrary.id(id));
    for (GtfsRealtime.TimeRange range : alert.getActivePeriodList()) {
      ServiceAlerts.TimeRange.Builder rangeBuilder = ServiceAlerts.TimeRange.newBuilder();
      if (range.hasStart())
        rangeBuilder.setStart(range.getStart());
      if (range.hasEnd())
        rangeBuilder.setEnd(range.getEnd());
      b.addActiveWindow(rangeBuilder);
    }
    if (alert.hasCause())
      b.setCause(convertCause(alert.getCause()));
    if (alert.hasHeaderText())
      b.setSummary(convertTranslatedString(alert.getHeaderText()));
    if (alert.hasDescriptionText())
      b.setDescription(convertTranslatedString(alert.getDescriptionText()));
    if (alert.hasEffect()) {
      Consequence.Builder consequence = Consequence.newBuilder();
      consequence.setEffect(convertEffect(alert.getEffect()));
      b.addConsequence(consequence);
    }
    for (EntitySelector selector : alert.getInformedEntityList()) {
      Affects.Builder affects = getEntitySelectorAsAffects(selector);
      b.addAffects(affects);
    }
    if (alert.hasUrl())
      b.setUrl(convertTranslatedString(alert.getUrl()));
    return b;
  }

  private Affects.Builder getEntitySelectorAsAffects(EntitySelector selector) {
    Affects.Builder affects = Affects.newBuilder();
    if (selector.hasAgencyId())
      affects.setAgencyId(selector.getAgencyId());
    if (selector.hasRouteId()) {
      Id routeId = id(_entityIdService.getRouteId(idOnly(selector.getRouteId())));
      affects.setRouteId(routeId);
    }
    if (selector.hasStopId()) {
      Id stopId = id(_entityIdService.getStopId(selector.getStopId()));
      affects.setStopId(stopId);
    }
    if (selector.hasTrip()) {
      TripDescriptor trip = selector.getTrip();
      if (trip.hasTripId())
        affects.setTripId(id(_entityIdService.getTripId(trip.getTripId())));
      else if (trip.hasRouteId())
        affects.setRouteId(id(_entityIdService.getRouteId(idOnly(trip.getRouteId()))));
    }
    return affects;
  }

  private String idOnly(String s) {
    if (s == null || !_stripAgencyPrefix) return s;
    for (String t : _agencyIds) {
      s = s.replace(t+"_", "");
    }
    return s;
  }

  private ServiceAlert.Cause convertCause(Alert.Cause cause) {
    switch (cause) {
      case ACCIDENT:
        return ServiceAlert.Cause.ACCIDENT;
      case CONSTRUCTION:
        return ServiceAlert.Cause.CONSTRUCTION;
      case DEMONSTRATION:
        return ServiceAlert.Cause.DEMONSTRATION;
      case HOLIDAY:
        return ServiceAlert.Cause.HOLIDAY;
      case MAINTENANCE:
        return ServiceAlert.Cause.MAINTENANCE;
      case MEDICAL_EMERGENCY:
        return ServiceAlert.Cause.MEDICAL_EMERGENCY;
      case OTHER_CAUSE:
        return ServiceAlert.Cause.OTHER_CAUSE;
      case POLICE_ACTIVITY:
        return ServiceAlert.Cause.POLICE_ACTIVITY;
      case STRIKE:
        return ServiceAlert.Cause.STRIKE;
      case TECHNICAL_PROBLEM:
        return ServiceAlert.Cause.TECHNICAL_PROBLEM;
      case UNKNOWN_CAUSE:
        return ServiceAlert.Cause.UNKNOWN_CAUSE;
      case WEATHER:
        return ServiceAlert.Cause.WEATHER;
      default:
        _log.warn("unknown GtfsRealtime.Alert.Cause " + cause);
        return ServiceAlert.Cause.UNKNOWN_CAUSE;
    }
  }

  private Consequence.Effect convertEffect(Alert.Effect effect) {
    switch (effect) {
      case ADDITIONAL_SERVICE:
        return Consequence.Effect.ADDITIONAL_SERVICE;
      case DETOUR:
        return Consequence.Effect.DETOUR;
      case MODIFIED_SERVICE:
        return Consequence.Effect.MODIFIED_SERVICE;
      case NO_SERVICE:
        return Consequence.Effect.NO_SERVICE;
      case OTHER_EFFECT:
        return Consequence.Effect.OTHER_EFFECT;
      case REDUCED_SERVICE:
        return Consequence.Effect.REDUCED_SERVICE;
      case SIGNIFICANT_DELAYS:
        return Consequence.Effect.SIGNIFICANT_DELAYS;
      case STOP_MOVED:
        return Consequence.Effect.STOP_MOVED;
      case UNKNOWN_EFFECT:
        return Consequence.Effect.UNKNOWN_EFFECT;
      default:
        _log.warn("unknown GtfsRealtime.Alert.Effect " + effect);
        return Consequence.Effect.UNKNOWN_EFFECT;

    }
  }

  private ServiceAlerts.TranslatedString convertTranslatedString(
      GtfsRealtime.TranslatedString string) {
    ServiceAlerts.TranslatedString.Builder b = ServiceAlerts.TranslatedString.newBuilder();
    for (GtfsRealtime.TranslatedString.Translation translation : string.getTranslationList()) {
      ServiceAlerts.TranslatedString.Translation.Builder tb = ServiceAlerts.TranslatedString.Translation.newBuilder();
      if (translation.hasLanguage())
        tb.setLanguage(translation.getLanguage());
      tb.setText(translation.getText());
      b.addTranslation(tb);
    }
    return b.build();
  }

}
