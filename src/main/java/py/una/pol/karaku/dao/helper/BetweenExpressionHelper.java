/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.Between;

/**
 * Helper para la clase {@link Between}.
 *
 * <p>
 * Simplemente crea un {@link Restrictions#between(String, Object, Object)}
 * generando antes un alias válido para la propiedad.
 * </p>
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 *
 */
@Component
public final class BetweenExpressionHelper extends BaseClauseHelper<Between> {

	@Override
	public Criterion getCriterion(Criteria criteria, @Nonnull Between clause,
			@Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		return Restrictions.between(aliasWithProperty, clause.getBegin(),
				clause.getEnd());
	}
}
