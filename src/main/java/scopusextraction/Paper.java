package scopusextraction;

import java.util.HashSet;

import authorOperations.Author;

public class Paper {
	String scopusID;
	HashSet<Author> authors;
	String extract;
	String title;
	
	public Paper(String scopusID, HashSet<Author> authors, String extract, String title) {
		super();
		this.scopusID = scopusID;
		this.authors = authors;
		this.extract = extract;
		this.title = title;
	}
	public String getScopusID() {
		return scopusID;
	}
	public void setScopusID(String scopusID) {
		this.scopusID = scopusID;
	}
	public HashSet<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(HashSet<Author> authors) {
		this.authors = authors;
	}
	public String getExtract() {
		return extract;
	}
	public void setExtract(String extract) {
		this.extract = extract;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
		result = prime * result + ((extract == null) ? 0 : extract.hashCode());
		result = prime * result + ((scopusID == null) ? 0 : scopusID.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Paper other = (Paper) obj;
		if (authors == null) {
			if (other.authors != null)
				return false;
		} else if (!authors.equals(other.authors))
			return false;
		if (extract == null) {
			if (other.extract != null)
				return false;
		} else if (!extract.equals(other.extract))
			return false;
		if (scopusID == null) {
			if (other.scopusID != null)
				return false;
		} else if (!scopusID.equals(other.scopusID))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
	
}
