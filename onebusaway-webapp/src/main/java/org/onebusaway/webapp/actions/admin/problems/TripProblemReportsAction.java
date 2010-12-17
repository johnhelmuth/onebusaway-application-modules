package org.onebusaway.webapp.actions.admin.problems;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.onebusaway.transit_data.model.problems.TripProblemReportBean;
import org.onebusaway.transit_data.services.TransitDataService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Results({@Result(type = "redirectAction", name = "list", params = {
    "actionName", "trip-problem-reports", "tripId", "${model.tripId}", "parse",
    "true"})})
public class TripProblemReportsAction extends ActionSupport implements
    ModelDriven<TripProblemReportBean> {

  private static final long serialVersionUID = 1L;

  private TransitDataService _transitDataService;

  private List<TripProblemReportBean> _reports;

  private TripProblemReportBean _model = new TripProblemReportBean();

  @Autowired
  public void setTransitDataService(TransitDataService transitDataService) {
    _transitDataService = transitDataService;
  }

  @Override
  public TripProblemReportBean getModel() {
    return _model;
  }

  public List<TripProblemReportBean> getReports() {
    return _reports;
  }

  @Override
  @Validations(requiredStrings = {@RequiredStringValidator(fieldName = "model.tripId", message = "missing tripId")})
  public String execute() {
    _reports = _transitDataService.getAllTripProblemReportsForTripId(_model.getTripId());
    return SUCCESS;
  }

  @Validations(requiredFields = {@RequiredFieldValidator(fieldName = "model.id", message = "missing id")}, requiredStrings = {@RequiredStringValidator(fieldName = "model.tripId", message = "missing tripId")})
  public String edit() {
    _model = _transitDataService.getTripProblemReportForTripIdAndId(
        _model.getTripId(), _model.getId());
    return "edit";
  }

  @Validations(requiredFields = {@RequiredFieldValidator(fieldName = "model.id", message = "missing id")}, requiredStrings = {@RequiredStringValidator(fieldName = "model.tripId", message = "missing tripId")})
  public String delete() {
    _transitDataService.deleteTripProblemReportForTripIdAndId(
        _model.getTripId(), _model.getId());

    return "list";
  }
}
