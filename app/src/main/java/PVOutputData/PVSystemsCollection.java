package solarntmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by royga on 5/11/2016.
 */
public class PVSystemsCollection {

    private PVAccountSettings mySettings;
    private PVSystem mySystem;
    private HashMap pvSystems;

    public PVSystemsCollection(PVAccountSettings mySettings) {
        this.mySettings = mySettings;
        pvSystems = new HashMap();
    }

    public String getMySystemName() {
        String mySystemName = "";

        String url = "http://pvoutput.org/service/r2/getsystem.jsp";
        url = url.concat("?sid=").concat(String.valueOf(mySettings.getSystemID()));
        url = url.concat("&key=").concat(mySettings.getKey());

        try {
            URL pvURL = new URL(url);
            URLConnection urlConnection = pvURL.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(pvURL.openStream()));

            String inputLine;
            inputLine = bufferedReader.readLine();
            String[] fields = inputLine.split(",");
            mySystemName = fields[0];

            bufferedReader.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mySystemName;
    }

    public HashMap getNearbySystemsWithLatestOutputs(int distance) {
        String mySystemName = getMySystemName();
        String url = "http://pvoutput.org/service/r2/search.jsp";
        url = url.concat("?sid=").concat(String.valueOf(mySettings.getSystemID()));
        url = url.concat("&key=").concat(mySettings.getKey());
        url = url.concat("&q=").concat(String.valueOf(mySettings.getPostCode())).concat("%20").concat(String.valueOf(distance)).concat("km");
        
        try {
            URL pvURL = new URL(url);
            URLConnection urlConnection = pvURL.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(pvURL.openStream()));

            String inputLine;
            pvSystems.clear();
            while ((inputLine = bufferedReader.readLine()).trim().length() > 0) {
                PVSystem currentSystem = new PVSystem(inputLine, mySettings);
                if (currentSystem.getName().compareToIgnoreCase(mySystemName) == 0) {
                    mySystem = currentSystem;
                    mySystem.retrieveDailyData(mySettings);
                    mySystem.retrieveMonthlyData(mySettings);
                    mySystem.retriveYearlyData(mySettings);
                } else {
                    if (currentSystem.getLastOutput().compareToIgnoreCase("Today") == 0) {
                        currentSystem.retrieveDailyData(mySettings);
                        currentSystem.retrieveMonthlyData(mySettings);
                        currentSystem.retriveYearlyData(mySettings);
                        pvSystems.put(currentSystem.getName(), currentSystem);
                    }
                }
            }
            bufferedReader.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pvSystems;
    }

    public String generateMyDailyData() {
        String html = "<html>\n" +
"\n" +
"<head>\n" +
"	<!--Load the AJAX API-->\n" +
"	<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
"	<script type=\"text/javascript\">\n" +
"		// Load the Visualization API and the corechart package.\n" +
"		google.charts.load('current', {\n" +
"			'packages': ['corechart']\n" +
"		});\n" +
"		// Set a callback to run when the Google Visualization API is loaded.\n" +
"		google.charts.setOnLoadCallback(drawChart);\n" +
"		// Callback that creates and populates a data table,\n" +
"		// instantiates the pie chart, passes in the data and\n" +
"		// draws it.\n" +
"		function drawChart() {\n" +
"			// Create the data table.\n" +
"			var data = new google.visualization.DataTable();\n" +
"			data.addColumn('string', 'Date');\n" +
"			data.addColumn('number', 'Power Generated');\n" +
"			data.addColumn('number', 'Efficiency');\n" +
"			data.addRows([";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        float power, efficiency;
        String year, month, day;
        String [] data = new String[30];
        cal.setTime(date);
        cal.add(Calendar.DATE, -30);
        for(int i = 0; i < 30; i++){
            cal.add(Calendar.DATE, 1);
            year = String.valueOf(cal.get(Calendar.YEAR));
            month = String.valueOf(cal.get(Calendar.MONTH)+1);
            if(month.length()<2)month="0".concat(month);
            day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            if(day.length()<2)day="0".concat(day);
            if(mySystem.getDailyData().get(year.concat(month).concat(day))==null){
                power = 0;
                efficiency = 0;
            }else{
                power = ((PVDailyData) mySystem.getDailyData().get(year.concat(month).concat(day))).getEnergyGenerated()/1000;
                efficiency=((PVDailyData) mySystem.getDailyData().get(year.concat(month).concat(day))).getEfficiency();
            }
            data[i] = "['".concat(day).concat("-").concat(month).concat("',").concat(String.valueOf(power)).concat(",").concat(String.valueOf(efficiency)).concat("]");
        }
        html = html.concat(String.join(",", data));
//          .concat("")
        html = html.concat("]);\n" +
"			// Set chart options\n" +
"			var options = {\n" +
"				'title': 'Power Generated for the past 30 days'\n" +
"				, vAxis: {\n" +
"					title: 'Power Generated'\n" +
"				}\n" +
"				, hAxis: {\n" +
"					title: 'Date'\n" +
"				}\n" +
"				, seriesType: 'bars'\n" +
"				, series: {\n" +
"					1: {\n" +
"						type: 'line'\n" +
"					}\n" +
"					, 'width': 1000\n" +
"					, 'height': 600\n" +
"					, 'bar': {\n" +
"						'groupWidth': \"95%\"\n" +
"					}\n" +
"				}\n" +
"			};\n" +
"			// Instantiate and draw our chart, passing in some options.\n" +
"			var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));\n" +
"			chart.draw(data, options);\n" +
"		}\n" +
"	</script>\n" +
"</head>\n" +
"\n" +
"<body>\n" +
"	<!--Div that will hold the pie chart-->\n" +
"	<div id=\"chart_div\"></div>\n" +
"</body>\n" +
"\n" +
"</html>");
        return html;
    }

    public PVSystem getMySystem() {
        return mySystem;
    }
    
    
}
