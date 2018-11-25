package org.jboss.docs.cmp.cd.bean;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import java.rmi.RemoteException;

/**
 * This class contains the implementation for the methods specified in the home
 * and remote interfaces for the "CD" EJB
 */
public class CDBean
implements EntityBean
{
   EntityContext ctx;

   public Integer id;
   public String title;
   public String artist;
   public String type;
   public String notes;

   /**
    * Create an instance of a CD. Note that this method returns null because the real
    * creation is managed by the EJB container.
    */
   public Integer ejbCreate (Integer _id)
   {
      id = _id;
      return null;
   }

   /**
    * Called when the object has been instantiated; does nothing in this example
    */
   public void ejbPostCreate(Integer id) { }

   public String getTitle() { return title; }
   public void setTitle(String _title) { title = _title; }

   public Integer getId() { return id; }
   public void setId(Integer _id) { id = _id; }

   public String getArtist() { return artist; }
   public void setArtist(String _artist) { artist = _artist; }

   public String getType() { return type; }
   public void setType(String _type) { type = _type; }

   public String getNotes() { return notes; }
   public void setNotes(String _notes) { notes = _notes; }

   public void setEntityContext(EntityContext ctx) { this.ctx = ctx; }

   public void unsetEntityContext() { ctx = null; }

   public void ejbActivate() { }
   public void ejbPassivate() { }
   public void ejbLoad() { }
   public void ejbStore() { }
   public void ejbRemove() { }

}

