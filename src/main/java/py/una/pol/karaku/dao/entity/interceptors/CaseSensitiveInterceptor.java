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
package py.una.pol.karaku.dao.entity.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.annotations.CaseSensitive;
import py.una.pol.karaku.dao.entity.Operation;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 *
 */
@Component
public class CaseSensitiveInterceptor extends AbstractInterceptor {

	@Override
	public Class<?>[] getObservedTypes() {

		return new Class<?>[] { String.class };
	}

	@Override
	public Class<?>[] getObservedAnnotations() {

		return new Class<?>[] { CaseSensitive.class, void.class };

	}

	@Override
	public boolean interceptable(Operation op, Field f, Object bean) {

		boolean interceptable = (op != Operation.DELETE)
				&& (f.getAnnotation(CaseSensitive.class) == null);
		for (Annotation a : f.getAnnotations()) {
			if (a.annotationType().isAnnotationPresent(CaseSensitive.class)) {
				interceptable = false;
			}
		}

		return interceptable;
	}

	@Override
	public void intercept(Operation op, Field f, Object bean) {

		Object o = ReflectionUtils.getField(f, bean);
		String s = (String) o;
		if (s != null) {
			s = s.toUpperCase();
			ReflectionUtils.setField(f, bean, s);
		}
	}

}
