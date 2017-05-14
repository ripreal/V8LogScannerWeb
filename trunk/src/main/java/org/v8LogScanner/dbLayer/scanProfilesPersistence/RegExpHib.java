package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import javax.persistence.Transient;

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
  @Transient
  private Map<PropTypes, FilterHib> filters = new HashMap<>();
  
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
    Map<PropTypes, Filter<String>> unwrapped = new HashMap<>();
    Set<PropTypes> props = filters.keySet();
    for(PropTypes prop : props) {
      unwrapped.put(prop, filters.get(prop));
    }
    return unwrapped;
  }
  
  @Override
  public void setFilters(Map<PropTypes, Filter<String>> filters) {
    this.filters.clear();
    Set<PropTypes> props = filters.keySet();
    for(PropTypes prop : props) {
      this.filters.put(prop, new FilterHib(filters.get(prop)));
    }
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
