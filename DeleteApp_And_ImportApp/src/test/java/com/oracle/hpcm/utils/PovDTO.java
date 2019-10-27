package com.oracle.hpcm.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PovDTO {
	
	private class PovDimMem{
		private String member;
		public PovDimMem(String mem){
			member=mem;
		}
		public String getMember() {
			return member;
		}
		
	}
	private PovDimMem[] dimMemArray= new PovDimMem[4];
	private String povStatus;
	
	public String getMember(int memPos) {
		if(memPos>4 || memPos<1)
			throw new IllegalArgumentException("POV group memeber should be between 1 and 4.");
		if(dimMemArray[memPos-1]!=null)
			return dimMemArray[memPos-1].getMember();
		return null;
	}
	public void setMember(String dimMem , int memPos) {
		if(memPos>4 || memPos<1)
			throw new IllegalArgumentException("POV group memeber should be between 1 and 4.");
		dimMemArray[memPos-1] = new PovDimMem(dimMem);
	}
	public String getStatus() {
		return povStatus;
	}
	public void setStatus(String povStatus) {
		this.povStatus = povStatus;
	}
	
	public PovDTO(String member1, String member2, String member3,
			String member4, String povStatus) {
		this.dimMemArray[0] = new PovDimMem( member1);
		this.dimMemArray[1] = new PovDimMem( member1);
		this.dimMemArray[2] = new PovDimMem( member1);
		this.dimMemArray[3] = new PovDimMem( member1);
		this.povStatus = povStatus;
	}
	public PovDTO(){}
	public PovDTO(String underScoredPOVText, int memCount){
		String[] memElements = underScoredPOVText.split("_");
		if(memCount > 4 || memCount < 1)
			throw new IllegalArgumentException();
		if(memCount  > 0)
			setMember(memElements[0],1);
		if(memCount > 1)
			setMember(memElements[1],2);
		if(memCount > 2)
			setMember(memElements[2],3);
		if(memCount > 3)
			setMember(memElements[3],4);
	}
	static public PovDTO[] getPOVSFromJSONText(String jsonText) throws ParseException{
		//{"povMemberGroupDTO":{"povDimensionMember1":"2013","povDimensionMember2":"January","povDimensionMember3":"Actual","povState":"Draft"}}
		JSONParser parser = new JSONParser();
	
		JSONArray povJSONArray = (JSONArray) parser.parse(jsonText);
		PovDTO[] povArray = new PovDTO[povJSONArray.size()];
		for (int i = 0; i < povArray.length; i++) {
			JSONObject  obj = (JSONObject) povJSONArray.get(i);
			PovDTO pov = new PovDTO();
			if(obj.containsKey("povDimensionMember1"))
				pov.setMember((String)obj.get("povDimensionMember1"),1);
			if(obj.containsKey("povDimensionMember2"))
				pov.setMember((String)obj.get("povDimensionMember2"),2);
			if(obj.containsKey("povDimensionMember3"))
				pov.setMember((String)obj.get("povDimensionMember3"),3);
			if(obj.containsKey("povDimensionMember4"))
				pov.setMember((String)obj.get("povDimensionMember4"),4);
			if(obj.containsKey("povState"))
				pov.setStatus((String)obj.get("povState"));
			povArray[i] = pov;
		}
		return povArray;
	}
	@Override
	public String toString(){
		String text = "";
		for (PovDimMem eachDimMem : dimMemArray){
			if(eachDimMem != null)
				if(eachDimMem.getMember()!=null)
					if(text.equals(""))
							text=eachDimMem.getMember();
						else
							text=text+ "_" + eachDimMem.getMember();
		}
		if(text.equals(""))
			return null;
		return text;
		
	}
	public static void main(String[] args) throws ParseException{
		String jsontext ="{\"povMemberGroupDTO\":[{\"povDimensionMember1\":\"2014\",\"povDimensionMember2\":\"June\",\"povDimensionMember3\":\"Actual\",\"povState\":\"Draft\"}]}";
		PovDTO[] array = getPOVSFromJSONText(jsontext);
		PovDTO p1 = array[0];
		System.out.println(p1);
	}
}
