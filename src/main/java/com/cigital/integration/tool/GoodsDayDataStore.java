package com.cigital.integration.tool;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cigital.integration.service.GoodsService;
import com.cigital.integration.service.impl.GoodsServicesImpl;
import com.cigital.integration.util.DataSourceUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class GoodsDayDataStore implements Job {
	
	final static Logger logger = Logger.getLogger(GoodsDayDataStore.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		Properties prop = null;
		InputStream is = null;
		DataSource ds = null;
		Connection con = null;
		try {
            prop = new Properties();
            is = this.getClass().getResourceAsStream("/database.properties");
            prop.load(is);
		
            /*    Data source creation   */
			MysqlDataSource mysqlDS = new MysqlDataSource();
			mysqlDS.setURL(prop.getProperty("db.url"));
	        mysqlDS.setUser(prop.getProperty("db.userName"));
	        mysqlDS.setPassword(prop.getProperty("db.password"));
	        
	        ds = (DataSource)mysqlDS;
	        con = DataSourceUtil.getConnectionThruDataSource(ds);
	        
	        con.setAutoCommit(false);
	        logger.debug("************** Cron job is started ******************");
	        
	        GoodsService goodsService = new GoodsServicesImpl();
	        goodsService.sellerGoodsBackupDayWise(con);
	        
	        logger.debug("************** Cron job is ended ******************");
	        
	        con.commit();
		}catch(Exception e){
			logger.error("error occured::"+e);
			e.printStackTrace();
		}finally {
			try{
				if(is != null){
					is.close();
				}if(con != null){
					con.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

}
