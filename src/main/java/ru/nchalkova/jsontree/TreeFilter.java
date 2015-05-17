package ru.nchalkova.jsontree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

/**
 * Json filter. It reads json from TEST_JSON file, filter by id in arguments and
 * print the filtered json tree into a standard output.
 * */
public class TreeFilter {
	private static final String TEST_JSON = "test.json";

	public static void main(String[] args) {

		if (args == null || args.length < 1) {
			System.out.println("Please input ids to filter");
			return;
		}
		JsonReader reader = new JsonReader();
		JSONObject obj = reader.readJson(TEST_JSON);

		if (obj == null) {
			System.out.println("Json reading error. JSONObject==null");
			return;
		}

		Set<String> filterIdSet = new HashSet<>(Arrays.asList(args));
		JsonObjectFilter filter = new JsonObjectFilter(filterIdSet);
		filter.go(obj);

		if (obj.isEmpty()) {
			System.out.println("No matches found");
		} else {
			System.out.println("Filtered json tree:");
			System.out.println(obj);
		}
	}

}
