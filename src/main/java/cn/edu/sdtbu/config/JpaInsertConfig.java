package cn.edu.sdtbu.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * used to generator primary key
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-04-29 19:35
 */
public class JpaInsertConfig extends IdentityGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) throws HibernateException {
        Serializable id = s.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, s);
        // if obj's id not null, use obj's id
        if (id != null && Integer.parseInt(id.toString()) > 0) {
            return id;
        } else {
            //else used auto id
            return super.generate(s, obj);
        }
    }
}
