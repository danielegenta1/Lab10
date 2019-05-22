package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO 
{
	
	public List<Author> getAllAuthors()
	{
		final String sql = "SELECT * FROM author";
		List<Author> res = new LinkedList<Author>();
		try 
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) 
			{
				res.add(new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname")));
			}
			conn.close();
			return res;

		} 
		catch (SQLException e)
		{
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) 
	{

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				conn.close();
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try 
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				conn.close();
				return paper;
			}

			return null;

		} 
		catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Author> getAllCoAuthors(Author autore) 
	{
		String sql = "SELECT DISTINCT a2.id, a2.lastname, a2.firstname " + 
				"FROM creator c1, creator c2, author a2 " + 
				"WHERE c1.eprintid=c2.eprintid " + 
				"AND c2.authorid=a2.id " + 
				"AND c1.authorid= ? " + 
				"AND a2.id <> c1.authorid " + 
				"ORDER BY a2.lastname ASC, a2.firstname ASC" ;
		
		List<Author> res = new LinkedList<Author>();
		try 
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, autore.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) 
			{
				res.add(new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname")));
			}
			conn.close();
			return res;

		} 
		catch (SQLException e)
		{
			throw new RuntimeException("Errore Db");
		}
	}

	public Paper getArticoloComune(Author s, Author d) 
	{
		String sql = "SELECT paper.eprintid, title, issn, publication, type, types " + 
				"FROM paper, creator c1, creator c2 " + 
				"WHERE paper.eprintid=c1.eprintid " + 
				"AND paper.eprintid=c2.eprintid " + 
				"AND c1.authorid=? " + 
				"AND c2.authorid=? " + 
				"LIMIT 1" ; //me ne basta 1
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getId());
			st.setInt(2, d.getId());
			
			ResultSet res = st.executeQuery() ;
			
			Paper p = null ;
			if(res.next()) 
			{
				// c'è almeno un articolo: ritornalo!
				p = new Paper(res.getInt("eprintid"), res.getString("title" ), res.getString("issn"),
						res.getString("publication"), res.getString("type"), res.getString("types")) ;
			}
			
			conn.close();
			return p ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}