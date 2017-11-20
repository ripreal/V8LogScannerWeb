package org.v8LogScanner.dbLayer.scanProfilesPersistence.Specifications;

import org.v8LogScanner.dbLayer.genericRepository.QuerySpecification;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ScanProfileHibSpecByName implements QuerySpecification<ScanProfileHib> {

    private String name;

    public ScanProfileHibSpecByName(String name) {
        this.name = name;
    }

    @Override
    public boolean specified(ScanProfileHib profile) {
        return name == profile.getName();
    }

    public CriteriaQuery<ScanProfileHib> toCriteria(CriteriaBuilder builder) {

        CriteriaQuery<ScanProfileHib> criteria = builder.createQuery(ScanProfileHib.class);
        Root<ScanProfileHib> root = criteria.from(ScanProfileHib.class);

        criteria.where(builder.equal(root.get("name"), name));
        criteria.select(root);

        return criteria;
    }
}
