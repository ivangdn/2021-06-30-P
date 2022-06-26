package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	
	private List<String> best;
	
	public Model() {
		this.dao = new GenesDao();
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getLocalizations());
		
		for(String loc1 : this.grafo.vertexSet()) {
			for(String loc2 : this.grafo.vertexSet()) {
				if(!loc1.equals(loc2)) {
					int peso = dao.getArchi(loc1, loc2);
					if(peso > 0) {
						Graphs.addEdge(grafo, loc1, loc2, peso);
					}
				}
			}
		}
		
		return "Grafo creato: "+grafo.vertexSet().size()+" vertici, "
				+grafo.edgeSet().size()+" archi";
	}
	
	public List<String> getVertici() {
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	public List<String> getLocalizzazioniConnesse(String loc) {
		return Graphs.neighborListOf(this.grafo, loc);
	}
	
	public int getPeso(String loc1, String loc2) {
		DefaultWeightedEdge e = this.grafo.getEdge(loc1, loc2);
		return (int) this.grafo.getEdgeWeight(e);
	}
	
	public List<String> ricercaCammino(String loc) {
		this.best = new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		parziale.add(loc);
		cerca(parziale);
		return best;
	}

	private void cerca(List<String> parziale) {
		if(calcolaLunghezza(parziale) > calcolaLunghezza(best) || parziale.size()==this.grafo.vertexSet().size()) {
			best = new ArrayList<>(parziale);
		}
		
		for(String s : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				cerca(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private int calcolaLunghezza(List<String> parziale) {
		int l = 0;
		for(int i=0; i<parziale.size()-1; i++) {
			String loc1 = parziale.get(i);
			String loc2 = parziale.get(i+1);
			l += (int) grafo.getEdgeWeight(grafo.getEdge(loc1, loc2));
		}
		return l;
	}
	
}