/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome 
//
// Author    r.kommer.extern@micromata.de
// Created   18.02.2013
// Copyright Micromata 2013
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.jpa.test;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.Emgr;
import de.micromata.genome.jpa.EmgrTx;

/**
 * Entity Manager for AttrBaseDAOImpl
 * 
 * @author roger
 * 
 */
public class JpaTestEntMgr extends Emgr<JpaTestEntMgr>
{

  /**
   * @param entityManager
   */
  public JpaTestEntMgr(EntityManager entityManager, JpaTestEntMgrFactory emgrFactory, EmgrTx<JpaTestEntMgr> emgrTx)
  {
    super(entityManager, emgrFactory, emgrTx);
  }

}
