package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private SimpleGraph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	private BordersDAO dao;
	private Map<Country, Country> visita;

	public Model() {
		this.idMap = new HashMap<Integer, Country>();
		this.dao = new BordersDAO();
		this.visita = new HashMap<Country, Country>();
	}
	
	public void creaGrafo(int year) {
		
		this.grafo = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
		this.dao.loadAllCountries(this.idMap, year);
		
		
		for(Country c : this.idMap.values()) {
			this.grafo.addVertex(c);
		}
		
		for(Country c : this.grafo.vertexSet()) {
			System.err.println(c.toString());
			List<Border> confini = dao.getCountryPairs(idMap, c, year);
			
			for(Border b : confini) {
				DefaultEdge e1 = this.grafo.getEdge(b.getC1(), b.getC2());
				DefaultEdge e2 = this.grafo.getEdge(b.getC2(), b.getC1());
				
				if(e1 == null && e2 == null) {
					//this.grafo.addEdge(b.getC2(), b.getC1());
					Graphs.addEdgeWithVertices(this.grafo, b.getC1(), b.getC2());
				}
			}
		}
		
	}
	
	public List<Country> trovaVicini(Country c) {
		List<Country> vicini = new ArrayList<Country>();
		
		BreadthFirstIterator<Country, DefaultEdge> it = new BreadthFirstIterator<Country, DefaultEdge>(this.grafo,c);
		
		// Nodo radice
		visita.put(c, null);
		
		it.addTraversalListener(new TraversalListener<Country, DefaultEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				Country sorgente = grafo.getEdgeSource(e.getEdge());
				Country destinazione = grafo.getEdgeTarget(e.getEdge());
				
				if(!visita.containsKey(destinazione) && visita.containsKey(sorgente)) {
					visita.put(destinazione, sorgente);
				}else if(visita.containsKey(destinazione) && !visita.containsKey(sorgente)) {
					visita.put(sorgente, destinazione);
				}
				
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(it.hasNext()) {
			it.next();
		}
		
		System.out.println(visita);
		for(Country p : visita.keySet()) {
			vicini.add(p);
		}
		vicini.remove(c);
		return vicini;
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<Country, DefaultEdge> getGrafo(){
		return this.grafo;
	}

}
