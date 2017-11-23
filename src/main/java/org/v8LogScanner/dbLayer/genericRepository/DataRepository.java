package org.v8LogScanner.dbLayer.genericRepository;

import java.io.Serializable;
import java.util.List;

public interface DataRepository<T> {

    public Serializable add(T object);

    public void remove(T object);

    public void update(T object);

    public void resetCache();

    public List<T> query(QuerySpecification<T> specification);

}
