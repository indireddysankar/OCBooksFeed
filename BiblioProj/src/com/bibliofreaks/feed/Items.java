 package com.bibliofreaks.feed;
 
 public class Items
 {
  private String kind = null;
  private String id = null;
  private String etag = null;
  private String selfLink = null;
  private VolumeInfo volumeInfo = null;
  private SaleInfo saleInfo = null;
  private AccessInfo accessInfo = null;
 
  public String getKind() { return this.kind; }
 
   public void setKind(String kind) {
   this.kind = kind;
   }
   public String getId() {
     return this.id;
   }
   public void setId(String id) {
     this.id = id;
   }
   public String getEtag() {
     return this.etag;
   }
   public void setEtag(String etag) {
     this.etag = etag;
   }
   public String getSelfLink() {
     return this.selfLink;
   }
   public void setSelfLink(String selfLink) {
     this.selfLink = selfLink;
   }
   public VolumeInfo getVolumeInfo() {
     return this.volumeInfo;
   }
   public void setVolumeInfo(VolumeInfo volumeInfo) {
     this.volumeInfo = volumeInfo;
   }
   public SaleInfo getSaleInfo() {
     return this.saleInfo;
   }
   public void setSaleInfo(SaleInfo saleInfo) {
     this.saleInfo = saleInfo;
   }
   public AccessInfo getAccessInfo() {
     return this.accessInfo;
   }
   public void setAccessInfo(AccessInfo accessInfo) {
     this.accessInfo = accessInfo;
   }
 }

