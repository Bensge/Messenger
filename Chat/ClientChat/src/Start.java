import java.util.Scanner;


public class Start {
  
  
  public static void main(String[] args) {
    System.out.print("Enter the IP you want to connect to:");
    //"192.168.178.22"
    //(new Scanner(System.in).next()
    new ChatSocket("10.32.111.6", 80);
  }
}
