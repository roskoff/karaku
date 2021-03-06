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
package py.una.pol.karaku.dynamic.forms;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import org.slf4j.LoggerFactory;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.KarakuConverter;
import py.una.pol.karaku.util.LabelProvider;
import py.una.pol.karaku.util.LabelProvider.StringLabelProvider;
import py.una.pol.karaku.util.ListHelper;
import py.una.pol.karaku.util.SelectHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class ComboBoxField<T> extends LabelField {

	/**
	 *
	 */
	private static final String DEFAULT_LABEL_ID = "seleccionar";
	private static final String DEFAULT_LABEL_TEXT = "COMBO_BOX_DEFAULT_VALUE";

	/**
	 * Tipo de este objeto
	 */
	public static final String TYPE = "py.una.pol.karaku.dynamic.forms.ComboBoxField";

	private KarakuConverter converter;
	private HtmlSelectOneMenu bind;
	private List<String> toRender;
	private boolean withDefault;
	private boolean defaultAdded = false;

	private AjaxBehavior ajax;

	private UISelectItem defaultSelectItem;

	public ComboBoxField() {

		this(true);
	}

	public ComboBoxField(final boolean withDefault) {

		super();
		this.withDefault = withDefault;
		converter = KarakuConverter.getInstance();
		this.bind = KarakuComponentFactory.getNewSelectOneMenu();
		this.bind.setId(getId());
		this.bind.setConverter(converter);
		toRender = new ArrayList<String>();
	}

	/**
	 * Retorna el converter por defecto de este objeto
	 * 
	 * @return converter
	 */
	public KarakuConverter getConverter() {

		return converter;
	}

	private UISelectItem getDefaultSelectItem() {

		if (defaultSelectItem == null) {
			defaultSelectItem = KarakuComponentFactory.getNewSelectItem();
			defaultSelectItem.setItemLabel(I18nHelper
					.getMessage(DEFAULT_LABEL_TEXT));
			defaultSelectItem.setId(getId() + "_" + DEFAULT_LABEL_ID);
		}

		return defaultSelectItem;
	}

	public void setItems(final List<T> items) {

		setItems(items, new StringLabelProvider<T>());
	}

	public void setItems(final List<T> items, final LabelProvider<T> label) {

		clear();
		UISelectItems uiItems = KarakuComponentFactory.getNewSelectItems();
		List<SelectItem> selectItems = SelectHelper
				.getSelectItems(items, label);
		uiItems.setValue(selectItems);
		addDefault();
		getBind().getChildren().add(uiItems);

	}

	public void addDefault() {

		if (withDefault && !defaultAdded) {
			getBind().getChildren().add(getDefaultSelectItem());
			defaultAdded = true;
		}
	}

	public void clear() {

		List<UISelectItem> toRetain = ListHelper
				.getAsList(getDefaultSelectItem());
		getBind().getChildren().retainAll(toRetain);
	}

	public void addValueChangeListener(final ValueChangeListener listener) {

		getBind().addValueChangeListener(listener);
		getBind().setImmediate(true);
		ajax = new AjaxBehavior();
		ajax.setTransient(true);
		ajax.setRender(getValueChangeRender());
		getBind().addClientBehavior("valueChange", ajax);
	}

	public void setDisable(final boolean disabled) {

		getBind().setDisabled(disabled);
	}

	public List<String> getValueChangeRender() {

		return toRender;
	}

	public void clearValueChangeRender() {

		toRender.clear();
	}

	public void addValueChangeRender(final String toRender) {

		this.toRender.add(toRender);
		if (ajax != null) {
			ajax.setRender(this.toRender);
		}
	}

	/**
	 * Define donde se guardara lo que se seleccione con este componente
	 */
	public void setTarget(final ValueExpression valueExpression) {

		getBind().setValueExpression("value", valueExpression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.forms.dynamic.Field#getType()
	 */
	@Override
	public String getType() {

		return TYPE;
	}

	/**
	 * @return bind
	 */
	public HtmlSelectOneMenu getBind() {

		return bind;
	}

	/**
	 * @param bind
	 *            bind para setear
	 */
	public void setBind(final HtmlSelectOneMenu bind) {

		this.bind = bind;
	}

	/**
	 * @return changeListener
	 */
	public void changeListener(final ValueChangeEvent event) {

		if (event == null) {
			return;
		}
		LoggerFactory.getLogger(ComboBoxField.class).debug(
				"change listener called {}", event);
	}

	@Override
	public boolean enable() {

		setDisable(false);
		return true;
	}

	@Override
	public boolean disable() {

		setDisable(true);
		return true;
	}
}
