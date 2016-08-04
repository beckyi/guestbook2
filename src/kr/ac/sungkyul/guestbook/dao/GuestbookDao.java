package kr.ac.sungkyul.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.ac.sungkyul.guestbook.vo.GuestbookVo;


public class GuestbookDao {
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			// 1.드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2.연결 얻어오기 (Connection 얻기)
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
			
		} catch (ClassNotFoundException e) {
			 e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return conn;
	}
	
	public boolean insert(GuestbookVo vo){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();
			
			// 3. PreparedStatement 준비 (? 인자값 전달 가능)
			String sql = "insert into guestbook values(seq_guestbook.nextval, ?, ?, ?, sysdate)";
			pstmt = conn.prepareStatement(sql);
			
			// 4.바인딩
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());

			// 5.query 실행
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				// 6.자원정리
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();

				}
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}
		}
		return (count==1);
	}
	
	public List<GuestbookVo> getList(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		
		try {
			conn = getConnection();
			
			stmt = conn.createStatement();
			
			String sql = "select no, name, password, content, to_char(reg_date,'yyyy-mm-dd am hh24:mm:ss') from guestbook order by no desc";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String content = rs.getString(4);
				String regDate = rs.getString(5);
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setPassword(password);
				vo.setContent(content);
				vo.setRegDate(regDate);
				
				list.add(vo);
			}

		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public int delete(Long no){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "delete from guestbook where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. 바인딩
			pstmt.setLong(1,no);
			
			//5. SQL 실행
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL ERROR: "+e);
		}finally{
			try {
				if(pstmt != null){
					pstmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("SQL ERROR: "+e);
			}
		}
		return count;
	}
	public String pass(Long no){
		Connection conn = null;
		PreparedStatement pstmt = null;
		String pass = "";
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "select password from guestbook where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			rs.next();
			
			//5. SQL 실행
			pass = rs.getString(1);
			
		} catch (SQLException e) {
			System.out.println("SQL ERROR: "+e);
		}finally{
			try {
				if(pstmt != null){
					pstmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("SQL ERROR: "+e);
			}
		}
		return pass;
	}
}
