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
package py.una.pol.karaku.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.security.HasRole;
import py.una.pol.karaku.security.KarakuSecurity;

/**
 * Implementacion base de la interfaz {@link IKarakuEmbeddableController}
 * 
 * @author Arturo Volpe
 * @since 2.0, 30/01/2013
 * @version 1.0
 * 
 * @param <T>
 *            Clase de la Entidad que maneja este controller
 * @param <ID>
 *            Clase del id de la entidad
 */
public abstract class KarakuBaseEmbeddableController<T, K extends Serializable>
		extends KarakuAdvancedController<T, K> implements
		IKarakuAdvancedController<T, K>, IKarakuEmbeddableController {

	/**
	 * Constante que determina el valor por defecto de la cantidad de registros
	 * a ser mostrada por este controlador en modo vista cuando esta en modo
	 * embebido.
	 * 
	 * @see #isEmbedded
	 */
	public static final int ROWS_PER_PAGE_IF_EMBEDDED = 5;

	private IKarakuMainController mainController;

	private boolean isEmbedded;

	@Override
	public void setMainController(IKarakuMainController mainController) {

		isEmbedded = mainController != null;
		this.mainController = mainController;
	}

	@Override
	public List<T> getEntities() {

		if (isEmbedded && getMainController().isEditingHeader()) {
			return Collections.emptyList();
		}
		return super.getEntities();
	}

	@Override
	public IKarakuMainController getMainController() {

		return mainController;
	}

	@Override
	public String getHeaderPath() {

		if (isEmbedded) {
			return mainController.getHeaderPath();
		}
		return "";
	}

	@Override
	public void delegateChild(Object child) {

		if (!getClazz().isAssignableFrom(child.getClass())) {
			throw new IllegalArgumentException(
					"Intentando delegar un hijo que no corresponde a la clase del controller");
		}
		mainController.configureChildBean(child);

	}

	public Object getHeaderBean() {

		return mainController.getHeaderBean();
	}

	@Override
	public void init() {

		reloadEntities();
	}

	@Override
	public Where<T> getBaseWhere() {

		Where<T> where = super.getBaseWhere();

		if (isEmbedded) {
			if (where.getExample() == null) {
				where.setExample(getBaseEntity());
			}
			mainController.configureBaseWhere(where, getClazz());
		}
		return where;
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_EDIT)
	public String doEdit() {

		if (mainController != null) {
			delegateChild(getBean());
		}
		return super.doEdit();
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_DELETE)
	public String doDelete() {

		return super.doDelete();
	}

	/**
	 * Metodo que se encarga de la craecion, delega la responsabilidad al
	 * controler principal y luego llama a {@link KarakuAdvancedController}
	 * heredado para que se encarge de guardar.
	 */
	@Override
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String doCreate() {

		if (mainController != null) {
			delegateChild(getBean());
		}
		return super.doCreate();
	}

	@Override
	public void save() {

	}

	@Override
	public void cancel() {

		setMainController(null);
	}

	@Override
	public String goList() {

		if (isEmbedded) {
			return mainController.childrenGoView();
		}
		return super.goList();
	}

	@Override
	public abstract String goEdit();

	@Override
	public String goNew() {

		return goEdit();
	}

	@Override
	public String goDelete() {

		return goEdit();
	}

	@Override
	public String goView() {

		return goEdit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDefaultPermission() {

		if (mainController == null) {
			return super.getDefaultPermission();
		} else {
			return ((IKarakuBaseController<T, K>) mainController)
					.getDefaultPermission();
		}
	}

	public boolean canEditDetail() {

		if (!isEmbedded) {
			return true;
		} else {
			return mainController.embeddableListCanEdit();
		}
	}

	public boolean canDeleteDetail() {

		if (!isEmbedded) {
			return true;
		} else {
			return mainController.embeddableListCanDelete();
		}
	}

	@Override
	public int getRowsForPage() {

		if (isEmbedded) {
			return ROWS_PER_PAGE_IF_EMBEDDED;
		} else {
			return super.getRowsForPage();
		}

	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT)
	public void doSearch() {

		if (isEmbedded) {
			controllerHelper.updateModel(getMessageIdName() + "_pgSearch");
			setExample(getBean());
			reloadEntities();
			return;
		}
		super.doSearch();
	}

	@Override
	public boolean isEditable(String campo) {

		if (getMode() == null) {
			setMode(Mode.VIEW);
		}
		switch (getMode()) {
			case VIEW:
				return false;
			case EDIT:
				return true;
			case NEW:
				return true;
			case DELETE:
				return false;
			case SEARCH:
				return true;
			default:
				break;
		}
		return true;
	}
}
