/*
 	Kate Malinis 	<kgm170030>
 
  	CS 2336.003 -- Project 2
  	Dr. Oladimeji
  
  	This project implements a multi-directional linked list towards an auditorium and its movie screening.
  	
  	AUDITORIUM CLASS
 */

package Tickets;

import java.util.Scanner;
import java.io.PrintWriter;

public class Auditorium 
{
	// members
	private TheaterSeat first = null;	// head pointer 
	private int numColumns, numRows;
	
	// methods
	public Auditorium()
	{
		// default constructor
	}
	
	// overloaded constructor
	public Auditorium(Scanner input)
	{
		// initialize number of rows to 0 with each instance of auditorium
		numRows = 0;
		
		// create a new node instance of TheaterSeat
		TheaterSeat newInstance = new TheaterSeat();
		first = newInstance;
		TheaterSeat checkRow;
		
		// get the first line of the file
		String file_input;
		file_input = input.nextLine();
		
		numColumns = file_input.length();		// number of columns = length of file_input
		
		// instantiate the head 
		numRows++;
		first.row = 1;
		first.seat = 'A';
		first.ticketType = file_input.charAt(0);
		first.reserved = (first.ticketType != '.');
		
		checkRow = newInstance;		// checkRow will keep track of the previous row
		int check = 1;
		
		// this loop gets the first row
		for (int index = 1; index < file_input.length(); index++)
		{
			TheaterSeat newNode = new TheaterSeat();	// create a new node
			newNode.row = 1;		// get the row and seat
			newNode.seat = (char)(index + 65);
			
			// now get the data
			newNode.reserved = (file_input.charAt(index) != '.');	// if there is a '#' it is reserved, otherwise it is false
			newNode.ticketType = file_input.charAt(index);
			
			newInstance.right = newNode;	// link newInstance to newNode
			newNode.left = newInstance;	// link newNode to newInstance
			newInstance = newNode;		// move newInstance to newNode
		}		
		
		while (input.hasNext())
		{
			// get the first node in the row
			file_input = input.nextLine();
			numRows++;
			TheaterSeat trackRow = new TheaterSeat(check, 'A', file_input.charAt(0) != '.', file_input.charAt(0));
			
			// trackRow will keep track of the previous node in the row
			TheaterSeat firstInRow = trackRow;
			
			trackRow.up = checkRow;		// link trackRow to the row above
			checkRow.down = trackRow;		// link the node above to trackRow
			checkRow = checkRow.right;	// move checkRow over
			
			for (int j = 1; j < file_input.length(); j++)
			{
				// get the data like previous
				TheaterSeat newNode = new TheaterSeat();
				newNode.row = check;
				newNode.seat = (char)(j + 65);
				newNode.ticketType = file_input.charAt(j);
				newNode.reserved = (newNode.ticketType != '.');
				
				newNode.up = checkRow;		// connect this node to the one above it
				checkRow.down = newNode;	// connect the node above to the one below
				checkRow = checkRow.right;		// move checkRow to the next seat to the right
				newNode.left = trackRow;	// link newNode to the previous
				trackRow.right = newNode;	// link the previous to the current
				trackRow = newNode;		// move trackRow to newNode
			}		
			
			checkRow = firstInRow;	// goes back to the start of the row
			
			check++;
			
		}
	}
	
	// mutators
	public void setFirst(TheaterSeat f)		{ first = f;	}

	// accessors
	public TheaterSeat getFirst()	{ return first;	}
	public int getNumColumns()	{	return numColumns;	}
	public int getNumRows()	{	return numRows;	}
	
	
	@Override
	// toString() prints out the auditorium for the user
	public String toString()
	{
		System.out.print("\n\n");
		String seats = "  ";
		
		// variables for traversing through the auditorium
		TheaterSeat row = first;
		TheaterSeat column = first;
		
		// print the seats while column != null
		int index = 0;
		while (column != null)
		{
			seats += (char)(index + 65);
			index++;
			column = column.right;		// move to the next seat
		}
		
		column = first;		// set the column back to first
		seats += '\n';		// add newline
		
		// print the rows while row != null
		index = 0;
		while (row != null)
		{
			seats += (index + 1);	// prints out the row number
			seats += " ";
			
			// display the reservation station ('#' or '.') for each seat
			while (column != null)
			{
				if (column.ticketType == '.')
					seats += '.';
				else
					seats += '#';
				
				column = column.right;		// move to the next column
			}
			
			row = row.down;		// move down a row
			column = row;		// set column equal to row pointer
			
			seats += '\n';		// add newline
			index++;
		}
		
		return seats;
	}
	
	// seatsAvailable returns whether or not given seat(s) are reserved
	public boolean seatsAvailable(int row, int adult, int child, int senior, char choice)
	{
		TheaterSeat node = first;
		
		// iterate through rowIndex to get to user-given row
		int rowIndex = 0;
		while (rowIndex < row)
		{
			if (node.down == null)	// if node is null, then seat is not available
				return false;
			node = node.down;		// move node down 
			rowIndex++;
		}
		
		// iterate through columnIndex to get to user-given column
		int columnIndex = 0;
		while (columnIndex < (choice - 65))		// ASCII starts at 65 for the alphabet
		{
			if (node.right == null)	// if node is null, then seat is not available
				return false;
			node = node.right;		// move node to the right
			columnIndex++;
		}
		
		// updating the seats begins here
		int index = 0;
		TheaterSeat update = node;
		
		// we first have to make sure there are enough seats to store the tickets
		// we do this by updating the index until we reach null or until we find a reserved seat
		while (node != null && index < (adult + child + senior) && node.isReserved() == false)
		{
			node = node.right;
			index++;
		}
		
		// if index != totalTickets
			// then there are not enough seats
		// else
			// update the auditorium
		if (index == (adult + child + senior))
		{
			// update adult tickets
			for (int aIndex = 0; aIndex < adult; aIndex++)
			{
				update.ticketType = 'A';		// ticketType becomes A
				update.reserved = true;			// seat becomes reserved
				update = update.right;			// go to the right
			}
			
			// update child tickets
			for (int cIndex = 0; cIndex < child; cIndex++)
			{
				update.ticketType = 'C';		// ticketType becomes C
				update.reserved = true;			// seat becomes reserved
				update = update.right;			// go to the right
			}
			
			// update senior tickets
			for (int sIndex = 0; sIndex < senior; sIndex++)
			{
				update.ticketType = 'S';		// ticketType becomes S
				update.reserved = true;			// seat becomes reserved
				update = update.right;			// go to the right
			}
			return true;
		}
		
		else	// else, there were not enough seats
			return false;	// return false to indicate not enough seats
	}
	
	// updateAuditorium updates the auditorium based on user-given input
	public void updateAuditorium(int row, int column, int adult, int child, int senior)
	{
		TheaterSeat node = first;
		
		// locate the row we want to update
		for (int index = 0; index < row; index++)
			node = node.down;
		
		// locate the column we want to update
		for (int cIndex = 0; cIndex < column; cIndex++)
			node = node.right;
		
		// put in all the adult tickets
		for (int aIndex = 0; aIndex < adult; aIndex++)
		{
			node.ticketType = 'A';
			node.reserved = true;
			node = node.right;
		}
		
		// put in all the child tickets
		for (int chIndex = 0; chIndex < child; chIndex++)
		{
			node.ticketType = 'C';
			node.reserved = true;
			node = node.right;
		}
		
		// put in all the senior tickets
		for (int sIndex = 0; sIndex < senior; sIndex++)
		{
			node.ticketType = 'S';
			node.reserved = true;
			node = node.right;
		}
	}
	
	// writeFile prints out the updated file status
	public void writeFile(PrintWriter updatedFile)
	{
		TheaterSeat node = first;		// node will navigate the auditorium
		TheaterSeat rowT = node;		// rowT will keep track of the start of rows
		
		// iterate through rows
		for (int index = 0; index < numRows; index++)
		{
			// iterate through columns
			for (int j = 0; j < numColumns; j++)
			{
				updatedFile.print(node.ticketType);		// print the ticket type
				node = node.right;	
			}
			
			updatedFile.println();
			rowT = rowT.down;		// goes down
			node = rowT;			// sets node to the next row
		}
	}
	
	// getAdult calculates the number of adults in the auditorium
	public int getAdult()
	{
		TheaterSeat node = first;
		TheaterSeat rowNode = node;
		
		int totalAdults = 0;
		
		for (int index = 0; index < numRows; index++)
		{
			for (int j = 0; j <  numColumns; j++)
			{
				if (node.ticketType == 'A')
					totalAdults++;
				node = node.right;
			}
			rowNode = rowNode.down;
			node = rowNode;
		}
		return totalAdults;
	}
	
	// getChild calculates the number of children in the auditorium
	public int getChild()
	{
		TheaterSeat node = first;
		TheaterSeat rowNode = node;
		
		int totalChildren = 0;
		
		for (int index = 0; index < numRows; index++)
		{
			for (int j = 0; j <  numColumns; j++)
			{
				if (node.ticketType == 'C')
					totalChildren++;
				node = node.right;
			}
			rowNode = rowNode.down;
			node = rowNode;
		}
		return totalChildren;
	}
	
	// getSenior calculates the number of seniors in the auditorium
	public int getSenior()
	{
		TheaterSeat node = first;
		TheaterSeat rowNode = node;
		
		int totalSeniors = 0;
		
		for (int index = 0; index < numRows; index++)
		{
			for (int j = 0; j <  numColumns; j++)
			{
				if (node.ticketType == 'S')
					totalSeniors++;
				node = node.right;
			}
			
			rowNode = rowNode.down;
			node = rowNode;
		}
		return totalSeniors;
	}
	
	// getTotal calculates the total ticket sales within each seating arrangement of the auditorium
	public double getTotal()
	{
		TheaterSeat node = first;
		TheaterSeat rowT = node;
		double total = 0;
		
		for (int index = 0; index < numRows; index++)
		{
			for (int j = 0; j < numColumns; j++)
			{
				if (node.ticketType == 'A')		// adult price = $10.00
					total += 10.00;
				else if(node.ticketType == 'C')	// children price = $5.00
					total += 5.00;
				else if(node.ticketType == 'S')	// senior price = $7.50
					total += 7.50;
				node = node.right;
			}
			
			rowT = rowT.down;	// goes down
			node = rowT;		// sets node to to the next row
		}
		return total;
	}
}
