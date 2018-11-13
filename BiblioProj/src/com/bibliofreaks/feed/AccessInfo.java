 package com.bibliofreaks.feed;
 
 public class AccessInfo
 {
   private String country;
   private String viewability;
   private String embeddable;
   private String publicDomain;
   private String textToSpeechPermission;
   private String webReaderLink;
   private String accessViewStatus;
 
   public String getCountry()
   {
     return this.country;
   }
   public void setCountry(String country) {
     this.country = country;
   }
   public String getViewability() {
     return this.viewability;
   }
   public void setViewability(String viewability) {
       this.viewability = viewability;
   }
   public String getEmbeddable() {
       return this.embeddable;
   }
   public void setEmbeddable(String embeddable) {
       this.embeddable = embeddable;
   }
   public String getPublicDomain() {
      return this.publicDomain;
   }
   public void setPublicDomain(String publicDomain) {
       this.publicDomain = publicDomain;
   }
   public String getTextToSpeechPermission() {
       return this.textToSpeechPermission;
   }
   public void setTextToSpeechPermission(String textToSpeechPermission) {
       this.textToSpeechPermission = textToSpeechPermission;
   }
   public String getWebReaderLink() {
       return this.webReaderLink;
   }
   public void setWebReaderLink(String webReaderLink) {
       this.webReaderLink = webReaderLink;
   }
   public String getAccessViewStatus() {
       return this.accessViewStatus;
   }
   public void setAccessViewStatus(String accessViewStatus) {
      this.accessViewStatus = accessViewStatus;
   }
 }


 