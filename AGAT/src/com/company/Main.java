package com.company;
import java.util.*;

public class Main {
    public static ArrayList<Process> arrayList = new ArrayList<>();
    public static HashMap<Integer, String> execution = new HashMap<Integer, String>();
    public static int number_process;
    public static ArrayList<Process> dead_list = new ArrayList<Process>();
    public static HashMap<String, Integer> Burst_time = new HashMap<String, Integer>();
    public static HashMap<String, Integer> Waiting_Time = new HashMap<String, Integer>();
    public static HashMap<String, Integer> Turn_around_time  = new HashMap<String, Integer>();
    public static ArrayList<Process> processes = new ArrayList<Process>();
    public static ArrayList<String> AGAT_Quantam_history = new ArrayList<String>();
    public static ArrayList<String> Order = new ArrayList<String>();
    public static float V1;
    //Calculate V1
    public static float SetV1()
    {
        float V1;
        int last_Arrive_time = arrayList.get(arrayList.size()-1).getProcessArrival();
        if (last_Arrive_time > 10)
            V1 = (float) last_Arrive_time / 10;
        else
            V1 = 1;
        return V1;
    }
    //Calculate V2
    public static float SetV2()
    {
        float V2;
        if (arrayList.size() != 0) {
            int max = arrayList.get(0).getProcessBurst();
            for(int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getProcessBurst() >= max)
                    max = arrayList.get(i).getProcessBurst();
            }
            if (max > 10)
                V2 = (float) max / 10;
            else
                V2 = 1;
            return V2;
        }
        else
            return 1;
    }
    public static HashMap<String, Integer> AGAT_factor()
    {
        float v2;
        v2 = SetV2();
        HashMap<String, Integer> ar = new HashMap<String,Integer>();
        for (int i = 0; i < arrayList.size(); i++)
        {
            int factor;
            factor = 10 - arrayList.get(i).getProcessPriority() +
                    (int)Math.ceil((float)arrayList.get(i).getProcessArrival()/V1) +
                    (int)Math.ceil((float)arrayList.get(i).getProcessBurst()/v2);
            ar.put(arrayList.get(i).getProcessName(),factor);
        }
        return ar;
    }
    public static boolean check_quantum()
    {
        int counter = 0;
        for (int i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).getQuantum() == 0)
                counter++;

        return counter == arrayList.size();
    }
    public static int max(HashMap<Integer, String> time, String name)
    {
        ArrayList<Integer> Ascending_order = new ArrayList<Integer>();

        for(int num :time.keySet()) {
            Ascending_order.add(num) ;
        }

        Collections.sort(Ascending_order);
        int max = 0;
        for(int num :time.keySet()) {
            if (num >= max && time.get(num).equals(name))
                max = num;
        }
        for (int i=0 ;i<Ascending_order.size();i++)
        {
            if(Ascending_order.get(i)>max)
            {
                max = Ascending_order.get(i) ;
                break;
            }
        }
        return max;
    }
    public static void AGAT()
    {
        for (int i = 0; i < arrayList.size(); i++)
        {
            processes.add(arrayList.get(i));
        }
        System.out.println("AGAT History ");
        V1 = SetV1();
        int time = 0;
        ArrayList<Process> q = new ArrayList<Process>();
        int non_primitive=0, begin=0; Process p = null;
        HashMap<String, Integer> factor = AGAT_factor();
        System.out.println("IN time: " + time);
        for (String s: factor.keySet())
        {
            System.out.println(s + "  " + factor.get(s));
        }
        HashMap<Integer, String> timming = new HashMap<Integer, String>();
        while (true)
        {
            String name = ""; int num = Integer.MAX_VALUE;
            for (int i = 0; i < arrayList.size(); i++) // p1
            {
                if (arrayList.get(i).getProcessArrival() ==time && !(q.contains(arrayList.get(i))) && arrayList.get(i).getProcessBurst()!=0 && !(arrayList.get(i).equals(p)))
                {
                    q.add(arrayList.get(i)); // p1 0
                }
            }
            if(p==null)
            {
                if(q.size()>0)
                {
                    p = q.get(0);
                    timming.put(time,p.getProcessName());
                    if(execution.size()==0) {
                        execution.put(time, p.getProcessName());
                        Order.add(p.getProcessName() + " " + time);
                    }
                    else
                    {
                        if(!execution.containsKey(time)) {
                            execution.put(time, p.getProcessName());
                            Order.add(p.getProcessName()+" " + time);
                        }
                    }
                    non_primitive = Math.round((float)(q.get(0).getQuantum() *40) / 100);
                    q.remove(0);
                    begin = time;//0
                }
                else
                    continue;
            }
            if(p != null)
            {
                if(time==(begin+p.getProcessBurst()))//0+17
                {
                    p.setWorking(p.getWorking()+(p.getProcessBurst()));
                    p.setProcessBurst(0);
                    p.setQuantum(0);
                    AGAT_Quantam_history.add(p.getProcessName() + " Has a Quantum: " + p.getQuantum() + " IN " + time);
                    dead_list.add(p);
                    arrayList.remove(p);
                    timming.put(time,p.getProcessName());
                    if(q.size()>0)
                    {
                        p = q.get(0);
                        if(!execution.containsKey(time)) {
                            execution.put(time, p.getProcessName());
                            Order.add(p.getProcessName()+" " + time);
                        }
                        timming.put(time, p.getProcessName());
                        non_primitive = Math.round((float)(p.getQuantum() *40) / 100);
                        q.remove(0);
                        begin = time;
                    }
                    else
                        p = null;
                    factor = AGAT_factor();
                    System.out.println("IN time: " + time);
                    for (String s: factor.keySet())
                    {
                        System.out.println(s + "  " + factor.get(s));
                    }
                }
                else if(time==(begin+p.getQuantum()))
                {
                    p.setProcessBurst(p.getProcessBurst()-(p.getQuantum()));
                    p.setQuantum(p.getQuantum()+2);
                    q.add(p);
                    AGAT_Quantam_history.add(p.getProcessName() + " Has a Quantum: " + p.getQuantum() + " IN " + time);
                    timming.put(time, p.getProcessName());
                    if(q.size()>0)
                    {
                        p = q.get(0);
                        if(!execution.containsKey(time)) {
                            execution.put(time, p.getProcessName());
                            Order.add(p.getProcessName()+" " + time);
                        }
                        timming.put(time, p.getProcessName());
                        non_primitive = Math.round((float)(p.getQuantum() *40) / 100);
                        q.remove(0);
                        begin = time;
                    }
                    else
                        p = null;
                    factor = AGAT_factor();
                    System.out.println("IN time: " + time);
                    for (String s: factor.keySet())
                    {
                        System.out.println(s + "  " + factor.get(s));
                    }
                }
                else if(time>=begin+non_primitive) // 2 3 4 5  6
                {
                    Process r=null;
                    for(String s : factor.keySet())
                    {
                        if(factor.get(s)< num )
                        {
                            name = s;
                            num  = factor.get(s);
                        }

                    }
                    for(int i=0 ;i<q.size();i++) {
                        if (name.equals(q.get(i).getProcessName())) {
                            r = q.get(i);
                            q.remove(i);
                            break;
                        }
                    }
                    if(r!=null)// here
                    {
                        if(!p.getProcessName().equals(r.getProcessName()))
                        {
                            //q.remove(r);
                            p.setQuantum(p.getQuantum()+(p.getQuantum()-(time-begin)));
                            AGAT_Quantam_history.add(p.getProcessName() + " Has a Quantum: " + p.getQuantum() + " IN " + time);
                            p.setProcessBurst(p.getProcessBurst()-(time-begin));
                            q.add(p);
                            p = r;
                            timming.put(time, p.getProcessName());
                            if(!execution.containsKey(time)) {
                                execution.put(time, p.getProcessName());
                                Order.add(p.getProcessName()+" " + time);
                            }
                            non_primitive = Math.round((float)(p.getQuantum() *40 )/ 100);
                            begin=time;
                            factor = AGAT_factor();
                            System.out.println("IN time: " + time);
                            for (String s: factor.keySet())
                            {
                                System.out.println(s + "  " + factor.get(s));
                            }
                        }
                    }
                }
            }
            if(check_quantum()) {
                String nn = null;
                int max = 0;
                for (int n : timming.keySet()) {
                    if (n >= max) {
                        max = n;
                        nn = timming.get(n);
                    }
                }
                //Order.add(nn+" " +time);
                timming.put(time, nn);
                break;
            }
            time++;
        }
        System.out.println("--------------------------------------------------------------------------------");
        float Avg =0 ;
        for (int i=0 ;i< processes.size();i++)
        {
            int ended = max(timming, processes.get(i).getProcessName());
            int wait = Math.abs((ended- (processes.get(i).getProcessArrival()))-(Burst_time.get(processes.get(i).getProcessName())));
            Waiting_Time.put(processes.get(i).getProcessName(),wait);
            Avg+=wait;
        }
        System.out.println("Waiting time: ");
        for(String s : Waiting_Time.keySet())
        {
            System.out.println(s + " " + Waiting_Time.get(s));
        }

        Avg = Avg/number_process ;
        System.out.println("Average waiting time=  " +Avg);
        float Avg_turn_time=0 ;
        for (int i=0 ;i< processes.size();i++)
        {
            int ended = max(timming, processes.get(i).getProcessName());
            int turn_around_time = (ended - (processes.get(i).getProcessArrival()));
            Turn_around_time.put(processes.get(i).getProcessName(),turn_around_time);
            Avg_turn_time +=turn_around_time;
        }
        Avg_turn_time = Avg_turn_time/number_process ;
        System.out.println("Turn around time:  ");
        for(String s : Turn_around_time.keySet())
        {
            System.out.println(s + "  " + Turn_around_time.get(s));
        }
        System.out.println("Average turn around time=  " +Avg_turn_time);


    }
    public static void main(String[] args) {

        Process process1 = new Process("p1",0,17,4, 4);
        Process process2 = new Process("p2",3,6,9, 3);
        Process process3 = new Process("p3",4,10,3, 5);
        Process process4 = new Process("p4",29,4,8, 2);
        arrayList.add(process1);
        arrayList.add(process2);
        arrayList.add(process3);
        arrayList.add(process4);
        number_process = 4 ;
        for(int i=0 ;i<arrayList.size();i++)
        {
            Burst_time.put(arrayList.get(i).getProcessName(),arrayList.get(i).getProcessBurst());
        }
        AGAT();
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("execution order:  ");
        for (int i=0 ;i<Order.size()-1;i++)
        {
            System.out.println((i+1)+" " + Order.get(i));
        }
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Quantum History: ");
        for (int i = 0; i < AGAT_Quantam_history.size(); i++)
        {
            System.out.println(AGAT_Quantam_history.get(i));
        }
    }
}