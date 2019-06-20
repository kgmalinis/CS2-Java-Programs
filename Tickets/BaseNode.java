/*
 	Kate Malinis 	<kgm170030>
 
  	CS 2336.003 -- Project 2
  	Dr. Oladimeji
  
  	This project implements a multi-directional linked list towards an auditorium and its movie screening.
  	
  	ABSTRACT BASE NODE CLASS
 */


package Tickets;

public abstract class BaseNode 
{
	// members
	protected int row;
	protected char seat;
	protected boolean reserved;
	protected char ticketType;
	
	// methods
	BaseNode()
	{
		row = 0;
		seat = 'A';
		reserved = false;
		ticketType = 'A';
	}
	
	// overloaded constructor
	BaseNode(int r, char s, boolean rsrvd, char type)
	{
		row = r;
		seat = s;
		reserved = rsrvd;
		ticketType = type;
	}
	
	// accessors
	public int getRow() {	return row;	}
	public char getSeat()	{	return seat;	}
	public boolean isReserved()	{	return reserved;	}
	public char getTicketType()	{	return ticketType;	}
	
	// mutators
	public void setRow(int r)	{	this.row = r;	}
	public void setSeat(char s)	{	this.seat = s;	}
	public void setReserved(boolean rsrvd)	{	this.reserved = rsrvd;	}
	public void setTicketType(char type)	{	this.ticketType = type;	}
}
