package org.allnix.vtk;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkNativeLibrary;

public class VtkLoader {
	static final private Logger logger = LoggerFactory.getLogger(VtkLoader.class);

    static public String getVtkLibraryName() {
	    if (SystemUtils.IS_OS_LINUX) {
	        return "vtk-8.1.2";
	    } else if (SystemUtils.IS_OS_WINDOWS) {
	        return "VTK-8.1.2";
	    } else {
	        throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);
	    }
	}
	
	
	
	static public Path getVtkFolder() {
	    String vtkLibName = getVtkLibraryName();
	    
	    if (SystemUtils.IS_OS_LINUX) {
	        throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);
	    } else if(SystemUtils.IS_OS_WINDOWS) {
	        Path path;
	        
	        // TODO: Record search path and print to exception
            path = Paths.get("C:", "local", vtkLibName);
            if (Files.isDirectory(path)) {
                return path.toAbsolutePath();
            }

            path = Paths.get("P:", "Data", "Hydraulic Fracturing", "FieldDeveloper", "local", vtkLibName);
            if (Files.isDirectory(path)) {
                return path.toAbsolutePath();
            }
            
            throw new RuntimeException("VTK library not found");
	    } else {
	        throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);
	    }
	}
	
	static public Path getVtkLibraryFolder() {
        Path vtkFolder = getVtkFolder();
        
        if (SystemUtils.IS_OS_LINUX) {
            Path path = Paths.get(vtkFolder.toString(), "lib");
            throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);
        } else if(SystemUtils.IS_OS_WINDOWS) {
            Path path = Paths.get(vtkFolder.toString(), "bin");
            return path;
        } else {
            throw new RuntimeException("Unsupported OS: " + SystemUtils.OS_NAME);
        }
    }
    
    /**
	 * Load native VTK libraries
	 * 
	 * It does not hurt to call this multiple times.
	 * 
	 * @param vtkLibDir Location to the VTK library.  For example,
	 * "C:/local/VTK-8.1.2/bin" or "/opt/VTK-8.1.2/lib"
	 * @return true if all VTK is loaded in this call
	 */
	static public boolean loadAllNativeLibraries(String vtkLibDir) {
	    System.setProperty("vtk.lib.dir", vtkLibDir);
	    return loadAllNativeLibraries();
	}
	
	static public boolean loadAllNativeLibraries(Path vtkLibDir) {
	    return loadAllNativeLibraries(vtkLibDir.toString());
    }
    
	static public boolean loadAllNativeLibraries() {
		boolean loaded = vtkNativeLibrary.LoadAllNativeLibraries();
		if (loaded) {
			return true;
		}

		for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
			if (!lib.IsLoaded()) {
				logger.error("Not loaded: {}", lib.GetLibraryName());
//				System.out.println(lib.GetLibraryName() + " not loaded");
			}
		}

		vtkNativeLibrary.DisableOutputWindow(null);
		
		return false;
	}
}
