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
package py.una.pol.karaku.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import py.una.pol.karaku.reports.Align;
import py.una.pol.karaku.reports.KarakuReportBlock;
import py.una.pol.karaku.reports.KarakuReportBlockField;

/**
 * Clase utilizada para el diseño de reportes.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/03/2014
 * 
 */

public final class ReportBuilder {

    private KarakuReportBlockField master;

    private List<KarakuReportBlock> details;

    private Map<String, Object> params;
    private String type;
    private boolean sectionCriteria;
    private Align align;
    private static final String SELECTIONFILTER = "selectionFilters";

    public ReportBuilder(String title, String type) {

        this.params = new HashMap<String, Object>();
        // Agrega el título del reporte en la los parámetros.
        this.addParam("titleReport", getMessage(title));
        this.sectionCriteria = false;
        this.setAlign(Align.VERTICAL);
        this.type = type;
    }

    /**
     * Constructor por defecto
     */
    public ReportBuilder() {

        this.params = new HashMap<String, Object>();
        // Agrega el título del reporte en la los parámetros.
        this.sectionCriteria = false;
        this.setAlign(Align.VERTICAL);
    }

    /**
     * @param block
     *            Representa la cabecera de un reporte cabecera-detalle
     */
    public ReportBuilder setMaster(KarakuReportBlockField block) {

        this.master = block;
        return this;
    }

    /**
     * @param block
     *            Representa un determinado detalle de un reporte
     *            cabecera-detalle
     */
    public ReportBuilder addDetail(KarakuReportBlock block) {

        if (details == null) {
            details = new ArrayList<KarakuReportBlock>();
        }
        this.details.add(block);
        return this;
    }

    /**
     * Agrega un bloque al reporte.
     * 
     * Cada bloque se considera como un subreporte.
     * 
     * @param block
     * @return
     */
    public ReportBuilder addBlock(KarakuReportBlock block) {

        return this.addDetail(block);
    }

    /**
     * Agrega una lista de bloques al reporte.
     * 
     * @param blocks
     *            bloques que se desean agregar al reporte
     * @return Reporte con los bloques agregados.
     */
    public ReportBuilder addBlocks(List<KarakuReportBlock> blocks) {

        for (KarakuReportBlock block : blocks) {
            addDetail(block);
        }
        return this;
    }

    public List<KarakuReportBlock> getDetails() {

        return details;
    }

    public KarakuReportBlockField getMaster() {

        return master;
    }

    public Map<String, Object> getParams() {

        return params;
    }

    public void setParams(Map<String, Object> params) {

        this.params = params;
    }

    public ReportBuilder addParam(String key, Object value) {

        this.params.put(key, value);
        return this;

    }

    public String getType() {

        return type;
    }

    public ReportBuilder setType(String type) {

        this.type = type;
        return this;
    }

    /**
     * Retorna los bloques del reporte master-detail.
     * 
     * Donde el primer bloque representa la cabecera y los demás cada uno de los
     * detalles asociados.
     * 
     * @return Lista de bloques del reporte del tipo master-detail.
     */
    public List<KarakuReportBlock> getBlocksMasterDetail() {

        List<KarakuReportBlock> blocks = new ArrayList<KarakuReportBlock>();
        blocks.add(master);
        blocks.addAll(details);
        return blocks;
    }

    /**
     * Retorna los bloques asociados al reporte.
     * 
     * @return Lista de bloques del reporte.
     */
    public List<KarakuReportBlock> getBlocks() {

        return details;
    }

    private static String getMessage(String key) {

        return I18nHelper.getSingleton().getString(key);
    }

    public boolean isSectionCriteria() {

        return sectionCriteria;
    }

    public void setSectionCriteria(boolean sectionCriteria) {

        this.sectionCriteria = sectionCriteria;
    }

    public Align getAlign() {

        return align;
    }

    public void setAlign(Align align) {

        this.align = align;
    }

    /**
     * Agrega un conjunto de parámetros al reporte.
     * 
     * @param params
     *            Parámetros que se desean agregar.
     */
    public void addAllParam(Map<String, Object> params) {

        this.params.putAll(params);
    }

    /*
     * Método para agregar filtros en los reportes, un título y una descripción.
     * Ejemplo: title=Fecha, description=22/10/2015 -> Resultado Fecha:
     * 22/10/2015
     */
    public void addFilter(String title, String description) {

        if (getParams().get(SELECTIONFILTER) == null) {
            addParam(SELECTIONFILTER, title + ": " + description);
        } else {
            addParam(SELECTIONFILTER, getParams().get(SELECTIONFILTER) + "; "
                    + title + ": " + description);
        }

    }

    /*
     * Método para agregar filtros en los reportes, un título y una lista de
     * descripciones. Ejemplo: title=Fechas, description=[22/10/2015,
     * 22/10/2016, 22/10/2017] -> Resultado Fecha: 22/10/2015, 22/10/2016,
     * 22/10/2017
     */
    public void addFilterList(String title, List<String> list) {

        String cadena = "";
        StringBuilder sb = new StringBuilder();
        for (String s : list) {

            sb.append(s).append(", ");
        }
        // se elimina la ultima coma de la cadena generada
        cadena = sb.toString();
        cadena = cadena.substring(0, cadena.length() - 2);

        if (getParams().get(SELECTIONFILTER) == null) {
            addParam(SELECTIONFILTER, title + ": " + cadena);
        } else {
            addParam(SELECTIONFILTER, getParams().get(SELECTIONFILTER) + "; "
                    + title + ": " + cadena);
        }
    }
}
