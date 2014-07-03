package io.spring.initializr.web;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

/**
 * Validate the json format.
 *
 * @author Stephane Nicoll
 */
class JsonAssert {

	private final JsonNode content;

	JsonAssert(String jsonContent) {
		try {
			this.content = new ObjectMapper().readTree(jsonContent);
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to parse '" + jsonContent + "'", e);
		}
	}

	JsonAssert(JsonNode node) {
		this.content = node;
	}

	public void assertField(String name, String value) {
		JsonNode field = this.content.get(name);
		assertEquals(JsonNodeType.STRING, field.getNodeType());
		assertEquals(value, field.textValue());
	}

	public void hasNoField(String... fieldNames) {
		for (String fieldName : fieldNames) {
			assertNull("Field '" + fieldName + "' is not expected on " + this.content, this.content.get(fieldName));
		}
	}

	public void assertArraySize(int size) {
		assertEquals("Not an array '" + this.content + "'", JsonNodeType.ARRAY, this.content.getNodeType());
		assertEquals("wrong number of elements for '" + this.content + "'", size, this.content.size());
	}

	public void assertRootSize(String name, int count) {
		assertEquals("Wrong number of '" + name + "'", count, getMandatory(name).size());
	}

	public JsonAssert getChild(String name) {
		return new JsonAssert(getMandatory(name));
	}

	public JsonAssert getElement(int index) {
		return new JsonAssert(this.content.get(index));
	}

	private JsonNode getMandatory(String name) {
		JsonNode node = this.content.get(name);
		assertNotNull("Field with name '" + name + "' does not exist", node);
		return node;
	}
}
