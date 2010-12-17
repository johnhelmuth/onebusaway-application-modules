package org.onebusaway.transit_data_federation.services.reporting;

import java.util.List;

import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.transit_data.model.ListBean;
import org.onebusaway.transit_data.model.problems.StopProblemReportBean;
import org.onebusaway.transit_data.model.problems.StopProblemReportSummaryBean;
import org.onebusaway.transit_data.model.problems.StopProblemReportQueryBean;
import org.onebusaway.transit_data.model.problems.TripProblemReportBean;
import org.onebusaway.transit_data.model.problems.TripProblemReportSummaryBean;
import org.onebusaway.transit_data.model.problems.TripProblemReportQueryBean;

public interface UserReportingService {

  public void reportProblemWithStop(StopProblemReportBean problem);

  public void reportProblemWithTrip(TripProblemReportBean problem);

  public ListBean<StopProblemReportSummaryBean> getStopProblemReportSummaries(
      StopProblemReportQueryBean query);

  public ListBean<TripProblemReportSummaryBean> getTripProblemReportSummaries(
      TripProblemReportQueryBean query);

  public ListBean<StopProblemReportBean> getStopProblemReports(
      StopProblemReportQueryBean query);

  public ListBean<TripProblemReportBean> getTripProblemReports(
      TripProblemReportQueryBean query);

  public List<StopProblemReportBean> getAllStopProblemReportsForStopId(
      AgencyAndId stopId);

  public List<TripProblemReportBean> getAllTripProblemReportsForTripId(
      AgencyAndId tripId);

  public StopProblemReportBean getStopProblemReportForId(long id);

  public TripProblemReportBean getTripProblemReportForId(long id);

  public void deleteStopProblemReportForId(long id);

  public void deleteTripProblemReportForId(long id);

}
