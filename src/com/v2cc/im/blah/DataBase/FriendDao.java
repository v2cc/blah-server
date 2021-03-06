package com.v2cc.im.blah.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.v2cc.im.blah.bean.User;


public class FriendDao {
	//防止初始化
	private FriendDao() {
	}
	public static ArrayList<User> getFriend(int id){
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use blah";
		String sql1 = "select * " +
				      "from friendlist as f left outer join user as u " +
				      "on f.friendid=u.id "+
				      "where master=?";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
				User user = new User();
				user.setId(rs.getInt("friendid"));
				user.setAccount(rs.getString("account"));
				//user.setPhoto(rs.getBytes("photo"));
				if(rs.getInt("isOnline")==1)
				   user.setIsOnline(true);
				else
					user.setIsOnline(false);
				user.setUserName(rs.getString("name"));
				user.setLocation(rs.getString("location"));
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		DBPool.close(con);
		return list;
	}
	public static void addFriend(int id, int friendID) {
		String sql0 = "use blah";
		String sql1 = "insert into friendlist(master,friendid) " +
				"values(?,?)";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			ps.setInt(2, friendID);
			ps.execute();
			con.commit();
			}catch (SQLException e) {
				try {
					System.out.println("正在发生回滚...");
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}	
		DBPool.close(con);
	}
	
}
