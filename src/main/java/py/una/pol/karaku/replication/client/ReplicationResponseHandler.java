/*
 * @ReplicationResponseHandler.java 1.0 Nov 26, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.client;

import static py.una.pol.karaku.util.Checker.notNull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.util.KarakuReflectionUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Component
public class ReplicationResponseHandler {

	private static final String[] ID_FIELDS = { "id", "lastId" };
	private static final String[] CHANGE_FIELDS = { "entities", "data" };

	/**
	 * Retorna un par inmutable que consta del ultimo identificador como llave,
	 * y la lista de cambios como valor.
	 * 
	 * @param t1
	 *            objeto retornado
	 * @return pair, nunca <code>null</code>, que consta de la llave la ultima
	 *         clave, y el valor la lista de cambios (no nula);
	 */
	public Pair<String, Collection<?>> getChanges(Object t1) {

		return new ImmutablePair<String, Collection<?>>(getLastId(t1),
				getItems(t1));
	}

	/**
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Collection getItems(Object response) {

		Object respuesta = notNull(response,
				"Cant get changes from null response");
		Class<?> clazz = notNull(respuesta.getClass());
		Field f = KarakuReflectionUtils.findField(clazz, CHANGE_FIELDS);

		if (f == null) {
			f = ReflectionUtils
					.findField(response.getClass(), null, List.class);
		}

		notNull(f, "Cant get the id field, "
				+ "use the @ReplicationData annotation or create "
				+ "a field with name %s, please see %s",
				Arrays.toString(CHANGE_FIELDS), response.getClass().getName());
		f.setAccessible(true);
		Collection c = (Collection) ReflectionUtils.getField(f, response);

		if (c == null) {
			return Collections.EMPTY_LIST;
		}
		return c;
	}

	/**
	 * @param response
	 * @return
	 */
	private String getLastId(Object response) {

		notNull(response, "Cant get id from null response");
		Class<?> clazz = notNull(response.getClass());

		Field f = KarakuReflectionUtils.findField(clazz, ID_FIELDS);

		notNull(f, "Cant get the id field, please use the @ReplicationId "
				+ "annotation, or create a field with name %s, see %s",
				Arrays.toString(ID_FIELDS), response.getClass());

		f.setAccessible(true);

		Object id = ReflectionUtils.getField(f, response);
		notNull(id, "Id null in response is not allowed");

		return id.toString();
	}
}