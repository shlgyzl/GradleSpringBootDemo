package com.application.dynamic_data_source;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import javax.sql.DataSource;
import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author yanghaiyong
 * 2020/7/2-20:52
 */
public class DynamicDataSourceRegisterUtil {
    public final static String DATASOURCE_PARAM_PREF = "spring.datasource.";
    public final static String DATASOURCE_PARAM_PRPO_KEY_DRIVER_CLASS_NAME = "driver-class-name";
    public final static String DRUID_DATASOURCE_PARAM_PREF = "spring.datasource.druid.";
    public final static String DATASOURCE_PARAM_TYPE = "druid.";
    public final static String DATASOURCE_PARAM_SPLIT = ".";
    public final static String DEFAULT_TARGET_DATA_SOURCE = "defaultTargetDataSource";
    public final static String TARGET_DATA_SOURCES = "targetDataSources";
    public final static String DATA_SOURCE = "dataSource";
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegisterUtil.class);
    private static final ConversionService conversionService = new DefaultConversionService();
    private static final String JDBC_FILE_NAME = "jdbc.properties";
    private static final String CUSTOM_DATA_SOURCES_MAP_NAME = "customDataSourcesMap";

    private static PropertyValues dataSourcePropertyValues;
    // 数据源
    private static DataSource defaultDataSource;
    private static Map<String, DataSource> customDataSources = new HashMap<>();

    /**
     * 创建DataSource
     *
     * @return
     */
    private static DataSource buildDataSource(Map<String, Object> dsMap) {
        DataSource dataSource;
        try {
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setUsername((String) dsMap.get("username"));
            hikariDataSource.setPassword((String) dsMap.get("password"));
            hikariDataSource.setJdbcUrl((String) dsMap.get("url"));
            hikariDataSource.setDriverClassName((String) dsMap.get("driver-class-name"));
            hikariDataSource.setPoolName("你好呀");
            Properties properties = new Properties();
            properties.putAll((Map<String, Object>) dsMap.get("hikari"));
            //hikariDataSource.setDataSourceProperties(properties);
            dataSource = hikariDataSource;
            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initAndRegisterDataSource(Environment env) {
        //将自定义的数据源的信息加载到spring 的Environment中
        try {
            initCustomEnvironment(env);
        } catch (IOException e) {
            logger.error("初始化自定义数据库配置文件出错", e);
        }
        //产生主数据源，默认数据源配置在application.properties中的数据源
        initDefaultDataSource(env);
        //产生主自定义的数据源，默认数据源配置在jdbc.properties中的数据源
        initCustomDataSources(env);
        //将主数据员，自定义数据源注入到动态数据源DynamicDataSource ，
        // 再将DynamicDataSource 以名称“dataSource”注入到spring中
        registerDataSource(DATA_SOURCE);
    }

    /**
     * 更新配置文件，并热加载到Environment中
     *
     * @param properties
     * @throws IOException
     */
    public synchronized static void refreshDataSoureProperties(Properties properties) throws IOException {
        //将属性持久化到配置文件
        OutputStream out = new FileOutputStream(URLEncoder.encode(DynamicDataSourceRegisterUtil.class.getClassLoader().getResource(JDBC_FILE_NAME).getPath(), "utf-8"));
        properties.store(out, "更新数据库");
        out.flush();
        out.close();
        //将属性热加载到环境中去
        Environment env = EnvironmentUtils.getEnvironment();
        PropertySource<?> source = new PropertiesPropertySource(CUSTOM_DATA_SOURCES_MAP_NAME, properties);
        ((StandardEnvironment) env).getPropertySources().addLast(source);
        refreshDataSource(env);
    }

    /**
     * 初始化主数据源
     */
    @SuppressWarnings("unchecked")
    private static void initDefaultDataSource(Environment env) {
        Iterable<ConfigurationPropertySource> propertySources = ConfigurationPropertySources.get(env);
        Binder binder = new Binder(propertySources);
        BindResult<Map> bindResult = binder.bind("spring.datasource", Map.class);
        Map<String, Object> map = (Map<String, Object>) bindResult.get();

        // 读取主数据源
        defaultDataSource = buildDataSource(map);
        dataBinder(defaultDataSource, env);
    }

    /**
     * 关闭老的数据源
     */
    private static void closeOldCustomDataSources() {
        if (customDataSources != null && customDataSources.size() > 0) {
            for (String key : customDataSources.keySet()) {
                DataSource dataSource = customDataSources.get(key);
                if (dataSource instanceof HikariDataSource) {
                    ((HikariDataSource) dataSource).close();
                    logger.info("closed datasource " + key);
                }
            }
        }
        if (customDataSources != null) {
            customDataSources.clear();
        }
    }

    public synchronized static void initCustomEnvironment(Environment env) throws IOException {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(URLEncoder.encode(DynamicDataSourceRegisterUtil.class.getClassLoader().getResource(JDBC_FILE_NAME).getPath(), "utf-8"));
            properties.load(in);
            PropertySource<?> source = new PropertiesPropertySource(CUSTOM_DATA_SOURCES_MAP_NAME, properties);
            ((StandardEnvironment) env).getPropertySources().addLast(source);
        } catch (Exception e) {

        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 初始化更多数据源
     */
    private static void initCustomDataSources(Environment env) {
        //初始化之前要先将老的数据源关闭
        closeOldCustomDataSources();
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("root");
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/slave_one?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true");
        try {
            hikariDataSource.getConnection();
            // 判断数据库中是否存在这样的数据源,有则更新,无则新增
        } catch (Exception e) {
            logger.error("保存数据库连接信息失败", e);
        }
        customDataSources.put("slave_one", hikariDataSource);
        /*RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, DATASOURCE_PARAM_PREF);
        String dsPrefixs = propertyResolver.getProperty("keys");
        if (dsPrefixs == null) {
            return;
        }
        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
            RelaxedPropertyResolver propertys = new RelaxedPropertyResolver(env, dsPrefix.trim() + DATASOURCE_PARAM_SPLIT + DATASOURCE_PARAM_PREF);
            Map<String, Object> dsMap = propertys.getSubProperties(DATASOURCE_PARAM_TYPE);
            DataSource dataSource = buildDataSource(dsMap);
            dataBinder(dataSource, env);
            customDataSources.put(dsPrefix, dataSource);
            try {
                //向库中插入信息
                String password = (String) dsMap.get(DruidDataSourceFactory.PROP_PASSWORD);
                String url = (String) dsMap.get(DruidDataSourceFactory.PROP_URL);
                String username = (String) dsMap.get(DruidDataSourceFactory.PROP_USERNAME);
                String strs[] = url.split(";");
                String dbName = "";
                for (String s : strs) {
                    if (s != null && s.trim() != null) {
                        if (s.toUpperCase().contains("DATABASENAME")) {
                            dbName = s.split("=")[1];
                        }
                    }
                }
                //Data Source=192.168.5.92;Initial Catalog=MS_CUBE_TEST;User ID=sa;Password=root;
                String connectionStr = "Data Source=" + strs[0].replaceAll(".*(\\d{3}(\\.\\d{1,3}){3}).*", "$1")
                        + ";Initial Catalog=" + dbName + ";User ID=" + username + ";Password=" + password + ";";

                updateConnectionStr2Db(dsMap, connectionStr);
            } catch (Exception e) {
                logger.error("将数据源配置信息写入到数据库失败！", e);
            }
        }*/
    }

    private static void updateConnectionStr2Db(Map<String, Object> dataSource, String connectionStr) throws SQLException {
       /* Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName((String) dataSource.get(DATASOURCE_PARAM_PRPO_KEY_DRIVER_CLASS_NAME));
            connection = DriverManager.getConnection((String) dataSource.get(DruidDataSourceFactory.PROP_URL),
                    (String) dataSource.get(DruidDataSourceFactory.PROP_USERNAME),
                    (String) dataSource.get(DruidDataSourceFactory.PROP_PASSWORD));
            preparedStatement = connection.prepareStatement("merge into ssas_base_config_prop p using(\n" +
                    "\tselect 'clrSSASConfigurationConnectionString' as  properties_key\n" +
                    ") t on (t.properties_key=p.properties_key)\n" +
                    "when matched then\n" +
                    "update set  properties_value=?\n" +
                    "when not matched then\n" +
                    "insert (properties_key,properties_value,properties_name,display_type)values('clrSSASConfigurationConnectionString',?,'当前环境数据库','text');\n");
            preparedStatement.setString(1, connectionStr);
            preparedStatement.setString(2, connectionStr);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            logger.error("保存数据库连接信息失败", e);
        } finally {
            preparedStatement.close();
            connection.close();
        }*/
    }

    /**
     * 更新配置之后要更新DynamicDataSource
     *
     * @param environment
     */
    private static void refreshDataSource(Environment environment) {
        initCustomDataSources(environment);
        DynamicDataSource dynamicDataSource = ApplicationContextUtil.getBean(DATA_SOURCE);
        dynamicDataSource.updateTargetDataSource(customDataSources);
        DynamicDataSourceContextHolder.dataSourceIds.clear();
        DynamicDataSourceContextHolder.dataSourceIds.addAll(customDataSources.keySet());
    }

    /**
     * 将动态数据源注册到spring中
     *
     * @param dataSourceName
     */
    private static void registerDataSource(String dataSourceName) {
        Map<String, Object> targetDataSources = new HashMap<>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put(DEFAULT_TARGET_DATA_SOURCE, defaultDataSource);
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        DynamicDataSourceContextHolder.dataSourceIds.addAll(customDataSources.keySet());
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put(DEFAULT_TARGET_DATA_SOURCE, defaultDataSource);
        paramValues.put(TARGET_DATA_SOURCES, targetDataSources);
        //ApplicationContextUtil.registerSingletonBean(dataSourceName, DynamicDataSource.class, paramValues);
        logger.info("Dynamic DataSource Registry");
    }

    /**
     * 为DataSource绑定更多数据
     *
     * @param dataSource
     * @param env
     */
    private static void dataBinder(DataSource dataSource, Environment env) {
        // 拿到数据源信息,利用转换工具将属性设置进数据源中
        Map<String, String> properties = org.springframework.cloud.env.EnvironmentUtils.getSubProperties(env, "spring.datasource");
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("driver-class-name", "driver-class");
        aliases.addAliases("url", "jdbc-url");
        aliases.addAliases("username", "user");
        Binder binder = new Binder(source.withAliases(aliases));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
        /*RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);//false
        dataBinder.setIgnoreInvalidFields(false);//false
        dataBinder.setIgnoreUnknownFields(true);//true
        if (dataSourcePropertyValues == null) {
            Map<String, Object> rpr = new RelaxedPropertyResolver(env, DATASOURCE_PARAM_PREF).getSubProperties(DATASOURCE_PARAM_TYPE);
            Map<String, Object> values = new HashMap<>(rpr);
            // 排除已经设置的属性
            values.remove("type");
            values.remove(DATASOURCE_PARAM_PRPO_KEY_DRIVER_CLASS_NAME);
            values.remove("url");
            values.remove("username");
            values.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);*/
    }
}

