package com.exercise;

import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class SortByEmployee implements Comparator<Employee> {
	@Override
	public int compare(Employee o1, Employee o2) {
		if(o1.getBaseSal() > o2.getBaseSal())
			return 1;
		if(o1.getBaseSal() < o2.getBaseSal())
			return -1;
//		return o1.getEmpName().compareTo(o2.getEmpName());
		if(o1.getEmpName().compareTo(o2.getEmpName()) < 0)
			return 1;
		if(o1.getEmpName().compareTo(o2.getEmpName()) > 0)
			return -1;
		return 0;
	}
}

public class CompanyManagement<E>{
	private ArrayList<Employee> empList;

	//path: path of ListOfEmployees, path1: path of PLInfo.txt
	public CompanyManagement(String path, String path1) throws FileNotFoundException, IOException {
		empList = getEmployeeFromFile(path, path1);
	}

	//Phan code cua sinh vien

	//path: path of ListOfEmployees, path1: path of PLInfo.txt
	public ArrayList<Employee> getEmployeeFromFile(String path, String path1) throws FileNotFoundException, IOException {
//        return new ArrayList<Employee>();
		Scanner in1 = new Scanner(Paths.get(path));
		Scanner in2 = new Scanner(Paths.get(path1));
		ArrayList<Employee> employees = new ArrayList<>();

		while(in1.hasNext()) {
			String s = in1.nextLine();
			String[] tokens = s.split(",");
			if(tokens[1].contains("D")) { //If ID contains "D" -> Developer
				String PL = in2.nextLine();
				ArrayList<String> programmingLanguages = new ArrayList<>(Arrays.asList(PL.split(",")));
				if(tokens[5].equals("L")) //If there's "L" -> TeamLeader
					employees.add(new TeamLeader(tokens[1], tokens[2], Integer.parseInt(tokens[7]), tokens[3],
							programmingLanguages, Integer.parseInt(tokens[4]), Double.parseDouble(tokens[6])));
				else employees.add(new Developer(tokens[1], tokens[2], Integer.parseInt(tokens[5]), tokens[3],
						programmingLanguages, Integer.parseInt(tokens[4])));
			} else { //Final case: Tester
				employees.add(new Tester(tokens[1], tokens[2], Integer.parseInt(tokens[5]),
						Double.parseDouble(tokens[3]), tokens[4]));
			}
		}
		in1.close();
		in2.close();
		return employees;
	}

	public ArrayList<Developer> getDeveloperByProgrammingLanguage(String pl){
		ArrayList<Developer> result = new ArrayList<>();
		for(Employee employee : empList) {
			if(employee instanceof Developer && ((Developer) employee).getProgrammingLanguages().contains(pl))
				result.add((Developer) employee);
		}
		return result;
	}

	public ArrayList<Tester> getTestersHaveSalaryGreaterThan(double value){
		ArrayList<Tester> testers = new ArrayList<>();
		for(Employee e : empList) {
			if(e instanceof Tester && e.getSalary() > value) {
				testers.add((Tester) e);
			}
		}
		return testers;
	}

	public Employee getEmployeeWithHigestSalary(){
		Employee maxSalaryEmployee = empList.get(0);
		for(Employee e : empList) {
			if(e.getSalary() > maxSalaryEmployee.getSalary()) {
				maxSalaryEmployee = e;
			}
		}
		return maxSalaryEmployee;
	}

	public TeamLeader getLeaderWithMostEmployees(){
		HashMap<String, Integer> membersOfTeam = new HashMap<>();
		HashMap<TeamLeader, String> teamLeaders = new HashMap<>();
		for(Employee e : empList) {
			if(e instanceof TeamLeader) {
				teamLeaders.putIfAbsent((TeamLeader) e, ((TeamLeader) e).getTeamName());
				if(membersOfTeam.containsKey(((TeamLeader) e).getTeamName())) //Tr?????ng h???p m?? c??i team ???? c?? tr??n map r???i th?? c???ng 1 v??o s??? ?????m
					membersOfTeam.put(((TeamLeader) e).getTeamName(), membersOfTeam.get(((TeamLeader) e).getTeamName()) + 1);
				else membersOfTeam.put(((TeamLeader) e).getTeamName(), 1); //Tr?????ng h???p ch??a c?? team ??? map th?? b???t ?????u ?????m l?? 1
			}
			else if(e instanceof Developer) {
				if(membersOfTeam.containsKey(((Developer) e).getTeamName()))
					membersOfTeam.put(((Developer) e).getTeamName(), membersOfTeam.get(((Developer) e).getTeamName()) + 1);
				else membersOfTeam.put(((Developer) e).getTeamName(), 1);
			}
		}
		List<TeamLeader> teamLeaderList = new ArrayList<>();
		AtomicInteger maxMembers = new AtomicInteger(); //s??? l?????ng th??nh vi??n l???n nh???t
		membersOfTeam.forEach((s, integer) -> {
			if(integer > maxMembers.get())
				maxMembers.set(integer);
		}); //T??m s??? l?????ng th??nh vi??n nhi???u nh???t
		teamLeaders.forEach((teamLeader1, s) -> {
			if(membersOfTeam.get(teamLeader1.getTeamName()) == maxMembers.get()) {
				teamLeaderList.add(teamLeader1);
			}
		}); //T??m c??c teamleader c?? s??? l?????ng th??nh vi??n trong team b???ng v???i s??? l?????ng th??nh vi??n l???n nh???t
		if(teamLeaderList.size() == 1) //N???u m?? c?? duy nh???t 1 teamleader c?? s??? l?????ng th??nh th??nh vi??n trong team l???n nh???t
			return teamLeaderList.get(0); //Tr??? v??? teamleader duy nh???t
		AtomicReference<TeamLeader> maxExpYearLeader = new AtomicReference<>(teamLeaderList.get(0)); //Teamleader c?? nhi???u n??m kinh nghi???m nh???t
		teamLeaderList.forEach(teamLeader -> {
			if(teamLeader.getExpYear() > maxExpYearLeader.get().getExpYear())
				maxExpYearLeader.set(teamLeader);
  		}); //T??m teamleader c?? nhi???u n??m kinh nghi???m nh???t
		return maxExpYearLeader.get(); //Tr??? v???
	}

	public ArrayList<Employee> sorted(){
		ArrayList<Employee> sortedEmpList = empList;
		sortedEmpList.sort(new SortByEmployee());
		Collections.reverse(sortedEmpList);
		return sortedEmpList;
	}


	//-------------------------------------------------------------------

	//Print empList
	public void printEmpList(){
		for(Employee tmp : empList){
			System.out.println(tmp);
		}
	}

	//Methods for writing file
	public <E> boolean writeFile(String path, ArrayList<E> list){
		try {
			FileWriter writer = new FileWriter(path);
			for(E tmp : list){
				writer.write(tmp.toString());
				writer.write("\r\n");
			}
			writer.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("Error.");
			return false;
		}
		return true;
	}

	public <E> boolean writeFile(String path, E object){
		try {
			FileWriter writer = new FileWriter(path);
			writer.write(object.toString());
			writer.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("Error.");
			return false;
		}
		return true;
	}
}