package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.v8LogScanner.commonly.Filter;
import org.v8LogScanner.commonly.Filter.ComparisonTypes;
import org.v8LogScanner.rgx.StrokeFilter;

@Entity
@Table
public class FilterHib {
  
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private int id;
  private Filter<String> filter;
  
  public FilterHib() {
    this.filter = new StrokeFilter();
  }
  
  public FilterHib(Filter<String> filter) {
    this.filter = filter;
  }
  
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name="filtervals", joinColumns=@JoinColumn(name="filter_id"))
  @Column(name="filterval")
  public List<String> getElements() {return filter.getElements();}
  public void setElements(List<String> elements) {this.setElements(elements);}
  
  public void setComparisonType(ComparisonTypes comparisonType) {filter.comparisonType(comparisonType);}
  public ComparisonTypes getComparisonType(ComparisonTypes comparisonType) { return filter.comparisonType();}
  
}
