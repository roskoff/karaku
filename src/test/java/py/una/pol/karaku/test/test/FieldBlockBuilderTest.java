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
package py.una.pol.karaku.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.reports.FieldBlockBuilder;
import py.una.pol.karaku.reports.KarakuReportBlockField;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.test.util.TestI18nHelper;

/**
 * Test del builder que construye bloques de reportes del tipo
 * fields(label:value).
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/05/2014
 * 
 */

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class FieldBlockBuilderTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Autowired
	private TestI18nHelper i18nHelper;

	@Before
	public void testBefore() {

		i18nHelper.addString("TITLE", "Bloque con internacionalizacion");
		i18nHelper.addString("FIELD1", "Nombre");
		i18nHelper.addString("FIELD2", "Apellido");
		i18nHelper.addString("PRUEBA", "Lista de personas");
	}

	@Test
	public void testCreate() throws Exception {

		FieldBlockBuilder builder1 = new FieldBlockBuilder(
				"Bloque sin internacionalizacion", false);

		assertEquals("Bloque sin internacionalizacion", builder1.getTitle());
		assertTrue(builder1.getFields().isEmpty());

		FieldBlockBuilder builder2 = new FieldBlockBuilder("TITLE", true);

		assertEquals("Bloque con internacionalizacion", builder2.getTitle());
		assertTrue(builder2.getFields().isEmpty());
	}

	@Test
	public void testAddField() throws Exception {

		/*
		 * Caso1. Con internacionalizacion del nombre del label
		 */
		FieldBlockBuilder builder1 = new FieldBlockBuilder("TITLE");
		builder1.addField("FIELD1", true, "GABRIELA");
		builder1.addField("FIELD2", true, "MEDINA");

		assertEquals(2, builder1.getFields().size());
		assertEquals("Nombre: ", builder1.getFields().get(0).getLabel());
		assertEquals("Apellido: ", builder1.getFields().get(1).getLabel());

		/*
		 * Caso2. Sin internacionalizacion de los nombres de los labels
		 */
		FieldBlockBuilder builder2 = new FieldBlockBuilder("TITLE");
		builder2.addField("Nombre", false, "VIVIANA");
		builder2.addField("Apellido", false, "OZUNA");

		assertFalse(builder1.getFields().isEmpty());
		assertEquals(2, builder2.getFields().size());
		assertEquals("Nombre: ", builder2.getFields().get(0).getLabel());
		assertEquals("Apellido: ", builder2.getFields().get(1).getLabel());
	}

	@Test
	public void testBuild() throws Exception {

		FieldBlockBuilder builder = new FieldBlockBuilder("PRUEBA")
				.setWidthLabel(20).setWidthValue(50);
		builder.addField("Nombre: ", false, "CARMEN");
		builder.addField("Apellido: ", false, "RIVAS");

		KarakuReportBlockField build = builder.build();

		assertEquals("Lista de personas", build.getTitle());
		assertEquals(20, build.getWidthLabel());
		assertEquals(50, build.getWidthValue());

	}
}
