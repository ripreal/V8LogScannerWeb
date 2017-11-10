package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.commonly.Filter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table
public class FilterHib extends Filter<String> {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "filtervals", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "filterval")
    private List<String> elements = new ArrayList<>();
    private ComparisonTypes comparisonType = ComparisonTypes.equal;
    private static final long serialVersionUID = 983788012091316309L;

    public FilterHib() {
    }

    public FilterHib(Filter<String> filter) {
        this.elements = filter.getElements();
        this.comparisonType = filter.getComparisonType();
    }

    @Override
    public List<String> getElements() {
        return this.elements;
    }

    @Override
    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    @Override
    public ComparisonTypes getComparisonType() {
        return this.comparisonType;
    }

    @Override
    public void setComparisonType(ComparisonTypes comparisonType) {
        this.comparisonType = comparisonType;
    }

    @Override
    public Filter<String> add(String val) {
        elements.add(val);
        return this;
    }

    @JsonIgnore
    @Override
    public boolean isActive() {
        return elements.size() > 0;
    }

    @Override
    public void reset() {
        elements.clear();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public String get(int index) {
        return elements.get(index);
    }

    @Override
    public Iterator<String> iterator() {
        return elements.iterator();
    }

}
