
/**
 * Created by Duan on 4/23/2017.
 */
import java.util.*;

import com.google.gson.Gson;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;

public class ChronoTimer {
    // system
    static final int MAX_CHANNELS = 8;
    static boolean power;
    static boolean run;
    static boolean parun;
    static String event = "";
    static boolean out;
    static Scanner console = new Scanner(System.in);
    static boolean[] channels = new boolean[MAX_CHANNELS];
    // race
    static int totRacers;
    // static Queue<Racer> racers = new LinkedList<Racer>();
    static LinkedList<Racer> racers = new LinkedList<Racer>();
    static LinkedList<Racer> toFinish = new LinkedList<Racer>();
    static LinkedList<Racer> completed = new LinkedList<Racer>();
	static LinkedList<Racer> sorted = new LinkedList<Racer>();
	static LinkedList<Racer> fileRacers = new LinkedList<Racer>();
	static LinkedList<ResultRacer> output = new LinkedList<ResultRacer>();
    // array of race end times [racers][bib#,end time]
    static long GRP[][];
    // how many racers have finished
    static int GRPC;
    static int GRPC2;
    // the final order of the racers
    static String GRPF[];
    // time
    static Clock time = Clock.systemUTC();
    static LocalTime time2;
    static double start = time.millis();
    static Time stopWatch = new Time();
    static String input;
    static String[] splitted;

    static int runCounter = 0;
    
    static File actual; 
	static String sharedResponse = "";
	static Client s = new Client();
	static String css;
	static String html;

    public ChronoTimer() {

        ChronoTimer.power = false;
        ChronoTimer.run = false;
        ChronoTimer.event = "";
        out = false;
        ChronoTimer.channels = new boolean[MAX_CHANNELS];
    }
    
    public static void readFile() throws FileNotFoundException, IOException {
		actual = new File("src/racers.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(actual))) {
			String[] split;
			String line;
			while ((line = br.readLine()) != null) {
				split = line.split("\\s+");
				fileRacers.add(new Racer(Integer.parseInt(split[0]), split[1] + " " + split[2]));

			}
		}
		for (Racer rac : fileRacers) {
			rac.printFileRacers();
		}
	}

    static void receipt() {
        // Cycles through the completed linkedlist and prints out the racer's
        // number and time
        for (int i = 0; i < completed.size(); i++) {

            Racer temp = completed.get(i);
            System.out.println(
                    "\nRacer " + temp.racerNum + " time: " + stopWatch.formatTime((temp.fin - temp.start)) + "\n");
        }
    }

    static void num(int racernum) {
        if(event.equalsIgnoreCase("PARGRP")) {
            if(totRacers < 8) {
                totRacers++;
                racers.add(new Racer(racernum, totRacers));
                GUI.stdoutArea.appendText("Racer " + racernum + " added\n");
            }
            else {
                GUI.stdoutArea.appendText("Race full \n");
            }
        }
        else if(event.equalsIgnoreCase("GRP")){
            if(GRPC == 0){
                totRacers++;
                racers.add(new Racer(racernum, totRacers));
                GUI.stdoutArea.appendText("Racer " + racernum + " added\n");
            }
            else{
                GUI.stdoutArea.appendText("Can't add racers after event start \n");
            }
        }
        else if(event.equalsIgnoreCase("IND"))
        {
            totRacers++;
            int Rnum = totRacers % 4;
            if(Rnum == 0)
            {
                racers.add(new Racer(racernum, 4));
            }
            else {
                racers.add(new Racer(racernum, Rnum));
            }
            GUI.stdoutArea.appendText("Racer " + racernum + " added\n");
        }
        else {
            totRacers++;
            racers.add(new Racer(racernum, totRacers));
            GUI.stdoutArea.appendText("Racer " + racernum + " added\n");
        }
    }

    static void event(String input) {
        if (power) {
            if (event.isEmpty()) {
                if (input.equalsIgnoreCase("IND")) {
                    event = input;
                    GUI.stdoutArea.appendText("Individual Race Selected\n");
                    GUI.stdoutArea.appendText("Press * to start a new run\n");
                }
                else if (input.equalsIgnoreCase("PARIND")) {
                    event = input;
                    GUI.stdoutArea.appendText("Parallel Individual Race Selected\n");
                    GUI.stdoutArea.appendText("Toggles are fixed for PARIND \n");
                    GUI.stdoutArea.appendText("Press * to start a new run\n");

                    channels[0] = true;
                    channels[1] = true;
                    channels[2] = true;
                    channels[3] = true;
                }
                else if (input.equalsIgnoreCase("GRP")) {
                    event = input;
                    GUI.stdoutArea.appendText("Group Race Selected\n");
                    GUI.stdoutArea.appendText("Toggles are fixed for GRP \n");
                    GUI.stdoutArea.appendText("Press * to start a new run\n");

                    channels[0] = true;
                    channels[1] = true;
                }
                else if  (input.equalsIgnoreCase("PARGRP")) {
                    event = input;
                    GUI.stdoutArea.appendText("Parallel Group Race Selected\n");
                    GUI.stdoutArea.appendText("Toggles are fixed for PARGRP \n");
                    GUI.stdoutArea.appendText("Press * to start a new run\n");
                }
                else {
                    GUI.stdoutArea.appendText("That is not a valid event type\n");
                }
            }
            else {
                GUI.stdoutArea.appendText("There is already an event. Please reset before starting a new event\n");
            }
        }
        else {
            GUI.stdoutArea.appendText("Must turn on power");
        }
    }

    static void startTime() {
        start = time.millis();
    }

    static double finishTime() {
        return (time.millis() - start) / 100;
    }
    // sets the power to be its opposite value
    static void setPower() {
        power = !power;


        if (power == true) {

            GUI.stdoutArea.appendText("The power is on\n");
            GUI.stdoutArea.appendText("Select an event\n");
            GUI.stdoutArea.appendText("   1. IND\n");
            GUI.stdoutArea.appendText("   2. PARIND\n");
            GUI.stdoutArea.appendText("   3. GRP\n");
            GUI.stdoutArea.appendText("   4. PARGRP\n");
        }
        else {
            GUI.stdoutArea.appendText("The power is off/n");
            reset();
        }
    }

    static void newRun() {
        run = true;
        runCounter = runCounter + 1;

        GUI.stdoutArea.appendText("\nNew run started\n");
        GUI.stdoutArea.appendText("Add racer number one at a time,\n");
        GUI.stdoutArea.appendText("And press # to enter each racer:\n\n");
        if(event.equalsIgnoreCase("ind")) {
            GUI.stdoutArea.appendText("When ready, toggle racer lane and \nTrigger racer start to begin event\n\n");
        }
        else if(event.equalsIgnoreCase("parind")) {
            GUI.stdoutArea.appendText("When ready hit trigger 1 or 3 \nTo start first racer\n\n");
        }
        else if(event.equalsIgnoreCase("grp")) {
            GUI.stdoutArea.appendText("When ready hit trigger 1 to\nBegin race\n\n");
        }
        else if(event.equalsIgnoreCase("Pargrp")) {
            GUI.stdoutArea.appendText("When ready hit trigger 1 to\nBegin race\n");
        }

    }

	static void endrun() {
		Converter.ConvertTo(completed, runCounter);
		run = false;
		createOutput();
		create_print_html(output);
		for (ResultRacer e : output){
			s.add(e);
		}
		racers.clear();
		toFinish.clear();
		completed.clear();
		totRacers = 0;
		GRP = null;
		GRPC = 0;
		GUI.stdoutArea.appendText("run ended\n");
		System.out.println("Run ended \n");
	}

    static void reset() {
        for (int i = 0; i < channels.length; i++) {
            channels[i] = false;
        }

        run = false;
        parun = false;
        event = "";
        racers.clear();
        toFinish.clear();
        completed.clear();
        totRacers = 0;
        runCounter = 0;
        GRP = null;
        GRPC = 0;

        GUI.stdoutArea.clear();
        GUI.stdoutArea.appendText("Select an event\n");
        GUI.stdoutArea.appendText("   1. IND\n");
        GUI.stdoutArea.appendText("   2. PARIND\n");
        GUI.stdoutArea.appendText("   3. GRP\n");
        GUI.stdoutArea.appendText("   4. PARGRP\n\n");
    }

    static void start() {
        // check to make sure 0 < racers.size()
        if (!racers.isEmpty()) {
            // if it is a grp event, all racers will have same start time, and
            // race will begin on start.
            if (event.equalsIgnoreCase("GRP")) {
                InitGRP();
                GUI.stdoutArea.appendText("Racers are off!\n\n");
            }
            else if(event.equalsIgnoreCase("PARGRP")) {
                InitGRP();
                GUI.stdoutArea.appendText("Racers are off!\n\n");
            }
            else {
                // look at first racer's start channel and let trigChannel doth rest
                trigChannel(racers.getFirst().index * 2 - 1);
            }
        }
        else {
            GUI.stdoutArea.appendText("No racers to start\n\n");
        }
    }

    static void finish() {
        if (!toFinish.isEmpty()) {
            trigChannel(toFinish.getFirst().index * 2);
        }
        else {
            GUI.stdoutArea.appendText("No racers to finish\n\n");
        }
    }

    static void togChannel(int channel) {
        if (event.equalsIgnoreCase("GRP")) {
            GUI.stdoutArea.appendText("Toggles disabled for group event\n\n");
            return;
        }
        else if(event.equalsIgnoreCase("PARGRP")){
            GUI.stdoutArea.appendText("Toggles disabled for group event\n\n");
            return;
        }
        else if(event.equalsIgnoreCase("PARIND")){
            GUI.stdoutArea.appendText("Toggles disabled for group event\n\n");
            return;
        }

        if (!channels[channel]) {
            channels[channel] = true;
        }
        else {
            channels[channel] = false;
        }
    }

    static void trigChannel(int channel) {
        if (event.equalsIgnoreCase("GRP")) {
            if (channel == 1) {
                start();
            }
            else if (channel == 2) {
                if (totRacers == 0) {
                    GUI.stdoutArea.appendText("Race not started\n\n");
                }
                else {
                    GUI.stdoutArea.appendText((GRPC+1) + " racers finished \n");
                    updGRP();
                }
            }
            else {
                GUI.stdoutArea.appendText("Invalid channel for this race\n\n");
            }
            updScreen();
            return;
        }
        else if(event.equalsIgnoreCase("PARGRP")){
            if (channel == 1 && parun == false) {
                start();
                parun = true;
            }
            else {
                for(int i = 0; i < toFinish.size(); ++i) {
                    Racer temp = toFinish.get(i);
                    if(temp.index == channel) {
                        temp.fin = time.millis();
                        completed.add(toFinish.remove(i));
                        channels[channel-1] = false;
                    }
                }
            }

            if(completed.size() == totRacers) {
                GUI.stdoutArea.appendText(" all racers finished " + "\n\n");
            }
            updScreen();
            return;
        }
        else if (channel <= MAX_CHANNELS && channel > 0) {
            // used for determining which list a racer is in
            boolean found = false;
            // list of elements to remove from list
            LinkedList<Racer> temp = new LinkedList<>();
            if (event.equalsIgnoreCase("IND")) {
                // determine if start or finish channel
                // if start channel:
                if (channel % 2 == 1) {
                    // if channel toggled on
                    if (channels[channel-1] == true) {
                        // first, check if racer already started. If so, update
                        // his start time
                        if (!toFinish.isEmpty()) {
                            for (Racer s : toFinish) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    s.start = time.millis();
                                    found = true;
                                }
                                if(found)
                                {
                                    break;
                                }
                            }
                        }
                        // If not, start the racer
                        if (!racers.isEmpty() && !found) {
                            for (Racer s : racers) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    temp.add(s);
                                    s.start = time.millis();
                                    toFinish.add(s);
                                    found = true;
                                }
                                if(found)
                                {
                                    break;
                                }
                            }
                            racers.removeAll(temp);
                        }
                        // if no racer found attached to channel
                        if (!found) {
                            GUI.stdoutArea.appendText("No racer linked to that channel\n\n");
                        }
                    }
                    else {
                        GUI.stdoutArea.appendText("Channel was not toggled\n\n");
                    }
                }
                // if finish channel
                if (channel % 2 == 0) {
                    if (channels[channel-1] == true) {
                        // First, check if racer has already finished. If so,
                        // update his finish time
                        // If not, finish him
                        if (!toFinish.isEmpty() && !found) {
                            for (Racer s : toFinish) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    temp.add(s);
                                    s.fin = time.millis();
                                    completed.add(s);
                                    found = true;
                                }
                                if(found)
                                {
                                    break;
                                }
                            }
                            toFinish.removeAll(temp);
                        }
                        if (!completed.isEmpty()) {
                            for (Racer s : completed) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    s.fin = time.millis();
                                    found = true;
                                }
                                if(found)
                                {
                                    break;
                                }
                            }
                        }

                        // if no racer found attached to channel
                        if (!found) {
                            GUI.stdoutArea.appendText("No racer linked to that channel\n\n");
                        }
                    }
                    else {
                        GUI.stdoutArea.appendText("Channel was not toggled\n\n");
                    }
                }
            }
            else if (event.equalsIgnoreCase("PARIND")) {
                // determine if start or finish channel
                // if start channel:
                if(channel == 1 || channel == 3) {
                    if(!racers.isEmpty()) {
                        Racer next = racers.remove();
                        next.start = time.millis();
                        toFinish.add(next);
                    }
                    else {
                        GUI.stdoutArea.appendText("no racer to start \n\n");
                    }

                }
                else if(channel == 2 || channel == 4) {
                    if(!toFinish.isEmpty()) {
                        Racer next = toFinish.remove();
                        next.fin = time.millis();
                        completed.add(next);
                    }
                    else {
                        GUI.stdoutArea.appendText("no racer to finish\nSelect Printer to get result\n\n");
                    }
                }
                else {
                    GUI.stdoutArea.appendText("invalid trigger number for this race \n\n");
                }
            }
            updScreen();
        }
        else {
            GUI.stdoutArea.appendText("Invalid channel number\n\n");
        }
    }

    static void swap(){
        if (event.equalsIgnoreCase("IND")) {
            if (toFinish.size() >= 2) {
                GUI.stdoutArea.appendText("Swap completed \n\n");
                int tmp = toFinish.get(0).index;
                toFinish.get(0).index = toFinish.get(1).index;
                toFinish.get(1).index = tmp;

                Racer temp = toFinish.removeFirst();
                toFinish.add(1, temp);
            }
            else {
                GUI.stdoutArea.appendText("Not enough racers to swap\n\n");
            }
        }
        else {
            GUI.stdoutArea.appendText("Can only use swap in event IND\n\n");
        }
    }



    static void printlists() {
        GUI.stdoutArea.appendText("Have Not Raced: \n");
        for (Racer s : racers) {
            GUI.stdoutArea.appendText(String.valueOf("Racer " + s.racerNum +  " time: " + stopWatch.formatTime(s.fin - s.start) + "\n"));
        }
        GUI.stdoutArea.appendText("Racing: \n");
        for (Racer s : toFinish) {
            GUI.stdoutArea.appendText(String.valueOf("Racer " + s.racerNum +  " time: " + stopWatch.formatTime(s.fin - s.start) + "\n"));
        }
        GUI.stdoutArea.appendText("Finished:  \n");
        for (Racer s : completed) {
            GUI.stdoutArea.appendText(String.valueOf("Racer " + s.racerNum +  " time: " + stopWatch.formatTime(s.fin - s.start) + "\n"));
        }
    }
    // initialize GRP array and values when start is called.
    static void InitGRP() {
        if (event.equalsIgnoreCase("GRP")) {
            // turn on event channel
            // set up end time array GRP[][]
            long initial = time.millis();
            int current = racers.size();
            GRP = new long[current][];
            // initilize GRP so it can be set
            for (int i = 0; i < current; ++i) {
                GRP[i] = new long[2];
            }
            // set GRP start values
            for (int i = 0; i < current; ++i) {
                Racer temp = racers.get(i);
                temp.start = initial;
                GRP[i][0] = temp.racerNum;
                GRP[i][1] = initial;
            }
        }

        else if(event.equalsIgnoreCase("PARGRP")){
            for(int i =  0; i < totRacers; i++){
                channels[i] = true;
            }
            parun = false;
            long initial = time.millis();

            for (int i = 0; i < totRacers; ++i) {
                Racer temp = racers.get(i);
                temp.start = initial;
                temp.index = i+1;
            }
        }

        while (!racers.isEmpty()) {
            toFinish.add(racers.pop());
        }
        updScreen();
    }
    // update the array after each trig 2 call
    static void updGRP() {

        GRP[GRPC][1] = time.millis();

        ++GRPC;
        // check if all racer finished
        if (GRPC == totRacers) {

            GUI.stdoutArea.appendText("All racers have finished!\nEnter their race order, one racer at a time,\n then hit # to submit each racer\n");
            // get racer order
            out = true;
            GRPF = new String[GRPC];
            GRPC2 = 0;
        }
    }
    static void updGRPF(String a) {
        GRPF[GRPC2] = a;
        GUI.stdoutArea.appendText(" place " + String.valueOf(GRPC2 + 1) + "\n");
        ++GRPC2;
        if(GRPC == GRPC2) {
            endGRP(GRPF);
            out = false;
        }
    }

    static void endGRP(String[] nums) {
        for (int i = 0; (i < nums.length) && (i < totRacers); ++i) {
            ListIterator<Racer> we = toFinish.listIterator();
            while (we.hasNext()) {
                Racer temp = we.next();
                int n = Integer.parseInt(nums[i]);
                if (temp.racerNum == n) {
                    temp.fin = GRP[i][1];
                }
            }
        }
        // add racers to completed
        while (!toFinish.isEmpty()) {
            completed.add(toFinish.pop());
        }
        updScreen();
    }

    static void updScreen() {
        GUI.resultArea.clear();
        GUI.resultArea.appendText("RACER QUEUE:\n");
        for (int i = 0; i < racers.size(); ++i) {
            GUI.resultArea.appendText("racer: " + String.valueOf(racers.get(i).racerNum) + "\n");
        }

        GUI.resultArea.appendText("RACING:\n");
        for (int i = 0; i < toFinish.size(); ++i) {
            GUI.resultArea.appendText("racer: " +  String.valueOf(toFinish.get(i).racerNum) + "\n");
        }

        GUI.resultArea.appendText("FINISHED:\n");
        for (int i = 0; i < completed.size(); ++i) {
            GUI.resultArea.appendText("racer: " +  String.valueOf(completed.get(i).racerNum)
                    + "  " + stopWatch.formatTime(completed.get(i).fin - completed.get(i).start) + "\n");
        }
    }
    ////////////////// getters ///////////////////////

    // returns the current value/state of power
    static boolean getPower() {

        return power;
    }
    static boolean getRun() {

        return run;
    }
    static String getEvent() {

        return event;
    }
    
    public static void createOutput(){
		for (Racer e : completed) {
			e.time = e.fin - e.start;
		}

		Collections.sort(completed, new timeComparator());

		for (Racer rac : completed) {
			sorted.add(rac);
		}
		for (Racer rac : racers) {
			sorted.add(rac);
		}
		for (Racer rac : toFinish) {
			sorted.add(rac);
		}
		
		for (Racer rac : sorted){
			for (Racer temp : fileRacers){
				if (rac.racerNum == temp.racerNum){
					rac.name = temp.name;
				}
			}
		}
		
		int i = 1;
		for (Racer rac : sorted){
			output.add(new ResultRacer(i, rac.racerNum, rac.name, rac.time));
			i++;
		}
	}

	public static void create_print_html(LinkedList<ResultRacer> out) {
		String cssurl = "styles.css";
		String url = "index.html";
		Gson g = new Gson();

		// ArrayList<Racer> printThis = new ArrayList<>();
		

		String json = g.toJson(out);

		// create text of css file
		css = "tr:nth-of-type(odd) {background-color:#42f45f; } body {background-color: powderblue;}";

		// create text of html file
		html = "<html>   <head>   <title>Lab 8</title>   <link rel=\"stylesheet\" href=\"" + cssurl
				+ "\"> <meta charset=\"UTF-8\">       <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js\"></script>       <title>title</title>   </head>   <body onLoad=\"buildHtmlTable('#excelDataTable')\">   <table id=\"excelDataTable\" border=\"1\">  <caption>Racers</caption> </table>  <script> var myList = "
				+ json
				+ ";  function buildHtmlTable(selector) {   var columns = addAllColumnHeaders(myList, selector);    for (var i = 0; i < myList.length; i++) {     var row$ = $('<tr/>');     for (var colIndex = 0; colIndex < columns.length; colIndex++) {       var cellValue = myList[i][columns[colIndex]];       if (cellValue == null) cellValue = \"\";       row$.append($('<td/>').html(cellValue));     }     $(selector).append(row$);   } }  function addAllColumnHeaders(myList, selector) {   var columnSet = [];   var headerTr$ = $('<tr/>');    for (var i = 0; i < myList.length; i++) {     var rowHash = myList[i];     for (var key in rowHash) {       if ($.inArray(key, columnSet) == -1) {         columnSet.push(key);         headerTr$.append($('<th/>').html(key));       }     }   }   $(selector).append(headerTr$);    return columnSet; }       </script> </body>  </html>";

		// create the css file

		File cssfile = new File(cssurl);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(cssfile));
			bw.write(css);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create the html file

		File f = new File(url);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(html);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// run the html file:

		try {
			Desktop.getDesktop().browse(f.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class timeComparator implements Comparator<Racer> {
	@Override
	public int compare(Racer a, Racer b) {
		return a.time < b.time ? -1 : a.time == b.time ? 0 : 1;
	}
}

