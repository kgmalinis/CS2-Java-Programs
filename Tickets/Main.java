/*
 	Kate Malinis 	<kgm170030>
 
  	CS 2336.003 -- Project 2
  	Dr. Oladimeji
  
  	This project implements a multi-directional linked list towards an auditorium and its movie screening.
  	
  	MAIN CLASS
 */

package Tickets;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;


public class Main 
{
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		boolean terminate = false;	// boolean variable for loop iteration
		
		while (!terminate)
		{
			// start of program: display main menu
			System.out.print("\n\n");
			System.out.println("\t\tWelcome to the online ticket reservation!");
			System.out.println("Enter your option: ");
			System.out.print("\n\n");
			
			System.out.println("1. Reserve Tickets");
			System.out.println("2. Exit");
			
			// Get the user's decision 
			Scanner input = new Scanner(System.in);
			int menuSelection = input.nextInt();
			
			switch (menuSelection)
			{
			
			// CASE 1: the user wants to reserve seats
			case 1:
				File input_file = new File("A1.txt");
				Scanner reader = new Scanner(input_file);
				
				if (!input_file.exists())
				{
					System.out.println("There is a problem trying to locate or open the file.");
					break;
				}
				
				// create instance of Auditorium based on file input
				Auditorium A1 = new Auditorium(reader);
				System.out.println(A1.toString());		// prints out the auditorium to the user
				reserveSeats(A1, input);		// call reserveSeats for seat reservations
				
				// Update the auditorium
				PrintWriter output = new PrintWriter("A1.txt");
				A1.writeFile(output);
				output.close();
				
				// print the summary of the auditorium
				printSummary(A1);
				break;
			
			// CASE 2: the user decides to exit
			case 2:
				System.out.println("Thank you for using the online ticket reservation.");
				System.out.println("Enjoy your movie!");
				terminate = true;
				break;
			}
		}
	}// end main
	
	// reserveSeats allows the user to reserve seats
	public static void reserveSeats(Auditorium screening, Scanner input)
	{
		int adultTix = 0, childTix = 0, seniorTix = 0, row = 0;
		boolean verify = false;
		char user = ' ';
		String check = "";
		int [] bestSeat = {0, 0};
		
		// Get desired row from user
		do
		{
			try
			{
				System.out.println("Enter desired row number: ");
				row = input.nextInt();
				verify = true;
			}
				
			catch(InputMismatchException e)		// catch input mismatch exception
			{
				verify = false;	
				input.next();
			}
			
			// if row is less than 1 or greater than auditorium arrangement, display error
			if (row < 1 || row > (screening.getNumRows()))
			{
				System.out.println("The row must be a valid number between 1 and " 
						+ (screening.getNumRows()));
				verify = false;
				input.nextLine();		
			}
				
			else	// else, row passes all verification tests
				verify = true;
		} while (verify == false);
				
		// Get desired seat from user
		do
		{
			try
			{
				System.out.println("Enter desired seat: ");
				check = input.next();
				user = check.charAt(0);
				verify = true;
			}
				
			catch(InputMismatchException e)
			{
				verify = false;
				input.next();
			}
			
			// seat has to be in-bounds of the alphabet 
			if (user < (char)65 || user > (screening.getNumColumns() + 65) || check.length() > 1)
			{
				System.out.println("The seat must be a valid letter between A to " + (char)((screening.getNumColumns()) + 64));
				verify = false;
				input.nextLine();
			}
		
			else	// else, seat passes all verification tests
				verify = true;
		} while (verify == false);
			
		// Get number of adult tickets from user
		do
		{
			try 
			{
				System.out.println("Enter the number of adult tickets: ");
				adultTix = input.nextInt();
				if (adultTix < 0)		// number of tickets has to be positive
					verify = false;
				else				// else, the user entered a positive number
					verify = true;
			}	
			catch(InputMismatchException e)
			{
				verify = false;
				input.nextLine();
			}
		}
		while (verify == false);	// loop until user enters a positive number or 0
			
		// Get number of child tickets from user
		do
		{
			try 
			{
				System.out.println("Enter the number of child tickets: ");
				childTix = input.nextInt();
				if (childTix < 0)		// number of tickets has to be positive
					verify = false;
				else				// else, the user entered a positive number
					verify = true;
			}
			catch(InputMismatchException e)
			{
				verify = false;
				input.nextLine();
			}
		}
		while (verify == false);	// loop until user enters a positive number or 0
			
			
		// Get number of senior tickets from user
		do
		{
			try 
			{
				System.out.println("Enter the number of senior tickets: ");
				seniorTix = input.nextInt();
				if (seniorTix < 0)		// number of tickets has to be positive
					verify = false;
				else				// else, the user entered a positive number
					verify = true;
			}
			catch(InputMismatchException e)
			{
				verify = false;
				input.nextLine();
			}
		}
			
		while (verify == false);	// loop until user enters a positive number or 0
			
		// if the combined tickets is greater than the seats in the column,
		if ((adultTix + childTix + seniorTix) > screening.getNumColumns())
			System.out.println("There are no seats available.");	// then there are no seats available
			
		// else, if the seats are available according to user input...
		else if (screening.seatsAvailable(row - 1, adultTix, childTix, seniorTix, user))
		{
			System.out.println("Your seats were reserved successfully.");	// display success to user
			System.out.print("\n");
		}
		
		// else, we need to find the best seat given the user input
		else
		{
			bestSeat = locateBest(screening, adultTix, childTix, seniorTix);
			if (bestSeat[0] < 0 && bestSeat[1] < 0)
				System.out.println("Unable to locate best seat.");
				
			else
			{
				System.out.println("We're sorry, but those seats are unodeailable.");
				System.out.println("Trying to locate the best possible seat(s) off your choices...");
					
				System.out.println("Congratulations!");
				System.out.println("We found a seat at row " + bestSeat[0]
						+ " and column " + (char)(bestSeat[1] + 65) + "-" +
						(char)(bestSeat[1] + 64 + adultTix + childTix + seniorTix));
				System.out.println("Would you like to reserve these seats? (Y/N) ");
				char decision = input.next().charAt(0);
					
				if (decision == 'Y' || decision == 'y')
				{
						screening.updateAuditorium(bestSeat[0] - 1, bestSeat[1], adultTix, childTix, seniorTix);
				}
			}
		}
	}//end reserveSeats
	
	// findDistance incorporates the distance formula to help find the best seat
	public static double findDistance(double x1, double x2, double y1, double y2)
	{
		// use the distance formula to find the distance
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	// locateBest locates the best seat within the auditorium
	public static int[] locateBest(Auditorium screening, int adultTix, int childTix, int seniorTix)
	{
		int i = 0;
		int j = 0;
		int coordinates[] = {-1, -1};	// if this is never updated, then there is no best seat available
		int totalTix = adultTix + childTix + seniorTix;
		
		double centerx = (screening.getNumColumns() + 1) / 2.0;		// center of columns
		double centery = (screening.getNumRows() + 1) / 2.0;		// center of rows
		double center = (totalTix + 1) / 2.0;			// center of the given auditorium
		TheaterSeat node = screening.getFirst();		// retrieve the first node to traverse through
		TheaterSeat head = node;		// set the first node of each row = node
		
		double shortestDistance = findDistance(0, centerx, 0, centery);
		double bestDistance = 0;
		
		// outer loop traverses rows
		while (i < screening.getNumRows() && node != null)
		{
			while (j + totalTix - 1 < screening.getNumColumns() && node != null)
			{
				// inner loop traverses columns
				if (node.isAvailable(totalTix) == true)
				{
					bestDistance = findDistance(j + center, centerx, i + 1, centery);
					
					// if the new distance is less than the shortest distance
					if (bestDistance < shortestDistance)
					{
						// set the shortestDistance to the bestDistance
						shortestDistance = bestDistance;		
						
						// update coordinates to keep track of new location
						coordinates[0] = i + 1;	
						coordinates[1] = j;
					}
					
					// if there is a tie between shortest and best distance
					if (bestDistance == shortestDistance)
					{
						// choose the coordinates with the closest row 
						if (Math.abs(coordinates[0] - centery) > Math.abs((i + 1) - centery))
						{
							// update coordinates to keep track of new location
							shortestDistance = bestDistance;
							coordinates[0] = i + 1;
							coordinates[1] = j;
						}
						
						// else if both of the tied locations are on the same row...
						else if ((Math.abs(coordinates[0] - centery) == Math.abs((i + 1) - centery)))
						{
							// pick the smaller between the two
							if (i + 1 < coordinates[0])
							{
								// update coordinates to keep track of new location
								shortestDistance = bestDistance;
								coordinates[0] = i + 1;
								coordinates[1] = j;
							}
						}
					}
				}
				
				node = node.getRight();		// move to the next seat
				j++;
			}
			
			j = 0;		// after traversing through an entire row, set j back to 0
			node = head;	// set node equal to the first node in the row
			node = node.getDown();		// move down
			head = node;		// set head to the node
			i++;
		}
			
		return coordinates;		// return the coordinates of the best seat
	}//end locateBest
	
	// printSummary will display the results of the auditorium
	public static void printSummary(Auditorium A1)
	{
		System.out.print("\n");
		
		// call toString to display the auditorium
		System.out.println(A1.toString());
		
		// print out the total seats in the auditorium
		System.out.println("Total seats in auditorium: " + (A1.getNumRows()) * (A1.getNumColumns()));
		
		// print out the total tickets sold in the auditorium
		System.out.println("Total tickets sold: " + (A1.getAdult() + A1.getChild() + A1.getSenior()));
		
		// print out total adult tickets sold
		System.out.println("Adult tickets sold: " + A1.getAdult());
		
		// print out total child tickets sold
		System.out.println("Child tickets sold: " + A1.getChild());
		
		// print out total senior tickets sold
		System.out.println("Senior tickets sold: " + A1.getSenior());
		
		// print out total tickets sales
		System.out.printf("Total ticket sales: $" +  "%.2f", A1.getTotal());
	}//end printSummary
}//end program
