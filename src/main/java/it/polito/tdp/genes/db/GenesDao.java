package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<String> getLocalizations() {
		String sql = "SELECT DISTINCT localization FROM classification";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				result.add(res.getString("localization"));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public int getArchi(String loc1, String loc2) {
		String sql = "SELECT COUNT(DISTINCT i.`Type`) AS peso "
				+ "FROM interactions i, classification c1, classification c2 "
				+ "WHERE (c1.Localization = ? AND c2.Localization = ? "
				+ "AND c1.GeneID = i.GeneID1 AND c2.GeneID = i.GeneID2) "
				+ "OR (c1.Localization = ? AND c2.Localization = ? "
				+ "AND c1.GeneID = i.GeneID1 AND c2.GeneID = i.GeneID2)";
		int result;
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, loc1);
			st.setString(2, loc2);
			st.setString(3, loc2);
			st.setString(4, loc1);
			ResultSet res = st.executeQuery();
			
			res.first();
			result = res.getInt("peso");
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	
}
