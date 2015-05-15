package ru.nchalkova.jsontree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonObjectFilter {
	private Set<String> path = new HashSet<>();
	private Set<String> filterId;
	private int count = 0;
	private Set<String> deleted = new HashSet<>();

	public JsonObjectFilter(Set<String> ids) {
		this.filterId = ids;
	}

	public void go(JSONObject obj) {
		String id = (String) obj.get("id");
		String title = (String) obj.get("title");
		JSONArray children = (JSONArray) obj.get("children");
		count++;
		// save children for current node
		Map<String, JSONObject> currentChilds = new HashMap<>();

		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				JSONObject o = (JSONObject) children.get(i);
				currentChilds.put((String) o.get("id"), o);
				go(o);
			}
			for (Map.Entry<String, JSONObject> entry : currentChilds.entrySet()) {
				String childId = entry.getKey();
				if (filterId.contains(childId)) {
					path.add(childId);
					path.add(id);
				} else {
					if (path.contains(childId)) {
						path.add(id);
					} else {
						deleted.add(entry.getKey());
						children.remove(entry.getValue());
					}
				}

				if (children.size() < 1) {
					obj.remove("children");
				}
			}
		}
		if ("root".equals(title) && !filterId.contains(id)) {
			obj.remove("id");
			obj.remove("title");
			obj.remove("type");
		}
	}

	public Set<String> getDeleted() {
		return deleted;
	}

	public int getCountNodes() {
		return count;
	}
}
