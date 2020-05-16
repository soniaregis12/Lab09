package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMap, int year) {

		String sql = "SELECT DISTINCT co.StateAbb, co.CCode, co.StateNme from contiguity AS c,country AS co WHERE conttype = 1 AND YEAR <=?  AND c.state1no = co.CCode";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(!idMap.containsKey(rs.getInt("CCode"))) {
					System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					Country c = new Country(rs.getString("StateAbb"), rs.getInt("CCode"), rs.getString("StateNme"));
					idMap.put(c.getCode(), c);
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(Map<Integer, Country> idMap, Country c, int anno) {
		
		String sql = "SELECT c.state2no from contiguity AS c,country AS co WHERE conttype = 1 AND YEAR <=? AND c.state1no = co.CCode AND co.CCode =?";
		List<Border> result = new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, c.getCode());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Border b = new Border(c, idMap.get(rs.getInt("state2no")) , 1);
				System.out.format("%d %d\n", c.getCode(), rs.getInt("state2no"));
				result.add(b);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
