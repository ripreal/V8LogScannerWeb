package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ScanProfileHib implements ScanProfile {

    private static final long serialVersionUID = -5587558849570472552L;
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @Column
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "profile",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    private List<LogsPathHib> logPaths;
    @Enumerated(EnumType.STRING)
    @Column
    private DateRanges dateRange;
    @Column(name = "evlimit")
    private int limit;
    @Enumerated(EnumType.STRING)
    @Column
    private LogTypes logType;
    @Enumerated(EnumType.STRING)
    @Column
    private PropTypes sortingProp;
    @Enumerated(EnumType.STRING)
    @Column
    private GroupTypes groupType;
    @OneToMany(mappedBy = "profile",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    private List<RegExpHib> rgxList;
    @Column
    private String rgxExp;
    @Column
    private RgxOpTypes rgxOp;
    @Column
    private String userStartDate;
    @Column
    private String userEndDate;

    // Mapping for simple collections
    //@ElementCollection(fetch = FetchType.LAZY)
    //@CollectionTable(name="logpaths",
    //  joinColumns=@JoinColumn(name="profile_id"))
    //@Column(name="path")
    //private List<String> logpaths = new ArrayList<>();

    public ScanProfileHib() {
        clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLogPaths() {
        List<String> paths = new ArrayList<>();
        for (LogsPathHib pathHib : this.logPaths) {
            paths.add(pathHib.getPath());
        }
        return paths;
    }

    @Override
    public void setLogPaths(List<String> logpaths) {
        this.logPaths = new ArrayList<>();
        for (String path : logpaths) {
            LogsPathHib pathHib = new LogsPathHib(this);
            pathHib.setServer("127.0.0.1");
            pathHib.setPath(path);
            pathHib.setProfile(this);
            this.logPaths.add(pathHib);
        }
    }

    @Override
    public void addLogPath(String logPath) {
        List<String> logpaths = getLogPaths();
        boolean logExist = logpaths.stream().anyMatch(n -> n.compareTo(logPath) == 0);
        if (!logExist) {
            LogsPathHib logPathHib = new LogsPathHib(this);
            logPathHib.setPath(logPath);
            logPathHib.setServer("127.0.0.1");
            logPathHib.setProfile(this);
            this.logPaths.add(logPathHib);
        }
    }

    @JsonProperty("logPaths")
    public List<LogsPathHib> getLogPathsHib() {
        return this.logPaths;
    }

    public void setLogPathsHib(List<LogsPathHib> logPathHib) {
        this.logPaths.clear();
        logPathHib.forEach((log) -> {
            this.logPaths.add(log);
            log.setProfile(this);
        });
    }

    public int getId() {
        return id;
    }

    public void setId() {
        // intentionally left blank
    }

    public DateRanges getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRanges dateRange) {
        this.dateRange = dateRange;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public LogTypes getLogType() {
        return logType;
    }

    public void setLogType(LogTypes logType) {
        this.logType = logType;
    }

    public PropTypes getSortingProp() {
        return sortingProp;
    }

    public void setSortingProp(PropTypes sortingProp) {
        this.sortingProp = sortingProp;
    }

    public GroupTypes getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupTypes groupType) {
        this.groupType = groupType;
    }

    public List<RegExp> getRgxList() {
        List<RegExp> unwrapped = new ArrayList<>();
        rgxList.forEach(rgx -> unwrapped.add(rgx));
        return unwrapped;
    }

    public void setRgxList(List<RegExp> rgxList) {
        this.rgxList.clear();
        rgxList.forEach((rgx) -> {
            this.rgxList.add(new RegExpHib(rgx, this));

        });
    }

    public void addRegExp(RegExp regExp) {

        RegExpHib rgx = new RegExpHib(regExp, this);
        rgx.setProfile(this);
        rgxList.add(rgx);
    }

    public String getRgxExp() {
        return rgxExp;
    }

    public void setRgxExp(String rgxExp) {
        this.rgxExp = rgxExp;
    }

    public RgxOpTypes getRgxOp() {
        return rgxOp;
    }

    public void setRgxOp(RgxOpTypes rgxOp) {
        this.rgxOp = rgxOp;
    }

    public String[] getUserPeriod() {
        return new String[]{userStartDate, userEndDate};
    }

    public void setUserPeriod(String userStartDate, String userEndDate) {
        this.userStartDate = userStartDate;
        this.userEndDate = userEndDate;
    }

    public ScanProfile clone() {
        ScanProfile cloned = new ScanProfileHib();
        cloned.setRgxOp(rgxOp);
        cloned.setDateRange(dateRange);
        cloned.setLimit(limit);
        cloned.setLogType(logType);
        cloned.setSortingProp(sortingProp);
        cloned.setGroupType(groupType);
        cloned.setRgxList(getRgxList());
        cloned.setRgxExp(rgxExp);
        cloned.setUserPeriod(userStartDate, userEndDate);

        return cloned;
    }

    @Override
    public void clear() {
        id = 0;
        name = "";
        logPaths = new ArrayList<>();
        dateRange = DateRanges.ANY;
        limit = 100; // restriction up on amount of events  from
        logType = LogTypes.ANY;
        sortingProp = PropTypes.ANY;
        groupType = GroupTypes.BY_PROPS;
        rgxList = new ArrayList<>();
        rgxExp = "";
        rgxOp = RgxOpTypes.CURSOR_OP;
        userStartDate = "";
        userEndDate = "";
        name = "default profile";
    }
}
