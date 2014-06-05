/*
 * ISIGHADvancedController.java 1.0
 */
package py.una.pol.karaku.controller;

import java.io.Serializable;

/**
 * Interfaz que define las funcionalidades de un controllador avanzado
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 18, 2013
 * @param <T>
 *            entidad
 * @param <K>
 *            clase del id de la entidad
 * 
 */
public interface IKarakuAdvancedController<T, K extends Serializable> extends
		IKarakuBaseController<T, K> {

	/**
	 * Método que se utiliza para capturar excepciones, manejarlas y mostrar
	 * mensajes de error personalizados
	 * 
	 * <p>
	 * <b>Excepciones que puede recibir</b>
	 * </p>
	 * Puede recibir cualquier excepción, pero las reconocidas son:
	 * <ol>
	 * <li> {@link py.una.pol.karaku.exception.UniqueConstraintException}:
	 * excepciones que se lanzan cuando se un atributo con la anotación
	 * {@link py.una.pol.karaku.model.Unique} duplicado.</li>
	 * </ol>
	 * <p>
	 * </p>
	 * 
	 * @param exception
	 *            es la excepción capturada
	 * @return <code>true</code> si se maneja la excepción y <code>false</code>
	 *         si no se maneja, se retorna <code>false</code> cuando se desea
	 *         que sea capturada y manejada mas arriba en la jerarquía
	 */
	boolean handleException(Exception exception);
}