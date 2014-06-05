/*
 * @NotExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.Not;

/**
 * 
 * {@link BaseClauseHelper} que se encarga de parsear expresiones del tipo
 * {@link Not}, invoca a
 * {@link RestrictionHelper#getCriterion(py.una.pol.karaku.dao.where.Clause, Criteria, Map)}
 * y agrega ese criterion a la consulta.
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Component
public class NotExpressionHelper extends BaseClauseHelper<Not> {

	@Autowired
	private RestrictionHelper helper;

	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull Not clause, @Nonnull Map<String, String> aliases) {

		Criterion c = helper
				.getCriterion(clause.getClause(), criteria, aliases);
		return Restrictions.not(c);

	}
}