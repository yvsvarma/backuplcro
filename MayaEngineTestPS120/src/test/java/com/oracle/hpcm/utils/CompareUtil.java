package com.oracle.hpcm.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Mohnish
 */
public class CompareUtil {
	public static Logger logger = Logger.getLogger("c");
	public static boolean compare(String filePathExp, String filePathAct)
	throws IOException, InterruptedException {
		if (new File(filePathExp).exists()) {

			if (new File(filePathAct).exists()) {

				boolean exitValue = FileUtils.contentEquals(new File(
				filePathExp), new File(filePathAct));

				if (exitValue) {
					logger.info("[CompareUtil.compare] : Expected and actual files are identical. ");
					return true;
				} else {
					logger.severe("[CompareUtil.compare] : Expected and actual files are NOT identical.");
					return false;
				}

			} else {
				logger.severe("[CompareUtil.compare] Actual file does not exist at : " + filePathAct + ".");
				logger.severe("[CompareUtil.compare] Aborting compare.");
				return false;
			}
		} else {
			logger.severe("[CompareUtil.compare] Expected file does not exist at : " + filePathExp + ".");
			logger.severe("[CompareUtil.compare] Aborting compare.");
			return false;
		}
	}

	public static boolean compareJSONRuleBalancing(String filePathExp, String filePathAct)
			throws IOException, InterruptedException {
		
				ObjectMapper mapper = new ObjectMapper();
				if (new File(filePathExp).exists()) {

					if (new File(filePathAct).exists()) {
						
						//MyClass[] myObjects = mapper.readValue(json, MyClass[].class);
						RuleBalanceDTO[] expectedRules = mapper.readValue(new File(filePathExp), RuleBalanceDTO[].class);
						RuleBalanceDTO[] actualRules = mapper.readValue(new File(filePathAct), RuleBalanceDTO[].class);
						boolean lengthTest = expectedRules.length==actualRules.length;
						boolean exitValue = false;
						exitValue  = lengthTest;
						if(lengthTest)
							for(int i =  0,j = 0; i< expectedRules.length;i++){
								
								boolean ruleFoundStatus =false;
								boolean ruleCompareStatus =false;
								for(j =0 ; j<actualRules.length && !ruleCompareStatus;j++){
									if(expectedRules[i].getName().equalsIgnoreCase(actualRules[j].getName())){
										ruleFoundStatus=true;
										if(expectedRules[i].equals(actualRules[j])){										
											ruleCompareStatus = true;
										}
										else{
											logger.severe("Comparison failed for following Expected rule : "+expectedRules[i]);
											logger.severe("With                            Actual rule   : "+actualRules[j]);
											StringBuilder sb = new StringBuilder();
											sb.append("----------------"+expectedRules[j].getName()+" : "+expectedRules[j].getRuleNumber()+"---------------");
											sb.append("\nrunningBalance : "+ expectedRules[i].getRunningBalance()+"<-->"+ actualRules[i].getRunningBalance());
											sb.append("\nbalance : "+ expectedRules[i].getBalance()+"<-->"+ actualRules[i].getBalance());
											sb.append("\nallocationIn : "+ expectedRules[i].getAllocationIn()+"<-->"+ actualRules[i].getAllocationIn());
											sb.append("\nallocationOut : "+ expectedRules[i].getAllocationOut()+"<-->"+ actualRules[i].getAllocationOut());
											sb.append("\nadjustmentIn : "+ expectedRules[i].getAdjustmentIn()+"<-->"+ actualRules[i].getAdjustmentIn());
											sb.append("\nadjustmentOut : "+ expectedRules[i].getAdjustmentOut()+"<-->"+ actualRules[i].getAdjustmentOut());
											sb.append("\ninput : "+ expectedRules[i].getInput()+"<-->"+ actualRules[i].getInput());
											sb.append("\nrunningRemainder : "+ expectedRules[i].getRunningRemainder()+"<-->"+ actualRules[i].getRunningRemainder());
											sb.append("\nremainder : "+ expectedRules[i].getRemainder()+"<-->"+ actualRules[i].getRemainder());
											sb.append("\nnetChange : "+ expectedRules[i].getNetChange()+"<-->"+ actualRules[i].getNetChange());
											sb.append("\noffset : "+ expectedRules[i].getOffset()+"<-->"+ actualRules[i].getOffset());
											sb.append("\ninputAsString : "+ expectedRules[i].getInputAsString()+"<-->"+ actualRules[i].getInputAsString());
											logger.severe(sb.toString());
											break;
										}
									}
									
								}
								if(ruleFoundStatus==false){
									logger.severe("Balancing Not found for following rule : "+expectedRules[i]);
									if(exitValue) 
										exitValue=ruleCompareStatus;
								}
								if(ruleCompareStatus==false){
									//logger.severe("Balancing Not found for following rule : "+expectedRules[i]);
									if(exitValue) 
										exitValue=ruleCompareStatus;
								}
								
							}			
						

						if (exitValue) {
							logger.info("[CompareUtil.compare] : Expected and actual files are identical. ");
							return true;
						} else {
							logger.severe("[CompareUtil.compare] : Expected and actual files are NOT identical.");
							return false;
						}

					} else {
						logger.severe("[CompareUtil.compare] Actual file does not exist at : " + filePathAct + ".");
						logger.severe("[CompareUtil.compare] Aborting compare.");
						return false;
					}
				} else {
					logger.severe("[CompareUtil.compare] Expected file does not exist at : " + filePathExp + ".");
					logger.severe("[CompareUtil.compare] Aborting compare.");
					return false;
				}
			}
}