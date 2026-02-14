package com.fawary.fawarypayment.entity.sequence;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;
import java.math.BigInteger;

public class GatewayIdGenerator extends SequenceStyleGenerator {
    private static final String PREFIX = "gateway_";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = session
                .createNativeQuery("SELECT nextval('gateway_config_seq')", Long.class)
                .getSingleResult();

        return PREFIX + nextVal;
    }

}
