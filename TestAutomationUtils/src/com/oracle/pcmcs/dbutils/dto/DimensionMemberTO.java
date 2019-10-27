package com.oracle.pcmcs.dbutils.dto;

import java.util.Collection;
import java.util.Comparator;

public class DimensionMemberTO implements Comparator<DimensionMemberTO> {
    private String name;
    private Collection<String> attrDimMemNames;
    private Collection<String> aliases;   
    private String parentName;
    public DimensionMemberTO() {
        super();
    }

    public void setAttrDimMemNames(Collection<String> attrDimMemNames) {
        this.attrDimMemNames = attrDimMemNames;
    }

    public Collection<String> getAttrDimMemNames() {
        return attrDimMemNames;
    }
  
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAliases(Collection<String> aliases) {
        this.aliases = aliases;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public int compare(DimensionMemberTO o1, DimensionMemberTO o2) {
        return o1.getName().compareTo(o2.getName());
    }
}

