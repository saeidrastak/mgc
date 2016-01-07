package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.metadata.FieldDescriptor;
import org.hibernate.search.metadata.IndexedTypeDescriptor;
import org.hibernate.search.metadata.PropertyDescriptor;

import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.ColumnMetadataBean;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.ClassUtils;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchFieldInfoProvider;
import de.micromata.mgc.jpa.hibernatesearch.api.HibernateSearchInfo;
import de.micromata.mgc.jpa.hibernatesearch.api.ISearchEmgr;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchColumnMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEntityMetadata;

/**
 * Figure out all Search Fields from the entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */

public class SearchEmgrFactoryRegistryUtils
{

  private static final Logger LOG = Logger.getLogger(SearchEmgrFactoryRegistryUtils.class);

  /**
   * Dummy method to hold default IndexedEmbedded anot.
   */
  @IndexedEmbedded()
  public static void annotationDummyMethod()
  {

  }

  private static IndexedEmbedded DEFAULT_IndexedEmbedded;

  static {
    Method method = ClassUtils.findMethod(SearchEmgrFactoryRegistryUtils.class, "annotationDummyMethod");
    DEFAULT_IndexedEmbedded = method.getAnnotation(IndexedEmbedded.class);
  }

  public static void initJpaMetadataRepostory(SearchEmgrFactory<?> emf)
  {
    emf.runWoTrans((emgr) -> {

      JpaMetadataRepostory repo = emf.getMetadataRepository();
      Map<Class<?>, SearchEntityMetadata> entitiesWithSearchFields = new HashMap<>();
      for (EntityMetadata entm : repo.getEntities().values()) {
        SearchEntityMetadata sf = getSearchFields(emgr, repo, entm, Integer.MAX_VALUE);
        entitiesWithSearchFields.put(entm.getJavaType(), sf);
      }
      resolveEmbeddedFields(emgr, entitiesWithSearchFields);
      repo.getServiceCustomAttributes().put(SearchEmgrFactory.REPO_ENTITY_SEARCHFIELDS, entitiesWithSearchFields);
      return null;
    });

  }

  private static void resolveEmbeddedFields(ISearchEmgr<?> emgr,
      Map<Class<?>, SearchEntityMetadata> entitiesWithSearchFields)
  {
    for (Map.Entry<Class<?>, SearchEntityMetadata> me : entitiesWithSearchFields.entrySet()) {
      resolveEmbeddedEntityFields(emgr, entitiesWithSearchFields, me.getValue());
    }

  }

  private static void resolveEmbeddedEntityFields(ISearchEmgr<?> emgr,
      Map<Class<?>, SearchEntityMetadata> entitiesWithSearchFields,
      SearchEntityMetadata sem)
  {
    SearchEntityMetadataBean semb = (SearchEntityMetadataBean) sem;
    if (semb.isResolved() == true) {
      return;
    }
    Set<String> names = new HashSet<>(sem.getColumns().keySet());
    for (String name : names) {
      SearchColumnMetadata cd = sem.getColumns().get(name);
      if ((cd instanceof NestedSearchColumnMetaBean) == false) {
        continue;
      }
      NestedSearchColumnMetaBean nscm = (NestedSearchColumnMetaBean) cd;
      if (nscm.isResolved() == true) {
        continue;
      }
      EntityMetadata targetent = nscm.getColumnMetadata().getTargetEntity();
      SearchEntityMetadata nsem = entitiesWithSearchFields.get(targetent.getJavaType());
      if (nsem == null) {
        throw new IllegalArgumentException(
            "Cannot find nested index type: " + targetent.getJavaType() + " for "
                + sem.getEntityMetadata().getJavaType().getName() + "." + name);
      }
      int maxDepth = nscm.getIndexEmbedded().depth();
      int curDepth = 0;
      String prefix = name;
      addNested(entitiesWithSearchFields, sem, nsem, prefix, curDepth, maxDepth, nscm.getIndexEmbedded());
    }
    semb.setResolved(true);
  }

  private static boolean includeNested(String name, SearchColumnMetadata scm, IndexedEmbedded indexedEmbedded,
      int curDepth, int maxDepth)
  {
    if (indexedEmbedded.includeEmbeddedObjectId() == false && scm.isIdField() == true) {
      return false;
    }
    int dotcount = StringUtils.countMatches(name, ".");
    if (dotcount > 0) {
      return false;
    }
    if (ArrayUtils.isEmpty(indexedEmbedded.includePaths()) == true) {
      return true;
    }
    for (String incp : indexedEmbedded.includePaths()) {
      String[] splittet = StringUtils.split(incp, '.');
      if (ArrayUtils.getLength(splittet) <= curDepth) {
        continue;
      }
      if (splittet[curDepth].equals(name) == true) {
        return true;
      }
    }
    return false;
  }

  private static void addNested(Map<Class<?>, SearchEntityMetadata> entitiesWithSearchFields,
      SearchEntityMetadata sem, SearchEntityMetadata nsem,
      String prefix, int curDepth, int maxDepth, IndexedEmbedded indexedEmbedded)
  {
    if (maxDepth < 0) {
      return;
    }
    for (Map.Entry<String, SearchColumnMetadata> nc : nsem.getColumns().entrySet()) {
      SearchColumnMetadata col = nc.getValue();
      if (includeNested(nc.getKey(), col, indexedEmbedded, curDepth, maxDepth) == false) {
        continue;
      }
      String key = nc.getKey();

      String subprefix = prefix + "." + key;
      if (col instanceof NestedSearchColumnMetaBean) {
        if (maxDepth <= 1) {
          continue;
        }
        NestedSearchColumnMetaBean nscm = (NestedSearchColumnMetaBean) col;
        EntityMetadata targetent = nscm.getColumnMetadata().getTargetEntity();
        SearchEntityMetadata nnscm = entitiesWithSearchFields.get(targetent.getJavaType());
        if (nsem == null) {
          throw new IllegalArgumentException(
              "Cannot find nested index type: " + targetent.getJavaType() + " for " + prefix + "."
                  + nc.getKey());
        }

        addNested(entitiesWithSearchFields, sem, nnscm, subprefix, ++curDepth, --maxDepth,
            indexedEmbedded);
      } else {
        SearchColumnMetadataBean copy = ((SearchColumnMetadataBean) col).createCopy();
        copy.setName(subprefix);
        sem.getColumns().put(copy.getName(), copy);
      }

    }

  }

  private static SearchEntityMetadata getSearchFields(ISearchEmgr<?> emgr, JpaMetadataRepostory repo,
      EntityMetadata entm,
      int maxDepth)
  {
    //    if (entm.getJavaType().getName().indexOf("PfHistoryMasterDO") != -1) {
    //      System.out.println("historymaster");
    //    }
    SearchEntityMetadataBean sem = new SearchEntityMetadataBean(entm);

    addHibernateSearchOwnFields(emgr, sem.getColumns(), entm);
    addAnnotationSearchFields(emgr, sem.getColumns(), entm);

    addCustomSearchFields(emgr, entm, sem.getColumns());

    return sem;
  }

  private static void addAnnotationSearchFields(ISearchEmgr<?> emgr, Map<String, SearchColumnMetadata> ret,
      EntityMetadata entm)
  {
    for (ColumnMetadata cm : entm.getColumns().values()) {

      {
        Field field = cm.findAnnoation(Field.class);
        if (field != null) {
          String name = field.name();

          if (StringUtils.isBlank(name) == true) {
            name = cm.getName();
          }
          if (ret.containsKey(name) == true) {
            continue;
          }
          LOG.warn("Found @Field not in Hibernate Search metadata: " + entm.getJavaType().getName() + "." + name);
          SearchColumnMetadataBean scmb = new SearchColumnMetadataBean(name, cm);
          scmb.setAnalyzed(field.analyze() == Analyze.YES);
          scmb.setIndexed(field.index() == Index.YES);
          scmb.setStored(field.store() == Store.YES);
          ret.put(name, scmb);
          SortableField sfield = cm.findAnnoation(SortableField.class);
          if (sfield != null) {
            // TODO RK set sortable.
          }
          continue;
        }
      }

      {

        IndexedEmbedded iemb = cm.findAnnoation(IndexedEmbedded.class);

        if (iemb != null) {
          NestedSearchColumnMetaBean nsc = new NestedSearchColumnMetaBean(cm.getName(), cm, iemb);
          ret.put(cm.getName(), nsc);
        }

      }
    }

  }

  private static void addHibernateSearchOwnFields(ISearchEmgr<?> emgr, Map<String, SearchColumnMetadata> ret,
      EntityMetadata entm)
  {
    IndexedTypeDescriptor tddesc = emgr.getFullTextEntityManager().getSearchFactory()
        .getIndexedTypeDescriptor(entm.getJavaType());
    if (tddesc == null) {
      return;
    }
    Set<FieldDescriptor> fields = tddesc.getIndexedFields();

    for (PropertyDescriptor idesc : tddesc.getIndexedProperties()) {
      String name = idesc.getName();
      if (ret.containsKey(name) == true) {
        continue;
      }
      ColumnMetadata coldesc = entm.findColumn(name);

      if (coldesc == null) {
        ColumnMetadataBean dcoldesc = new ColumnMetadataBean(entm);
        dcoldesc.setName(name);
        dcoldesc.setJavaType(String.class);
        coldesc = dcoldesc;
      }
      Set<FieldDescriptor> indexedfields = idesc.getIndexedFields();
      indexedfields.size();

      if (idesc.getIndexedFields().isEmpty() == true) {
        LOG.warn("Field has no indexed fields: " + entm.getJavaType().getName() + "." + name);
      }
      boolean isId = idesc.isId();
      for (FieldDescriptor fdesc : idesc.getIndexedFields()) {
        SearchColumnMetadataBean nb = new SearchColumnMetadataBean(fdesc.getName(), coldesc);
        switch (fdesc.getType()) {
          case BASIC:
            nb.setIndexType(String.class);
            break;
          case NUMERIC:
            nb.setIndexType(Long.class);
            break;
          case SPATIAL:
            nb.setIndexType(void.class);
            break;
        }
        nb.setIdField(isId);
        nb.setIndexed(fdesc.getIndex() == Index.YES);
        nb.setAnalyzed(fdesc.getAnalyze() == Analyze.YES);
        nb.setStored(fdesc.getStore() == Store.YES);
        ret.put(fdesc.getName(), nb);
      }

    }
  }

  private static void addCustomSearchFields(ISearchEmgr<?> emgr, EntityMetadata entm,
      Map<String, SearchColumnMetadata> ret)
  {
    List<HibernateSearchInfo> cil = ClassUtils.findClassAnnotations(entm.getJavaType(), HibernateSearchInfo.class);
    for (HibernateSearchInfo ci : cil) {
      HibernateSearchFieldInfoProvider fip = PrivateBeanUtils.createInstance(ci.fieldInfoProvider());
      Map<String, SearchColumnMetadata> sm = fip.getAdditionallySearchFields(entm, ci.param());
      ret.putAll(sm);
    }
  }

  //  private static void addNestedSearchFields(ISearchEmgr<?> emgr, JpaMetadataRepostory repo, ColumnMetadata masterColumn,
  //      IndexedEmbedded iemb, int maxDepth,
  //      Map<String, ColumnMetadata> ret)
  //  {
  //    if (maxDepth < 1) {
  //      return;
  //    }
  //    String[] paths = iemb.includePaths();
  //    if (paths.length > 0) {
  //      for (String path : paths) {
  //        ret.put(masterColumn.getName() + "." + path, masterColumn);
  //      }
  //      return;
  //    }
  //    EntityMetadata assicatedEntity = getAssocatedJavaType(masterColumn);
  //    if (assicatedEntity == null) {
  //      LOG.error("Search; Cannot find EntityMetadata for " + masterColumn.getJavaType().getName());
  //      return;
  //    }
  //    int maxLevel = Math.min(iemb.depth(), maxDepth);
  //    // 
  //    //    Map<String, ColumnMetadata> nested = getSearchFields(emgr, repo, assicatedEntity, maxLevel);
  //    //    for (Map.Entry<String, ColumnMetadata> me : nested.entrySet()) {
  //    //      ret.put(masterColumn.getName() + "." + me.getKey(), masterColumn);
  //    //    }
  //  }

  //  private static EntityMetadata getAssocatedJavaType(ColumnMetadata masterColumn)
  //  {
  //    EntityMetadata targetEnt = masterColumn.getTargetEntity();
  //    Class<?> javaType = masterColumn.getJavaType();
  //    if (Collection.class.isAssignableFrom(javaType) == true || Map.class.isAssignableFrom(javaType) == true) {
  //      if (targetEnt == null) {
  //        LOG.error("Have to define attribute targetEntity in anntotation to support search: "
  //            + masterColumn.getShortDeclaration());
  //      }
  //    }
  //    return targetEnt;
  //  }

}
