package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
public class LogsPathHib {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @JsonIgnore
    @ManyToOne(targetEntity = ScanProfileHib.class)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private ScanProfileHib profile;
    @Column
    private String Server;
    @Column
    private String Path;

    public String getServer() {
        return Server;
    }

    public void setServer(String Server) {
        this.Server = Server;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }

    public void setProfile(ScanProfileHib profile) {
        this.profile = profile;
    }

    public ScanProfileHib getProfile() {
        return profile;
    }


}
