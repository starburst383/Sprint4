package Sprint3;
/**
 * Created by Duan on 4/23/2017.
 */
////EXACTLY THE SAME AS CHRONOTIMER, JUST DOESN'T HAVE PRINT CALLS TO GUI!
import java.time.Clock;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class ChronoTimerFORTESTSONLY {
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

    public ChronoTimerFORTESTSONLY() {

        ChronoTimerFORTESTSONLY.power = false;
        ChronoTimerFORTESTSONLY.run = false;
        ChronoTimerFORTESTSONLY.event = "";
        out = false;
        ChronoTimerFORTESTSONLY.channels = new boolean[MAX_CHANNELS];
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

    //static void num() {
    //    totRacers++;
    //    racers.add(new Racer(Integer.parseInt(splitted[1]), totRacers));
    //System.out.println("Racer " + splitted[1] + " has been added");
    //}

    static void num(int racernum) {
        totRacers++;
        racers.add(new Racer(racernum, totRacers));

        //GUI.stdoutArea.appendText("Racer " + racernum + " added\n");
    }

    static void event(String input) {
        if (power) {
            if (event.isEmpty()) {
                if (input.equalsIgnoreCase("IND")) {
                    event = input;

                   //GUI.stdoutArea.appendText("Individual Race Selected\n");
                   // GUI.stdoutArea.appendText("Press * to start a new run\n");
                }

                else if (input.equalsIgnoreCase("PARIND")) {
                    event = input;

                   // GUI.stdoutArea.appendText("Parallel Individual Race Selected\n");
                   // GUI.stdoutArea.appendText("Press * to start a new run\n");
                }

                else if (input.equalsIgnoreCase("GRP")) {
                    event = input;

                   // GUI.stdoutArea.appendText("Group Race Selected\n");
                   // GUI.stdoutArea.appendText("Press * to start a new run\n");
                }

                else if  (input.equalsIgnoreCase("PARGRP")) {
                    event = input;

                   // GUI.stdoutArea.appendText("Parallel Group Race Selected\n");
                   // GUI.stdoutArea.appendText("Press * to start a new run\n");
                }

                else {
                   // GUI.stdoutArea.appendText("That is not a valid event type\n");
                }
            } else {
                //GUI.stdoutArea.appendText("There is already an event. Please reset before starting a new event\n");
            }
        } else {
           // GUI.stdoutArea.appendText("Must turn on power");
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
        reset();

        if (power == true) {

           // GUI.stdoutArea.appendText("The power is on\n");
           // GUI.stdoutArea.appendText("Select an event\n");
           // GUI.stdoutArea.appendText("   1. IND\n");
          //  GUI.stdoutArea.appendText("   2. PARIND\n");
          //  GUI.stdoutArea.appendText("   3. GRP\n");
           // GUI.stdoutArea.appendText("   4. PARGRP\n");
        }

        else {
           // GUI.stdoutArea.appendText("The power is off/n");
        }
    }

    static void newRun() {
        run = true;
        runCounter = runCounter + 1;

        //GUI.stdoutArea.appendText("New run started\n");
        //GUI.stdoutArea.appendText("Add racer numbers,\n");
       // GUI.stdoutArea.appendText("press # to enter:\n");
    }

    static void endrun() {
        Converter.ConvertTo(completed, runCounter);
        run = false;
        parun = false;
        racers.clear();
        toFinish.clear();
        completed.clear();
        totRacers = 0;
        GRP = null;
        GRPC = 0;
       // GUI.stdoutArea.appendText("run ended\n");
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
    }

    static void start() {
        // check to make sure 0 < racers.size()
        if (!racers.isEmpty()) {
            // if it is a grp event, all racers will have same start time, and
            // race will begin on start.
            if (event.equalsIgnoreCase("GRP")) {
                InitGRP();
               // GUI.stdoutArea.appendText("Racers are off!\n");
            }
            else if(event.equalsIgnoreCase("PARGRP")) {
                InitGRP();
               // GUI.stdoutArea.appendText("Racers are off!\n");
            }

            else {
                // look at first racer's start channel and let trigChannel doth rest
                trigChannel(racers.getFirst().index * 2 - 1);
            }
        } else {
           // GUI.stdoutArea.appendText("No racers to start\n");
        }
    }

    static void finish() {

        if (!toFinish.isEmpty()) {
            trigChannel(toFinish.getFirst().index * 2);
        } else {
            //GUI.stdoutArea.appendText("No racers to finish\n");
        }
    }

    static void togChannel(int channel) {

        if (event.equalsIgnoreCase("GRP")) {
            //GUI.stdoutArea.appendText("Toggles disabled for group event\n");
            return;
        }

        else if(event.equalsIgnoreCase("PARGRP")){
            //GUI.stdoutArea.appendText("Toggles disabled for group event\n");
            return;
        }

        if (!channels[channel]) {
            channels[channel] = true;
        } else {
            channels[channel] = false;
        }
    }

    static void trigChannel(int channel) {

        if (event.equalsIgnoreCase("GRP")) {
            if (channel == 1) {
                start();
            } else if (channel == 2) {
                if (totRacers == 0) {
                   // GUI.stdoutArea.appendText("Race not started\n");
                } else {
                   // GUI.stdoutArea.appendText("Triggered\n");
                    updGRP();
                }
            } else {
               // GUI.stdoutArea.appendText("Invalid channel for this race\n");
            }
            updScreen();
            return;
        }
        else if(event.equalsIgnoreCase("PARGRP")){
            if (channel == 1 && parun == false)
            {
                start();
                parun = true;
            }
            else
            {
                for(int i = 0; i < toFinish.size(); ++i)
                {
                    Racer temp = toFinish.get(i);
                    if(temp.index == channel)
                    {
                        temp.fin = time.millis();
                        completed.add(toFinish.remove(i));
                        channels[channel] = false;
                    }
                }
            }
            if(completed.size() == totRacers)
            {
                //GUI.stdoutArea.appendText(" all racers finished " + "\n");
            }
            updScreen();
            return;
        }

        else if (channel < MAX_CHANNELS && channel > 0) {
            // used for determining which list a racer is in
            boolean found = false;

            // list of elements to remove from list
            LinkedList<Racer> temp = new LinkedList<>();
            if (event.equalsIgnoreCase("IND")) {
                // determine if start or finish channel
                // if start channel:
                if (channel % 2 == 1) {
                    // if channel toggled on
                    if (channels[channel] == true) {
                        // first, check if racer already started. If so, update
                        // his start time
                        if (!toFinish.isEmpty()) {
                            for (Racer s : toFinish) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    s.start = time.millis();
                                    found = true;
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
                            }
                            racers.removeAll(temp);
                        }
                        // if no racer found attached to channel
                        if (!found) {
                         //   GUI.stdoutArea.appendText("No racer linked to that channel\n");
                        }
                    } else {
                       // GUI.stdoutArea.appendText("Channel was not toggled\n");
                    }
                }
                // if finish channel
                if (channel % 2 == 0) {
                    if (channels[channel] == true) {
                        // First, check if racer has already finished. If so,
                        // update his finish time
                        // If not, finish him
                        if (!completed.isEmpty()) {
                            for (Racer s : completed) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    s.fin = time.millis();
                                    found = true;
                                }
                            }
                        }
                        if (!toFinish.isEmpty() && !found) {
                            for (Racer s : toFinish) {
                                if (s.index == Math.ceil((double) channel / 2)) {
                                    temp.add(s);
                                    s.fin = time.millis();
                                    completed.add(s);
                                    found = true;
                                }
                            }
                            toFinish.removeAll(temp);
                        }
                        // if no racer found attached to channel
                        if (!found) {
                           // GUI.stdoutArea.appendText("No racer linked to that channel\n");
                        }
                    }

                    else {
                       // GUI.stdoutArea.appendText("Channel was not toggled\n");
                    }
                }
            }

            else if (event.equalsIgnoreCase("PARIND")) {
                // determine if start or finish channel
                // if start channel:

                if (channel % 2 == 1) {
                    // if channel toggled on
                    // first, check if racer already started. If so, update
                    // his start time
                    if (!toFinish.isEmpty()) {
                        for (Racer s : toFinish) {
                            if (s.index == Math.ceil((double) channel / 2)) {
                                s.start = time.millis();
                                found = true;
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
                        }
                        racers.removeAll(temp);
                    }
                    // if no racer found attached to channel
                    if (!found) {
                       // GUI.stdoutArea.appendText("No racer linked to that channel\n");
                    }
                }
                // if finish channel
                if (channel % 2 == 0) {
                    // First, check if racer has already finished. If so,
                    // update his finish time
                    // If not, finish him
                    if (!completed.isEmpty()) {

                        for (Racer s : completed) {
                            if (s.index == Math.ceil((double) channel / 2)) {
                                s.fin = time.millis();
                                found = true;
                            }
                        }
                    }
                    if (!toFinish.isEmpty() && !found) {
                        for (Racer s : toFinish) {
                            if (s.index == Math.ceil((double) channel / 2)) {
                                temp.add(s);
                                s.fin = time.millis();
                                completed.add(s);
                                found = true;
                            }
                        }
                        toFinish.removeAll(temp);
                    }
                    // if no racer found attached to channel
                    if (!found) {
                        //GUI.stdoutArea.appendText("No racer linked to that channel\n");
                    }
                }
            }
            updScreen();
        }

        else {
            //GUI.stdoutArea.appendText("Invalid channel number\n");
        }
    }

    static void printlists() {

       // GUI.stdoutArea.appendText("racers: \n");

        for (Racer s : racers) {

           // GUI.stdoutArea.appendText(String.valueOf("Racer " + s.racerNum +  " time: " + stopWatch.formatTime(s.fin - s.start) + "\n"));
        }

        //GUI.stdoutArea.appendText("toFinish: \n");

        for (Racer s : toFinish) {

           // GUI.stdoutArea.appendText(String.valueOf("Racer " + s.racerNum +  " time: " + stopWatch.formatTime(s.fin - s.start) + "\n"));
        }

        //GUI.stdoutArea.appendText("completed:  \n");

        for (Racer s : completed) {

            //GUI.stdoutArea.appendText(String.valueOf("Racer " + s.racerNum +  " time: " + stopWatch.formatTime(s.fin - s.start) + "\n"));
        }
    }
    // initialize GRP array and values when start is called.
    static void InitGRP() {
        if (event.equalsIgnoreCase("GRP")) {
            // turn on event channels
            channels[1] = true;
            channels[2] = true;
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
            for(int i =  0; i <= totRacers; i++){
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

            GUI.stdoutArea.appendText("Enter racer order\n");
            // get racer order
            out = true;
            GRPF = new String[GRPC];
            GRPC2 = 0;
        }
    }
    static void updGRPF(String a) {
        GRPF[GRPC2] = a;
      //  GUI.stdoutArea.appendText(" place " + String.valueOf(GRPC2) + "\n");
        ++GRPC2;
        if(GRPC == GRPC2) {
            endGRP(GRPF);
            out = false;
        }
    }

    static void endGRP(String[] nums) {
        for (int i = 0; (i < nums.length) && (i < totRacers); ++i) {
            ListIterator<Racer> we = racers.listIterator();
            while (we.hasNext()) {
                Racer temp = we.next();
                int n = Integer.parseInt(nums[i]);
                if (temp.racerNum == n) {
                    temp.fin = GRP[i][1];
                }
            }
        }
        // add racers to completed
        while (!racers.isEmpty())
        {
            completed.add(toFinish.pop());
        }
        updScreen();
    }

    static void updScreen() {
      //  GUI.resultArea.clear();
      //  GUI.resultArea.appendText("To race:\n");
        for (int i = 0; i < racers.size(); ++i) {
         //   GUI.resultArea.appendText(String.valueOf(racers.get(i).racerNum) + "\n");
        }

      //  GUI.resultArea.appendText("Racing:\n");
        for (int i = 0; i < toFinish.size(); ++i) {
           // GUI.resultArea.appendText(String.valueOf(toFinish.get(i).racerNum) + "\n");
        }

       // GUI.resultArea.appendText("Completed:\n");
        for (int i = 0; i < completed.size(); ++i) {
           // GUI.resultArea.appendText(String.valueOf(completed.get(i).racerNum)
                   // + "  " + stopWatch.formatTime(completed.get(i).fin - completed.get(i).start) + "\n");
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
}