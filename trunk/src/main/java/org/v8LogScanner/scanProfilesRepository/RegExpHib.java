package org.v8LogScanner.scanProfilesRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.EventTypes;

@Entity
@Table
public class RegExpHib{
  
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int id;
  private RegExp regExp;
  @Column
  private EventTypes eventType;
  
  public RegExpHib() {
    this.regExp = new RegExp();
  }
  
  public RegExpHib(RegExp regExp) {
    this.regExp = regExp;
  }
  
  public RegExp unwrap() {
    return regExp;
  }
  
  public EventTypes getEventType() {return eventType;}
  public void setEventTypes(EventTypes eventType) {
    regExp = new RegExp(eventType);
    this.eventType = eventType;
  }
  
}
