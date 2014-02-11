package edu.illinois.t25.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.json.*;

public class UserGroup {

	private UserGroup parent;
	private ArrayList<UserGroup> children;
	private String name;

	// / Constructors ///
	/**
	 * Constructor which sets the name and parent of the group and initialize
	 * children and user list.
	 * 
	 * @param name
	 *            : name of the group
	 * @param parent
	 *            : the parent of this group, could be null if there's no
	 *            parents
	 */
	public UserGroup(String name, UserGroup parent) {
		this.name = name;
		if (parent != null)
			parent.addChild(this);
		this.parent = parent;
		this.children = new ArrayList<UserGroup>();
	}

	// / Methods ///
	/**
	 * Adds a child group to the group and sets the parent attribute of the
	 * child group to this one.
	 * 
	 * @param newChild
	 *            : the child to be added
	 * @return true if added successfully, false if the child group's already in
	 *         the library
	 */
	public boolean addChild(UserGroup newChild) {
		if (newChild.getParent() != null)
			return false;
		UserGroup itr = this.getParent();
		while (itr != null) {
			if (itr == newChild)
				return false;
			itr = itr.getParent();
		}
		children.add(newChild);
		return true;
	}

	/**
	 * Deletes a child group and sets its parent attribute to null
	 * 
	 * @param childToDelete
	 *            : the child to be deleted from the branch
	 * @return true if deleted successfully, false if child doesn't exists
	 */
	public boolean deleteChild(UserGroup childToDelete) {
		if (!children.contains(childToDelete))
			return false;
		children.remove(childToDelete);
		return true;
	}

	// / Helper Methods ///
	/**
	 * Checks if a given group is in this branch
	 * 
	 * @param group
	 *            : the group to check
	 * @return true if the target group is in the children list of this group,
	 *         false otherwise.
	 */
	public boolean isChild(UserGroup group) {
		if (this.children.contains(group))
			return true;
		ListIterator<UserGroup> i = children.listIterator();
		while (i.hasNext()) {
			if (i.next().isChild(group))
				return true;
		}
		return false;
	}

	/**
	 * getter for the parent of this group.
	 * 
	 * @return the parent of this group.
	 */
	public UserGroup getParent() {
		return this.parent;
	}

	/**
	 * reset the parent of this group to null
	 */
	public void resetParent() {
		parent = null;
	}

	/**
	 * getter for the name of the group.
	 * 
	 * @return the name of the group.
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter for the parent of a given group. Only processes when the new
	 * parent doesn't exists in the branch already.
	 * 
	 * @param newParent
	 */
	public void setParent(UserGroup newParent) {
		if (!this.isChild(newParent))
			this.parent = newParent;
	}

	/**
	 * get the string representation in JSON syntax for this group and all its
	 * children.
	 * 
	 * @return string representation for the group.
	 * @throws JSONException
	 */
	public String getStringRepresentation() throws JSONException {
		JSONObject groupAsJSON = new JSONObject();
		JSONArray childrenAsJSON = new JSONArray();
		groupAsJSON.put("name", this.name);
		ListIterator<UserGroup> itr = children.listIterator();
		while (itr.hasNext()) {
			childrenAsJSON.put(new JSONObject(itr.next()
					.getStringRepresentation()));
		}
		groupAsJSON.put("children", childrenAsJSON);
		return groupAsJSON.toString();
	}

	/**
	 * A static method to restore a whole tree of groups from a string
	 * representation.
	 * 
	 * @param stringRepresentation
	 * @return the UserGroup object of the root of the tree
	 * @throws JSONException
	 */
	public static UserGroup restoreGroup(String stringRepresentation)
			throws JSONException {
		UserGroup restoredGroup = null;
		JSONObject groupInJSON = new JSONObject(stringRepresentation);
		JSONArray childrenInJSON = groupInJSON.getJSONArray("children");
		restoredGroup = new UserGroup(groupInJSON.getString("name"), null);
		int numOfChildren = childrenInJSON.length();
		for (int i = 0; i < numOfChildren; i++) {
			UserGroup child = restoreGroup(childrenInJSON.getJSONObject(i)
					.toString());
			restoredGroup.addChild(child);
			child.setParent(restoredGroup);
		}
		return restoredGroup;
	}

	/**
	 * Get all the groups that a user could access to if they have access to
	 * this group.
	 * 
	 * @return A list of all the accessible groups (including the current one.)
	 */
	public ArrayList<UserGroup> getAccessibleGroups() {
		ArrayList<UserGroup> accessibleGroupList = new ArrayList<UserGroup>();
		accessibleGroupList.add(this);
		if (this.parent != null)
			accessibleGroupList.addAll(this.parent.getAccessibleGroups());
		return accessibleGroupList;
	}

	/**
	 * Get all the groups in this branch
	 * 
	 * @return A list of all the children groups.
	 */
	public ArrayList<UserGroup> getAllChildren() {
		ArrayList<UserGroup> childrenList = new ArrayList<UserGroup>();
		ListIterator<UserGroup> itr = children.listIterator();
		while (itr.hasNext()) {
			UserGroup kid = itr.next();
			childrenList.add(kid);
			childrenList.addAll(kid.getAllChildren());
		}
		return childrenList;
	}
}
