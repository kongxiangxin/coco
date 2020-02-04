package coco.demo.generator;

import com.zy.welfare.entity.DefaultIdEntity;
import org.yidan.coco.TemplateProcessor;
import org.yidan.coco.meta.Table;

import java.util.HashMap;

public class Generator2 {
    public static void main(String[] args){
        TemplateProcessor processor = new TemplateProcessor();

        HashMap<String, Object> model = new HashMap<>();
        model.put("table", new Table("sys_user", "user", "User2", "abc user table"));
        model.put("package", "com.demo.entity");
        model.put("modelPkg", "com.demo.model");

        DefaultIdEntity zz = new DefaultIdEntity();

//        ClassPathXmlApplicationContext cc = new ClassPathXmlApplicationContext("")

        processor.generate("classpath*:tpl/Entity2.vm", "../../src/main/java/coco/demo/entity/User2.java", true, model);
    }
}
