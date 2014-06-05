/**
 * @SIGHBaseReportSimpleAdvanced 1.0 25/03/13. Sistema Integral de Gestion
 *                               Hospitalaria
 */
package py.una.pol.karaku.business.reports;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.Column;
import py.una.pol.karaku.reports.ExportReport;
import py.una.pol.karaku.util.ListHelper;

/**
 * Clase que implementa el servicio para los reportes simples cuyas columnas de
 * la grilla posee atributos calculados.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 25/03/2013
 * 
 */
public abstract class KarakuBaseReportSimpleAdvanced<T> implements
		IKarakuBaseReportSimpleAdvanced<T> {

	@Autowired
	private ExportReport exportReport;

	@Override
	public DRDataSource getStructDataSource() {

		DRDataSource dataSource = new DRDataSource(
				ListHelper.asArray(getColumnsDataSource()));
		return dataSource;
	}

	@Override
	public List<?> getList(IKarakuBaseLogic<T, ?> logic, Where<T> where) {

		if (where != null) {
			return logic.getAll(where, null);
		}
		return logic.getAll(null);
	}

	@Override
	public List<String> getColumnsDataSource() {

		LinkedList<String> template = new LinkedList<String>();
		for (Column column : getColumnsReport()) {
			template.add(column.getField());
		}
		return template;
	}

	@Override
	public abstract List<Column> getColumnsReport();

	@Override
	public abstract DRDataSource getDataSource(IKarakuBaseLogic<T, ?> logic,
			Where<T> where);

	@Override
	public void generateReport(Map<String, Object> params, String type,
			IKarakuBaseLogic<T, ?> logic, Where<T> where) throws ReportException {

		exportReport.exportSimpleReport(getColumnsReport(),
				getDataSource(logic, where), params, type);

	}

}