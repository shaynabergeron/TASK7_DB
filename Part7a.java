import java.sql.*;
import java.util.*;
import java.io.*;
public class Part7a{
    
    public ConnectToDBLD ld  = new ConnectToDBLD();
    public  Connection ldcn = ld.getConn();
    public Person human = new Person(ldcn);
    public  void run(){
        try{
            //get ids
            System.out.println("Enter a position code id for the new employee");
            Scanner input = new Scanner(System.in);
            int P_PositionCode = input.nextInt();
            System.out.println("\n Enter a job code id for the new employee");
            int JobCode = input.nextInt();
            System.out.println("Employee data:\n-------------------------\n"+dataAndSkills(P_PositionCode)+"\n");
            //get transcript new employee 
            File file = new File("transcript"+P_PositionCode+".txt");
            Scanner reader = new Scanner(file);
            String transcript = reader.nextLine();
            String[] courses = transcript.split(", ");
            ArrayList<String> skills = new ArrayList<String>();
            for(String course:courses){
                String [] details = course.split(": ");
               // skillFromCourses(details[0],skills);
                human.insertintotakes(P_PositionCode,human.courseNum(details[0]),details[1]);
            }
            ArrayList<String> selftaught = human.haskills(P_PositionCode);
            int count = 1;
            System.out.println("skills not derived from taken classes\n--------------------");
            for (String skill: selftaught){
                if(!skills.contains(skill)){
                    System.out.println(count+"\t"+human.skillidToSkillname(skill));
                    count = count+1;
                }
            }
            System.out.println("adding skills database......\n--------------------------------");
            human.insertSkills(P_PositionCode,skills);
            Job jobinques = new Job(ldcn);
            int[] pskill = {4,5,9,7,21,3,10};
            if(count >2){

                while(true){
                System.out.println("employee has too many self-claimed skill would you like to rescind the job offer?\n y for yes\n n for no");
                    char value = input.next().charAt(0);
                if(value == 'y'){
                    System.out.println("Job offer has been recinded");
                    break;
                }
                else if(value == 'n'){
                    System.out.println("You now have a new employee");
                    human.insertIntoWorks(P_PositionCode,(""+JobCode));
                    break;
                }
                else{
                    System.out.println("Invalid choice");
                }
            }}
            ArrayList<String> allSkills = human.haskills(P_PositionCode);
            ArrayList<String> requiredskills = jobinques.requiresSkill(JobCode);
            ArrayList<String> missingSkills = new ArrayList<String>();
            for (String skill: requiredskills){
                if (!allSkills.contains(skill)){
                    missingSkills.add(skill);
                }
            }
            System.out.println("Recomended courses are:\n---------------------------------------------------------\n");
            for(String skill: missingSkills){
            System.out.println(human.coruseNumFromSkill(skill)+" "+human.skillidToSkillname(skill));
            }            

        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public ArrayList<String> dataAndSkills(int P_PositionCode){
            ArrayList<String> join = human.getPerson(P_PositionCode);
            return join;
        }
   /* public void skillFromCourses(String course,ArrayList join){
            join.addAll(human.skillFromTakenCourses(course));
        }*/
    
}