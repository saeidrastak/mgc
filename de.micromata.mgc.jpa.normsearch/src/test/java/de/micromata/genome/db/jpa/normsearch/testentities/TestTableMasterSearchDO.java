package de.micromata.genome.db.jpa.normsearch.testentities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.junit.Ignore;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
@Ignore
@Entity
@Table(name = "TB_TST_NSEARCHM_NSEARCH")
@SequenceGenerator(name = "SQ_TST_NSEARCHM_NSEARCH_PK", sequenceName = "SQ_TST_NSEARCHM_NSEARCH_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(//
    indexes = { //
        @Index(name = "IX_TST_AM_NSEARCH_MODAT", columnNames = { "MODIFIEDAT" }), //
        @Index(name = "IX_TST_AM_NSH_PARENT_FK", columnNames = { "PARENT" }), //
        @Index(name = "IX_TST_AM_NSH_PV1", columnNames = { "VALUE", "PARENT" }), //
        @Index(name = "IX_TST_AM_NSH_PV2", columnNames = { "VALUE", "TST_NSEARCHM_NSEARCH" }), //
        @Index(name = "IX_TST_AM_NSH_VALUE", columnNames = { "VALUE" }),//
    }, appliesTo = "TB_TST_NSEARCHM_NSEARCH")
public class TestTableMasterSearchDO extends NormSearchDO
{

  private static final long serialVersionUID = 2430755316655780089L;

  @Id
  @Column(name = "TST_NSEARCHM_NSEARCH")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TST_NSEARCHM_NSEARCH_PK")
  @Override
  public Long getPk()
  {
    return pk;
  }
}
