package it.prova.dottori.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.dottori.dto.DottoreDTO;
import it.prova.dottori.model.Dottore;
import it.prova.dottori.repository.DottoreRepository;

@Service
@Transactional
public class DottoreServiceImpl implements DottoreService {
	
	@Autowired
	private DottoreRepository repository;

	@Override
	@Transactional
	public DottoreDTO inserisciNuovo(DottoreDTO dottore) {
		repository.save(dottore.buildDottoreModel());
		return dottore;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DottoreDTO> listAll() {
		return DottoreDTO.createDottoreDTOListFromModelList((List<Dottore>) repository.findAll());
	}

	@Override
	@Transactional
	public DottoreDTO aggiorna(DottoreDTO dottore) {
		repository.save(dottore.buildDottoreModel());
		return dottore;
	}

	@Override
	@Transactional(readOnly = true)
	public DottoreDTO caricaSingoloElemento(Long id) {
		return DottoreDTO.buildDottoreDTOFromModel(repository.findById(id).orElse(null));
	}

	@Override
	@Transactional
	public void rimuovi(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Dottore findByCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisitaInstance) {
		return repository
				.findDottoreByCodFiscalePazienteAttualmenteInVisita(codFiscalePazienteAttualmenteInVisitaInstance);
	}

	@Override
	public Dottore findByCodiceDottore(String codiceDottoreInstance) {
		return repository.caricaDottoreFromCodiceDottore(codiceDottoreInstance);
	}

	@Override
	public Dottore verificaDisponibilita(String codiceDottoreInstance) {
		return repository.caricaDottoreFromCodiceDottore(codiceDottoreInstance);
	}

	@Override
	public Dottore assegnaDottore(Dottore dottoreInstance) {
		Dottore result = repository.caricaDottoreFromCodiceDottore(dottoreInstance.getCodiceDottore());
		result.setCodFiscalePazienteAttualmenteInVisita(dottoreInstance.getCodFiscalePazienteAttualmenteInVisita());
		return repository.save(result);
	}

	@Override
	public Dottore ricoveraPaziente(Dottore dottoreInstance) {
		Dottore result = repository.caricaDottoreFromCodiceDottore(dottoreInstance.getCodiceDottore());
		result.setCodFiscalePazienteAttualmenteInVisita(null);
		result.setInVisita(false);
		return repository.save(result);
	}

	@Override
	public Dottore cambiaServizio(Long id) {
		Dottore dottore = repository.findById(id).orElse(null);

		if (dottore.isInServizio() == true)
			throw new RuntimeException("impossibile cambiare lo stato in servizio se ha dei pazienti");

		if (dottore.isInServizio() == true)
			dottore.setInServizio(false);
		else
			dottore.setInServizio(true);

		return dottore;
	}

	@Override
	public Page<Dottore> findByExampleWithPagination(Dottore example, Integer pageNo, Integer pageSize, String sortBy) {
		Specification<Dottore> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getNome()))
				predicates.add(cb.like(cb.upper(root.get("nome")), "%" + example.getNome().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCognome()))
				predicates.add(cb.like(cb.upper(root.get("cognome")), "%" + example.getCognome().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCodiceDottore()))
				predicates.add(cb.like(cb.upper(root.get("codiceDottore")),
						"%" + example.getCodiceDottore().toUpperCase() + "%"));
			if (StringUtils.isNotEmpty(example.getCodFiscalePazienteAttualmenteInVisita()))
				predicates.add(cb.like(cb.upper(root.get("codFiscalePazienteAttualmenteInVisita")),
						"%" + example.getCodFiscalePazienteAttualmenteInVisita().toUpperCase() + "%"));

//			if (example.isInServizio() != null)
//				predicates.add(cb.equal(root.get("inServizio"), example.isInServizio()));
//			if (example.isInVisita() != null)
//				predicates.add(cb.equal(root.get("inVisita"), example.isInVisita()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		Pageable paging = null;
		// se non passo parametri di paginazione non ne tengo conto
		if (pageSize == null || pageSize < 10)
			paging = Pageable.unpaged();
		else
			paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		return repository.findAll(specificationCriteria, paging);
	}

}
