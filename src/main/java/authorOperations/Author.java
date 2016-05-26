package authorOperations;

public class Author {
	String authorID;
	String name;
	String surname; 
	String affiliation_id;
	String affiliation_name;
	
	
	public Author(String authorID, String name, String surname, String affiliation_id, String affiliation_name) {
		super();
		this.authorID = authorID;
		this.name = name;
		this.surname = surname;
		this.affiliation_id = affiliation_id;
		this.affiliation_name = affiliation_name;
	}
	public Author() {
		// TODO Auto-generated constructor stub
	}
	public String getAffiliation_id() {
		return affiliation_id;
	}
	public void setAffiliation_id(String affiliation_id) {
		this.affiliation_id = affiliation_id;
	}
	public String getAffiliation_name() {
		return affiliation_name;
	}
	public void setAffiliation_name(String affiliation_name) {
		this.affiliation_name = affiliation_name;
	}
	public String getAuthorID() {
		return authorID;
	}
	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getAffiliation() {
		return affiliation_id;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation_id = affiliation;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((affiliation_id == null) ? 0 : affiliation_id.hashCode());
		result = prime * result + ((affiliation_name == null) ? 0 : affiliation_name.hashCode());
		result = prime * result + ((authorID == null) ? 0 : authorID.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
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
		Author other = (Author) obj;
		if (affiliation_id == null) {
			if (other.affiliation_id != null)
				return false;
		} else if (!affiliation_id.equals(other.affiliation_id))
			return false;
		if (affiliation_name == null) {
			if (other.affiliation_name != null)
				return false;
		} else if (!affiliation_name.equals(other.affiliation_name))
			return false;
		if (authorID == null) {
			if (other.authorID != null)
				return false;
		} else if (!authorID.equals(other.authorID))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Author [authorID=" + authorID + ", name=" + name + ", surname=" + surname + ", affiliation_id="
				+ affiliation_id + ", affiliation_name=" + affiliation_name + "]";
	}
	
}
