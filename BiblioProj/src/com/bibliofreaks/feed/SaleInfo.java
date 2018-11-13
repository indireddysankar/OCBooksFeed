 package com.bibliofreaks.feed;
 
 public class SaleInfo
 {
   private String country;
   private String saleability;
   private String isEbook;
 
   public String getCountry()
   {
     return this.country;
   }
   public void setCountry(String country) {
     this.country = country;
   }
   public String getSaleability() {
     return this.saleability;
   }
   public void setSaleability(String saleability) {
    this.saleability = saleability;
   }
   public String getIsEbook() {
    return this.isEbook;
   }
   public void setIsEbook(String isEbook) {
    this.isEbook = isEbook;
   }
 }

