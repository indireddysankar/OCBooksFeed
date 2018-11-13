 package com.bibliofreaks.feed;
 
 import java.util.ArrayList;
 
 public class BookInfo
 {
   private String kind = null;
  private int totalItems = 0;
   ArrayList<Items> items = null;
 
   public String getKind() { return this.kind; }
 
   public void setKind(String kind) {
     this.kind = kind;
   }
   public int getTotalItems() {
     return this.totalItems;
   }
   public void setTotalItems(int totalItems) {
     this.totalItems = totalItems;
   }
   public ArrayList<Items> getItems() {
     return this.items;
   }
   public void setItems(ArrayList<Items> items) {
     this.items = items;
   }

@Override
public String toString() {
	return "BookInfo [kind=" + kind + ", totalItems=" + totalItems + ", items="
			+ items + "]";
}
   
 }

