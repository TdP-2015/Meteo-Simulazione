package it.polito.tdp.meteo.bean;

import it.polito.tdp.meteo.db.MeteoDAO;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

	private List<Situazione> situazioni;

	public Map<String, Double> getUmiditaMedia(Month mese) {
		Map<String, Double> umidita = new HashMap<String, Double>();
		Map<String, Integer> count = new HashMap<String, Integer>();

		// fai le somme delle umidità
		for (Situazione s : situazioni) {

			// verifica il mese
			if (s.getData().getMonth().equals(mese)) {

				String citta = s.getLocalita();

				if (count.containsKey(citta)) {
					count.put(citta, count.get(citta) + 1);
					umidita.put(citta, umidita.get(citta) + s.getUmidita());
				} else {
					count.put(citta, 1);
					umidita.put(citta, (double) s.getUmidita());
				}
			}
		}

		// dividi per il count, per avere le medie
		for (String citta : umidita.keySet()) {
			umidita.put(citta, umidita.get(citta) / count.get(citta));
		}

		return umidita;
	}

	public void loadSituazioni() {
		MeteoDAO dao = new MeteoDAO();
		this.situazioni = dao.getAllSituazioni();
	}

}
