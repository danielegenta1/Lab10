package it.polito.tdp.porto.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model 
{
	Graph<Author, DefaultEdge> grafo;
	List<Author>authors;
	
	public Model()
	{
		PortoDAO dao = new PortoDAO();
		authors = dao.getAllAuthors();
	}

	public List<Author> loadAuthors() 
	{
		return authors;
	}

	public List<Author> loadCoAuthors(Author autore) 
	{
		// creo grafo
		this.grafo = new SimpleGraph(DefaultEdge.class);
		
		// vertici
		Graphs.addAllVertices(this.grafo, authors);
		
		// archi (aggiungo solo quelli dell'autore di interesse per ora)
		PortoDAO dao = new PortoDAO();
		List<Author> coautori = dao.getAllCoAuthors(autore);
		for (Author a1 : authors)
			for (Author a2 : coautori)
				if(!grafo.containsEdge(a1, a2) && !a1.equals(a2))
					this.grafo.addEdge(a1, a2);
		return coautori;
	}

	public List<Author> loadAuthors2(Author autore) 
	{
		List<Author>res = new LinkedList<Author>();
		for (Author a : authors)
		{
			if (!Graphs.neighborListOf(grafo, autore).contains(a) && !a.equals(autore))
				res.add(a);
		}
		return res;
	}

	public List<Paper> trovaSequenza(Author primo, Author secondo) 
	{
		// M1 - cammino minimo (uso implementazione di dijkstra)
		List<Paper>result = new LinkedList<Paper>();
		
		ShortestPathAlgorithm<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<Author, DefaultEdge>(this.grafo);
		
		GraphPath<Author,DefaultEdge> gp = dijkstra.getPath(primo, secondo);
		List<DefaultEdge> edges = gp.getEdgeList();
		
		PortoDAO dao = new PortoDAO();
		// trovare almeno un articolo 
		for (DefaultEdge edge : edges)
		{
			Author s = grafo.getEdgeSource(edge);
			Author d = grafo.getEdgeTarget(edge);
			
			Paper p = dao.getArticoloComune(s,d);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

}
