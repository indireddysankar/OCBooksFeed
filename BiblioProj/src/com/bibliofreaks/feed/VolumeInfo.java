 package com.bibliofreaks.feed;
 
 import java.util.ArrayList;
 
 public class VolumeInfo
 {
  private String title = null;
  private ArrayList<String> authors = null;
  private String publisher = null;
  private String publishedDate = null;
  private String description = null;
  ArrayList<IndustryIdentifiers> industryIdentifiers = null;
   private String printType;
   private String averageRating;
   private String ratingsCount;
   private String contentVersion;
   private String language;
   private String previewLink;
   private String infoLink;
   private String canonicalVolumeLink;
   private ImageLinks imageLinks = null;
 
   public String getTitle() { return this.title; }
 
   public void setTitle(String title) {
     this.title = title;
   }
   public ArrayList<String> getAuthors() {
     return this.authors;
   }
   public void setAuthors(ArrayList<String> authors) {
    this.authors = authors;
   }
   public String getPublisher() {
    return this.publisher;
   }
   public void setPublisher(String publisher) {
     this.publisher = publisher;
   }
   public String getPublishedDate() {
     return this.publishedDate;
   }
   public void setPublishedDate(String publishedDate) {
     this.publishedDate = publishedDate;
   }
   public String getDescription() {
    return this.description;
   }
   public void setDescription(String description) {
     this.description = description;
   }
   public ArrayList<IndustryIdentifiers> getIndustryIdentifiers() {
     return this.industryIdentifiers;
   }
 
   public void setIndustryIdentifiers(ArrayList<IndustryIdentifiers> industryIdentifiers) {
    this.industryIdentifiers = industryIdentifiers;
   }
   public String getPrintType() {
     return this.printType;
   }
   public void setPrintType(String printType) {
  this.printType = printType;
   }
   public String getAverageRating() {
    return this.averageRating;
   }
   public void setAverageRating(String averageRating) {
    this.averageRating = averageRating;
   }
   public String getRatingsCount() {
     return this.ratingsCount;
   }
   public void setRatingsCount(String ratingsCount) {
    this.ratingsCount = ratingsCount;
   }
   public String getContentVersion() {
    return this.contentVersion;
   }
   public void setContentVersion(String contentVersion) {
    this.contentVersion = contentVersion;
   }
   public String getLanguage() {
    return this.language;
   }
   public void setLanguage(String language) {
    this.language = language;
   }
   public String getPreviewLink() {
   return this.previewLink;
   }
   public void setPreviewLink(String previewLink) {
    this.previewLink = previewLink;
   }
   public String getInfoLink() {
     return this.infoLink;
   }
   public void setInfoLink(String infoLink) {
     this.infoLink = infoLink;
   }
   public String getCanonicalVolumeLink() {
    return this.canonicalVolumeLink;
   }
   public void setCanonicalVolumeLink(String canonicalVolumeLink) {
     this.canonicalVolumeLink = canonicalVolumeLink;
   }
   public ImageLinks getImageLinks() {
    return this.imageLinks;
   }
   public void setImageLinks(ImageLinks imageLinks) {
   this.imageLinks = imageLinks;
   }
 }

