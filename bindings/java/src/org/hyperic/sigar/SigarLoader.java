package org.hyperic.sigar;

import org.hyperic.jni.ArchLoader;
import org.hyperic.jni.ArchLoaderException;
import org.hyperic.jni.ArchName;
import org.hyperic.jni.ArchNotSupportedException;

public class SigarLoader extends ArchLoader {

    private static String location = null;
    private static String nativeName = null;

    public SigarLoader(Class loaderClass) {
        super(loaderClass);
    }

    //XXX same as super.getArchLibName()
    //but db2monitor.jar gets loaded first in jboss
    //results in NoSuchMethodError
    public String getArchLibName()
        throws ArchNotSupportedException {

        return getName() + "-" + ArchName.getName();
    }

    public String getDefaultLibName()
        throws ArchNotSupportedException {

        return getArchLibName(); //drop "java" prefix
    }

    //override these methods to ensure our ClassLoader
    //loads the native library.
    protected void systemLoadLibrary(String name) {
        System.loadLibrary(name);
    }

    protected void systemLoad(String name) {
        System.load(name);
    }

    /**
     * Returns the path where sigar.jar is located.
     */
    public synchronized static String getLocation() {
        if (location == null) {
            SigarLoader loader = new SigarLoader(Sigar.class);
            try {
                location = loader.findJarPath("sigar.jar");
            } catch (ArchLoaderException e) {
                location = ".";
            }
        }
        return location;
    }

    /**
     * Returns the name of the native sigar library.
     */
    public synchronized static String getNativeLibraryName() {
        if (nativeName == null) {
            SigarLoader loader = new SigarLoader(Sigar.class);

            try {
                nativeName = loader.getLibraryName();
            } catch (ArchNotSupportedException e) {
                nativeName = null;
            }
        }

        return nativeName;
    }
}
