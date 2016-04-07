package tagmeextraction;

import java.util.HashSet;
import java.util.Set;


public class WPExtract {
	HashSet<String> tag;
	HashSet<String> categories;
	HashSet<String> extract;
	
	public WPExtract(Set<String> t, Set<String> c, Set<String> e){
		tag = (HashSet<String>) t;
		categories = (HashSet<String>) c;
		extract = (HashSet<String>) e;
	}
	public WPExtract(){
		HashSet<String> tag = new HashSet<String>();
		HashSet<String> categories = new HashSet<String>();
		HashSet<String> extract = new HashSet<String>();
		
	}

	public HashSet<String> getTag() {
		return tag;
	}

	public void setTag(HashSet<String> tag) {
		this.tag = tag;
	}

	public HashSet<String> getCategories() {
		return categories;
	}

	public void setCategories(HashSet<String> categories) {
		this.categories = categories;
	}

	public HashSet<String> getExtract() {
		return extract;
	}

	public void setExtract(HashSet<String> extract) {
		this.extract = extract;
	}

	@Override
	public String toString() {
		return "Paper [tag=" + tag + ", categories=" + categories + ", extract=" + extract + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((extract == null) ? 0 : extract.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		WPExtract other = (WPExtract) obj;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (extract == null) {
			if (other.extract != null)
				return false;
		} else if (!extract.equals(other.extract))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
	
	
}