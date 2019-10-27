/**
 * 
 */
package com.oracle.pcmcs.dbconn.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.oracle.pcmcs.dbutils.dto.DimensionMemberTO;
import com.oracle.pcmcs.dbutils.dto.DimensionTO;

/**
 * @author mbanchho
 *
 */
public class DatabaseUtils {
	private final static Logger log = Logger.getLogger(DatabaseUtils.class.toString());
	public static HashMap<String,String> getDBConnectionDataMap(String host,  String port, String sid,String user,String password){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("host",host);
		map.put("port",port);
		map.put("sid",sid);
		map.put("user",user);
		map.put("password",password);
		return map;
	}
	public static Connection getDBConnection(Map<String,String> connectionData){

		String host = connectionData.get("host");
		String port = connectionData.get("port");
		String user = connectionData.get("user");
		String password = connectionData.get("password");
		String sid = connectionData.get("sid");

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException e){
			//log.error("JDBC Driver class not found");
			throw new RuntimeException("JDBC Driver class not found");
		}
		Connection connection = null;
		try{
			String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
			connection = DriverManager.getConnection(url, user, password);
		}catch(SQLException s){
			//log.error(s.getMessage());
			throw new RuntimeException("Failed to create the connection.");
		}
		return connection;
	}

	public static long getApplicationID(String appName, Map<String,String> connectionData){
		Connection connection = getDBConnection(connectionData);
		String getAppQuery = "select id,name,enabled_flg from hpm_application ";//where name ='"+"'";
		long id = 0;
		try{
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(getAppQuery);
			if(resultSet != null){
				while(resultSet.next()){
					id = resultSet.getLong("id");
					String name = resultSet.getString("name");
					String enabledFlag = resultSet.getString("enabled_flg");
					System.out.println("name : "+name);
					System.out.println("id : "+id);
					System.out.println("enabled : "+enabledFlag);
				}
			}
		}
		catch(SQLException s){
			System.out.println(s.getMessage());
			throw new RuntimeException("Failed to get application from db.");
		}
		return id;
	}
	//select * from hpm_alyt_item where hpm_application_id = 1630961;
	public static long getAnalyticItems(long appID, Map<String,String> connectionData){
		Connection connection = getDBConnection(connectionData);
		String getAppQuery = "select * from hpm_alyt_item where hpm_application_id =  "+ appID;//where name ='"+"'";
		long id = 0;
		try{
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(getAppQuery);
			if(resultSet != null){
				while(resultSet.next()){
					id = resultSet.getLong("id");
					String name = resultSet.getString("name");
					String enabledFlag = resultSet.getString("enabled_flg");
					String item_type = resultSet.getString("ITEM_TYPE");
					System.out.println("name : "+ name + "   enabled : "+enabledFlag+ "    Type : "+item_type);
				}
			}
		}
		catch(SQLException s){
			System.out.println(s.getMessage());
			throw new RuntimeException("Failed to get application from db.");
		}
		return id;
	}

	public static long getRules(long appID, Map<String,String> connectionData){
		Connection connection = getDBConnection(connectionData);
		String getAppQuery = "select * from hpm_alyt_item where hpm_application_id =  "+ appID;//where name ='"+"'";
		long id = 0;
		try{
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(getAppQuery);
			if(resultSet != null){
				while(resultSet.next()){
					id = resultSet.getLong("id");
					String name = resultSet.getString("name");
					String enabledFlag = resultSet.getString("enabled_flg");
					String item_type = resultSet.getString("ITEM_TYPE");
					System.out.println("name : "+ name + "   enabled : "+enabledFlag+ "    Type : "+item_type);
				}
			}
		}
		catch(SQLException s){
			System.out.println(s.getMessage());
			throw new RuntimeException("Failed to get application from db.");
		}
		return id;
	}
	public static List<DimensionTO> getDimensionsFromRDB(String applicationName,Map<String,String> connectionData) {

		//log.entry();
		log.info("Query is going to start running.");
		Connection connection = getDBConnection(connectionData);
		Statement stmt;
		List<DimensionTO> dimensions = null;
		try {
			stmt = connection.createStatement();
			String query = "select d.name dim_name, null mem_name, null mem_parent_name, ( select wm_concat(ad.name) from hpm_dimension_attribute da, hpm_dimension ad where da.hpm_dimension_id_attr = ad.id " +
					"and da.hpm_dimension_id = d.id and ad.hpm_dim_type_id = 5 ) attributes, null udas, ( select "+
					"wm_concat(dmat.name||':'||da.name) from  hpm_dimension_alias da, hpm_alias_table dmat where da.hpm_alias_table_id = dmat.id and da.hpm_dimension_id = d.id ) aliases from  "+
					"hpm_dimension d, hpm_application ha where d.hpm_application_id = ha.id and ha.name = '"+applicationName+ "' and  hpm_dim_type_id not in 6 UNION ALL select d.name dim_name, dm.name mem_name, dmp.name mem_parent_name, (\n" +
					"\n" +
					"select wm_concat(ad.name||':'||adm.name) from hpm_dimension_member_attribute dma, hpm_dimension_member adm, hpm_dimension ad where dma.hpm_dim_mem_id_attr = adm.id and adm.hpm_dimension_id = ad.id\n" +
					"\n" +
					"and ad.hpm_dim_type_id = 5\n" +
					"\n" +
					"and dma.hpm_dim_mem_id = dm.id\n" +
					"\n" +
					") attributes,\n" +
					"\n" +
					"(\n" +
					"\n" +
					"select\n" +
					"\n" +
					"wm_concat(ad.name||':'||adm.name)\n" +
					"\n" +
					"from  hpm_dimension_member_attribute dma,\n" +
					"\n" +
					"      hpm_dimension_member adm,\n" +
					"\n" +
					"      hpm_dimension ad     \n" +
					"\n" +
					"where dma.hpm_dim_mem_id_attr = adm.id\n" +
					"\n" +
					"and adm.hpm_dimension_id = ad.id\n" +
					"\n" +
					"and ad.hpm_dim_type_id = 6\n" +
					"\n" +
					"and dma.hpm_dim_mem_id = dm.id\n" +
					"\n" +
					") udas,\n" +
					"\n" +
					"(\n" +
					"\n" +
					"select\n" +
					"\n" +
					"wm_concat(dmat.name||':'||dma.name)\n" +
					"\n" +
					"from  hpm_dim_mem_alias dma,\n" +
					"\n" +
					"      hpm_alias_table dmat\n" +
					"\n" +
					"where dma.hpm_alias_table_id = dmat.id\n" +
					"\n" +
					"and dma.hpm_dim_mem_id = dm.id\n" +
					"\n" +
					") aliases\n" +
					"\n" +
					"from  hpm_dimension d,\n" +
					"\n" +
					"      hpm_dimension_member dm,\n" +
					"\n" +
					"      hpm_dimension_member dmp,\n" +
					"\n" +
					"      hpm_application ha\n" +
					"\n" +
					"where dm.hpm_dimension_id = d.id\n" +
					"\n" +
					"and dm.parent_id = dmp.id(+)\n" +
					"\n" +
					"and d.hpm_application_id = ha.id\n" +
					"\n" +
					"and ha.name = '" +applicationName+ "' and  hpm_dim_type_id not in 6";

			log.info("Query is done.");
			ResultSet resultSet = stmt.executeQuery(query);
			dimensions = parsingResultToDimensionTO(resultSet);

		} catch (SQLException e) {
			//log.error(e.getMessage());
			throw new RuntimeException("Failed to get Dimension info for  meta data");
		}

		//LOG.exit();
		return dimensions;
	}
	
	// this below query supports 11g data base only - pushed to local branch initially 
public static DimensionTO getDimensionFromRDB(String applicationName,String dimName, Map<String,String> connectionData) {

    	//log.entry();
    	
        Connection connection = getDBConnection(connectionData);
        Statement stmt;
        List<DimensionTO> dimensions = null;
        try {
            stmt = connection.createStatement();
            String query = "select d.name dim_name, null mem_name, null mem_parent_name, ( select wm_concat(ad.name) from hpm_dimension_attribute da, hpm_dimension ad where da.hpm_dimension_id_attr = ad.id " +
                "and da.hpm_dimension_id = d.id and ad.hpm_dim_type_id = 5 ) attributes, null udas, ( select "+
                "wm_concat(dmat.name||':'||da.name) from  hpm_dimension_alias da, hpm_alias_table dmat where da.hpm_alias_table_id = dmat.id and da.hpm_dimension_id = d.id ) aliases from  "+
                "hpm_dimension d, hpm_application ha where d.hpm_application_id = ha.id and d.name like '"+dimName+"' and ha.name = '"+applicationName+ "' and  hpm_dim_type_id not in 6 UNION ALL select d.name dim_name, dm.name mem_name, dmp.name mem_parent_name, (\n" +
            "\n" +
            "select wm_concat(ad.name||':'||adm.name) from hpm_dimension_member_attribute dma, hpm_dimension_member adm, hpm_dimension ad where dma.hpm_dim_mem_id_attr = adm.id and adm.hpm_dimension_id = ad.id\n" +
            "\n" +
            "and ad.hpm_dim_type_id = 5\n" +
            "\n" +
            "and dma.hpm_dim_mem_id = dm.id\n" +
            "\n" +
            ") attributes,\n" +
            "\n" +
            "(\n" +
            "\n" +
            "select\n" +
            "\n" +
            "wm_concat(ad.name||':'||adm.name)\n" +
            "\n" +
            "from  hpm_dimension_member_attribute dma,\n" +
            "\n" +
            "      hpm_dimension_member adm,\n" +
            "\n" +
            "      hpm_dimension ad     \n" +
            "\n" +
            "where dma.hpm_dim_mem_id_attr = adm.id\n" +
            "\n" +
            "and adm.hpm_dimension_id = ad.id\n" +
            "\n" +
            "and ad.hpm_dim_type_id = 6\n" +
            "\n" +
            "and dma.hpm_dim_mem_id = dm.id\n" +
            "\n" +
            ") udas,\n" +
            "\n" +
            "(\n" +
            "\n" +
            "select\n" +
            "\n" +
            "wm_concat(dmat.name||':'||dma.name)\n" +
            "\n" +
            "from  hpm_dim_mem_alias dma,\n" +
            "\n" +
            "      hpm_alias_table dmat\n" +
            "\n" +
            "where dma.hpm_alias_table_id = dmat.id\n" +
            "\n" +
            "and dma.hpm_dim_mem_id = dm.id\n" +
            "\n" +
            ") aliases\n" +
            "\n" +
            "from  hpm_dimension d,\n" +
            "\n" +
            "      hpm_dimension_member dm,\n" +
            "\n" +
            "      hpm_dimension_member dmp,\n" +
            "\n" +
            "      hpm_application ha\n" +
            "\n" +
            "where dm.hpm_dimension_id = d.id\n" +
            "\n" +
            "and dm.parent_id = dmp.id(+)\n" +
            "\n" +
            "and d.hpm_application_id = ha.id\n" +
            "\n" +
            "and ha.name = '" +applicationName+ "' and  hpm_dim_type_id not in 6 and d.name like '"+dimName+"'";
            ResultSet resultSet = stmt.executeQuery(query);
            dimensions = parsingResultToDimensionTO(resultSet);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get Dimension info for  meta data");
        }

        //LOG.exit();
        return dimensions.get(0);
    }

	//this below query supports 12c only data base
	/*public static DimensionTO getDimensionFromRDB(String applicationName,String dimName, Map<String,String> connectionData) {

		//log.entry();

		Connection connection = getDBConnection(connectionData);
		Statement stmt;
		List<DimensionTO> dimensions = null;
		try {
			stmt = connection.createStatement();
			String query = " select d.name dim_name, null mem_name, null mem_parent_name, \r\n" + 
					"(select LISTAGG(ad.name,',') within group(order by ad.id)\r\n" + 
					"from hpm_dimension_attribute da, hpm_dimension ad \r\n" + 
					"where da.hpm_dimension_id_attr = ad.id and da.hpm_dimension_id = d.id and ad.hpm_dim_type_id = 5 )attributes, null udas,\r\n" + 
					"\r\n" + 
					"(SELECT LISTAGG(da.name,',') WITHIN GROUP ( ORDER BY dmat.ID) \"LISTAGG(DA.NAME,',')WITHIN GROUP(ORDER BY DMAT.ID)\" \r\n" + 
					"FROM HPM_DIMENSION_ALIAS da,HPM_ALIAS_TABLE dmat \r\n" + 
					"WHERE da.HPM_ALIAS_TABLE_ID=dmat.ID AND da.HPM_DIMENSION_ID=d.ID )aliases \r\n" + 
					"from  hpm_dimension d, hpm_application ha where d.hpm_application_id = ha.id and d.name like 'Customer' and ha.name = 'BksML30' and  hpm_dim_type_id not in 6 UNION ALL select d.name dim_name, dm.name mem_name, dmp.name mem_parent_name, (\r\n" + 
					"\r\n" + 
					"SELECT LISTAGG(adm.name,',') WITHIN GROUP ( ORDER BY ad.ID) \"LISTAGG(ad.NAME,',')WITHIN GROUP(ORDER BY adm.ID)\" \r\n" + 
					"from hpm_dimension_member_attribute dma, hpm_dimension_member adm, hpm_dimension ad \r\n" + 
					"where dma.hpm_dim_mem_id_attr = adm.id and adm.hpm_dimension_id = ad.id\r\n" + 
					"and ad.hpm_dim_type_id = 5\r\n" + 
					"and dma.hpm_dim_mem_id = adm.id\r\n" + 
					") attributes,\r\n" + 
					"\r\n" + 
					"(SELECT LISTAGG(adm.name,',') WITHIN GROUP ( ORDER BY ad.ID) \"LISTAGG(ad.NAME,',')WITHIN GROUP(ORDER BY adm.ID)\" \r\n" + 
					"from  hpm_dimension_member_attribute dma,\r\n" + 
					"      hpm_dimension_member adm,\r\n" + 
					"\r\n" + 
					"      hpm_dimension ad     \r\n" + 
					"\r\n" + 
					"where dma.hpm_dim_mem_id_attr = adm.id\r\n" + 
					"\r\n" + 
					"and adm.hpm_dimension_id = ad.id\r\n" + 
					"\r\n" + 
					"and ad.hpm_dim_type_id = 6\r\n" + 
					"\r\n" + 
					"and dma.hpm_dim_mem_id = dm.id\r\n" + 
					"\r\n" + 
					") udas,\r\n" + 
					"\r\n" + 
					"(SELECT LISTAGG(dma.name,',') WITHIN GROUP ( ORDER BY dmat.ID) \"LISTAGG(dmat.NAME,',')WITHIN GROUP(ORDER BY dma.ID)\" \r\n" + 
					"\r\n" + 
					"from  hpm_dim_mem_alias dma,\r\n" + 
					"\r\n" + 
					"      hpm_alias_table dmat\r\n" + 
					"\r\n" + 
					"where dma.hpm_alias_table_id = dmat.id\r\n" + 
					"\r\n" + 
					"and dma.hpm_dim_mem_id = dm.id\r\n" + 
					"\r\n" + 
					") aliases\r\n" + 
					"\r\n" + 
					"from  hpm_dimension d,\r\n" + 
					"\r\n" + 
					"      hpm_dimension_member dm,\r\n" + 
					"\r\n" + 
					"      hpm_dimension_member dmp,\r\n" + 
					"\r\n" + 
					"      hpm_application ha\r\n" + 
					"\r\n" + 
					"where dm.hpm_dimension_id = d.id\r\n" + 
					"\r\n" + 
					"and dm.parent_id = dmp.id(+)\r\n" + 
					"\r\n" + 
					"and d.hpm_application_id = ha.id\r\n" + 
					"\r\n" + 
					"and ha.name = '" + applicationName + " ' and  hpm_dim_type_id not in 6 and d.name like ' " + dimName + " '";

			//System.out.println("Executing Query: " + query);
			ResultSet resultSet = stmt.executeQuery(query);

			dimensions = parsingResultToDimensionTO(resultSet);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Failed to get Dimension info for  meta data");
		}

		//LOG.exit();
		return dimensions.get(0);
	}
	
	
*/

	private static List<DimensionTO> parsingResultToDimensionTO(ResultSet resultSet) throws SQLException {

		Map<String, DimensionTO> dimensions = new HashMap<String,DimensionTO>();
		StringCSVParser myParser = new StringCSVParser();
		if(resultSet != null) {
			while(resultSet.next()) {
				String dimName = resultSet.getString("DIM_NAME");
				DimensionTO dimension = null;
				List<DimensionMemberTO> dimMembers = null;
				if(dimensions.get(dimName) == null) {
					dimension = new DimensionTO();
					dimension.setName(dimName);
					dimMembers = new ArrayList<DimensionMemberTO>();
					dimension.setDimMems(dimMembers);
					String attributes = resultSet.getString("ATTRIBUTES");
					Collection attrDimNames= myParser.parse(attributes);
					dimension.setAttrDimNames(attrDimNames);
					dimensions.put(dimName, dimension);
				} else {
					dimension = dimensions.get(dimName);
					dimMembers = dimension.getDimMems();
					String memName = resultSet.getString("MEM_NAME");
					String memParentName = resultSet.getString("MEM_PARENT_NAME");
					String attributes = resultSet.getString("ATTRIBUTES");
					String aliases = resultSet.getString("ALIASES");
					DimensionMemberTO dimMember = new DimensionMemberTO();
					Collection aliasesList = myParser.parse(aliases);
					dimMember.setAliases(aliasesList);
					Collection attrDimMemNames= myParser.parse(attributes);
					dimMember.setAttrDimMemNames(attrDimMemNames);
					dimMember.setName(memName);
					dimMember.setParentName(memParentName);
					dimMembers.add(dimMember);
				}
			}
		}
		List<DimensionTO> dims = new ArrayList<DimensionTO>();
		if(!dimensions.isEmpty()) {
			for (String key : dimensions.keySet()) {
				dims.add(dimensions.get(key));
			}
		}
		return dims;       
	}

	public static String getEssbaseAppName(String applicationName) {
		// TODO Auto-generated method stub
		return null;
	}

}
