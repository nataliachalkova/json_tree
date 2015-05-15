package ru.nchalkova.jsontree;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {

	public JSONObject readJson(String jsonFileName) {
		JSONObject jsonObject = null;

		String filePath = getPath(jsonFileName).toString();
		try (FileReader reader = new FileReader(filePath)) {
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(reader);
		} catch (IOException | ParseException e) {
			System.out.println("Cannot read file " + jsonFileName + " : " + e);
		}

		return jsonObject;
	}

	private Path getPath(String fileName) {
		return Paths.get(System.getProperty("user.dir"), "src", "main", "resources", fileName);
	}
}
