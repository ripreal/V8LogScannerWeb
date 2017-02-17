package org.v8LogScanner.scanProfilesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.DateRanges;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;
import org.v8LogScanner.rgx.ScanProfile.LogTypes;
import org.v8LogScanner.rgx.ScanProfile.RgxOpTypes;

@Entity
@Table
public class ScanProfileHib {

  private static final long serialVersionUID = -5587558849570472552L;
  
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int id;
  
  @Column
  private String name;
  
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name="logpaths", 
    joinColumns=@JoinColumn(name="profile_id"))
  @Column(name="path")
  private List<String> logpaths = new ArrayList<>();
  
  @Enumerated(EnumType.STRING)
  @Column
  private DateRanges dateRange  = DateRanges.ANY;
  
  @Column(name="evlimit")
  private int limit = 100; // restriction up on amount of events  from
  
  @Enumerated(EnumType.STRING)
  @Column
  private LogTypes logType = LogTypes.ANY;
  
  @Enumerated(EnumType.STRING)
  @Column
  private PropTypes sortingProp = PropTypes.ANY;
  
  @Enumerated(EnumType.STRING)
  @Column
  private GroupTypes groupType = GroupTypes.BY_PROPS;
  
  @OneToMany(mappedBy="profile", 
      fetch = FetchType.EAGER, 
      cascade={CascadeType.ALL})
  private List<RegExpHib> rgxList = new ArrayList<>();

  @Column
  private String rgxExp = "";
  
  @Column
  private RgxOpTypes rgxOp = RgxOpTypes.CURSOR_OP;
  
  @Column
  private String userStartDate = "";
 
  @Column
  private String userEndDate = "";
  
  public ScanProfileHib() {}
  
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
  
  public List<String> getLogPaths() {return this.logpaths;}
  public void setLogPaths(List<String> logpaths) {this.logpaths = logpaths;}
  
  public int getId() {return id;}

  public DateRanges getDateRange() {return dateRange;}
  public void setDateRange(DateRanges dateRange) {this.dateRange = dateRange;}
  
  public int getLimit() {return limit;}
  public void setLimit(int limit) {this.limit = limit;}

  public LogTypes getLogType() {return logType;}
  public void setLogType(LogTypes logType) {this.logType = logType;}
  
  public PropTypes getSortingProp() {return sortingProp;}
  public void setSortingProp(PropTypes sortingProp) {this.sortingProp = sortingProp;}

  public GroupTypes getGroupType() {return groupType;}
  public void setGroupType(GroupTypes groupType) { this.groupType = groupType;}
  

  public List<RegExpHib> getRgxList() {
    //List<RegExp>unwrapped = new ArrayList<>();
    //rgxList.forEach(rgx -> unwrapped.add(rgx.unwrap()));
    //return unwrapped;
    return rgxList;
  }
  public void setRgxList(List<RegExpHib> rgxList) {
   // this.rgxList.clear();
    this.rgxList = rgxList;
    //
   
  }

  public void addRegExp(RegExp regExp) {
    RegExpHib hib = new RegExpHib(regExp);
    rgxList.add(hib);
    hib.setProfile(this);
  }
  
  public String getRgxExp() {return rgxExp;}
  public void setRgxExp(String rgxExp) { this.rgxExp = rgxExp;}

  public RgxOpTypes getRgxOp() {return rgxOp;}
  public void setRgxOp(RgxOpTypes rgxOp) {this.rgxOp = rgxOp;}

  public String[] getUserPeriod() {return new String[] {userStartDate, userEndDate};}
  public void setUserPeriod(String userStartDate, String userEndDate) {
    this.userStartDate = userStartDate;
    this.userEndDate = userEndDate;
  }

  
}
