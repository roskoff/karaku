/*
 * @TestConfiguration.java 1.0 Aug 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import py.una.med.base.log.LogPostProcessor;
import py.una.med.base.math.MathContextProvider;
import py.una.med.base.services.util.NumberAdapter;
import py.una.med.base.test.util.TestI18nHelper;
import py.una.med.base.test.util.TestPropertiesUtil;
import py.una.med.base.util.FormatProvider;
import py.una.med.base.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * 
 */
@Configuration
@Profile(BaseTestConfiguration.TEST_PROFILE)
public class BaseTestConfiguration {

	/**
	 * Ubicacion del archivo de configuraciones
	 */
	public static final String CONFIG_LOCATION = "karaku.properties";

	/**
	 * Cadena que representa el profile de test
	 */
	public static final String TEST_PROFILE = "test";

	protected static TestPropertiesUtil properties;

	@Bean
	public static TestPropertiesUtil propertiesUtil() {

		properties = new TestPropertiesUtil();
		properties.setLocation(new ClassPathResource(CONFIG_LOCATION));
		return properties;
	}

	@Bean
	LogPostProcessor logPostProcessor() {

		return new LogPostProcessor();
	}

	@Bean(name = "fp")
	FormatProvider formatProvider() {

		return new FormatProvider();
	}

	@Bean
	MathContextProvider mathContextProvider() {

		return MathContextProvider.INSTANCE;
	}

	@Bean
	NumberAdapter quantityAdapter() {

		return NumberAdapter.INSTANCE;
	}

	@Bean
	I18nHelper i18nHelper() {

		return new TestI18nHelper();
	}

}
