/*
 * @EncuestaPlantillaLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.KarakuBaseLogic;
import py.una.pol.karaku.repo.IKarakuBaseDao;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;
import py.una.pol.karaku.survey.repo.IEncuestaPlantillaDAO;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Service
@Transactional
public class EncuestaPlantillaLogic extends KarakuBaseLogic<EncuestaPlantilla, Long>
		implements IEncuestaPlantillaLogic {

	@Autowired
	private IEncuestaPlantillaDAO dao;

	@Override
	public IKarakuBaseDao<EncuestaPlantilla, Long> getDao() {

		return dao;
	}

}