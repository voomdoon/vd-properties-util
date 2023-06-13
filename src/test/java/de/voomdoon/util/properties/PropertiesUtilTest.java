package de.voomdoon.util.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import de.voomdoon.testing.tests.TestBase;
import de.voomdoon.util.io.IOStreamUtil;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
public class PropertiesUtilTest {

	/**
	 * Test class for {@link PropertiesUtil#loadProperties(String...)}.
	 *
	 * @author André Schulz
	 *
	 * @since 0.1.0
	 */
	static class LoadProperties_StringArray_Test extends TestBase {

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_String_quotedWithLeadingSpace() {
			logTestStart();

			Properties properties = PropertiesUtil
					.loadProperties("PropertiesUtil/stringQuotedStartWithSpace.properties");

			assertThat(properties.get("string")).isEqualTo(" abc");
		}

		/**
		 * @throws IOException
		 * @throws FileNotFoundException
		 * @since 0.1.0
		 */
		@Test
		void test_umlauts_file() throws FileNotFoundException, IOException {
			logTestStart();

			File file = new File(getTempDirectory() + "/umlauts.properties");

			IOStreamUtil.copy(IOStreamUtil.getInputStream("PropertiesUtil/umlauts.properties"),
					new FileOutputStream(file));

			Properties properties = PropertiesUtil.loadProperties(file.toString());

			assertThat(properties).containsEntry("string", "äöüß");
		}

		/**
		 * @throws IOException
		 * @throws FileNotFoundException
		 * @since 0.1.0
		 */
		@Test
		void test_umlauts_file_UTF8() throws FileNotFoundException, IOException {
			logTestStart();

			File file = new File(getTempDirectory() + "/umlauts_UTF8.properties");

			IOStreamUtil.copy(IOStreamUtil.getInputStream("PropertiesUtil/umlauts_UTF8.properties"),
					new FileOutputStream(file));

			Properties properties = PropertiesUtil.loadProperties(file.toString());

			assertThat(properties).containsEntry("string", "äöüß");
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_umlauts_resource() {
			logTestStart();

			Properties properties = PropertiesUtil.loadProperties("PropertiesUtil/umlauts.properties");

			assertThat(properties).containsEntry("string", "äöüß");
		}

		/**
		 * @since 0.1.0
		 */
		@Test
		void test_umlauts_resource_UTF8() {
			logTestStart();

			Properties properties = PropertiesUtil.loadProperties("PropertiesUtil/umlauts_UTF8.properties");

			assertThat(properties).containsEntry("string", "äöüß");
		}
	}
}
