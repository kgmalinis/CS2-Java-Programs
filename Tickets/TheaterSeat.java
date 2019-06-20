/*
 	Kate Malinis 	<kgm170030>
 
  	CS 2336.003 -- Project 2
  	Dr. Oladimeji
  
  	THEATER SEAT DERIVED CLASS
 */


package Tickets;

public class TheaterSeat extends BaseNode
{
	// members
	int numColumns;
	TheaterSeat up;
	TheaterSeat down;
	TheaterSeat left;
	TheaterSeat right;
	
	// methods
	TheaterSeat()
	{
		super();
		up = null;
		down = null;
		left = null;
		right = null;
		numColumns = 0;
	}
	
	// overloaded constructor
	TheaterSeat(int r, char s, boolean rsrvd, char type) 
	{
		super(r, s, rsrvd, type);
		up = null;
		down = null;
		left = null;
		right = null;
	}
	
	// accessors
	public TheaterSeat getUp() {	return up;	}
	public TheaterSeat getDown() {	return down; }
	public TheaterSeat getLeft() {	return left; }
	public TheaterSeat getRight() {	return right; }
	
	// mutators
	public void setUp(TheaterSeat up) {	this.up = up; }
	public void setDown(TheaterSeat down) {	this.down = down; }
	public void setLeft(TheaterSeat left) {	this.left = left; }
	public void setRight(TheaterSeat right) {	this.right = right; }
	
	// isAvailable returns if the seats are not reserved
	public boolean isAvailable(int totalTickets)
	{
		int index = 0;
		TheaterSeat ptr = this;
		
		// while ptr != node && it is not reserved..
		while (ptr != null && index < totalTickets && ptr.reserved == false)
		{
			index++;		// increment the index
			ptr = ptr.right;	// and move to the right
		}
		
		// if index != totalTickets, there were not enough seats
		if (index < totalTickets)	
			return false;
		
		return true;		// else, seats are available
	}
}
