package net.nicoll.boot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.bind.RelaxedNames;
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.boot.configurationprocessor.metadata.JsonMarshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 *
 * @author Stephane Nicoll
 */
@EnableAutoConfiguration
@Configuration
public class ConfigurationValidator implements CommandLineRunner {

	private static final Log logger = LogFactory.getLog(ConfigurationValidator.class);

	@Autowired
	@Qualifier("advertizedProperties")
	private Properties advertizedProperties;

	@Autowired
	private ConfigurationMetadata configurationMetadata;

	private final Map<String, List<ItemMetadata>> items = new HashMap<String, List<ItemMetadata>>();

	private final Map<String, List<ItemMetadata>> groups = new HashMap<String, List<ItemMetadata>>();

	@PostConstruct
	public void initialize() {
		for (ItemMetadata item : configurationMetadata.getItems()) {
			Map<String, List<ItemMetadata>> mapToUse =
					(item.isOfItemType(ItemMetadata.ItemType.PROPERTY) ? this.items : this.groups);
			List<ItemMetadata> list = mapToUse.get(item.getName());
			if (list == null) {
				list = new ArrayList<ItemMetadata>();
				mapToUse.put(item.getName(), list);
			}
			list.add(item);
		}
	}


	@Override
	public void run(String... args) throws Exception {
		List<String> found = new ArrayList<String>();
		List<String> undocumented = new ArrayList<String>();
		List<String> unresolved = new ArrayList<String>();

		// Generate relax names for all properties
		List<ConfigKeyCandidates> advertized = new ArrayList<ConfigKeyCandidates>();
		for (Object item : advertizedProperties.keySet()) {
			advertized.add(new ConfigKeyCandidates((String) item));
		}


		// Check advertized properties
		for (ConfigKeyCandidates propertyItem : advertized) {
			String key = getDocumentedKey(propertyItem);
			if (key != null) {
				found.add(key);
			}
			else {
				unresolved.add(propertyItem.item);
			}
		}

		// Check non advertized properties
		for (String key : this.items.keySet()) {
			if (!found.contains(key)) {
				undocumented.add(key);
			}
		}

		StringBuilder sb = new StringBuilder("\n");
		sb.append("Configuration key statistics").append("\n");
		sb.append("Advertized keys: ").append(advertizedProperties.size()).append("\n");
		sb.append("Repository items: ").append(configurationMetadata.getItems().size()).append("\n");
		sb.append("Matching items: ").append(found.size()).append("\n");
		sb.append("Unresolved items (found in documentation but not in generated metadata): ").append(unresolved.size()).append("\n");
		sb.append("Undocumented items (found in generated metadata but not in documentation): ").append(undocumented.size()).append("\n");
		sb.append("\n");
		sb.append("\n");
		sb.append("Unresolved items").append("\n");
		sb.append("----------------").append("\n");
		Collections.sort(unresolved);
		for (String id : unresolved) {
			sb.append(id).append("\n");
		}
		sb.append("\n");
		sb.append("Undocumented items").append("\n");
		sb.append("--------------------").append("\n");
		List<String> ids = new ArrayList<String>();
		for (String item : undocumented) {
			ids.add(item);

		}
		Collections.sort(ids);
		for (String id : ids) {
			sb.append(id).append("\n");
		}

		logger.info(sb.toString());
	}

	private String getDocumentedKey(ConfigKeyCandidates candidates) {
		for (String candidate : candidates) {
			boolean hasKey = this.items.containsKey(candidate);
			if (hasKey) {
				return candidate;
			}
		}
		return null;
	}


	@Bean
	public ConfigurationMetadata configurationMetadata() throws IOException {
		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources("classpath*:META-INF/spring-configuration-metadata.json");
		JsonMarshaller marshaller = new JsonMarshaller();
		ConfigurationMetadata metadata = new ConfigurationMetadata();
		for (Resource resource : resources) {
			metadata.addAll(readMetadata(marshaller, resource));
		}
		return metadata;
	}

	private ConfigurationMetadata readMetadata(JsonMarshaller marshaller, Resource resource) throws IOException {
		InputStream in = resource.getInputStream();
		try {
			return marshaller.read(in);
		}
		finally {
			in.close();
		}
	}

	@Bean
	public PropertiesFactoryBean advertizedProperties() {
		PropertiesFactoryBean factory = new PropertiesFactoryBean();
		factory.setLocation(new PathMatchingResourcePatternResolver().getResource("classpath:advertized.properties"));
		return factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationValidator.class, args);
	}

	private static class ConfigKeyCandidates implements Iterable<String> {
		private final String item;

		private final Set<String> values;

		private ConfigKeyCandidates(String item) {
			this.item = item;
			this.values = initialize(item);
		}

		@Override
		public Iterator<String> iterator() {
			return this.values.iterator();
		}

		private static Set<String> initialize(String item) {
			String itemToUse = item;
			if (itemToUse.endsWith(".*")) {
				itemToUse = itemToUse.substring(0, itemToUse.length() - 2);
			}

			Set<String> values = new LinkedHashSet<String>();
			int i = itemToUse.lastIndexOf('.');
			if (i == -1) {
				for (String o : new RelaxedNames(itemToUse)) {
					values.add(o);
				}
			}
			else {
				String prefix = itemToUse.substring(0, i + 1);
				String suffix = itemToUse.substring(i + 1, itemToUse.length());
				for (String value : new RelaxedNames(suffix)) {
					values.add(prefix + value);
				}
			}
			return values;
		}
	}

}
