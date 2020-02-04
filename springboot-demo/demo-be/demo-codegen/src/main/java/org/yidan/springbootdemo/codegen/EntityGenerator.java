package org.yidan.springbootdemo.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.yidan.coco.TemplateProcessor;
import org.yidan.coco.db.MetaDataDao;
import org.yidan.coco.db.MetaDataDaoBuilder;
import org.yidan.coco.meta.Database;

import java.util.Arrays;
import java.util.HashMap;

@EnableAutoConfiguration
@ComponentScan(basePackages = "org.yidan")
public class EntityGenerator {
    protected static final Logger logger = LoggerFactory.getLogger(EntityGenerator.class);

    @Autowired
    private MetaDataDaoBuilderFactory builderFactory;


    public void generate(){
        MetaDataDaoBuilder builder = builderFactory.createDaoBuilder();
        String[] prefixes = new String[]{
                "eb_",
                "sys_"
        };
        Arrays.stream(prefixes).forEach(prefix -> {
            MetaDataDao dao = builder.tablePrefix(prefix).build();
            generate(dao);
        });
    }

    public void generate(MetaDataDao metaDataDao){
        try {
            Database database = metaDataDao.getDatabase();

            TemplateProcessor processor = new TemplateProcessor();

            String targetDir = "../../../demo-service/src/main/java/org/yidan/springbootdemo/entity/";
            database.getTables().forEach(table -> {
                HashMap<String, Object> model = new HashMap<>();
                model.put("table", table);
                model.put("package", "com.demo.entity");
                model.put("modelPkg", "com.demo.model");

                processor.generate("classpath*:tpl/Entity2.vm", targetDir + table.getClassName() + ".java", true, model);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EntityGenerator.class).web(WebApplicationType.NONE)
                .run(args);

        EntityGenerator generator = ctx.getBean(EntityGenerator.class);
        generator.generate();
        ctx.close();
    }
}
