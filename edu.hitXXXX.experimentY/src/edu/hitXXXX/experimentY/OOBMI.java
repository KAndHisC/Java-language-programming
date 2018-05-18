package edu.hitXXXX.experimentY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class OOBMI {
	static ArrayList<Student> students=new ArrayList<Student>();
	
	public static void main(String[] args ){
		Scanner in=new Scanner(System.in);
		//inputStudents(in);
		genStudents(in.nextInt());
		double med=MedianofBMI();
		//comparator.SortbyID();
		printStatics(AverofBMI(),ModofBMI(),med,VarianceofBMI());
		in.close();
	}
	public static boolean isExists(String id){
		for(Iterator<Student> iter = students.iterator();iter.hasNext();){
			Student temp=iter.next();
			if(temp.number==null)continue;
			if(id.compareTo(temp.number)==0)return true;
		}
		return false;
	}
	
	public static void genStudents(int n){
		
		Random ron=new Random();
		
		char Char[]={
				'A','B','C','D','E','F','G',
				'H','I','J','K','L','M','N',
				'O','P','Q','R','S','T',
				'U','V','W','X','Y','Z',
				'a','b','c','d','e','f','g',
				'h','i','j','k','l','m','n',
				'o','p','q','r','s','t',
				'u','v','w','x','y','z'};
		
		for(int i=0;i<n;i++){
			String number=String.valueOf(100+ron.nextInt(100));
			if(isExists(number)){
				i--;
				}
			else {
			String name="";
			for(int j=0;j<5;j++){name+=Char[ron.nextInt(46)];}
			double height=ron.nextDouble()+1;
			double weight=ron.nextDouble()*60+40;
			students.add(new Student(number,name,height,weight));
			}
		}
		System.out.println("Success!");
	}
	
	public static Student inputStudent(Scanner in){
		String number=null,name=null;
		double height=0.0,weight=0.0;
		do{
			System.out.println("please input the ID,name,height and weight:");
			number=in.next();
			name=in.next();
			height=in.nextDouble();
			weight=in.nextDouble();
		}while(!isExists(number));
		
		Student student=new Student(number,name,height,weight);

		return student;
	}
	
	public static void  inputStudents(Scanner in){
		String str=null;
		do{
			System.out.println("please input the ID,name,height and weight:");
			String number=in.next();
			String name=in.next();
			double height=in.nextDouble();
			double weight=in.nextDouble();
			if(isExists(number)){
				System.out.println("error!");
				continue;
				}
			students.add(new Student(number,name,height,weight));
			System.out.println("Do you want to creat next random student?");
			str=in.next();
		}while(str.compareTo("Y")==0||str.compareTo("y")==0);
	}
	
	public static void printStatics(double aver,double mod,double median,double variance){
		
		System.out.printf("Average is:%.2f\tMode is:%.2f\tMedian is:%.2f\tVariance is:%.2f\n"
				,aver,mod,median,variance);
		System.out.printf("ID\tNAME\tHeight\tWeight\tBMI\n");
		for(Iterator<Student> iter = students.iterator();iter.hasNext();){
			Student temp=iter.next();
			System.out.printf("%s\t%s\t%.2f\t%.2f\t%.2f\n",
					temp.number,temp.name,
					temp.height,temp.weight,temp.bmi);
		}
	}
	
	public static void saveFile(ArrayList<Student> students2, String filename){
		File file = new File("."+File.separator+filename);
	    if(!file.exists()) { 
	    	 try {
		            file.createNewFile(); 
		            System.out.println(file.getName() + " �����ɹ�");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    }
	    FileWriter writer;
    	try {
    	      writer = new FileWriter(filename, false);
    	      writer.write(String.format("ID\tNAME\tHEIGHT\tWEIGHT\tBMI\r\n"));
              for(Student st: students2){
            	  writer.write(String.format("%s\t%s\t%.2f\t%.2f\t%.2f\r\n", 
            			  st.number,st.name,st.height,st.weight,st.bmi));
              }
              writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	System.out.println("SAVE SUCCESS!");
	}
	
	public static  ArrayList<Student>  readFile(String filename){
		ArrayList<Student> buff=new ArrayList<Student>();	
		File file = new File("."+File.separator+filename);
	    if(file.exists()) { 
	        try {
				BufferedReader reader =new BufferedReader(new FileReader(file));
	            String tempString = null;
	            tempString = reader.readLine();
	            while ((tempString = reader.readLine()) != null) {
	                String[] a= tempString.split("\t");
	                Student st=new Student(a[0],a[1],
	                		Double.parseDouble(a[2]),Double.parseDouble(a[3]));
	                buff.add(st);
	            }
	            reader.close();
	            System.out.println("READ SUCCESS!");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	    } else {
	        System.out.println("the file have noe been found!!");
	    }    
	    return buff;
	}
	
	public static void findStudent(ArrayList<Student> stu,Scanner in){
		System.out.println("please input the id:");
		String id=in.next();
		
		for(int i=0;i<students.size();i++){
			
			if(id.compareTo(students.get(i).number)==0){
				System.out.printf("The ID has been found,do you want:\n"
						+ "1 modify\n"
						+ "2 delete\n");
				switch(in.nextInt()){
				case 1:students.set(i,inputStudent(in));break;
				case 2:students.remove(i);break;
				default:System.out.println("ERROR CHIOSE");break;
				}
				return;
			}
		}
		System.out.printf("The ID has not been found,do you want creat?input 1 to do it\n");
		if(in.nextInt()==1)stu.add(inputStudent(in));
		saveFile(students, "student.txt");
	}

	public static double AverofBMI(){
		double sum=0;
		for(Iterator<Student> iter = students.iterator();iter.hasNext();)
			sum+=iter.next().bmi;
		
		return sum/students.size();
	}
	
	public static double ModofBMI(){
		
		int hash[]=new int[10000];
		for(Iterator<Student> iter = students.iterator();iter.hasNext();)
			hash[(int)iter.next().bmi*100-1]++;
		int max=0;
		for(int i=1;i<hash.length;i++){
			if(hash[i]>hash[max])max=i;
		}
		return max/100.0;
	}	
	
	public static double MedianofBMI(){
		comparator.SortbyBMI();
		if(students.size()%2==0)
			return students.get(students.size()/2).bmi+students.get(students.size()/2-1).bmi;
		
		return students.get(students.size()/2).bmi;
	}	
	
	public static double VarianceofBMI(){
		double sum=0;
		double aver=AverofBMI();
		for(Iterator<Student> iter = students.iterator();iter.hasNext();){
			Student temp=iter.next();
			sum+=(temp.bmi-aver)*(temp.bmi-aver);
		}
			
		
		return sum/students.size();
	}
	
	public static class comparator{
		
		public static void SortbyBMI(){
				
			Collections.sort(students, new Comparator<Student>() {
	            @Override
	            public int compare(Student o1, Student o2) {
	                //����
	                return (int)(100*o1.bmi-100*o2.bmi);
	            }
	        });
		}
		
		public static void SortbyID(){
			Collections.sort(students, new Comparator<Student>() {
	            @Override
	            public int compare(Student o1, Student o2) {
	                //����
	                return o1.number.compareTo(o2.number);
	            }
	        });
		}
		
		public static void SortbyNAME(){
			Collections.sort(students, new Comparator<Student>() {
	            @Override
	            public int compare(Student o1, Student o2) {
	                //����
	                return o1.name.compareTo(o2.name);
	            }
	        });
		}
		
		
		public static void SortbyHeight(){
			Collections.sort(students, new Comparator<Student>() {
	            @Override
	            public int compare(Student o1, Student o2) {
	                //����
	                return (int)(100*o1.height-100*o2.height);
	            }
	        });
		}
		
		public static void SortbyWeight(){
			Collections.sort(students, new Comparator<Student>() {
	            @Override
	            public int compare(Student o1, Student o2) {
	                //����
	                return (int)(100*o1.weight-100*o2.weight);
	            }
	        });
		}
	}
	
	public static class Student{
		
		String number=null,name=null;
		double height=0.0,weight=0.0,bmi=0.0;
		public Student(){}
		public Student(String number,String name,double height,double weight){
			this.number=number;
			this.name=name;
			this.height=(int)(height*100)/100.0;
			this.weight=(int)(weight*100)/100.0;
			this.bmi=(int)(weight/(height*height))*100/100.0;
		}
	}

}
