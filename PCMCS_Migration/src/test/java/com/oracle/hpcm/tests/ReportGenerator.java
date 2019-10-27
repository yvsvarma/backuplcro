package com.oracle.hpcm.tests;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;

public class ReportGenerator {
	public static String getHtml(){
		ArrayList<Report> reports = Reporter.get();
		StringBuilder buf = new StringBuilder();
		buf.append("<html>" +
		//http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css
		"<link href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\">"
		+"<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>"
		+"<script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>"
		+"<script>$(document).ready(function(){    $('.hidden-text').hide();    $('.display').click(function(){  var text = $(this).next().html();    $('#model-data').html(text);        });});</script>"+          
		"<body>" +
		           "<div class=\"container\">"+
		  "<h2>Report</h2>"+
		  
		  
		           "<table class=\"table-bordered\">" +
		           "<tr>" +
		           "<th>Name</th>" +
		           "<th>Expected Artifact</th>" +
		           "<th>Actual Artifact</th>" +
		           "<th>Status</th>" +
		           "<th>Artifact Name</th>" +
		           "<th>Message</th>" +
		           "</tr>");
		for (Report r : reports) {
			HashMap<String,String> map = r.getComparisons();
			int fails = map.keySet().size();
			String state = null;
			if(map.keySet().size()==0){
				state = "PASS";
			}else{
				state = "FAIL";
			}
		    buf.append("<tr><td rowspan=\""+(fails+1)+"\">")
		       .append(r.getName())
		       .append("</td><td rowspan=\""+(fails+1)+"\">")
		       .append(r.getExpFileName())
		       .append("</td><td rowspan=\""+(fails+1)+"\">")
		       .append(r.getActFileName())
		       .append("</td><td rowspan=\""+(fails+1)+"\">")
		       .append(state)
		       .append("</td>");
		    if(fails == 0){
		    	buf.append("<td></td><td></td>");
		    }
		    else{
		    	for(String key : map.keySet()){
		    		buf.append("<tr><td>")
		    		.append(key)
		    		.append("</td>")
		    		.append("<td><button type='button' class='display btn btn-info btn-lg' data-toggle='modal' data-target='#myModal'>Click for Details.</button>")
		    		.append("<p class='hidden-text'>")
		    		.append(StringEscapeUtils.escapeXml((String)map.get(key)))
		    		.append("</p></td></tr>");
		    	}
		    }
		    buf.append("</tr>");
		}
		buf.append("</table>"+
  "<div class='modal fade' id='myModal' role='dialog'>"+
  "  <div class='modal-dialog'>"+
      "<div class='modal-content'>"+
        "<div class='modal-header'>"+
          "<button type='button' class='close' data-dismiss='modal'>&times;</button>"+
          "<h4 class='modal-title'>Modal Header</h4>"+
        "</div>"+
        "<div class='modal-body'>"+
          "<p id='model-data'></p>"+
        "</div>"+
        "<div class='modal-footer'>"+
          "<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"+
        "</div></div></div></div></div>" +
		           "</body>" +
		           "</html>");
		String html = buf.toString();
		return html;
	}
}
