package com.utils;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import model.Bapteme;
import model.Fidele;
import model.Sacrement;
import model.User;

/**
 *
 * @author Harold
 */
@SuppressWarnings("rawtypes")
public class OperationsDb {
	
	public final static String QUERY_SEARCH_FIDELES = "select f from Fidele f where f.nom like :val or f.prenoms like :val "
                                      + " or exists (select b  from Bapteme b where b.fidele = f and b.numero like :val)" ;
	
	public static List doSearch(String entity, String value){
		List returnedList = null;
        
        switch(entity){            
            case(Constants.fideles):
            	
//            	Criteria crFideles = HibernateUtil.getHibSession().createCriteria(Fidele.class);
//        		crFideles.addOrder(Order.asc("id"));
//        		returnedList = crFideles.add(Restrictions.or(Restrictions.ilike(Constants.nom, "%"+value+"%"), 
//        				Restrictions.ilike(Constants.prenoms, "%"+value+"%"))).list();
//                returnedList = crFideles.list();
        		
            	Query q = HibernateUtil.getHibSession().createQuery(QUERY_SEARCH_FIDELES);
            	q.setParameter("val", "%"+value+"%");
            	returnedList = q.list();
            	break;
        }
        return returnedList;
	}
    
	public static List find (String strEntity, Map<String,Object> mapParams){
        
		List returnedList = null;
        
        switch(strEntity){
            
            case(Constants.users):
                       Criteria crUsers = HibernateUtil.getHibSession().createCriteria(User.class);
                       crUsers.addOrder(Order.asc("id"));
                       		if(mapParams != null){
                       			
                       			Integer id =  mapParams.get(Constants.id) != null ? (Integer) (mapParams.get(Constants.id)) : null; 
                           	   String nom = (String)mapParams.get(Constants.nom);
                          	   String prenoms = (String)mapParams.get(Constants.prenoms);
                          	   String identifiant = (String)mapParams.get(Constants.identifiant);
	                          	 String motPasse = (String)mapParams.get(Constants.mdp);
                           	   
                           	   
                       			if ( id!= null ){
                                         crUsers.add(Restrictions.eq(Constants.id, id));
                                     }                    			

                                if (nom != null && !nom.equals("")){
                                     	crUsers.add(Restrictions.ilike(Constants.nom, "%"+nom+"%"));
                                }

                                if (prenoms != null && !prenoms.equals("")){
                                     	crUsers.add(Restrictions.ilike(Constants.prenoms, "%"+prenoms+"%"));
                                }
                                
                                if (identifiant != null && !identifiant.equals("")){
                                 	crUsers.add(Restrictions.eq(Constants.identifiant, identifiant));
                                }
                                
                                if (motPasse != null && !motPasse.equals("")){
                                 	crUsers.add(Restrictions.eq(Constants.mdp, motPasse));
                                }
                                        					
                       		} // end if params != null

                       		returnedList = crUsers.list();
                        break;
            	case(Constants.fideles):
            	
            	Criteria crFideles = HibernateUtil.getHibSession().createCriteria(Fidele.class);
            	crFideles.addOrder(Order.asc("id"));
            		if(mapParams != null){
            			
            			Integer id =  mapParams.get(Constants.id) != null ? (Integer) (mapParams.get(Constants.id)) : null; 
                    	String nom = (String)mapParams.get(Constants.nom);
                   	    String prenoms = (String)mapParams.get(Constants.prenoms);
               	        Date dob = (Date)mapParams.get(Constants.dob);
                	   
                	   
            			if ( id!= null ){
            				crFideles.add(Restrictions.eq(Constants.id, id));
                          }                    			

                     if (nom != null && !nom.equals("")){
                    	 crFideles.add(Restrictions.ilike(Constants.nom, "%"+nom+"%"));
                     }

                     if (prenoms != null && !prenoms.equals("")){
                    	 crFideles.add(Restrictions.ilike(Constants.prenoms, "%"+prenoms+"%"));
                     }
                     
                     if (dob != null){
                    	 crFideles.add(Restrictions.eq(Constants.dob, dob));
                     }
            	}	// end if params != null
           
            	returnedList = crFideles.list();
            	break;
            	
            	case(Constants.bapteme):
            		Criteria crBapteme = HibernateUtil.getHibSession().createCriteria(Bapteme.class);
            		crBapteme.addOrder(Order.asc("id"));
            		crBapteme.createAlias("fidele", "fid");
            		if(mapParams != null){
            			Integer id =  mapParams.get(Constants.id) != null ? (Integer) (mapParams.get(Constants.id)) : null;
            			Integer fideleId =  mapParams.get(Constants.fideleId) != null ? (Integer) (mapParams.get(Constants.fideleId)) : null;
            			if ( id!= null ){
            				crBapteme.add(Restrictions.eq("id", id));
                          }
            			if ( fideleId!= null ){
            				crBapteme.add(Restrictions.eq("fid.id", fideleId));
                          }
            		}
            		returnedList = crBapteme.list();
            		break;
            		
            	case(Constants.sacrements):
            		Criteria crSacrements = HibernateUtil.getHibSession().createCriteria(Sacrement.class);
            			crSacrements.addOrder(Order.asc("id"));
            			crSacrements.createAlias("fidele", "fid");
            		if(mapParams != null){
            			Integer id =  mapParams.get(Constants.id) != null ? (Integer) (mapParams.get(Constants.id)) : null;
            			Integer fideleId =  mapParams.get(Constants.fideleId) != null ? (Integer) (mapParams.get(Constants.fideleId)) : null;
            			if ( id!= null ){
            				crSacrements.add(Restrictions.eq("id", id));
                          }
            			if ( fideleId!= null ){
            				crSacrements.add(Restrictions.eq("fid.id", fideleId));
                          }
            		}
            		returnedList = crSacrements.list();
            		break;
            		
            	case(Constants.mariage):
            		break;
        }
        
        System.out.println("list for keyword: "+strEntity +" -  size: "+returnedList.size());
        return returnedList;
    }
    
  
  public static void persistObject(Object obj){
       
          try{
              Session session = HibernateUtil.getHibSession();
              session.beginTransaction();
              session.save(obj);
              session.getTransaction().commit();
              session.close();
              
          } catch (HibernateException e){
              e.printStackTrace();
              
          }
  }

  public static void updateObject(Object obj){
       
          try{
              Session session = HibernateUtil.getHibSession();
              session.beginTransaction();
              session.merge(obj);  
              session.getTransaction().commit();
              session.close();
              
          } catch (HibernateException e){
              e.printStackTrace();
              
          }
  }
  
  
  
  public static Object getById(Class<?> type, Serializable id){
	  Object obj = null;
	  try{
    	  Session session = HibernateUtil.getHibSession();
    	  obj = session.get(type, id);  
	  } catch (HibernateException e){
          e.printStackTrace();
  }
	return obj;
  }
  
  public static void deleteById(Class<?> type, Serializable id) {
       
          try{
        	  Session session = HibernateUtil.getHibSession();
        	  Object persistentInstance = session.get(type, id);
        	  if (persistentInstance != null) {
        		  session.beginTransaction();
        	      session.delete(persistentInstance);
        	      session.getTransaction().commit();
                  session.close();
        	  }              
          } catch (Exception e){
              e.printStackTrace();
              
          }
  }
  
    
}
