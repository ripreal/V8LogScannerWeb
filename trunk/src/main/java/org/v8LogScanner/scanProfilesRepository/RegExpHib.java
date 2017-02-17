package org.v8LogScanner.scanProfilesRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
  
  transient private RegExp regExp; 
  
  @ManyToOne(targetEntity=ScanProfileHib.class)
  @JoinColumn(name="profile_id", referencedColumnName="id")
  private ScanProfileHib profile;
  
  public ScanProfileHib getProfile() {return profile;}
  public void setProfile(ScanProfileHib profile) {this.profile = profile;}

  public RegExpHib() {
    this.regExp = new RegExp();
  }
  
  public RegExpHib(RegExp regExp) {
    this.regExp = regExp;
  }
  
  public RegExp unwrap() {
    return regExp;
  }
  
}
