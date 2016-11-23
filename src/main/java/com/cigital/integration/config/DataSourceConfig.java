package com.cigital.integration.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.cigital.integration.service.ConfigService;
import com.cigital.integration.service.GoodsService;
import com.cigital.integration.service.impl.ConfigServiceImpl;
import com.cigital.integration.service.impl.GoodsServicesImpl;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceConfig implements ServletContextListener {
	
	final static Logger logger = Logger.getLogger(DataSourceConfig.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ServletContext servletContext = arg0.getServletContext();
		servletContext.setAttribute("dataSource", null);
		
		logger.debug("Servlet Context is destroyed.........");
	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.debug("Servlet Context is intialized.........");
		
		Properties prop = null;
		InputStream is = null;
        try {
            prop = new Properties();
            is = this.getClass().getResourceAsStream("/database.properties");
            prop.load(is);
		
            /*    Data source creation   */
			MysqlDataSource mysqlDS = new MysqlDataSource();
			mysqlDS.setURL(prop.getProperty("db.url"));
	        mysqlDS.setUser(prop.getProperty("db.userName"));
	        mysqlDS.setPassword(prop.getProperty("db.password"));
	        
	        DataSource ds = (DataSource)mysqlDS;
	        
	        
	        /*    URL with roles map  */
	        ConfigService configService = new ConfigServiceImpl();
	        Map<Integer, List<String>> restrictedUrlMap = configService.getRestrictedUrls(ds.getConnection());
	        
	        System.out.println(restrictedUrlMap);
	        
	        GoodsService goodsService = new GoodsServicesImpl();
	        String goodsJson = goodsService.getAllGoods(ds.getConnection());
	        System.out.println(goodsJson);
	        
	        // Cron job for maintain every day goods price.
	        GoodsDayDetailsJob goodsDayDetailsJob = new GoodsDayDetailsJob();
	        goodsDayDetailsJob.startCronJob();
	        
	        ServletContext servletContext = arg0.getServletContext();
	        servletContext.setAttribute("dataSource", ds);
	        servletContext.setAttribute("restrictedUrlMap", restrictedUrlMap);
	        servletContext.setAttribute("goodsJson", goodsJson);
	        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
		}
        System.out.println("done");
	}

}
