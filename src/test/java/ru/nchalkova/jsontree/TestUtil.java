package ru.nchalkova.jsontree;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.json.simple.JSONObject;

public class TestUtil {
	public static JSONObject readMockJson(String fileName) {
		
		JsonReader r = mock(JsonReader.class);
		when(r.getPath(fileName)).thenReturn(Paths.get(System.getProperty("user.dir"), "src", "test", "resources", fileName));
		when(r.readJson(fileName)).thenCallRealMethod();

		return r.readJson(fileName);
	}
}
