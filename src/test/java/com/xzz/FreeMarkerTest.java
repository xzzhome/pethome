package com.xzz;

import com.xzz.pet.domain.Pet;
import com.xzz.pet.service.IPetService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= PetHomeApp.class)
public class FreeMarkerTest {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${templatedir}")
    private String templatedir;

    @Value("${htmldir}")
    private String htmldir;

    @Autowired
    private IPetService petService;

    @Test
    public void test1() throws Exception{
        //创建模板技术的核心配置对象
        Configuration config = freeMarkerConfigurer.getConfiguration();
        //设置页面中静态内容的编码集
        config.setDefaultEncoding("UTF-8");
        //指定模板的加载路径
        File file = new File(templatedir);
        //设置模板的默认加载路径
        config.setDirectoryForTemplateLoading(file);

        //获取模板对象
        Template template = config.getTemplate("petDetail.ftl");

        Pet pet = petService.findById(1L);

        String fileName = "petDetail_" + pet.getId() + ".html";
        FileOutputStream out = new FileOutputStream(new File(htmldir, fileName));
        OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
        template.process(pet, osw);

    }
}