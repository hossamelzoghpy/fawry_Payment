package com.fawary.fawarypayment.entity.sequence;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;

public class BillerIdGenerator extends SequenceStyleGenerator {
    private static final String PREFIX = "BILL_";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = session
                .createNativeQuery("SELECT nextval('biller_id_seq')", Long.class)
                .getSingleResult();

        return PREFIX + nextVal;
    }

}
