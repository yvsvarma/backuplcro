//-----------------------------------com.example.Example.java-----------------------------------

package com.oracle.hpcm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"ruleNumber",
"rules",
"balanceTypeRule",
"scale",
"sequence",
"name",
"description",
"runningBalance",
"balance",
"allocationIn",
"allocationOut",
"adjustmentIn",
"adjustmentOut",
"input",
"runningRemainder",
"remainder",
"netChange",
"offset",
"inputAsString",
"adjInAsString",
"adjOutAsString",
"allocInAsString",
"allocOutAsString",
"balanceAsString",
"runningBalanceAsString",
"runningRemainderAsString",
"remainderAsString",
"netChangeAsString",
"offsetAsString"
})
public class RuleBalanceDTO {

@JsonProperty("ruleNumber")
private String ruleNumber;
@JsonProperty("rules")
private List<Object> rules = new ArrayList<Object>();
@JsonProperty("balanceTypeRule")
private Boolean balanceTypeRule;
@JsonProperty("scale")
private Integer scale;
@JsonProperty("sequence")
private Integer sequence;
@JsonProperty("name")
private String name;
@JsonProperty("description")
private Object description;
@JsonProperty("runningBalance")
private Object runningBalance;
@JsonProperty("balance")
private Double balance;
@JsonProperty("allocationIn")
private Double allocationIn;
@JsonProperty("allocationOut")
private Double allocationOut;
@JsonProperty("adjustmentIn")
private Object adjustmentIn;
@JsonProperty("adjustmentOut")
private Object adjustmentOut;
@JsonProperty("input")
private Double input;
@JsonProperty("runningRemainder")
private Object runningRemainder;
@JsonProperty("remainder")
private Double remainder;
@JsonProperty("netChange")
private Double netChange;
@JsonProperty("offset")
private Object offset;
@JsonProperty("inputAsString")
private String inputAsString;
@JsonProperty("adjInAsString")
private String adjInAsString;
@JsonProperty("adjOutAsString")
private String adjOutAsString;
@JsonProperty("allocInAsString")
private String allocInAsString;
@JsonProperty("allocOutAsString")
private String allocOutAsString;
@JsonProperty("balanceAsString")
private String balanceAsString;
@JsonProperty("runningBalanceAsString")
private String runningBalanceAsString;
@JsonProperty("runningRemainderAsString")
private String runningRemainderAsString;
@JsonProperty("remainderAsString")
private String remainderAsString;
@JsonProperty("netChangeAsString")
private String netChangeAsString;
@JsonProperty("offsetAsString")
private String offsetAsString;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The ruleNumber
*/
@JsonProperty("ruleNumber")
public String getRuleNumber() {
return ruleNumber;
}

/**
* 
* @param ruleNumber
* The ruleNumber
*/
@JsonProperty("ruleNumber")
public void setRuleNumber(String ruleNumber) {
this.ruleNumber = ruleNumber;
}

/**
* 
* @return
* The rules
*/
@JsonProperty("rules")
public List<Object> getRules() {
return rules;
}

/**
* 
* @param rules
* The rules
*/
@JsonProperty("rules")
public void setRules(List<Object> rules) {
this.rules = rules;
}

/**
* 
* @return
* The balanceTypeRule
*/
@JsonProperty("balanceTypeRule")
public Boolean getBalanceTypeRule() {
return balanceTypeRule;
}

/**
* 
* @param balanceTypeRule
* The balanceTypeRule
*/
@JsonProperty("balanceTypeRule")
public void setBalanceTypeRule(Boolean balanceTypeRule) {
this.balanceTypeRule = balanceTypeRule;
}

/**
* 
* @return
* The scale
*/
@JsonProperty("scale")
public Integer getScale() {
return scale;
}

/**
* 
* @param scale
* The scale
*/
@JsonProperty("scale")
public void setScale(Integer scale) {
this.scale = scale;
}

/**
* 
* @return
* The sequence
*/
@JsonProperty("sequence")
public Integer getSequence() {
return sequence;
}

/**
* 
* @param sequence
* The sequence
*/
@JsonProperty("sequence")
public void setSequence(Integer sequence) {
this.sequence = sequence;
}

/**
* 
* @return
* The name
*/
@JsonProperty("name")
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The description
*/
@JsonProperty("description")
public Object getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
@JsonProperty("description")
public void setDescription(Object description) {
this.description = description;
}

/**
* 
* @return
* The runningBalance
*/
@JsonProperty("runningBalance")
public Object getRunningBalance() {
return runningBalance;
}

/**
* 
* @param runningBalance
* The runningBalance
*/
@JsonProperty("runningBalance")
public void setRunningBalance(Object runningBalance) {
this.runningBalance = runningBalance;
}

/**
* 
* @return
* The balance
*/
@JsonProperty("balance")
public Double getBalance() {
return balance;
}

/**
* 
* @param balance
* The balance
*/
@JsonProperty("balance")
public void setBalance(Double balance) {
this.balance = balance;
}

/**
* 
* @return
* The allocationIn
*/
@JsonProperty("allocationIn")
public Double getAllocationIn() {
return allocationIn;
}

/**
* 
* @param allocationIn
* The allocationIn
*/
@JsonProperty("allocationIn")
public void setAllocationIn(Double allocationIn) {
this.allocationIn = allocationIn;
}

/**
* 
* @return
* The allocationOut
*/
@JsonProperty("allocationOut")
public Double getAllocationOut() {
return allocationOut;
}

/**
* 
* @param allocationOut
* The allocationOut
*/
@JsonProperty("allocationOut")
public void setAllocationOut(Double allocationOut) {
this.allocationOut = allocationOut;
}

/**
* 
* @return
* The adjustmentIn
*/
@JsonProperty("adjustmentIn")
public Object getAdjustmentIn() {
return adjustmentIn;
}

/**
* 
* @param adjustmentIn
* The adjustmentIn
*/
@JsonProperty("adjustmentIn")
public void setAdjustmentIn(Object adjustmentIn) {
this.adjustmentIn = adjustmentIn;
}

/**
* 
* @return
* The adjustmentOut
*/
@JsonProperty("adjustmentOut")
public Object getAdjustmentOut() {
return adjustmentOut;
}

/**
* 
* @param adjustmentOut
* The adjustmentOut
*/
@JsonProperty("adjustmentOut")
public void setAdjustmentOut(Object adjustmentOut) {
this.adjustmentOut = adjustmentOut;
}

/**
* 
* @return
* The input
*/
@JsonProperty("input")
public Double getInput() {
return input;
}

/**
* 
* @param input
* The input
*/
@JsonProperty("input")
public void setInput(Double input) {
this.input = input;
}

/**
* 
* @return
* The runningRemainder
*/
@JsonProperty("runningRemainder")
public Object getRunningRemainder() {
return runningRemainder;
}

/**
* 
* @param runningRemainder
* The runningRemainder
*/
@JsonProperty("runningRemainder")
public void setRunningRemainder(Object runningRemainder) {
this.runningRemainder = runningRemainder;
}

/**
* 
* @return
* The remainder
*/
@JsonProperty("remainder")
public Double getRemainder() {
return remainder;
}

/**
* 
* @param remainder
* The remainder
*/
@JsonProperty("remainder")
public void setRemainder(Double remainder) {
this.remainder = remainder;
}

/**
* 
* @return
* The netChange
*/
@JsonProperty("netChange")
public Double getNetChange() {
return netChange;
}

/**
* 
* @param netChange
* The netChange
*/
@JsonProperty("netChange")
public void setNetChange(Double netChange) {
this.netChange = netChange;
}

/**
* 
* @return
* The offset
*/
@JsonProperty("offset")
public Object getOffset() {
return offset;
}

/**
* 
* @param offset
* The offset
*/
@JsonProperty("offset")
public void setOffset(Object offset) {
this.offset = offset;
}

/**
* 
* @return
* The inputAsString
*/
@JsonProperty("inputAsString")
public String getInputAsString() {
return inputAsString;
}

/**
* 
* @param inputAsString
* The inputAsString
*/
@JsonProperty("inputAsString")
public void setInputAsString(String inputAsString) {
this.inputAsString = inputAsString;
}

/**
* 
* @return
* The adjInAsString
*/
@JsonProperty("adjInAsString")
public String getAdjInAsString() {
return adjInAsString;
}

/**
* 
* @param adjInAsString
* The adjInAsString
*/
@JsonProperty("adjInAsString")
public void setAdjInAsString(String adjInAsString) {
this.adjInAsString = adjInAsString;
}

/**
* 
* @return
* The adjOutAsString
*/
@JsonProperty("adjOutAsString")
public String getAdjOutAsString() {
return adjOutAsString;
}

/**
* 
* @param adjOutAsString
* The adjOutAsString
*/
@JsonProperty("adjOutAsString")
public void setAdjOutAsString(String adjOutAsString) {
this.adjOutAsString = adjOutAsString;
}

/**
* 
* @return
* The allocInAsString
*/
@JsonProperty("allocInAsString")
public String getAllocInAsString() {
return allocInAsString;
}

/**
* 
* @param allocInAsString
* The allocInAsString
*/
@JsonProperty("allocInAsString")
public void setAllocInAsString(String allocInAsString) {
this.allocInAsString = allocInAsString;
}

/**
* 
* @return
* The allocOutAsString
*/
@JsonProperty("allocOutAsString")
public String getAllocOutAsString() {
return allocOutAsString;
}

/**
* 
* @param allocOutAsString
* The allocOutAsString
*/
@JsonProperty("allocOutAsString")
public void setAllocOutAsString(String allocOutAsString) {
this.allocOutAsString = allocOutAsString;
}

/**
* 
* @return
* The balanceAsString
*/
@JsonProperty("balanceAsString")
public String getBalanceAsString() {
return balanceAsString;
}

/**
* 
* @param balanceAsString
* The balanceAsString
*/
@JsonProperty("balanceAsString")
public void setBalanceAsString(String balanceAsString) {
this.balanceAsString = balanceAsString;
}

/**
* 
* @return
* The runningBalanceAsString
*/
@JsonProperty("runningBalanceAsString")
public String getRunningBalanceAsString() {
return runningBalanceAsString;
}

/**
* 
* @param runningBalanceAsString
* The runningBalanceAsString
*/
@JsonProperty("runningBalanceAsString")
public void setRunningBalanceAsString(String runningBalanceAsString) {
this.runningBalanceAsString = runningBalanceAsString;
}

/**
* 
* @return
* The runningRemainderAsString
*/
@JsonProperty("runningRemainderAsString")
public String getRunningRemainderAsString() {
return runningRemainderAsString;
}

/**
* 
* @param runningRemainderAsString
* The runningRemainderAsString
*/
@JsonProperty("runningRemainderAsString")
public void setRunningRemainderAsString(String runningRemainderAsString) {
this.runningRemainderAsString = runningRemainderAsString;
}

/**
* 
* @return
* The remainderAsString
*/
@JsonProperty("remainderAsString")
public String getRemainderAsString() {
return remainderAsString;
}

/**
* 
* @param remainderAsString
* The remainderAsString
*/
@JsonProperty("remainderAsString")
public void setRemainderAsString(String remainderAsString) {
this.remainderAsString = remainderAsString;
}

/**
* 
* @return
* The netChangeAsString
*/
@JsonProperty("netChangeAsString")
public String getNetChangeAsString() {
return netChangeAsString;
}

/**
* 
* @param netChangeAsString
* The netChangeAsString
*/
@JsonProperty("netChangeAsString")
public void setNetChangeAsString(String netChangeAsString) {
this.netChangeAsString = netChangeAsString;
}

/**
* 
* @return
* The offsetAsString
*/
@JsonProperty("offsetAsString")
public String getOffsetAsString() {
return offsetAsString;
}

/**
* 
* @param offsetAsString
* The offsetAsString
*/
@JsonProperty("offsetAsString")
public void setOffsetAsString(String offsetAsString) {
this.offsetAsString = offsetAsString;
}

@Override
public String toString() {
return ToStringBuilder.reflectionToString(this);
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

@Override
public int hashCode() {
return new HashCodeBuilder().append(ruleNumber).append(rules).append(balanceTypeRule).append(scale).append(sequence).append(name).append(description).append(runningBalance).append(balance).append(allocationIn).append(allocationOut).append(adjustmentIn).append(adjustmentOut).append(input).append(runningRemainder).append(remainder).append(netChange).append(offset).append(inputAsString).append(adjInAsString).append(adjOutAsString).append(allocInAsString).append(allocOutAsString).append(balanceAsString).append(runningBalanceAsString).append(runningRemainderAsString).append(remainderAsString).append(netChangeAsString).append(offsetAsString).append(additionalProperties).toHashCode();
}

@Override
public boolean equals(Object other) {
if (other == this) {
return true;
}
if ((other instanceof RuleBalanceDTO) == false) {
return false;
}
RuleBalanceDTO rhs = ((RuleBalanceDTO) other);
return new EqualsBuilder().append(ruleNumber, rhs.ruleNumber).append(rules, rhs.rules).append(balanceTypeRule, rhs.balanceTypeRule).append(scale, rhs.scale).append(sequence, rhs.sequence).append(name, rhs.name).append(description, rhs.description).append(runningBalance, rhs.runningBalance).append(balance, rhs.balance).append(allocationIn, rhs.allocationIn).append(allocationOut, rhs.allocationOut).append(adjustmentIn, rhs.adjustmentIn).append(adjustmentOut, rhs.adjustmentOut).append(input, rhs.input).append(runningRemainder, rhs.runningRemainder).append(remainder, rhs.remainder).append(netChange, rhs.netChange).append(offset, rhs.offset).append(inputAsString, rhs.inputAsString).append(adjInAsString, rhs.adjInAsString).append(adjOutAsString, rhs.adjOutAsString).append(allocInAsString, rhs.allocInAsString).append(allocOutAsString, rhs.allocOutAsString).append(balanceAsString, rhs.balanceAsString).append(runningBalanceAsString, rhs.runningBalanceAsString).append(runningRemainderAsString, rhs.runningRemainderAsString).append(remainderAsString, rhs.remainderAsString).append(netChangeAsString, rhs.netChangeAsString).append(offsetAsString, rhs.offsetAsString).append(additionalProperties, rhs.additionalProperties).isEquals();
}

}