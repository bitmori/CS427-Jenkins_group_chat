package edu.illinois.t25;

import edu.illinois.t25.data.*;

import java.net.InetAddress;
import java.util.ArrayList;

import org.junit.Test;
import org.json.*;
import static org.junit.Assert.*;

public class UserGroupTest {

	
	@Test
	public void testConstructWithParent(){
		UserGroup group1 = new UserGroup("CS427",null);
		UserGroup child = new UserGroup("T25",group1);
		assertTrue(group1.isChild(child));
	}
	
	
	
	@Test
	public void preventAddingUpperLevelGroups(){
		UserGroup group1 = new UserGroup("CS427",null);
		UserGroup child = new UserGroup("T25",group1);
		UserGroup grandChild = new UserGroup("pair1",child);
		assertEquals(false,grandChild.addChild(group1));
	}
	
	
	
	
	
	@Test
	public void storeGroup() throws JSONException{
		UserGroup group1 = new UserGroup("CS427",null);
		UserGroup child = new UserGroup("T25",group1);
		String stored = group1.getStringRepresentation();
		JSONObject correct = new JSONObject();
		correct.put("name", "CS427");
		JSONArray sampleList = new JSONArray();
		JSONObject sampleChild = new JSONObject();
		sampleChild.put("name","T25");
		JSONArray emptyArray = new JSONArray();
		sampleChild.put("children", emptyArray);
		sampleList.put(sampleChild);
		correct.put("children", sampleList);
		System.out.println(correct.toString());
		System.out.println(stored);

		assertTrue(correct.toString().equals(stored));
	}
	
	@Test
	public void restoreGroup() throws JSONException{
		JSONObject correct = new JSONObject();
		correct.put("name", "CS427");
		JSONArray sampleList = new JSONArray();
		JSONObject sampleChild = new JSONObject();
		sampleChild.put("name","T25");
		JSONArray emptyArray = new JSONArray();
		sampleChild.put("children", emptyArray);
		sampleList.put(sampleChild);
		correct.put("children", sampleList);
		UserGroup restoredGroup = UserGroup.restoreGroup(correct.toString());
		assertTrue(restoredGroup.getName().equals("CS427"));
		ArrayList<UserGroup> children = restoredGroup.getAllChildren();
		assertTrue(children.get(0).getName().equals("T25"));
	}
}

	
