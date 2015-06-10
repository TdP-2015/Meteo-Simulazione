package it.polito.tdp.meteo.bean;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Optimizer {

	private Model model;

	private List<String> mieCitta;

	private List<String> path; // città visitate durante la ricorsione
	private List<String> bestPath; // visita migliore vista finora

	private double bestCost;

	private final double C = 100; // euro
	private final double k = 1.0; // euro per %umidità
	
	private final int maxDaysPerCity = 12 ;

	private int daysMax;

	public Optimizer(Model model) {
		this.model = model;
	}

	public List<String> findOptimalPath(Month mese) {

		this.daysMax = mese.minLength();

		bestCost = 10e6;

		this.path = new ArrayList<String>();
		this.mieCitta = new ArrayList<String>();
		mieCitta.add("Torino");
		mieCitta.add("Milano");
		mieCitta.add("Genova");

		double cost;

		path.add(0, "Torino");
		cost = k * model.getUmidita("Torino", mese, 1);
		recursive(path, 1, mese, cost);
		path.remove(0) ;

		path.add(0, "Milano");
		cost = k * model.getUmidita("Milano", mese, 1);
		recursive(path, 1, mese, cost);
		path.remove(0) ;

		path.add(0, "Genova");
		cost = k * model.getUmidita("Genova", mese, 1);
		recursive(path, 1, mese, cost);
		path.remove(0) ;

		return bestPath;

	}

	private void recursive(List<String> path, int giorno, Month mese,
			double cost) {

		// sono alla fine del mese
		if (giorno == daysMax) {
			if (cost < bestCost) {
				bestCost = cost;
				bestPath = new ArrayList<String>(path);
				
				System.out.println(path) ;
				System.out.println(cost) ;

			}
			return;
		}
		
		// non vale la pena di continuare? (già superato il best senza ancora avere finito il mese)
		if(cost > bestCost)
			return ;

		for (String proxCitta : mieCitta) {
			double proxCosto = cost;

			if (contaCitta(proxCitta, path) < maxDaysPerCity ) {
				if (!path.get(giorno - 1).equals(proxCitta))
					proxCosto = proxCosto + C;
				proxCosto = proxCosto + k
						* model.getUmidita(proxCitta, mese, giorno + 1);
				path.add(giorno, proxCitta);
				recursive(path, giorno + 1, mese, proxCosto);
				path.remove(giorno);
			}
		}
	}

	private int contaCitta(String citta, List<String> path) {
		int count = 0;
		for (String s : path) {
			if (s.equals(citta))
				count++;
		}
		return count;
	}

}
