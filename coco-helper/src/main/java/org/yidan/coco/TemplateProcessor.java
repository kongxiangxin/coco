package org.yidan.coco;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.Properties;


/**
 * velocity template helper
 */
public class TemplateProcessor {

    private Logger logger;
    private String fileEncoding;
    private static final int BUFFER_SIZE = 1024 * 1024 / 2;

    public TemplateProcessor(){
        this.logger = new StdoutLogger();
    }

    private String parseTemplate(Resource templateResource, Object model) {
        Thread thread = Thread.currentThread();
        ClassLoader loader = thread.getContextClassLoader();
        thread.setContextClassLoader(this.getClass().getClassLoader());

        String content;
        try {

            VelocityEngine engine = new VelocityEngine();
            Properties prop = new Properties();
            prop.put(Velocity.ENCODING_DEFAULT, getFileEncoding());
            prop.put(Velocity.INPUT_ENCODING, getFileEncoding());
            prop.put(Velocity.OUTPUT_ENCODING, getFileEncoding());
            prop.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
            prop.put("runtime.log.logsystem.log4j.category", "velocity");
            prop.put("runtime.log.logsystem.log4j.logger", "velocity");
            File templateFile = null;
            if(templateResource.isFile()){
                templateFile = templateResource.getFile();
                String templateFileFolder = templateFile.getParentFile().getCanonicalPath();
                prop.put("file.resource.loader.path", templateFileFolder);
            }
            engine.init(prop);

            StringWriter writer = new StringWriter();
            VelocityContext context = new VelocityContext();
            context.put("model", model);
            context.put("processor", this);

            if(templateFile != null){
                Template template = engine.getTemplate(templateFile.getName(), getFileEncoding());
                template.merge(context, writer);
            }else{
                engine.evaluate(context, writer, "tp", new InputStreamReader(templateResource.getInputStream(), getFileEncoding()));
            }
            content = writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            thread.setContextClassLoader(loader);
        }

        return content;
    }

    private String concatPath(String basePath, String path){
        String result = FilenameUtils.concat(basePath, path);
        if(result != null && File.separatorChar == '\\'){
            //make the path linux style
            result = result.replace(File.separator, "/");
        }
        return result;
    }

    /**
     * 调用velocity引擎解析模板，并把解析出的文本，保存至outputFile中
     * 可以在模板中调用本方法，用来在模板中解析别的模板，并保存至outputFile中
     * @param templateFile 模板路径，相对于.jm.vm文件的路径
     * @param outputFile 输出文件路径，相对于.jm.vm文件的路径
     * @param replaceIfExists 如果outputFile已经存在，是否替换
     * @param model 传入模板velocityContext中的对象，以model为key，在模板中可以用$model引用它
     */
    public void generate(String templateFile, String outputFile, boolean replaceIfExists, Object model){
		try {
            ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
//            String basePath = resource.getFile().getParentFile().getCanonicalPath();
//            String basePath = templateEntry.getParentFile().getCanonicalPath();

            Resource outputResource = resourceLoader.getResource("/");

            String outputFilePath = concatPath(outputResource.getFile().getCanonicalPath(), outputFile);
            File output = new File(outputFilePath);
			if(output.exists() && !replaceIfExists){
				return;
			}

            Resource[] templateResources = resourceLoader.getResources(templateFile);
			if(templateResources.length == 0){
                logger.error("template resource " + templateFile + " not found");
                return;
            }
            Resource templateResource = templateResources[0];
			logger.info("generating " + output.getCanonicalPath() + "...\r");
			String content = parseTemplate(templateResource, model);
			write(content, output);
            logger.info("done. \n ");
        } catch (IOException e) {
            logger.error(e);
		}
    }

    private void write(String content, File output) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            File targetDir = output.getParentFile();
            if(!targetDir.exists()){
                if(!targetDir.mkdirs()){
                    throw new IOException("failed to make dir " + targetDir.getCanonicalPath());
                }
            }
            reader = new BufferedReader(new StringReader(content));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), getFileEncoding()));
            char[] buffer = new char[BUFFER_SIZE];
            int read;
            while((read = reader.read(buffer)) != -1){
                writer.write(buffer, 0, read);
            }
        }  finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getFileEncoding() {
        return StringUtils.isBlank(fileEncoding) ? "UTF-8" : fileEncoding;
    }
}
