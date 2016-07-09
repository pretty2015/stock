package com.lzj.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lzj.DBTools;
import com.lzj.bean.Blotter;
import com.lzj.bean.StockDataDay;
import com.lzj.util.DateUtil;

public class StockDataDayDao {

	public List<StockDataDay> getByCreateDate(Date createDate) {
		List<StockDataDay> stockDataDays = new ArrayList<StockDataDay>();

		Connection conn = DBTools.getConn();
		PreparedStatement pstmt = null;
		try {
			conn = DBTools.getConn();
			conn.setAutoCommit(false);
			String sql = "select * from stock_data_day sdd left join stock_ticai_detail td on sdd.b = td.code and orderBy=1 where sdd.create_date = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setObject(1, DateUtil.getDateBegin(createDate));
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				StockDataDay stockDataDay = new StockDataDay();
				stockDataDay.setId(resultSet.getInt("id"));
				stockDataDay.setB(resultSet.getString("b"));
				stockDataDay.setC(resultSet.getString("c"));
				stockDataDay.setD(resultSet.getDouble("d"));
				stockDataDay.setE(resultSet.getDouble("e"));
				stockDataDay.setR(resultSet.getDouble("r"));
				stockDataDay.setP(resultSet.getDouble("p"));
				stockDataDay.setQ(resultSet.getDouble("q"));
				stockDataDay.setS(resultSet.getDouble("s"));
				stockDataDay.setO(resultSet.getDouble("o"));
				stockDataDay.setM(resultSet.getDouble("m"));
				stockDataDay.setM5(resultSet.getDouble("m5"));
				stockDataDay.setM10(resultSet.getDouble("m10"));
				stockDataDay.setM20(resultSet.getDouble("m20"));
				stockDataDay.setM30(resultSet.getDouble("m30"));
				stockDataDay.setM60(resultSet.getDouble("m60"));
				stockDataDay.setM120(resultSet.getDouble("m120"));
				stockDataDay.setM250(resultSet.getDouble("m250"));
				stockDataDay.setContent(resultSet.getString("content"));
				if (stockDataDay.getContent() != null) {
					stockDataDay.setContent(stockDataDay.getContent().replace("要点一#所属板块#", ""));
				}
				stockDataDay.setCreateDate(resultSet.getDate("sdd.create_date"));
				stockDataDays.add(stockDataDay);

			}
			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException ex) {
			try {
				// 提交失败，执行回滚操作
				conn.rollback();

			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("updateExistsInfo回滚执行失败!!!");
			}
			ex.printStackTrace();
			System.err.println("updateExistsInfo执行失败");

		} finally {
			try {
				// 关闭资源
				if (pstmt != null)
					pstmt.close();
				/*
				 * if (conn != null) conn.close();
				 */
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("资源关闭失败!!!");
			}
		}
		return stockDataDays;

	}

	public void addBlotter(Blotter blotter) {
		Connection conn = DBTools.getConn();
		PreparedStatement pstmt = null;
		try {
			conn = DBTools.getConn();
			conn.setAutoCommit(false);
			String sql = "insert blotter(szzs,balance,balance_yy,create_date) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setObject(1, blotter.getSzzs());
			pstmt.setObject(2, blotter.getBalance());
			pstmt.setObject(3, blotter.getBalanceYy());
			pstmt.setObject(4, blotter.getCreateDate());
			pstmt.executeUpdate();
			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException ex) {
			try {
				// 提交失败，执行回滚操作
				conn.rollback();

			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("updateExistsInfo回滚执行失败!!!");
			}
			ex.printStackTrace();
			System.err.println("updateExistsInfo执行失败");

		} finally {
			try {
				// 关闭资源
				if (pstmt != null)
					pstmt.close();
				/*
				 * if (conn != null) conn.close();
				 */
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("资源关闭失败!!!");
			}
		}

	}

	public void delBlotter(int blotterId) {
		Connection conn = DBTools.getConn();
		PreparedStatement pstmt = null;
		try {
			conn = DBTools.getConn();
			conn.setAutoCommit(false);
			String sql = "delete from  blotter where id = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setObject(1, blotterId);
			pstmt.executeUpdate();

			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException ex) {
			try {
				// 提交失败，执行回滚操作
				conn.rollback();

			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("updateExistsInfo回滚执行失败!!!");
			}
			ex.printStackTrace();
			System.err.println("updateExistsInfo执行失败");

		} finally {
			try {
				// 关闭资源
				if (pstmt != null)
					pstmt.close();
				/*
				 * if (conn != null) conn.close();
				 */
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("资源关闭失败!!!");
			}
		}

	}

	public void modifyBlotterBalance(float balance) {
		Connection conn = DBTools.getConn();
		PreparedStatement pstmt = null;
		try {
			conn = DBTools.getConn();
			conn.setAutoCommit(false);
			String sql = "update blotter set balance = balance + ?,balance_yy = balance_yy + ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setObject(1, balance);
			pstmt.setObject(2, balance);
			pstmt.executeUpdate();

			conn.commit();

			conn.setAutoCommit(true);
		} catch (SQLException ex) {
			try {
				// 提交失败，执行回滚操作
				conn.rollback();

			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("updateExistsInfo回滚执行失败!!!");
			}
			ex.printStackTrace();
			System.err.println("updateExistsInfo执行失败");

		} finally {
			try {
				// 关闭资源
				if (pstmt != null)
					pstmt.close();
				/*
				 * if (conn != null) conn.close();
				 */
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("资源关闭失败!!!");
			}
		}

	}

	public Date getLastModifyDate() {
		return DBTools.getDate("select create_date from stock_data_day order by create_date desc limit 1");
	}
	
	public String getNextDateByCreateDate(String currentDate) {
		return DBTools.getString("select create_date from stock_data_day where create_date > '"+currentDate+"' group by create_date order by create_date asc limit 1");
	}

	public String getPreDateByCreateDate(String currentDate) {
		return DBTools.getString("	select create_date from stock_data_day where create_date < '"+currentDate+"' group by create_date order by create_date desc limit 1");
	}
}
