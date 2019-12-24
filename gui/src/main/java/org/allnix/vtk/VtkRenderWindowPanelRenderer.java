package org.allnix.vtk;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Collections;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import vtk.vtkActor;
import vtk.vtkCamera;
import vtk.vtkGenericRenderWindowInteractor;
import vtk.vtkInteractorObserver;
import vtk.vtkProp;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderWindowPanel;
import vtk.vtkRenderer;
/**
 * Use vtkRenderWindowPanel as renderer
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class VtkRenderWindowPanelRenderer implements VtkRenderer {
	private double azimuth = 1.0; // +: rotate to the right, -:rotate to the left
    private double elevation = 1.0; // +: rotate up, -: rotate down
    private double dolly = 0.1; // +: move cameral forward, -: move camera backward
    private double zoom = 0.1; // +: increase view angle, -: reduce view angle
    private double roll = 1.0; // +: rotate clockwise, -: roate counter-clockwise 
    private double yaw = 0.1; // +: look right, -: look left
    private double pitch = 0.1; // +: look up, -: look down
    
	private vtkRenderWindowPanel renPanel;
	private ArrayDeque<vtkActor> exclusionActorList = new ArrayDeque<>();
	
	public VtkRenderWindowPanelRenderer() {
	    this.renPanel = new vtkRenderWindowPanel();
	}
	public VtkRenderWindowPanelRenderer(vtkRenderWindowPanel panel) {
        this.renPanel = panel;
    }
	
	
	public void addFocusExclusionActor(vtkActor value) {
//	    logger.info("Actor: {}", value);
//	    logger.info("List: {}", this.exclusionActorList);
	    this.exclusionActorList.add(value);
	}
	public void removeFocusExclusionActor(vtkActor value) {
	    this.exclusionActorList.removeAll(Collections.singletonList(value));
	}
	public void clearFocusExclusionActor() {
	    this.exclusionActorList.clear();
	}
	
	
	
	public vtkRenderWindowPanel getRenderWindowPanel() {
	    return renPanel;
	}
	
	public void setupKeyListener() {
	    renPanel.getRenderWindowInteractor().RemoveObservers("CharEvent");
        renPanel.getRenderWindowInteractor().RemoveObservers("KeyPressEvent");
        renPanel.getRenderWindowInteractor().RemoveObservers("KeyDownEvent");
        renPanel.removeKeyListener(renPanel);
        renPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                controlCamera(e.getKeyChar(), e.isAltDown(), e.isShiftDown());
                render();                
            }
        });
	}
	
	/**
	 * Set the wrapped vtk object
	 * 
	 * @param vtk
	 */
	public void setImplementation(vtkRenderWindowPanel vtk) {
		this.renPanel = vtk;
	}
	
	
	@Override
	public vtkCamera getActiveCamera() {
	    return getRenderer().GetActiveCamera();
	}
	
	@Override
	public void render() {
		Runnable x = () -> {
			renPanel.Render();
		};
		invokeAndWait(x);
	}
	
	@Override
	public vtkRenderer getRenderer() {
		return renPanel.GetRenderer();
	}
	
	@Override
	public void addActor(vtkProp v) {
		Runnable x = () -> {
			getRenderer().AddActor(v);
		};
		invokeAndWait(x);
	}
	
	@Override
	public vtkGenericRenderWindowInteractor getRenderWindowInteractor() {
	    return renPanel.getRenderWindowInteractor();
	}
	
	@Override
	public void removeActor(vtkProp v) {
		Runnable x = () -> {
			getRenderer().RemoveActor(v);
		};
		invokeAndWait(x);
	}
	
	@Override
	public void resetCamera() {
		this.invokeAndWait(()->{
			getRenderer().ResetCamera();
		});
	}
	
//	@Override
//	public void setBackground(double[] rgb) {
//	    this.getRenderer().SetBackground(rgb);
//	}
	
	public void setParallelProjection(int value) {
	    this.getRenderer().GetActiveCamera().SetParallelProjection(value);
	}

	/**
	 * Set parallel projection
	 * 
	 * @param value true for parallel projection, false for perspective projection
	 */
	public void setParallelProjection(boolean value) {
	    // TODO: Use Boolean to convert
	    if (value) {
	        this.setParallelProjection(1);
	    } else {
	        this.setParallelProjection(0);
	    }
        
    }
	
	
	
	
	/**
	 * Camera control
	 * <p>
	 * Keyboard control of the active camera. 
	 * Supported "key" parameter:  
	 * <ul>
	 * <li><b>s, f:</b> Azimuth, rotate around the focal point</li> 
	 * <li><b>e, d:</b> Elevation</li>
	 * <li><b>w, r:</b> Roll</li>
	 * <li><b>t, g:</b> Dolly</li>
	 * <li><b>j, l:</b> Yaw</li>
	 * <li><b>i, k:</b> Pitch</li>
	 * <li><b>q, a:</b> Zoom</li>
	 * <li><b>x:</b> Focus</li>
	 * <li><b>z:</b> View up</li>
	 * <li><b>c:</b> Reset camera</li>
     * </ul>
     * 
	 * 
	 * @param key Key pressed
	 * @param alt Indicate if "ALT" is pressed, when true the action is refined
	 * @param shift Indicate if "SHIFT" is pressed, when true the action is coarsen
	 */
	@Override
    public void controlCamera(char key, boolean alt, boolean shift) {
//	    logger.info("Key: {}", key);
	    vtkRenderWindowInteractor rwi = renPanel.getRenderWindowInteractor();
        
         double azimuth = this.azimuth; // +: rotate to the right, -:rotate to the left
         double elevation = this.elevation; // +: rotate up, -: rotate down
         double dolly = this.dolly; // +: move cameral forward, -: move camera backward
         double zoom = this.zoom; // +: increase view angle, -: reduce view angle
         double roll = this.roll; // +: rotate clockwise, -: roate counter-clockwise
         double yaw = this.yaw; // +: look right, -: look left
         double pitch = this.pitch; // +: look up, -: look down
        vtkCamera camera = this.getActiveCamera();
       
        if (alt) {
            azimuth *= 0.1;
            elevation *= 0.1;
            dolly /= 5.0;
            zoom *= 0.1;
            roll *= 0.1;
            yaw *= 0.1;
            pitch *= 0.1;
        }
        
        if (shift) {
            azimuth /= 0.5;
            elevation /= 0.5;
            dolly *= 5.0;
            zoom /= 0.1;
            roll /= 0.1;
            yaw /= 0.1;
            pitch /= 0.1;
        }
        
        switch(key) {
        // > Rotate around the focal point
        case 's':
        case 'S':
            camera.Azimuth(-azimuth);
            break;
        case 'f':
        case 'F':
            camera.Azimuth(azimuth);
            break;
        case 'e':
        case 'E':
//            logger.info("View UP: {}:", Arrays.toString(camera.GetViewUp()));
            camera.Elevation(elevation);
            camera.OrthogonalizeViewUp();
            break;
        case 'd':
        case 'D':
            camera.Elevation(-elevation);
            camera.OrthogonalizeViewUp();
            break;
        case 'w':
        case 'W':
            camera.Roll(-roll);
            break;
        case 'r':
        case 'R':
            camera.Roll(roll);
            break;
        // >
        // > Move camera forward, backward, left and right
        // >
        case 't':
        case 'T':
            camera.Dolly(1. + dolly);
            getRenderer().ResetCameraClippingRange();
            break;
        case 'g':
        case 'G':
            camera.Dolly(1. - dolly);
            getRenderer().ResetCameraClippingRange();
            break;
        case ',':
        case '<':
            // TODO: move to left
            break;
        case '.':
        case '>':
            // TODO: move to right
            break;
        // >      
        // > Look up, down, left and right
        // >
        case 'j':
        case 'J':
            camera.Yaw(-yaw);
            break;
        case 'l':
        case 'L':
            camera.Yaw(yaw);
            break;
        case 'i':
        case 'I':
            camera.Pitch(-pitch);
            camera.OrthogonalizeViewUp();
            break;
        case 'k':
        case 'K':
            camera.Pitch(pitch);
            camera.OrthogonalizeViewUp();
            break;
            
        // > Zoom
        case 'q':
        case 'Q':
            camera.Zoom(1. + zoom);
            break;
        case 'a':
        case 'A':
            camera.Zoom(1. - zoom);
            break;
            

        case 'x': // > focus
        case 'X':
            vtkInteractorObserver vio = rwi.GetInteractorStyle();
            vtkRenderer renderer = vio.GetCurrentRenderer();
            
            if (renderer == null) {
                return;
            }
            
            //> Remove outer shell (culled geomodel for example)
            //> otherwise it interferes with focus
            ArrayDeque<vtkActor> putItBackList = new ArrayDeque<>();
            for (vtkActor actor : this.exclusionActorList) {
                if (renderer.HasViewProp(actor) == 1) {
                    putItBackList.add(actor);
                    renderer.RemoveActor(actor);
                }
            }
            
            rwi.SetKeyCode('f');
            vio.OnChar();
            
            //> Put outer shell back
            for (vtkActor actor: putItBackList) {
                renderer.AddActor(actor);
            }
            renderer.ResetCameraClippingRange();
            
            break;
        // > View up
        case 'z':
        case 'Z':
            Vector3D viewUp = new Vector3D(camera.GetViewUp());
            viewUp = viewUp.normalize();
            
            Vector3D up = new Vector3D(new double[] {0, 0, 1});
//            Vector3D viewPlaneNormal = new Vector3D(camera.GetViewPlaneNormal());
//            viewPlaneNormal = viewPlaneNormal.normalize();
            
            double dot = Vector3D.dotProduct(viewUp, up);
//            double dot = viewUp[0] * viewPlaneNormal[0] + viewUp[1] * viewPlaneNormal[1] + 
//                viewUp[2] * viewPlaneNormal[2];
//            logger.info("View Up = {}", viewUp.toString());
            //logger.info("View Plane Normal = {}", viewPlaneNormal.toString());
//            logger.info("dot = {}", dot);
            if (Math.abs(dot) > 1.e-1) { // c++ crashes without this
                camera.SetViewUp(0, 0, 1);
            }
            break;
            
            // > View up
        case 'c':
        case 'C':
            getRenderer().ResetCamera();
            break;
        default:
            break;
        }
    }
	/**
	 * Get azimuth step size
	 * @return
	 */
    public double getAzimuth() {
        return azimuth;
    }
    /**
     * Set azimuth step size, default = 1.0.
     * 
     * @param azimuth
     */
    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }
    /**
     * Get elevation step size
     * 
     * @return
     */
    public double getElevation() {
        return elevation;
    }
    /**
     * Set elevation step size, default = 1.0.
     * 
     * @param elevation
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    /**
     * Get dolly value
     * @return
     */
    public double getDolly() {
        return dolly;
    }
    /**
     * Set dolly step value, default = 0.1.
     * <p>
     * (VTK dolly value) = 1 + (dolly step)
     * <p>
     * The distance between focal point and camera is divided by (VTK dolly value)
     * 
     * @param dolly Dolly step value
     */
    public void setDolly(double dolly) {
        this.dolly = dolly;
    }
    
    public double getZoom() {
        return zoom;
    }
    
    /**
     * Set zoom step value, default = 0.1.
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
    public double getRoll() {
        return roll;
    }
    /**
     * Set roll step value, default = 1.
     * @param roll
     */
    public void setRoll(double roll) {
        this.roll = roll;
    }
    public double getYaw() {
        return yaw;
    }
    /**
     * Set yaw step value, default = 0.1.
     * @param yaw
     */
    public void setYaw(double yaw) {
        this.yaw = yaw;
    }
    public double getPitch() {
        return pitch;
    }
    /**
     * Set pitch step value, default = 0.1.
     * @param pitch
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }
	
}
