package org.onebusaway.twilio.actions.stops;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.onebusaway.twilio.actions.TwilioSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Results({
  @Result(name="back", location="index", type="redirectAction"),
  @Result(name="stop-for-code", location="stop-for-code", type="chain")
  })
public class IndexAction extends TwilioSupport {

  private static Logger _log = LoggerFactory.getLogger(IndexAction.class);
  private String _stopCode;
  
  public String getStopCode() {
    return _stopCode;
  }
  
  public void setStopCode(String stopCode) {
    _stopCode = stopCode;
  }
  
  @Override
  public String execute() throws Exception {
    _log.debug("in stops index with input=" + getInput());
    
    if (getInput() != null) {
      if ("8".equals(getInput())) {
        return "back";
      }
      setStopCode(getInput());
      _log.debug("forwarding to stops");
      return "stop-for-code";
    }
    
    return INPUT;
  }
}
