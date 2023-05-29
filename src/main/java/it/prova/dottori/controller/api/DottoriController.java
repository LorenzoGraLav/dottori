package it.prova.dottori.controller.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.dottori.dto.DottoreDTO;
import it.prova.dottori.dto.PazienteEDottoreDTO;
import it.prova.dottori.model.Dottore;
import it.prova.dottori.service.DottoreService;

@RestController
@RequestMapping("/api/dottori")
public class DottoriController {

	@Autowired
	private DottoreService dottoreService;
	
	@GetMapping
	public List<DottoreDTO> visualizzaDottori() {
		return dottoreService.listAll();
	}
	
	@GetMapping("/{id}")
	public DottoreDTO visualizza(@PathVariable(required = true) Long id) {
		return dottoreService.caricaSingoloElemento(id);
	}
	
	@PostMapping
	public DottoreDTO createNew(@Valid @RequestBody DottoreDTO dottoreInput) {
		if (dottoreInput.getId() != null)
			throw new RuntimeException();
		return dottoreService.inserisciNuovo(dottoreInput);
	}
	@PutMapping("/{id}")
	public DottoreDTO update(@Valid @RequestBody DottoreDTO dottoreInput, @PathVariable(required = true) Long id) {
//		if (dottoreInput.getId() != null)
//			throw new RuntimeException();
		return dottoreService.aggiorna(dottoreInput);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		dottoreService.rimuovi(id);
	}
	
	
	@GetMapping("/cercaCodiceFiscalePaziente/{codFiscale}")
	public DottoreDTO cercaPerCodiceFiscalePaziente(@PathVariable(required = true) String codFiscale) {
		Dottore result = dottoreService.findByCodFiscalePazienteAttualmenteInVisita(codFiscale);

		if (result == null)
			throw new RuntimeException("nessun dottore sul paziente");

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}
	
	
	
	@GetMapping("/verificaDisponibilitaDottore/{codFiscale}")
	public DottoreDTO assegnaPaziente(@PathVariable(required = true) String codFiscale) {
		Dottore result = dottoreService.verificaDisponibilita(codFiscale);

		if (result == null)
			throw new RuntimeException("dottore non trovato");

		if (!result.isInServizio() || result.isInVisita())
			throw new RuntimeException("dottore non disponibile");

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping("/impostaVisita")
	public PazienteEDottoreDTO impostaVisita(@RequestBody PazienteEDottoreDTO dottorePazienteDTO) {

		Dottore dottore = Dottore.builder().codiceDottore(dottorePazienteDTO.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(dottorePazienteDTO.getCodFiscalePazienteAttualmenteInVisita())
				.build();

		return PazienteEDottoreDTO.buildDottoreDTOFromModel(dottoreService.assegnaDottore(dottore));
	}

	@PostMapping("/ricoveraPaziente")
	public PazienteEDottoreDTO ricovera(@RequestBody PazienteEDottoreDTO pazienteEDottoreDTO) {
		Dottore dottore = Dottore.builder().codiceDottore(pazienteEDottoreDTO.getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(pazienteEDottoreDTO.getCodFiscalePazienteAttualmenteInVisita())
				.build();
		return PazienteEDottoreDTO.buildDottoreDTOFromModel(dottoreService.ricoveraPaziente(dottore));
	}
}