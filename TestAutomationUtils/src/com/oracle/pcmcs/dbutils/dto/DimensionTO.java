package com.oracle.pcmcs.dbutils.dto;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class DimensionTO implements Comparator<DimensionTO>{
    private String name;
    private List<DimensionMemberTO> dimMems;
    private Collection<String> attrDimNames;
    
    public DimensionTO() {
        super();
    }

    public void setDimMems(List<DimensionMemberTO> dimMems) {
        this.dimMems = dimMems;
    }

    public List<DimensionMemberTO> getDimMems() {
        return dimMems;
    }

    public void setAttrDimNames(Collection<String> attrDimNames) {
        this.attrDimNames = attrDimNames;
    }

    public Collection<String> getAttrDimNames() {
        return attrDimNames;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int compare(DimensionTO o1, DimensionTO o2) {
        return o1.getName().compareTo(o2.getName());
    }
    public DimensionMemberTO searchAndReturnMember(String memberName){
    	if(memberName == null ) return null;
    	for(DimensionMemberTO mem : getDimMems()){
    		if(mem.getName().equals(memberName))
    			return mem;
    	}
    	return null;
    }
}

