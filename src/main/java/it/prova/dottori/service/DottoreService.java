package it.prova.dottori.service;

import java.util.List;

import org.springframework.data.domain.Page;

import it.prova.dottori.dto.DottoreDTO;
import it.prova.dottori.model.Dottore;

public interface DottoreService {
	public DottoreDTO inserisciNuovo(DottoreDTO dottore);

	public List<DottoreDTO> listAll();
	
	public DottoreDTO aggiorna(DottoreDTO dottore);
	
	public DottoreDTO caricaSingoloElemento(Long id);
	
	public void rimuovi(Long id);
	
	Dottore findByCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisitaInstance);

	Dottore findByCodiceDottore(String codiceDottoreInstance);

	Dottore verificaDisponibilita(String codiceDottoreInstance);
	
	Dottore assegnaDottore(Dottore dottoreInstance);
	
	Dottore ricoveraPaziente(Dottore dottoreInstance);
	
Dottore cambiaServizio(Long id);
	
	public Page<Dottore> findByExampleWithPagination(Dottore example, Integer pageNo, Integer pageSize, String sortBy);
}
