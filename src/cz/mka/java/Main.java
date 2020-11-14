package cz.mka.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        /* Save paths to files */
        String pathToFileDirectory = "C:\\JavaEE_projects\\directoryTest.txt";
        String pathToFileFind = "C:\\JavaEE_projects\\find.txt";
        /* Create new ArrayLists */
        List<String> directoryList = new ArrayList<>();
        List<String> findList = new ArrayList<>();
        /* Fill Arraylists with data from Files */
        File directoryFile = new File(pathToFileDirectory);
        File findFile = new File(pathToFileFind);
        try (Scanner scanner = new Scanner(directoryFile)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                directoryList.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFileDirectory);
        }
        try (Scanner scanner = new Scanner(findFile)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                findList.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFileFind);
        }

        /* Create advance object ArrayLists */
        List<contact> directoryObjectList = new ArrayList<>();
        try (Scanner scanner = new Scanner(directoryFile)) {
            int id = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] arrayLine = line.split(" ");
                int num = Integer.parseInt(arrayLine[0]);
                String name = arrayLine[1];
                String surname;
                if (arrayLine.length > 2 ) {
                    surname = arrayLine[2];
                }else {
                    surname = "empty";
                }
                contact newContact = new contact(id,num,name,surname);
                directoryObjectList.add(newContact);
                id++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFileDirectory);
        }
        List<contact> findObjectList = new ArrayList<>();
        try (Scanner scanner = new Scanner(findFile)) {
            int idFind = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] arrayLine = line.split(" ");
                int num = 0;
                String name = arrayLine[0];
                String surname;
                if (arrayLine.length > 1 ) {
                    surname = arrayLine[1];
                }else {
                    surname = "empty";
                }
                contact newContact = new contact(idFind,num,name,surname);
                findObjectList.add(newContact);
                idFind++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFileFind);
        }


        /* Do Algo
        System.out.println("Start searching (linear search)...");
        linearSearch(directoryList,findList);
        */
        System.out.println("Start searching (bubble sort + jump search)...");

        long startBubbleSort = System.currentTimeMillis();
        bubbleSort(directoryObjectList);
        long endBubbleSort = System.currentTimeMillis();
        long finalTime = endBubbleSort - startBubbleSort;
        int m = (int) (((finalTime / 1000) / 60) % 60);
        int s = (int) ((finalTime / 1000) % 60);
        int ms = (int) ((finalTime % 1000));


        long startJumpSearch = System.currentTimeMillis();
        int found = 0;
        for (int i = 1; i < findList.size(); i++) {
            if (jumpSearch(directoryObjectList,findObjectList.get(i).name,findObjectList.get(i).surname) == 1) {
                found++;
            }
        }
        long endJumpSearch = System.currentTimeMillis();
        long finalJumpTime = endJumpSearch - startJumpSearch;
        int m2 = (int) (((finalJumpTime / 1000) / 60) % 60);
        int s2 = (int) ((finalJumpTime / 1000) % 60);
        int ms2 = (int) ((finalJumpTime % 1000));

        long sumBubbleJump = finalTime + finalJumpTime;
        int m3 = (int) (((sumBubbleJump / 1000) / 60) % 60);
        int s3 = (int) ((sumBubbleJump / 1000) % 60);
        int ms3 = (int) ((sumBubbleJump % 1000));

        System.out.println("Found "+found+"/"+ "500" + ". Time taken: "+m3+ " min. "+s3+" sec. "+ms3+" ms.");
        System.out.println("Sorting time: "+m+ " min. "+s+" sec. "+ms+" ms.");
        System.out.println("Searching time: "+m2+ " min. "+s2+" sec. "+ms2+" ms.");

    }

    public static void linearSearch(List<String> directoryList, List<String> findList ) {
        long start = System.currentTimeMillis();
        int numberOfFindings = findList.size();
        int found = 0;

        for (int i = 0; i < findList.size(); i++) {
            for (int j = 0; j < directoryList.size(); j++) {
                if (directoryList.get(j).contains(findList.get(i))) {
                    found++;
                    break;
                }
            }
        }
        long end = System.currentTimeMillis();
        long finalTime = end - start;

        int m = (int) (((finalTime / 1000) / 60) % 60);
        int s = (int) ((finalTime / 1000) % 60);
        int ms = (int) ((finalTime % 1000));
        System.out.println("Found "+found+"/"+numberOfFindings+ ". Time taken: "+m+ " min. "+s+" sec. "+ms+" ms.");
    }

    public static List<contact> bubbleSort(List<contact> inputList) {
        int count = 0;
        for (int i = 0; i < inputList.size() - 1; i++) {
            for (int j = 0; j < inputList.size() - i - 1; j++) {
                //* if a pair of adjacent elements has the wrong order it swaps them *//*
                if (inputList.get(j).name.compareTo(inputList.get(j + 1).name)>0 ) {
                    count++;
                    contact temp = new contact(inputList.get(j).id,inputList.get(j).number,inputList.get(j).name,inputList.get(j).surname);
                    inputList.get(j).setId(inputList.get(j + 1).id);
                    inputList.get(j).setNumber(inputList.get(j + 1).number);
                    inputList.get(j).setName(inputList.get(j + 1).name);
                    inputList.get(j).setSurname(inputList.get(j + 1).surname);
                    inputList.get(j + 1).setId(temp.id);
                    inputList.get(j + 1).setNumber(temp.number);
                    inputList.get(j + 1).setName(temp.name);
                    inputList.get(j + 1).setSurname(temp.surname);
                }
            }
        }

        return inputList;
    }
    public static int jumpSearch(List<contact> inputList, String targetName, String targetSurname) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If array is empty, the element is not found */
        if (inputList.size() == 0) {
            return -1;
        }

        /* Check the first element */
        if (inputList.get(currentRight).getName().equals(targetName)) {
            return 1;
        }

        /* Calculating the jump length over array elements */
        int jumpLength = (int) Math.sqrt(inputList.size());

        /* Finding a block where the element may be present */
        while (currentRight < inputList.size() - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(inputList.size() - 1, currentRight + jumpLength);

            if (inputList.get(currentRight).getName().compareTo(targetName) > 0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == inputList.size() - 1) && targetName.compareTo(inputList.get(currentRight).getName()) > 0 ) {
            return -1;
        }

        /* Doing linear search in the found block */
        return backwardSearch(inputList, targetName, prevRight, currentRight);
    }

    public static int backwardSearch(List<contact> inputList, String targetName, int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (inputList.get(i).getName().equals(targetName)) {
                return 1;
            }
        }
        return -1;
    }

}
class contact {
    int id;
    int number;
    String name;
    String surname;

    public contact(int id, int number, String name, String surname) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
