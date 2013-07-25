/**
 * @ELHelper.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.ajax4jsf.component.behavior.MethodExpressionAjaxBehaviorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Clase que provee funcionalidades para el uso de expresiones
 * 
 * @author Arturo Volpe Torres
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.3.2 25/07/2013
 * 
 */
@Component
public class ELHelper {

	public static final ELHelper INSTANCE = new ELHelper();
	private static final String ACTION_METHOD = "#{CONTROLLER.METHOD}";

	/**
	 * Dada una expresión del tipo:<br />
	 * 
	 * <pre>
	 * 		#{controller.bean.field}
	 * </pre>
	 * 
	 * Determina tres grupos, de la siguiente forma
	 * <table>
	 * <tr>
	 * <th>Expresión</th>
	 * <th>Significado</th>
	 * </tr>
	 * <tr>
	 * <td>'#{controller.bean':</td>
	 * <td>Parte que determina el bean, completar con <code>}</code> para
	 * obtener la expresión.</td>
	 * </tr>
	 * <tr>
	 * <td>'nombre1':</td>
	 * <td>Parte que determina el nombre del atributo.</td>
	 * </tr>
	 * <tr>
	 * <td>'}':</td>
	 * <td>Parte que completa la Expresión</td>
	 * </tr>
	 * </table>
	 * <br />
	 * <br />
	 * Reemplazando por ejemplo con $1$3 obtenemos la expresion:
	 * 
	 * <pre>
	 * #{controller.bean}
	 * </pre>
	 */
	public static final String EXTRACT_FIELD_FROM_EXPRESSION_REGEX = "(#\\{.*)\\.(.*)(\\})";
	private static Pattern pattern;

	private static final Logger log = LoggerFactory.getLogger(ELHelper.class);

	/**
	 * Crea una expression que se utiliza para representar el tipo
	 * 
	 * @param expression
	 * @param type
	 * @return value expression con la expression pasada que retorna el type
	 *         especificado
	 */
	public ValueExpression makeValueExpression(final String expression,
			final Class<?> type) {

		ValueExpression ve = getExpressionFactory().createValueExpression(
				getElContext(), expression, type);
		return ve;
	}

	public MethodExpression makeMethodExpression(final String expression,
			final Class<?> expectedReturnType,
			final Class<?> ... expectedParamTypes) {

		MethodExpression methodExpression = null;
		try {
			FacesContext fc = getContext();
			ExpressionFactory factory = getExpressionFactory();
			methodExpression = factory.createMethodExpression(
					fc.getELContext(), expression, expectedReturnType,
					expectedParamTypes);

			return methodExpression;
		} catch (Exception e) {
			throw new FacesException("Method expression '" + expression
					+ "' no se puede crear.");
		}
	}

	/**
	 * Este método debe retornar un String que se usara para el mapa de
	 * navegación, retornar null o "" para refrescar la página
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	public MethodExpression makeActionMethodExpression(final String controller,
			final String action) {

		String expression = formatExpression(controller, action);
		return makeMethodExpression(expression, String.class);
	}

	/**
	 * Crea un método con el formato #{CONTROLLER.ACTION}
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	private String formatExpression(final String controller, final String action) {

		return ACTION_METHOD.replaceFirst("CONTROLLER", controller)
				.replaceFirst("METHOD", action);
	}

	/**
	 * Este método genera la expresion para invocar a un metodo utilizando ajax.
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	public MethodExpressionAjaxBehaviorListener createAjaxBehaviorListener(
			final String controller, final String action) {

		String expression = formatExpression(controller, action);
		return new MethodExpressionAjaxBehaviorListener(
				makeMethodExpression(expression, Void.class,
						new Class[] { AjaxBehaviorEvent.class }));
	}

	private ExpressionFactory getExpressionFactory() {

		return getApplication().getExpressionFactory();
	}

	/**
	 * @return context
	 */
	public FacesContext getContext() {

		return FacesContext.getCurrentInstance();
	}

	/**
	 * @return application
	 */
	public Application getApplication() {

		return getContext().getApplication();
	}

	/**
	 * @return elContext
	 */
	public ELContext getElContext() {

		return getContext().getELContext();
	}

	/**
	 * Dada una expresión del tipo
	 * 
	 * <pre>
	 * 		#{controller.bean.field}
	 * </pre>
	 * 
	 * Retorna el {@link Field} donde se almacenara el campo field, notar que es
	 * a nivel de {@link Field}, es decir, requiere que el getter y el setter,
	 * tengan el mismo nombre (getField, setField), no sirve para campos que no
	 * cumplan esta condicion.
	 * 
	 * @param beanExpression
	 *            expresión con el formato de
	 *            {@link #EXTRACT_FIELD_FROM_EXPRESSION_REGEX}
	 * @return {@link Field} del atributo
	 * @since 1.3.2
	 */
	public static Field getFieldByExpression(String beanExpression) {

		FacesContext context = FacesContext.getCurrentInstance();
		Matcher ma = getPattern().matcher(beanExpression);
		if (ma.matches()) {
			String withoutField = ma.replaceFirst("$1$3");
			String field = ma.replaceFirst("$2");
			Object bean = context
					.getApplication()
					.getExpressionFactory()
					.createValueExpression(context.getELContext(),
							withoutField, Object.class)
					.getValue(context.getELContext());
			if (bean != null) {
				Field f;
				try {
					f = bean.getClass().getDeclaredField(field);
				} catch (Exception e) {
					log.info("Can not find the field of the expression: "
							+ beanExpression);
					return null;
				}
				return f;
			}
		}
		return null;
	}

	private static Pattern getPattern() {

		if (pattern == null) {
			pattern = Pattern
					.compile(ELHelper.EXTRACT_FIELD_FROM_EXPRESSION_REGEX);
		}
		return pattern;
	}
}
