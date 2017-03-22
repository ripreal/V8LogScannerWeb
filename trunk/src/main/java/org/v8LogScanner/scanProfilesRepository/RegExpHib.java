package org.v8LogScanner.scanProfilesRepository;

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
import org.v8LogScanner.rgx.RegExp;

@Entity
@Table 
public class RegExpHib extends RegExp{
  
  private static final long serialVersionUID = 7179273637694103185L;
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int id;
  //transient private RegExp regExp; 
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
  
}
