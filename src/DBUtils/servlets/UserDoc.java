package DBUtils.servlets;

public class UserDoc {

private String userId;
private String articleId;
private int  weight;
private String categoryName;
private int categoryFrequency;


public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getArticleId() {
	return articleId;
}
public void setArticleId(String articleId) {
	this.articleId = articleId;
}
public int getWeight() {
	return weight;
}
public void setWeight(int weight) {
	this.weight = weight;
}
public String getCategoryName() {
	return categoryName;
}
public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}
public int getCategoryFrequency() {
	return categoryFrequency;
}
public void setCategoryFrequency(int categoryFrequency) {
	this.categoryFrequency = categoryFrequency;
}
}
