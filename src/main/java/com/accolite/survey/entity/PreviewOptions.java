package com.accolite.survey.entity;

import java.util.List;

public class PreviewOptions {
private  boolean isOptionsValid;
private List<PreviewOptionArray> optionsArray;
public boolean isOptionsValid() {
	return isOptionsValid;
}
public void setOptionsValid(boolean isOptionsValid) {
	this.isOptionsValid = isOptionsValid;
}
public List<PreviewOptionArray> getOptionsArray() {
	return optionsArray;
}
public void setOptionsArray(List<PreviewOptionArray> optionsArray) {
	this.optionsArray = optionsArray;
}

}
