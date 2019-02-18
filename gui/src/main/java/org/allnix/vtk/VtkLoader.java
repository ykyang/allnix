package org.allnix.vtk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtk.vtkNativeLibrary;

public class VtkLoader {
	static final private Logger logger = LoggerFactory.getLogger(VtkLoader.class);

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
