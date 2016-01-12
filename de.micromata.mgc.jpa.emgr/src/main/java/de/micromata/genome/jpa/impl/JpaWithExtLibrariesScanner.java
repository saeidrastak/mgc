package de.micromata.genome.jpa.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.boot.archive.internal.StandardArchiveDescriptorFactory;
import org.hibernate.boot.archive.scan.internal.ScanResultCollector;
import org.hibernate.boot.archive.scan.spi.ClassFileArchiveEntryHandler;
import org.hibernate.boot.archive.scan.spi.NonClassFileArchiveEntryHandler;
import org.hibernate.boot.archive.scan.spi.PackageInfoArchiveEntryHandler;
import org.hibernate.boot.archive.scan.spi.ScanEnvironment;
import org.hibernate.boot.archive.scan.spi.ScanOptions;
import org.hibernate.boot.archive.scan.spi.ScanParameters;
import org.hibernate.boot.archive.scan.spi.ScanResult;
import org.hibernate.boot.archive.scan.spi.Scanner;
import org.hibernate.boot.archive.spi.ArchiveContext;
import org.hibernate.boot.archive.spi.ArchiveDescriptor;
import org.hibernate.boot.archive.spi.ArchiveDescriptorFactory;
import org.hibernate.boot.archive.spi.ArchiveEntry;
import org.hibernate.boot.archive.spi.ArchiveEntryHandler;
import org.hibernate.jpa.boot.internal.StandardJpaScanEnvironmentImpl;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.matcher.BooleanListRulesFactory;
import de.micromata.genome.util.matcher.CommonMatchers;
import de.micromata.genome.util.matcher.Matcher;

/**
 * Scanner, which looks into class path, and optionally additionally libraries.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Florian Blumenstein
 *
 */
public class JpaWithExtLibrariesScanner implements Scanner
{
  static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JpaWithExtLibrariesScanner.class);

  /**
   * Class name which has to implement JpaExtScannerUrlProvider.
   */
  public static final String EXTLIBURLPROVIDER = "de.micromata.genome.jpa.extlibrary.urlprovider";
  /**
   * Url matcher expression
   */
  public static final String EXTLIBURLMATCHER = "de.micromata.genome.jpa.extlibrary.urlmatcher";

  private final ArchiveDescriptorFactory archiveDescriptorFactory;

  private final Map<String, ArchiveDescriptorInfo> archiveDescriptorCache = new HashMap<String, ArchiveDescriptorInfo>();

  public JpaWithExtLibrariesScanner()
  {
    this(StandardArchiveDescriptorFactory.INSTANCE);
  }

  public JpaWithExtLibrariesScanner(ArchiveDescriptorFactory value)
  {
    this.archiveDescriptorFactory = value;
  }

  @Override
  public ScanResult scan(ScanEnvironment environment, ScanOptions options, ScanParameters parameters)
  {
    ScanResultCollector collector = new ScanResultCollector(environment, options, parameters);

    if (environment.getNonRootUrls() != null) {
      ArchiveContext context = new ArchiveContextImpl(false, collector);
      for (URL url : environment.getNonRootUrls()) {
        ArchiveDescriptor descriptor = buildArchiveDescriptor(url, false);
        descriptor.visitArchive(context);
      }
    }
    Set<URL> loadedUrls = new HashSet<>();
    if (environment.getRootUrl() != null) {
      URL rootUrl = environment.getRootUrl();
      visitUrl(rootUrl, collector, CommonMatchers.always());
    }
    visitExternUrls(environment, collector, loadedUrls);
    return collector.toScanResult();
  }

  protected void visitUrl(URL url, ScanResultCollector collector, Matcher<String> urlMatcher)
  {
    if (urlMatcher.match(url.toString()) == false) {
      return;
    }
    ArchiveContext context = new ArchiveContextImpl(true, collector);
    if (url.toString().contains("!")) {
      String customUrlStr = url.toString();
      customUrlStr = "jar:" + customUrlStr;
      log.info("Custom URL: " + customUrlStr);
      try {
        URL customUrl = new URL(customUrlStr);
        ArchiveDescriptor descriptor = buildArchiveDescriptor(customUrl, true);
        descriptor.visitArchive(context);
      } catch (MalformedURLException e) {
        log.error("Error while getting custom URL: " + customUrlStr);
      }
    } else {
      final ArchiveDescriptor descriptor = buildArchiveDescriptor(url, true);
      descriptor.visitArchive(context);
      handleClassManifestClassPath(url, collector, urlMatcher);
    }
  }

  /**
   * A jar may have also declared more deps in manifest (like surefire).
   * 
   * @param url
   * @param collector
   */
  @SuppressWarnings("deprecation")
  private void handleClassManifestClassPath(URL url, ScanResultCollector collector, Matcher<String> urlMatcher)
  {
    String urls = url.toString();

    if (urls.endsWith(".jar") == false) {
      return;
    }
    try (InputStream is = url.openStream()) {
      try (JarInputStream jarStream = new JarInputStream(is)) {
        Manifest manifest = jarStream.getManifest();
        if (manifest == null) {
          return;
        }
        Attributes attr = manifest.getMainAttributes();
        String val = attr.getValue("Class-Path");
        if (StringUtils.isBlank(val) == true) {
          return;
        }
        String[] entries = StringUtils.split(val, " \t\n");
        for (String entry : entries) {
          URL surl = new URL(entry);
          visitUrl(surl, collector, urlMatcher);
        }

      }

    } catch (IOException ex) {
      log.warn("JpaScan; Cannot open jar: " + url + ": " + ex.getMessage());
    }

  }

  protected void visitExternUrls(ScanEnvironment environment, ScanResultCollector collector, Set<URL> loadedUrls)
  {
    String matcherexppr = getPersistenceProperties(environment).getProperty(EXTLIBURLMATCHER);
    Matcher<String> urlmatcher = CommonMatchers.always();
    if (StringUtils.isNotBlank(matcherexppr) == true) {
      urlmatcher = new BooleanListRulesFactory<String>().createMatcher(matcherexppr);
    }
    JpaExtScannerUrlProvider prov = loadJpaExtScannerUrlProvider(environment, collector);
    Collection<URL> urls = prov.getScannUrls();
    for (URL url : urls) {
      if (loadedUrls.contains(url) == true) {
        continue;
      }

      try {
        visitUrl(url, collector, urlmatcher);
        loadedUrls.add(url);

      } catch (Exception ex) {
        log.warn("Cannot scan " + url + "; " + ex.getMessage());
      }
    }
  }

  private Properties getPersistenceProperties(ScanEnvironment environment)
  {
    if ((environment instanceof StandardJpaScanEnvironmentImpl) == false) {
      log.warn("environment is not StandardJpaScanEnvironmentImpl: " + environment.getClass());
      return new Properties();
    }
    PersistenceUnitDescriptor pud = (PersistenceUnitDescriptor) PrivateBeanUtils.readField(environment,
        "persistenceUnitDescriptor");
    return pud.getProperties();
  }

  protected JpaExtScannerUrlProvider loadJpaExtScannerUrlProvider(ScanEnvironment environment,
      ScanResultCollector collector)
  {
    Properties properties = getPersistenceProperties(environment);
    String provider = properties.getProperty(EXTLIBURLPROVIDER);
    if (StringUtils.isBlank(provider) == true) {
      return null;
    }
    try {
      Class<?> clazz = Class.forName(provider);
      JpaExtScannerUrlProvider urlpr = (JpaExtScannerUrlProvider) clazz.newInstance();
      return urlpr;
    } catch (Exception ex) {
      log.error("Cannot create JpaExtScannerUrlProvider: " + ex.getMessage(), ex);
      return null;
    }
  }

  private ArchiveDescriptor buildArchiveDescriptor(URL url, boolean isRootUrl)
  {
    final ArchiveDescriptor descriptor;
    final ArchiveDescriptorInfo descriptorInfo = archiveDescriptorCache.get(url.toString());
    if (descriptorInfo == null) {
      descriptor = archiveDescriptorFactory.buildArchiveDescriptor(url);
      archiveDescriptorCache.put(
          url.toString(),
          new ArchiveDescriptorInfo(descriptor, isRootUrl));
    } else {
      validateReuse(descriptorInfo, isRootUrl);
      descriptor = descriptorInfo.archiveDescriptor;
    }
    return descriptor;
  }

  // This needs to be protected and attributes/constructor visible in case
  // a custom scanner needs to override validateReuse.
  protected static class ArchiveDescriptorInfo
  {
    public final ArchiveDescriptor archiveDescriptor;
    public final boolean isRoot;

    public ArchiveDescriptorInfo(ArchiveDescriptor archiveDescriptor, boolean isRoot)
    {
      this.archiveDescriptor = archiveDescriptor;
      this.isRoot = isRoot;
    }
  }

  @SuppressWarnings("UnusedParameters")
  protected void validateReuse(ArchiveDescriptorInfo descriptor, boolean root)
  {
    // is it really reasonable that a single url be processed multiple times?
    // for now, throw an exception, mainly because I am interested in situations where this might happen
    throw new IllegalStateException("ArchiveDescriptor reused; can URLs be processed multiple times?");
  }

  public static class ArchiveContextImpl implements ArchiveContext
  {
    private final boolean isRootUrl;

    private final ClassFileArchiveEntryHandler classEntryHandler;
    private final PackageInfoArchiveEntryHandler packageEntryHandler;
    private final ArchiveEntryHandler fileEntryHandler;

    public ArchiveContextImpl(boolean isRootUrl, ScanResultCollector scanResultCollector)
    {
      this.isRootUrl = isRootUrl;

      this.classEntryHandler = new ClassFileArchiveEntryHandler(scanResultCollector);
      this.packageEntryHandler = new PackageInfoArchiveEntryHandler(scanResultCollector);
      this.fileEntryHandler = new NonClassFileArchiveEntryHandler(scanResultCollector);
    }

    @Override
    public boolean isRootUrl()
    {
      return isRootUrl;
    }

    @Override
    public ArchiveEntryHandler obtainArchiveEntryHandler(ArchiveEntry entry)
    {
      final String nameWithinArchive = entry.getNameWithinArchive();

      if (nameWithinArchive.endsWith("package-info.class")) {
        return packageEntryHandler;
      } else if (nameWithinArchive.endsWith(".class")) {
        return classEntryHandler;
      } else {
        return fileEntryHandler;
      }
    }
  }
}
