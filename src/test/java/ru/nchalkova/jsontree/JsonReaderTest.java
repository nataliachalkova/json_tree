package ru.nchalkova.jsontree;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class JsonReaderTest {
	private JsonReader reader;

	@Before
	public void setUp() throws Exception {
		reader = mock(JsonReader.class);
	}

	@Test
	public void test1() {
		JsonReader r = new JsonReader();
		JSONObject obj = r.readJson("test.json");
		assertNotNull(obj);
	}

	@Test
	public void test2() {
		JsonReader r = new JsonReader();
		JSONObject obj = r.readJson("filenotexist.json");
		assertNull(obj);
	}

	@Test
	public void emptyFile1() {
		when(reader.getPath("empty.json")).thenReturn(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "empty.json"));
		when(reader.readJson("empty.json")).thenCallRealMethod();

		JSONObject obj = reader.readJson("empty.json");
		assertNotNull(obj); // {} -- file contains empty json
	}

	@Test
	public void emptyFile2() {
		when(reader.getPath("empty1.json")).thenReturn(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "empty1.json"));
		when(reader.readJson("empty1.json")).thenCallRealMethod();

		JSONObject obj = reader.readJson("empty1.json");
		assertNull(obj);
	}
}
