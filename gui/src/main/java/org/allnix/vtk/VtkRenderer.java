package org.allnix.vtk;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.exception.ExceptionUtils;
import vtk.vtkActor;
import vtk.vtkCamera;
import vtk.vtkGenericRenderWindowInteractor;

import vtk.vtkProp;
import vtk.vtkRenderer;

public interface VtkRenderer {
	void render();
	vtkRenderer getRenderer();
	void addActor(vtkProp v);
	vtkCamera getActiveCamera();
	void removeActor(vtkProp v);
	void resetCamera();
//	VtkCameraSetting getActiveCameraSettings();
	//void setActiveCameraSettings(VtkCameraSetting v);
	/**
	 * Parallel means orthogonal
	 * Parallel means no perspective
	 * 
	 * @param value
	 */
	void setParallelProjection(int value);
	void setParallelProjection(boolean value);
	
	vtkGenericRenderWindowInteractor getRenderWindowInteractor();
    void controlCamera(char key, boolean alt, boolean shift);
	

    default VtkCameraSetting getActiveCameraSetting() {
        VtkCameraSetting setting = new VtkCameraSetting();
        setting.fromCameraToSettings(getActiveCamera());
        return setting;
    }
    default void setActiveCameraSetting(VtkCameraSetting setting) {
        Runnable x = () -> {
          setting.fromSettingsToCamera(getActiveCamera());  
          getActiveCamera().Modified();
        };
        invokeLater(x);
    }
    
//	void setBackground(double r, double g, double b);
	default void setBackground(double[] rgb) {
	    // TODO: Does this need to be in EDT
	    this.getRenderer().SetBackground(rgb);
	}
	default void setBackground(double r, double g, double b) {
        this.getRenderer().SetBackground(r, g, b);
    }
	
	default double[] getBackground() {
	    return getRenderer().GetBackground();
	}
	default void setBackground(Color color) {
	    float[] frgb = new float[3];
	    color.getColorComponents(frgb);
//	    double[] rgb = new double[]{frgb[0], frgb[1], frgb[2]};
	    getRenderer().SetBackground(frgb[0], frgb[1], frgb[2]);
	}
	
	
	
	default void invokeAndWait(Runnable v) {
		invokeLater(v);
//		try {
//			SwingUtilities.invokeAndWait(v);
//		} catch (InvocationTargetException | InterruptedException e) {
//			throw new RuntimeException(e);
//		}
	}
	
	default void invokeLater(Runnable v) {
		SwingUtilities.invokeLater(v);
	}

	void addFocusExclusionActor(vtkActor value);
    void removeFocusExclusionActor(vtkActor value); 
    void clearFocusExclusionActor();	
}
