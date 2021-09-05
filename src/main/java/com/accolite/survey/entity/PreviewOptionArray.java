package com.accolite.survey.entity;

public class PreviewOptionArray {
private String option;
private String optionId;
public String getOption() {
	return option;
}
public void setOption(String option) {
	this.option = option;
}
public String getOptionId() {
	return optionId;
}
public void setOptionId(String optionId) {
	this.optionId = optionId;
}
@Override
public String toString() {
	return "PreviewOptionArray [option=" + option + ", optionId=" + optionId + "]";
}

}
