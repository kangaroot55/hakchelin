package com.example.hakchelin;

public class Menu {
	
	public String loc;
	public String price;
	public String menu;
	public String rate;
	public float sum;
	public int human;
	public int menu_id;

	public Menu(){
		
	}
	public Menu(String loc, String price, String menu, String rate, float sum, int human, int menu_id){
		this.loc=loc;
		this.price=price;
		this.menu=menu;
		this.sum=sum;
		this.human=human;
		this.rate="0.00";
		this.menu_id=menu_id;
	}
	
	public Menu(String loc, String price, String menu, String rate, float sum, int human){
		this.loc=loc;
		this.price=price;
		this.menu=menu;
		this.sum=sum;
		this.human=human;
		this.rate="0.00";
	}
	

	public String getLoc(){
		return this.loc;
	}
	
	public String getPrice(){
		return this.price;
	}
	
	public String getMenu(){
		return this.menu;
	}
	
	public float getSum(){
		return this.sum;
	}
	
	public int getHuman(){
		return this.human;
	}
	
	public int getMenu_id(){
		return this.menu_id;
	}
	
	public void plusSum(float s){
		this.sum+=s;
		this.human++;
	}
	
	public String getRate(){
		if(this.human!=0)
			return String.format("%.2f", this.sum/(float)this.human);
		else 
			return "0.00";
	}
	
	public void setLoc(String loc){
		this.loc=loc;
	}
	
	public void setPrice(String price){
		this.price=price;
	}
	
	public void setMenu(String menu){
		this.menu=menu;
	}
	
	public void setSum(float sum){
		this.sum=sum;
	}
	
	public void setHuman(int human){
		this.human=human;
	}
	
	public void setMenu_id(int menu_id){
		this.menu_id=menu_id;
	}
	

	public void setRate(String rate){
		this.rate=rate;
	}
}
