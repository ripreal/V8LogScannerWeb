package org.v8LogScanner.dbLayer.scanProfilesPersistence.Specifications;

import org.v8LogScanner.dbLayer.genericRepository.QuerySpecification;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ScanProfileHibSpecIfPresent implements QuerySpecification<ScanProfileHib> {

    @Override
    public boolean specified(ScanProfileHib profile) {
        return profile.getId() >= 0;
    }

    public CriteriaQuery<ScanProfileHib> toCriteria(CriteriaBuilder builder) {

        CriteriaQuery<ScanProfileHib> criteria = builder.createQuery(ScanProfileHib.class);

        Root<ScanProfileHib> root = criteria.from(ScanProfileHib.class);
        criteria.select(root).from(ScanProfileHib.class);
        criteria.orderBy(builder.desc(root.get("id")));

        return criteria;
    }
}
