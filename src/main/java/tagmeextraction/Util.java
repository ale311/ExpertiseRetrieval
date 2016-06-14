package tagmeextraction;

public class Util {
	private static final String oldkey = "8020b57e2d41b6041c4fd06937acbec7";
	private static final String key = "e5b419f2-125a-4e67-94f7-d91de75364d1";
	private static final String urlTag = "https://tagme.d4science.org/tagme/tag";
	private static final String urlRel = "https://tagme.d4science.org/tagme/rel";
	private static final String sampleText ="Hardness is a measure of how resistant solid matter is to various kinds of permanent shape change when a compressive force is applied. Some materials, such as metal, are harder than others. Macroscopic hardness is generally characterized by strong intermolecular bonds, but the behavior of solid materials under force is complex; therefore, there are different measurements of hardness: scratch hardness, indentation hardness, and rebound hardness.Hardness is dependent on ductility, elastic stiffness, plasticity, strain, strength, toughness, viscoelasticity, and viscosity.Common examples of hard matter are ceramics, concrete, certain metals, and superhard materials, which can be contrasted with soft matter.";
	static String getOldkey() {
		return oldkey;
	}
	public static String getKey() {
		return key;
	}
	public static String getUrlTag() {
		return urlTag;
	}
	public static String getUrlrel() {
		return urlRel;
	}
	public static String getSampletext() {
		return sampleText;
	}
	
	
}
