import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorCode {

    // 数据库驱动名称
    private static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    // 数据库连接地址
    private static final String url = "jdbc:sqlserver://192.168.252.101:1433;databaseName=solidwaste_battery_dev";
    // 数据库用户名
    private static final String userName = "sa";
    // 数据库密码
    private static final String passWord = "mlsc.1234";
    // 生成文件顶部 注解 作者的名字
    private static final String author = "XueYuan";
    // 生成代码父类包名 eg: cc.bolgx.wechat
    private static final String parentPackName = "com.shencai.solidwaste";
    // 生成代码的模块名 eg：systemUser
    private static final String moduleName = "";
    // 输入生成代码的路径
    private static final String outPutDir = "";
    // 需要快速生成的表名 多个可以用,分割  eg:system,user,xxxx
    private static final String tableNames = "";


    @Test
    public void generatorCode() {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(initDataSourceConfig());
        mpg.setGlobalConfig(initGlobalConfig());
        mpg.setPackageInfo(initPackageConfig());
        mpg.setStrategy(initStrategyConfig());
        mpg.setCfg(initInjectionConfig());
        mpg.setTemplate(initTemplateConfig());
        mpg.execute();
    }


    /**
     * 数据源配置
     */
    private DataSourceConfig initDataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName(driverName);
        dsc.setUrl(url);
        dsc.setUsername(userName);
        dsc.setPassword(passWord);
        return dsc;
    }

    /**
     * 全局配置
     */
    private GlobalConfig initGlobalConfig() {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outPutDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor(author);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        return gc;
    }

    /**
     * 包配置
     */
    private PackageConfig initPackageConfig() {
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackName);
        pc.setEntity(MessageFormat.format("{0}.entity", moduleName));
        pc.setController(MessageFormat.format("{0}.controller", moduleName));
        pc.setService(MessageFormat.format("{0}.service", moduleName));
        pc.setServiceImpl(MessageFormat.format("{0}.service.impl", moduleName));
        pc.setMapper(MessageFormat.format("{0}.mapper", moduleName));
        return pc;
    }

    /**
     * 策略配置
     */
    private StrategyConfig initStrategyConfig() {
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // strategy.setTablePrefix(new String[] { "yunxin_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(tableNames.split(",")); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
//         strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuliderModel(true);
        return strategy;
    }

    /**
     * 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
     */
    private InjectionConfig initInjectionConfig() {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outPutDir + "/xml/" + tableInfo.getEntityName() + ".xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    /**
     * 模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
     * 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
     */
    private TemplateConfig initTemplateConfig() {
        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);

        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        return tc;
    }


}
