package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.commonly.Filter;
import org.v8LogScanner.commonly.Filter.ComparisonTypes;
import org.v8LogScanner.rgx.RegExp;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@Entity
@Table
public class RegExpHib extends RegExp {

    private static final long serialVersionUID = 7179273637694103185L;
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @JsonIgnore
    @ManyToOne(targetEntity = ScanProfileHib.class)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private ScanProfileHib profile;

    @Transient
    private Map<PropTypes, FilterHib> filters = new HashMap<>();

    public RegExpHib() {
        super();
    }

    public RegExpHib(EventTypes eventType) {
        super(eventType);
        filters.put(PropTypes.Event, new FilterHib(getFilter(PropTypes.Event)));
    }

    public RegExpHib(RegExp rgx, ScanProfileHib profile) {
        super(rgx.getEventType());
        Map<PropTypes, Filter<String>> filters = rgx.getFilters();
        this.setFilters(filters);
        this.profile = profile;
    }

    public ScanProfileHib getProfile() {
        return profile;
    }

    public void setProfile(ScanProfileHib profile) {
        this.profile = profile;
    }

    @Column
    @Enumerated(EnumType.STRING)
    @Override
    public EventTypes getEventType() {
        return super.getEventType();
    }

    @JsonIgnore // can't construct interface type Filter<String>
    @Override
    public Map<PropTypes, Filter<String>> getFilters() {
        Map<PropTypes, Filter<String>> unwrapped = new HashMap<>();
        Set<PropTypes> props = filters.keySet();
        for (PropTypes prop : props) {
            unwrapped.put(prop, filters.get(prop));
        }
        return unwrapped;
    }

    @JsonIgnore // can't construct interface type Filter<String>
    @Override
    public void setFilters(Map<PropTypes, Filter<String>> filters) {
        this.filters.clear();
        Set<PropTypes> props = filters.keySet();
        for (PropTypes prop : props) {
            this.filters.put(prop, new FilterHib(filters.get(prop)));
        }
    }

    public Map<PropTypes, FilterHib> getFIltersHib() {
        return filters;
    }

    public void setFiltersHib(Map<PropTypes, FilterHib> filters) {
        this.filters = filters;
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
