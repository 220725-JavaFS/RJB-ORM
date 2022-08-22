package com.Matrix;

public class Matrix {
	protected int id;
	protected String ObjectName;
	protected String ObjectType;
	
	public Matrix() {
		
	}
	
	public Matrix(int ID, String Name, String Type) {
		super();
		this.id = ID;
		this.ObjectName = Name;
		this.ObjectType = Type;
	}

	@Override
	public String toString() {
		return "Matrix [id=" + id + ", ObjectName=" + ObjectName + ", ObjectType=" + ObjectType + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getObjectName() {
		return ObjectName;
	}

	public void setObjectName(String objectName) {
		ObjectName = objectName;
	}

	public String getObjectType() {
		return ObjectType;
	}

	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ObjectName == null) ? 0 : ObjectName.hashCode());
		result = prime * result + ((ObjectType == null) ? 0 : ObjectType.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (ObjectName == null) {
			if (other.ObjectName != null)
				return false;
		} else if (!ObjectName.equals(other.ObjectName))
			return false;
		if (ObjectType == null) {
			if (other.ObjectType != null)
				return false;
		} else if (!ObjectType.equals(other.ObjectType))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
