package org.v8LogScanner.scanProfilesRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.rgx.ScanProfile;

@Entity
@Table
public class LogsPathHib {
  
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int id;
  @ManyToOne(targetEntity=ScanProfileHib.class)
  @JoinColumn(name="profile_id", referencedColumnName="id")
  private ScanProfileHib profile;
  @Column
  private String Server;
  @Column
  private String Path;
  
  public String getServer() {return Server;}
  public void setServer(String Server) {this.Server = Server;}
  
  public String getPath() {return Path;}
  public void setPath(String Path) {this.Path = Path;}
  
  public void setProfile(ScanProfileHib profile) {
    this.profile = profile;
  }
  public ScanProfileHib getProfile() {
    return profile;
  }
  
}
