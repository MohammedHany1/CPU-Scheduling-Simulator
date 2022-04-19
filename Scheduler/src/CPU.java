import java.util.ArrayList;
import java.util.Scanner;
public class CPU {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        int processesNum;

        int choice;
        System.out.println("1- Scheduling by shortest job first\n2- Scheduling by priority\n" +
                "3- Scheduling by shortest remaining time first");
        choice = input.nextInt();

        System.out.print("Enter the number of processes: ");
        processesNum = input.nextInt();

        for (int i = 0; i < processesNum; i++){
            Process process = new Process();
            System.out.print("Enter process " + (i+1) + " name: ");
            process.processName = input.next();
            System.out.print("Enter process " + (i + 1) + " arrival time: ");
            process.processArrivalTime = input.nextInt();
            System.out.print("Enter process " + (i+1) + " burst time: ");
            process.processBurstTime = input.nextInt();
            if (choice == 2){
                System.out.print("Enter process " + (i+1) + " priority number: ");
                process.processPriorityNumber = input.nextInt();
            }
            processes.add(process);
            System.out.println("=========================\n");
        }

        Scheduler scheduler = new Scheduler(processes, processesNum);
        switch (choice){
            case 1:
                for (int i = 0; i < processesNum; i++) {
                    scheduler.shortestJobFirst();
                }
                System.out.println("Average waiting time = " + (Scheduler.totalWaitingTime/processesNum));
                System.out.println("Average Turnaround = " + (Scheduler.totalTurnAround/processesNum));
                break;
            case 2:
                for (int i = 0; i < processesNum; i++) {
                    scheduler.priority();
                }
                System.out.println("Average waiting time = " + (Scheduler.totalWaitingTime/processesNum));
                System.out.println("Average Turnaround = " + (Scheduler.totalTurnAround/processesNum));
                break;
            case 3:
                scheduler.shortestRemainingTimeFirst();
                break;
            default:
                System.out.println("Wrong choice");
                break;
        }
    }
}

class Process {
    String processName;
    int processArrivalTime;
    int processBurstTime;
    int processPriorityNumber;
    int processWaitedTime;

    public Process(){
        processName = "";
        processArrivalTime = 0;
        processBurstTime = 0;
        processPriorityNumber = 0;
        processWaitedTime = 0;
    }
}

class Scheduler {
    ArrayList<Process> processes;
    int processesNum;
    static float turnAround = 0;
    static float waitingTime = 0;
    static float totalTurnAround = 0;
    static float totalWaitingTime = 0;
    static int wait = 0;


    Scheduler(ArrayList<Process> processes, int processesNum){
        this.processes = processes;
        this.processesNum = processesNum;
    }

    public void shortestJobFirst(){
        int min = processes.get(0).processBurstTime;
        int index = 0;
        for (int i = 1; i < processes.size(); i++){
            if (min > processes.get(i).processBurstTime){
                min = processes.get(i).processBurstTime;
                index = i;
            }
            else if (min == processes.get(i).processBurstTime){
                if (processes.get(index).processArrivalTime > processes.get(i).processArrivalTime){
                    min = processes.get(i).processBurstTime;
                    index = i;
                }
            }
        }

        //starvation problem solution
        wait += totalTurnAround;
        if (wait >= 50){
            int minArrive = processes.get(0).processArrivalTime;
            index = 0;
            for (int i = 1; i < processes.size(); i++){
                if (minArrive >= processes.get(i).processArrivalTime){
                    minArrive = processes.get(i).processArrivalTime;
                    index = i;
                }
            }
        }
        wait = 0;

        System.out.println(processes.get(index).processName+ " Waited for " + waitingTime +
                " sec " + processes.get(index).processName + " started "
                + processes.get(index).processName
                + " Turnaround time = " + (processes.get(index).processBurstTime + waitingTime) + " sec");
        if (processes.size() != 1) {
            waitingTime += processes.get(index).processBurstTime;
            totalWaitingTime += waitingTime;
        }
        turnAround += processes.get(index).processBurstTime;
        totalTurnAround += turnAround;

        processes.remove(index);

    }

    public void priority(){
        int max = processes.get(0).processPriorityNumber;
        int index = 0;
        for (int i = 1; i < processes.size(); i++){
            if (max > processes.get(i).processPriorityNumber){
                max = processes.get(i).processPriorityNumber;
                index = i;
            }
            else if (max == processes.get(i).processPriorityNumber){
                if (processes.get(index).processBurstTime > processes.get(i).processBurstTime){
                    max = processes.get(i).processPriorityNumber;
                    index = i;
                }
            }
        }

        System.out.println(processes.get(index).processName+ " Waited for " + waitingTime +
                " sec " + processes.get(index).processName + " started "
                + processes.get(index).processName + " Turnaround time = "
                + (processes.get(index).processBurstTime + waitingTime) + " sec");
        if (processes.size() != 1) {
            waitingTime += processes.get(index).processBurstTime;
            totalWaitingTime += waitingTime;
        }
        turnAround += processes.get(index).processBurstTime;
        totalTurnAround += turnAround;

        //starvation problem solution
        int minArrive = processes.get(0).processArrivalTime;
        int indexArrival = 0;
        for (int i = 1; i < processes.size(); i++){
            if (minArrive >= processes.get(i).processArrivalTime){
                minArrive = processes.get(i).processArrivalTime;
                indexArrival = i;
            }
        }
        processes.get(indexArrival).processPriorityNumber++;
        processes.remove(index);
    }

    public ArrayList<Process> sortByArriveTime(ArrayList<Process> processes){
        ArrayList<Process> sortedProcesses = new ArrayList<>();
        while (processes.size() > 0){
            int shortest = processes.get(0).processArrivalTime;
            int index = 0;
            for(int i = 0; i < processes.size(); i++){
                if(processes.get(i).processArrivalTime < shortest){
                    shortest = processes.get(i).processArrivalTime;
                    index = i;
                }
                else if(processes.get(i).processArrivalTime == shortest){
                    if(processes.get(i).processBurstTime < processes.get(index).processBurstTime){
                        shortest = processes.get(i).processArrivalTime;
                        index = i;
                    }
                }
            }
            sortedProcesses.add(processes.get(index));
            processes.remove(index);
        }
        return sortedProcesses;
    }

    public ArrayList<Process> sortByBurstTime(ArrayList<Process> processes){
        ArrayList<Process> sortedProcesses = new ArrayList<>();
        while (processes.size() > 0){
            int shortest = processes.get(0).processBurstTime;
            int index = 0;
            for(int i = 0; i < processes.size(); i++){
                if(processes.get(i).processBurstTime < shortest){
                    shortest = processes.get(i).processBurstTime;
                    index = i;
                }
                else if(processes.get(i).processBurstTime == shortest){
                    if(processes.get(i).processArrivalTime < processes.get(index).processArrivalTime){
                        shortest = processes.get(i).processBurstTime;
                        index = i;
                    }
                }
            }
            sortedProcesses.add(processes.get(index));
            processes.remove(index);
        }
        return sortedProcesses;
    }

    public ArrayList<Process> sortByWaitedTime(ArrayList<Process> processes){
        ArrayList<Process> sortedProcesses = new ArrayList<>();
        while (processes.size() > 0){
            int shortest = processes.get(0).processWaitedTime;
            int index = 0;
            for(int i = 0; i < processes.size(); i++){
                if(processes.get(i).processWaitedTime < shortest){
                    shortest = processes.get(i).processWaitedTime;
                    index = i;
                }
                else if(processes.get(i).processWaitedTime == shortest){
                    if(processes.get(i).processArrivalTime > processes.get(index).processArrivalTime){
                        shortest = processes.get(i).processWaitedTime;
                        index = i;
                    }
                }
            }
            sortedProcesses.add(processes.get(index));
            processes.remove(index);
        }
        return sortedProcesses;
    }

    public void shortestRemainingTimeFirst() {
        ArrayList<Integer> waitedTime = new ArrayList<>();
        ArrayList<Integer> turnaroundTime = new ArrayList<>();
        int totalBurstTime = 0;
        for (Process item : processes) {
            turnaroundTime.add(item.processBurstTime);
            totalBurstTime += item.processBurstTime;
        }
        for(int seconds = 0; seconds < totalBurstTime; seconds++){
            ArrayList<Process> arrivedProcesses = new ArrayList<>();
            processes = sortByArriveTime(processes);
            for (Process value : processes) {
                if (value.processArrivalTime <= seconds) {
                    arrivedProcesses.add(value);
                }
            }
            arrivedProcesses = sortByBurstTime(arrivedProcesses);
            ArrayList<Process> mostImportantProcesses = new ArrayList<>();
            for(int m = 0; m < arrivedProcesses.size(); m++){
                if(arrivedProcesses.get(m).processWaitedTime > 50){
                    mostImportantProcesses.add(arrivedProcesses.get(m));
                }
            }
            if(mostImportantProcesses.size() >= 1){
                mostImportantProcesses = sortByWaitedTime(mostImportantProcesses);
                for(int m = 0; m < arrivedProcesses.size(); m++){
                    if(mostImportantProcesses.get(mostImportantProcesses.size() - 1).processName.equals(arrivedProcesses.get(m).processName)){
                        arrivedProcesses.remove(m);
                    }
                }
            arrivedProcesses.add(0, mostImportantProcesses.get(mostImportantProcesses.size() - 1));
            }
            System.out.println(arrivedProcesses.get(0).processName + " is running now");
            for(int m = 1; m < arrivedProcesses.size(); m++){
                arrivedProcesses.get(m).processWaitedTime++;
                for (Process process : processes) {
                    if (process.processName.equals(arrivedProcesses.get(m).processName)) {
                        process.processWaitedTime = arrivedProcesses.get(m).processWaitedTime;
                    }
                }
            }
            arrivedProcesses.get(0).processBurstTime--;
            if(arrivedProcesses.get(0).processBurstTime == 0){
                System.out.println(arrivedProcesses.get(0).processName + " has finished now");
                for(int k = 0; k < processes.size(); k++){
                    if(processes.get(k).processName.equals(arrivedProcesses.get(0).processName)){
                        waitedTime.add(processes.get(k).processWaitedTime);
                        System.out.println( "Process " + processes.get(k).processName + " total time waited is: " +
                                processes.get(k).processWaitedTime + " sec");
                        processes.remove(k);
                    }
                }
            }
        }
        float sum = 0;
        System.out.print("\nAverage waiting time = ( ");
        for(int i = 0; i < waitedTime.size(); i++){
            if(i != waitedTime.size() - 1)
                System.out.print(waitedTime.get(i) + " + ");
            else
                System.out.print(waitedTime.get(i));
            sum += waitedTime.get(i);
        }
        System.out.print(" ) / " + waitedTime.size() + " = " +  sum / waitedTime.size() + " sec" + '\n');
        System.out.print("\nAverage turnaround time = ");
        for(int i = 0; i < turnaroundTime.size(); i++){
            sum += turnaroundTime.get(i);
        }
        System.out.print(sum / turnaroundTime.size() + " sec" + '\n');
    }
}