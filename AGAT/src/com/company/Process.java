package com.company;
public class Process {
    private String ProcessName;
    private int ProcessArrival;
    private int ProcessBurst;
    private int ProcessPriority;
    private int Quantum;
    private int working = 0;

    public int getWorking() {
        return working;
    }

    public void setWorking(int working) {
        this.working = working;
    }

    public int getQuantum() {
        return Quantum;
    }
    public String getProcessName() {
        return ProcessName;
    }

    public Process(String processName, int processArrival, int processBurst, int processPriority, int quantum) {
        ProcessName = processName;
        ProcessArrival = processArrival;
        ProcessBurst = processBurst;
        ProcessPriority = processPriority;
        Quantum = quantum;
    }

    public int getProcessArrival() {
        return ProcessArrival;
    }
    public int getProcessBurst() {
        return ProcessBurst;
    }
    public int getProcessPriority() {
        return ProcessPriority;
    }
    public void setProcessName(String processName) {
        ProcessName = processName;
    }
    public void setProcessArrival(int processArrival) {
        ProcessArrival = processArrival;
    }
    public void setProcessBurst(int processBurst) {
        ProcessBurst = processBurst;
    }
    public void setProcessPriority(int processPriority) {
        ProcessPriority = processPriority;
    }
    public void setQuantum(int quantum) {
        Quantum = quantum;
    }
}
