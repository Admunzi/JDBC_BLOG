package modelo;

import java.util.Objects;

public class Category {
	
	private String nameCategory;
	private int recommendedAge;
	
	public Category(int recommendedAge) {
		this.recommendedAge = recommendedAge;
	}

	public Category() {
		this.recommendedAge = 0;
	}

	public String getNameCategory() {
		return nameCategory;
	}

	public void setNameCategory(String name) {
		this.nameCategory = (name.length() <= 30) ? name:name.substring(0,30);
	}

	public int getRecommendedAge() {
		return recommendedAge;
	}

	public void setRecommendedAge(int recommendedAge) {
		this.recommendedAge = (recommendedAge >= 0 && recommendedAge <= 100) ? recommendedAge:0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nameCategory, recommendedAge);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(nameCategory, other.nameCategory) && recommendedAge == other.recommendedAge;
	}

	@Override
	public String toString() {
		return "Category [nameCategory=" + nameCategory + ", recommendedAge=" + recommendedAge + "]";
	}

}
