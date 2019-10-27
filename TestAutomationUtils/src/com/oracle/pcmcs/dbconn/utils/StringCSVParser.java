package com.oracle.pcmcs.dbconn.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class to split a CSV input line into individual String elements
 * Collection Mode defined as:  inside quotes - possible multi-value depending on header
 * Parse logic:
 * GET_VALUE - read the character and put into String unless terminate char
 * if double-quote ignore - but turn on Collection mode so commas ignored
 * if Collection = true:  read commas as normal character;  read double quote as turn off Collection mode
 * if Collection = false:  comma is terminate character
 * double double-quotes are read as a single double quote (instead of terminate character)
 * if see double double quote:  all commas ignored until after next double double quote
 * Comment line starts with #
 * Comment Block starts with #!--
 * Comment Block ends with #--!
 */
public class StringCSVParser {
   
    private static final String COMMA = ",";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String HASH = "#";
    private static final String EXCLAMATION = "!";
    private static final String DASH = "-";
    private static final String NONE = "<none>";
    private boolean isInCommentBlock = false;


    private boolean isCommentLine(String pInputLine) {
        boolean isCommentLine = false;
        String myChar = String.valueOf(pInputLine.charAt(0));
        if (myChar.equals(HASH)) {
            isCommentLine = true;
        }
        return isCommentLine;
    }
    private boolean isEndCommentBlock(String pInputLine) {
        boolean isEndCommentBlock = false;
        if (pInputLine.length() >= 4) {
            String my1stChar = String.valueOf(pInputLine.charAt(0));
            String my2ndChar = String.valueOf(pInputLine.charAt(1));
            String my3rdChar = String.valueOf(pInputLine.charAt(2));
            String my4thChar = String.valueOf(pInputLine.charAt(3));
            
            if (my1stChar.equals(HASH) && my2ndChar.equals(DASH) &&
                my3rdChar.equals(DASH) && my4thChar.equals(EXCLAMATION)) {
                    isEndCommentBlock = true;
                }
        }
        return isEndCommentBlock;
    }

    private boolean isStartCommentBlock(String pInputLine) {
        boolean isStartCommentBlock = false;
        if (pInputLine.length() >= 4) {
            String my1stChar = String.valueOf(pInputLine.charAt(0));
            String my2ndChar = String.valueOf(pInputLine.charAt(1));
            String my3rdChar = String.valueOf(pInputLine.charAt(2));
            String my4thChar = String.valueOf(pInputLine.charAt(3));
            
            if (my1stChar.equals(HASH) && my2ndChar.equals(EXCLAMATION) &&
                my3rdChar.equals(DASH) && my4thChar.equals(DASH)) {
                    isStartCommentBlock = true;
                }
        }
        return isStartCommentBlock;
    }
    
    
    public List<String> parse(String pInputLine) {        
        List<String> myReturnValues = new ArrayList<String>(); // this is what we return
        if (pInputLine != null && pInputLine.length() > 0) {
            List<String> myFinalValues = new ArrayList<String>();  // we don't return these yet -have to filter out the <none>
            int myPositionCount = 0;
            // we start off getting single values
            boolean isCollection = false;
            boolean isDoubleQuoteMode = false;
            
            String myTempValue = "";
            
            // We do NOT process in the following conditions:
            // -- normal comment line
            // -- in a comment block (from previous line)
            // -- start of a comment block
            // -- end of a comment block (from previous line)
            boolean isStartCommentBlock = isStartCommentBlock(pInputLine);
            boolean isEndCommentBlock = isEndCommentBlock(pInputLine);
            
            if (isStartCommentBlock) {
                isInCommentBlock = true;
            }
            if (isEndCommentBlock) {
                isInCommentBlock = false;
            }
            if (isCommentLine(pInputLine) || isStartCommentBlock || isEndCommentBlock || isInCommentBlock) {
                // do not process
            }
            else {
                //process
                for (int i = 0; i< pInputLine.length(); i++ ) {
                    String myChar = String.valueOf(pInputLine.charAt(i));

                    if (!isInCommentBlock && !isCollection) {
                        // ignore double-quotes but turn on collection mode and reset the String
                        if (myChar.equals(DOUBLE_QUOTE)) {
                        // look at the immediate next character - if another double quote then error
                            String myImmediateNextChar = String.valueOf(pInputLine.charAt(i+1));
                            if (myImmediateNextChar.equals(DOUBLE_QUOTE)) {
                                System.out.println("InputLine has illegal double quotes at Property position " + myPositionCount);
                                System.out.println("InputLine= " + pInputLine);
                                throw new IllegalArgumentException("Illegal Double quotes");
                
                            }
                            // didn't find a next double quote, so now we are starting collection mode
                            else {
                                isCollection = true;
                                if (!myTempValue.trim().equals("")) {
                                    //LOG ERROR - we found double-quotes in the middle of a read
                                }
                                myTempValue = "";
                            }
                            
                        }
                        else if (myChar.equals(COMMA) && !isDoubleQuoteMode) {
                            myFinalValues.add(myTempValue);
                            myPositionCount = myPositionCount+1;
                            myTempValue = "";
                        }
                        // normal read
                        else {
                            myTempValue = myTempValue + myChar;
                        }
                    }
                    else if (!isInCommentBlock && isCollection) {
                        // double quote says turn off collection as long as not double double quote                    	
                        if (myChar.equals(DOUBLE_QUOTE) && pInputLine.length()>i+1) {
                        // look at the immediate next character - if another double quote then add as " and we keep going
                            String myImmediateNextChar = String.valueOf(pInputLine.charAt(i+1));
                            if (myImmediateNextChar.equals(DOUBLE_QUOTE)) {
                                myTempValue = myTempValue + "\"";
                                i = i+1;
                            }
                            // looking for the next comma now since we didn't find an immediate next double quote
                            else {
                                isCollection = false;
                                myFinalValues.add(myTempValue);
                                myPositionCount = myPositionCount + 1;
                                myTempValue = "";
                                // ending a collection means look for next comma so ignore anything in between
                                for (int j = i+1; j <pInputLine.length(); j++) {
                                    String myNextChar = String.valueOf(pInputLine.charAt(j));
                                    if (myNextChar.equals(COMMA)) {
                                        i = j;
                                        break;
                                    }
                                }                        
                            }

                        }
                        // normal read
                        else {
                            myTempValue = myTempValue + myChar;
                        }
                    }
                }
                // we read all the chars - if the tempValue != null it means we did not see an end comma
                // so we want to add the current temp value to the final list
                if (!isInCommentBlock && myTempValue !=null && !myTempValue.equals("")) {
                    myFinalValues.add(myTempValue);
                }
                // Now remove the "<none>" values since those are meant to be "null"
                Iterator myIt = myFinalValues.iterator();
                while (myIt.hasNext()) {
                    String myValue = (String)myIt.next();
                    if (!myValue.equals(NONE)) {
                        myReturnValues.add(myValue);
                    }
                }

            }
            
        }       
        return myReturnValues;
    }
}

