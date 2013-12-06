/*
 * @KarakuReflectionUtils.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import org.springframework.util.ReflectionUtils;

/**
 * Provee funcionalidades básicas para utilizar Reflection.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
public final class KarakuReflectionUtils {

	private KarakuReflectionUtils() {

	}

	/**
	 * Retorna el tipo parámetrico implementado por <i>root</i> de un nivel de
	 * jerarquía.
	 * 
	 * @param leaf
	 *            clase raíz (implementación final)
	 * @param index
	 *            número de clase paramétrica
	 * @return clase paramétrica.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getParameterizedClass(Object leaf, int index) {

		ParameterizedType type = (ParameterizedType) leaf.getClass()
				.getGenericSuperclass();
		return ((Class<T>) type.getActualTypeArguments()[index]);
	}

	/**
	 * Busca un {@link Field} por su nombre entre una lista de nombres.
	 * 
	 * <p>
	 * Retorna el primer {@link Field} que encuentra, el orden de búsqueda es el
	 * mismo que el vector de nombres.
	 * </p>
	 * 
	 * @see ReflectionUtils#findField(Class, String)
	 * @param base
	 *            clase en la que se busca
	 * @param names
	 *            nombres de los métodos
	 * @return {@link Field} encontrado, <code>null</code> si no encuentra
	 *         ninguno.
	 */
	public static Field findField(Class<?> base, String ... names) {

		Field f = null;
		for (String s : names) {
			f = ReflectionUtils.findField(base, s);
			if (f != null) {
				return f;
			}
		}
		return f;
	}
}