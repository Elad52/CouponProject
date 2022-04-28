/*
Created by Elad on 04/04/2022

*/

public class Program {
    public static void main(String[] args) {
        try {
            Test.testAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
