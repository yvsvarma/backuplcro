package com.oracle.hpcm.utils;

public class ItemsObject extends ResultObject{
	private String itemsText;
	
	public String getItemsText() {
		return itemsText;
	}
	@Override
	public String getText(){
		return itemsText;
	}
	public ItemsObject(boolean result, String statusText, String details, String itemsText) {
		super(result, statusText, details);
		this.itemsText=itemsText;
	}

}
