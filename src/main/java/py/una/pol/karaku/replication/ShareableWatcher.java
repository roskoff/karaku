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
package py.una.pol.karaku.replication;

import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.watchers.AbstractWatcher;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 *
 */
@Component
public class ShareableWatcher extends AbstractWatcher<Shareable> {

	@Override
	public Shareable process(Operation origin, Operation redirected,
			Shareable bean) {

		if ((Operation.DELETE == origin) && (Operation.UPDATE == redirected)) {
			bean.inactivate();
		}
		return bean;
	}

	@Override
	public Operation redirect(Operation operation, Shareable bean) {

		if (Operation.DELETE == operation) {
			return Operation.UPDATE;
		}
		return operation;
	}
}
