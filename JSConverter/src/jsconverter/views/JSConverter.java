package jsconverter.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 */

/**
 * @author iivanenko
 * 
 */
public class JSConverter {
	private final static String URL = "http://maps.google.com/maps/geo?q=50.414915,30.523249&output=json&sensor=false";

	private StringBuilder writter = new StringBuilder();

	enum Type {
		Boolean, Integer, String, Double, Array, Object
	};

	public static void main(String argsp[]) throws JSONException,
			ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL);
		HttpResponse execute = client.execute(get);
		String json = EntityUtils.toString(execute.getEntity());

		new JSConverter().toSystemOut("GeoDecoder", json);
	}

	public String toString(String json) throws JSONException {
		JSONObject root = new JSONObject(json);
		proccess("Root", root);
		return writter.toString();
	}

	public void toSystemOut(String rootName, String json) throws JSONException {
		JSONObject root = new JSONObject(json);
		proccess(rootName, root);
		System.out.println(writter.toString());

	}

	public void proccess(String className, JSONObject root)
			throws JSONException {
		writter.append("\npublic class " + toUpperName(className) + " {\n");

		JSONArray names = root.names();

		Map<String, String> getSet = new LinkedHashMap<String, String>();

		for (int i = 0; i < names.length(); i++) {
			String name = (String) names.get(i);
			Object value = root.get(name);

			String[] res = printTypeName(name, value);
			getSet.put(res[0], res[1]);
		}
		for (String type : getSet.keySet()) {
			String name = getSet.get(type);
			//
			writter.append("\n");
			writter.append(String.format("\tpublic %s get%s() {\n", type,
					toUpperName(name)));
			writter.append(String.format("\t\treturn %s;\n", name));
			writter.append("\t}");
			// setter
			writter.append("\n");
			writter.append(String.format("\tpublic void set%s(%s %s) {\n",
					toUpperName(name), type, name));
			writter.append(String.format("\t\tthis.%s = %s;\n", name, name));
			writter.append("\t}\n");

		}

		writter.append("}");

		for (int i = 0; i < names.length(); i++) {
			String name = (String) names.get(i);
			Object value = root.get(name);
			if (value instanceof JSONObject) {
				proccess(name, (JSONObject) value);
			}
			if (value instanceof JSONArray) {
				Object chilValue = ((JSONArray) value).get(0);

				if (!isPrimitives((JSONArray) value)) {
					proccess(name, (JSONObject) chilValue);
				}
			}
		}
	}

	private static boolean isPrimitives(JSONArray array) throws JSONException {
		Object value = array.get(0);
		if (value instanceof JSONObject || value instanceof JSONArray) {
			return false;
		} else {
			return true;
		}
	}

	public String[] printTypeName(String name, Object value)
			throws JSONException {
		String className = toUpperName(name);

		String[] typeName = new String[2];

		StringBuffer result = new StringBuffer();
		if (isFirstUpperCase(name)) {
			result.append(String.format("\t @SerializedName(\"%s\") private ",
					name));
		} else {
			result.append("\t private ");
		}

		if (value instanceof JSONObject) {
			typeName[0] = className;
			typeName[1] = name.toLowerCase();

		} else if (value instanceof JSONArray) {
			String type = className;
			if (isPrimitives((JSONArray) value)) {
				type = getType(((JSONArray) value).get(0)).toString();
			}
			typeName[0] = String.format("List<%s>", type);
			typeName[1] = name.toLowerCase();

		} else {
			typeName[0] = getType(value).toString();
			typeName[1] = name.toLowerCase();
		}
		result.append(typeName[0]);
		result.append(" ");
		result.append(typeName[1]);
		result.append(";\n");
		writter.append(result);
		return typeName;
	}

	public static String convertStreamToString(InputStream is) {
		return new Scanner(is).useDelimiter("\\A").next();
	}

	private static String toUpperName(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private static boolean isFirstUpperCase(String str) {
		return Character.isUpperCase(str.charAt(0));
	}

	public Type getType(Object value) throws JSONException {

		if (value instanceof JSONObject) {
			return Type.Object;
		} else if (value instanceof JSONArray) {
			return Type.Array;
		} else if (value instanceof String) {
			return Type.String;
		} else if (value instanceof Integer) {
			return Type.Integer;
		} else if (value instanceof Double) {
			return Type.Double;
		} else if (value instanceof Boolean) {
			return Type.Boolean;
		}

		return Type.String;

	}
}
