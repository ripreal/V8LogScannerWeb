package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.commonly.Filter;
import org.v8LogScanner.commonly.Filter.ComparisonTypes;
import org.v8LogScanner.rgx.RegExp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table 
public class RegExpHib extends RegExp{
  
  private static final long serialVersionUID = 7179273637694103185L;
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int id;
  @JsonIgnore
  @ManyToOne(targetEntity=ScanProfileHib.class)
  @JoinColumn(name="profile_id", referencedColumnName="id")
  private ScanProfileHib profile;
  
  public RegExpHib() { super(); }
  
  public RegExpHib(EventTypes eventType){
    super(eventType);
  }
  
  public ScanProfileHib getProfile() {return profile;}
  
  public void setProfile(ScanProfileHib profile) {this.profile = profile;}
  
  @Column
  @Enumerated(EnumType.STRING)
  @Override
  public EventTypes getEventType() {return super.getEventType();}
  
  @Override
  public Map<PropTypes, Filter<String>> getFilters() {
    Map<PropTypes, Filter<String>> t1 = super.getFilters();
    return t1;
  }
  public void setFilters(Map<PropTypes, Filter<String>> filters) {
    super.setFilters(filters);
  }
  
  @JsonIgnore
  @Override
  public ArrayList<PropTypes> getPropsForFiltering() {
    return null;
  }
  @JsonIgnore
  @Override
  public ConcurrentMap<PropTypes, ComparisonTypes> getIntegerCompTypes() {
    return null;
  }
  @JsonIgnore
  @Override
  public ArrayList<String> getUnicRgxPropText() {
    return null;
  }
  @JsonIgnore
  @Override
  public ConcurrentMap<PropTypes, List<String>> getIntegerFilters() {
    return null;
  }
  @JsonIgnore
  @Override
  public List<PropTypes> getGroupingProps() {
    return null;
  }
  @JsonIgnore
  @Override
  public ArrayList<PropTypes> getPropsForGrouping() {
    return null;
  }

}
