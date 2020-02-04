package org.yidan.springbootdemo.codegen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yidan.coco.db.MetaDataDaoBuilder;
import org.yidan.coco.settings.JdbcProperty;

@Component
public class MetaDataDaoBuilderFactory {
    @Autowired
    private JdbcProperty jdbcProperty;

    public MetaDataDaoBuilder createDaoBuilder(){
        MetaDataDaoBuilder builder = new MetaDataDaoBuilder();
        return builder.jdbcProperty(jdbcProperty)
                .truncatePrefix(true);
    }
}
