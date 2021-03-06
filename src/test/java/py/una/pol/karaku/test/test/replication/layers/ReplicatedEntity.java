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
package py.una.pol.karaku.test.test.replication.layers;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.envers.Audited;
import py.una.pol.karaku.dao.entity.annotations.URI;
import py.una.pol.karaku.dao.entity.annotations.URI.Type;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.replication.client.CacheAll;

/**
 * Entidad para los test de replicación.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 * 
 */
@Audited
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "description"),
		@UniqueConstraint(columnNames = "uri") })
@CacheAll
public class ReplicatedEntity extends BaseEntity implements DTO, Shareable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101207945256545348L;

	/**
	 * Clave primaria igual al 80% de los casos implementados.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Clave única dentro del sistema
	 */

	@URI(baseUri = "test", type = Type.SEQUENCE, sequenceName = "tt1")
	private String uri;

	/**
	 * Atributo comun para realizar pruebas
	 */
	private String description;

	private boolean active;

	@OneToMany(mappedBy = "father")
	private Set<ReplicatedEntityChild> childs;

	/**
	 * @return id
	 */
	@Override
	public Long getId() {

		return id;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	@Override
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @return uri
	 */
	@Override
	public String getUri() {

		return uri;
	}

	/**
	 * @param uri
	 *            uri para setear
	 */
	public void setUri(String uri) {

		this.uri = uri;
	}

	/**
	 * @return description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * @param description
	 *            description para setear
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * @return active
	 */
	public Boolean getActive() {

		return active;
	}

	/**
	 * @param active
	 *            active para setear
	 */
	public void setActive(Boolean active) {

		this.active = active;
	}

	/**
	 * @return
	 */
	@Override
	public boolean isActive() {

		return active;
	}

	/**
	 * @return childs
	 */
	public Set<ReplicatedEntityChild> getChilds() {

		return childs;
	}

	/**
	 * @param childs
	 *            childs para setear
	 */
	public void setChilds(Set<ReplicatedEntityChild> childs) {

		this.childs = childs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate() {

		active = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {

		active = true;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ReplicatedEntity) {
			ReplicatedEntity oth = (ReplicatedEntity) obj;
			if (uri == null && oth.uri == null) {
				return true;
			}
			return uri.equals(oth.uri) && description.equals(oth.description);
		}
		return false;
	}

	@Override
	public String toString() {

		return uri;
	}
}
