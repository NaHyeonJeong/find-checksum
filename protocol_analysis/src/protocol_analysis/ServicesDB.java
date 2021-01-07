package protocol_analysis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServicesDB {
	Connection con = null;
	ResultSet rs;
	Statement stmt;
	PreparedStatement pstmt;
	String url = "jdbc:mysql://localhost:3306/int_protocol_studydb?characterEncoding=UTF-8&serverTimezone=Asia/Seoul";
	String id = "int_protocol_user";
	String pw = "int_protocol_user";
	
	public ServicesDB() {
		//JDBC 연결, DB연결
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, id, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectAll(int port) {
		String aliases = "", serviceName = "", comment = "", protocol = "";
		int portNum = 0;
		try {
			String sql = "SELECT *\r\n" + 
					"FROM SERVICE_PORTS\r\n" + 
					"WHERE PORT_NUMBER = ?\r\n" + 
					";";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, port);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				serviceName = rs.getString("SERVIE_NAME");
				portNum = Integer.parseInt(rs.getString("PORT_NUMBER"));
				protocol = rs.getString("PROTOCOL");
				aliases = rs.getString("ALIASES");
				comment = rs.getString("COMMENT");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.print(portNum + " port \n\t( Aliases: " + aliases + ", Service Name: " + serviceName + 
				", Protocol: " + protocol + " ) \n\t" + comment + " ");
	}
	
}
