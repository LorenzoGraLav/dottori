package it.prova.dottori.dto;

import it.prova.dottori.model.Dottore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PazienteEDottoreDTO {

	private String codiceDottore;
	private String codFiscalePazienteAttualmenteInVisita;

	public void setCodFiscalePazienteAttualmenteInVisita(String codFiscalePazienteAttualmenteInVisita) {
		this.codFiscalePazienteAttualmenteInVisita = codFiscalePazienteAttualmenteInVisita;
	}

	public static PazienteEDottoreDTO buildDottoreDTOFromModel(Dottore dottoreModel) {
		PazienteEDottoreDTO result = new PazienteEDottoreDTO(dottoreModel.getCodiceDottore(),
				dottoreModel.getCodFiscalePazienteAttualmenteInVisita());
		return result;
	}

}