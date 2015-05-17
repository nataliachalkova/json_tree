package ru.nchalkova.jsontree;

import static org.junit.Assert.*;
import static ru.nchalkova.jsontree.TestUtil.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class JsonObjectFilterTest {
	private JSONObject obj;
	private int count;
	private Set<String> filtered;

	@Before
	public void setUp() throws Exception {
		JsonReader r = new JsonReader();
		obj = r.readJson("test.json");
		count = 0;
		filtered = new HashSet<>();
	}

	@Test
	public void fullTree() {

		String[] ids = { "c369f682-bef9-413a-a7d8-0ad3091631a8", "ea579bb6-c6f7-401a-8117-aa0f9343b0cc", "acaed672-d5df-43a2-9e30-5cf55f74b1ce",
				"29a73327-d5be-44cc-8c1d-e45ddb8be2b7", "ccbe91ef-7dec-4dc7-bbf5-ef79161670df", "b3d888b1-c4f0-4337-87a3-d51961d81c0b",
				"undefined-id" };

		JSONObject o = readMockJson("tiny1.json");

		JsonObjectFilter f = new JsonObjectFilter(new HashSet<>(Arrays.asList(ids)));
		f.go(o);
		String title = (String) o.get("title");
		String id = (String) o.get("id");
		String type = (String) o.get("type");

		assertEquals("root", title);
		assertEquals("data", type);
		assertEquals("fe13e84e-fa26-46fb-bd39-6b581dad9eb7", id);
		JSONArray rootChildren = (JSONArray) o.get("children");
		assertNotNull(rootChildren);

		countTreeParams(o);
		assertEquals(12, count);
		assertEquals(0, f.getDeleted().size());
	}

	@Test
	public void onlyRoot() {
		String[] ids = { "fe13e84e-fa26-46fb-bd39-6b581dad9eb7" };

		JsonObjectFilter f = new JsonObjectFilter(new HashSet<>(Arrays.asList(ids)));
		f.go(obj);
		String title = (String) obj.get("title");
		String id = (String) obj.get("id");
		String type = (String) obj.get("type");

		assertEquals("root", title);
		assertEquals("data", type);
		assertEquals(ids[0], id);
		assertNull(obj.get("children"));
		countTreeParams(obj);
		assertEquals(1, count);
	}

	@Test
	public void onlyClasses() {
		String[] ids = { "c4756f1b-914f-4fa7-9754-d90310049767", "b3d888b1-c4f0-4337-87a3-d51961d81c0b" };

		JSONObject o = readMockJson("tiny2.json");

		JsonObjectFilter f = new JsonObjectFilter(new HashSet<>(Arrays.asList(ids)));
		f.go(o);

		assertEquals(9, f.getDeleted().size());

		// root contains 1 group with 2 child
		String title = (String) o.get("title");
		String id = (String) o.get("id");
		String type = (String) o.get("type");

		assertEquals("root", title);
		assertEquals("data", type);
		assertEquals("fe13e84e-fa26-46fb-bd39-6b581dad9eb7", id);

		JSONArray rootChildren = (JSONArray) o.get("children");
		JSONObject groupFirst = (JSONObject) rootChildren.get(0);
		assertNotNull(groupFirst);

		JSONArray classes = (JSONArray) groupFirst.get("children");
		assertEquals(2, classes.size());
		assertEquals("b3d888b1-c4f0-4337-87a3-d51961d81c0b", ((JSONObject) classes.get(0)).get("id"));
		assertEquals("c4756f1b-914f-4fa7-9754-d90310049767", ((JSONObject) classes.get(1)).get("id"));
		assertNull(((JSONObject) classes.get(0)).get("children"));
		assertNull(((JSONObject) classes.get(1)).get("children"));
	}

	@Test
	public void onlyOneTaste() {
		String[] ids = { "125a7cd0-1d8f-4c90-9882-c413b88d933c" };
		JsonObjectFilter f = new JsonObjectFilter(new HashSet<>(Arrays.asList(ids)));
		f.go(obj);

		assertNotNull(obj.get("children"));

		countTreeParams(obj);
		Set<String> set = new HashSet<>();
		set.add("125a7cd0-1d8f-4c90-9882-c413b88d933c");
		set.add("6cbdb247-e939-4e1a-b29b-ccb570b4ac8d");
		set.add("1e6b129e-8adf-4b02-8024-5f24ff49e5e6");
		set.add("1e1fcdf5-8440-4ad9-b343-b0c307be7657");
		set.add("97cbabe6-398e-4940-9a81-325b920f5fb1");
		set.add("fe13e84e-fa26-46fb-bd39-6b581dad9eb7");
		assertEquals(set, filtered);
		assertEquals(6, count);
	}

	@Test
	public void noMatch() {
		String[] ids = { "kva-kva-SE" };
		JsonObjectFilter f = new JsonObjectFilter(new HashSet<>(Arrays.asList(ids)));
		f.go(obj);
		String title = (String) obj.get("title");
		String id = (String) obj.get("id");
		String type = (String) obj.get("type");

		assertNull(title);
		assertNull(type);
		assertNull(id);
		assertNull(obj.get("children"));
	}

	@Test
	public void emptyJson() {
		String[] ids = { "kva-kva-SE", "fe13e84e-fa26-46fb-bd39-6b581dad9eb7" };
		JSONObject o = readMockJson("empty.json");

		JsonObjectFilter f = new JsonObjectFilter(new HashSet<>(Arrays.asList(ids)));
		f.go(o);
		String title = (String) o.get("title");
		String id = (String) o.get("id");
		String type = (String) o.get("type");

		assertNull(title);
		assertNull(type);
		assertNull(id);
		assertNull(o.get("children"));
	}

	private void countTreeParams(JSONObject obj) {
		JSONArray children = (JSONArray) obj.get("children");
		count++;
		filtered.add((String) obj.get("id"));
		Map<String, JSONObject> currentChilds = new HashMap<>();

		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				JSONObject o = (JSONObject) children.get(i);
				currentChilds.put((String) o.get("id"), o);
				countTreeParams(o);
			}
		}

	}

}
