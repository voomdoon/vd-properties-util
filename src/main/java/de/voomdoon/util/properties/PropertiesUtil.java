package de.voomdoon.util.properties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import lombok.experimental.UtilityClass;

/**
 * DOCME add JavaDoc for
 *
 * @author André Schulz
 *
 * @since 0.1.0
 */
@UtilityClass
public class PropertiesUtil {

	/**
	 * DOCME add JavaDoc for method loadProperties
	 * 
	 * @param sources
	 * @return
	 * @since 0.1.0
	 */
	public static Properties loadProperties(String... sources) {
		Properties result = new Properties();

		for (String source : sources) {
			Properties properties = loadProperties(source, PropertiesUtil.class);
			putAll(result, properties);
		}

		return result;
	}

	/**
	 * DOCME add JavaDoc for method loadProperties
	 * 
	 * @param inputStream
	 * @return
	 * @since 0.1.0
	 */
	private static Properties loadProperties(InputStream inputStream) {
		final Properties properties = new Properties();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int read;
		byte[] buffer = new byte[1024];

		try {
			while ((read = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, read);
			}
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'loadProperties': " + e.getMessage(), e);
		}

		try {
			inputStream.close();// TESTME (at old its working without)
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'loadProperties': " + e.getMessage(), e);
		}

		byte[] data = baos.toByteArray();

		String utf8String = new String(data, StandardCharsets.UTF_8);
		byte[] stringBytes = utf8String.getBytes(StandardCharsets.UTF_8);

		try {
			if (Arrays.equals(stringBytes, data)) {
				properties.load(new InputStreamReader(new ByteArrayInputStream(data), StandardCharsets.UTF_8));
			} else {
				properties.load(new ByteArrayInputStream(data));
			}
		} catch (IOException e) {
			// TODO implement error handling
			throw new RuntimeException("Error at 'loadProperties': " + e.getMessage(), e);
		}

		return properties;
	}

	/**
	 * Returns the {@link Properties} loaded as resource.
	 * 
	 * @param source
	 *            The name of the file containing the {@link Properties}. (Without {@code /} as prefix.)
	 * @param clazz
	 *            The {@link Class} by which to get the resource.
	 * @return The loaded {@link Properties}.
	 * @throws IOException
	 *             If an error occurs while loading the {@link Properties}.
	 * @since 0.1.0
	 * @see ClassLoader#getResourceAsStream(String)
	 */
	private static Properties loadProperties(String source, Class<?> clazz) {
		Objects.requireNonNull(source, "source");
		Objects.requireNonNull(clazz, "clazz");

		String resourceName;

		if (!source.startsWith("/")) {
			resourceName = "/" + source;
		} else {
			resourceName = source;
		}

		InputStream inputStream;

		try {
			inputStream = clazz.getResourceAsStream(resourceName);

			if (inputStream == null) {
				throw new IOException("Failed to get resource " + resourceName);
			}
		} catch (final IOException e) {
			try {
				inputStream = new FileInputStream(source);
			} catch (IOException e1) {
				throw new IllegalStateException("Failed to get properties: " + e1.getMessage(), e1);
			}
		}

		return loadProperties(inputStream);
	}

	/**
	 * @param result
	 * @param properties
	 * @since 0.1.0
	 */
	private static void putAll(Properties result, Properties properties) {
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String value = entry.getValue().toString();

			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.substring(1, value.length() - 1);
			}

			result.put(entry.getKey(), value);
		}
	}

}
