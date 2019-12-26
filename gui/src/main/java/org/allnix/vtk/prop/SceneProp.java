package org.allnix.vtk.prop;

import java.awt.Color;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.allnix.gui.ColorList;
import org.allnix.vtk.VtkRenderer;

import vtk.vtkAxesActor;
import vtk.vtkCamera;
import vtk.vtkCubeAxesActor;
import vtk.vtkGenericRenderWindowInteractor;
import vtk.vtkNamedColors;
import vtk.vtkOrientationMarkerWidget;

public class SceneProp {
    static final private Logger logger = LoggerFactory.getLogger(SceneProp.class);
    private vtkCubeAxesActor cubeAxesActor;
    private vtkAxesActor axesActor;
    private vtkOrientationMarkerWidget widget;
    private VtkRenderer renderer;
    
    public SceneProp() {
        this.init();
    }
    
//    public vtkCubeAxesActor getCubeAxesActor() {
//        return cubeAxesActor;
//    }
    public void setVtkRenderer(VtkRenderer v) {
        renderer = v;
        if (widget != null) {
            vtkGenericRenderWindowInteractor interactor = renderer.getRenderWindowInteractor(); 
            widget.SetInteractor(interactor);
            widget.SetEnabled(1);
            widget.SetViewport(0.0, 0.0, 0.2, 0.2);
            widget.InteractiveOn();
        }
        if (cubeAxesActor != null) {
            vtkCamera camera = renderer.getRenderer().GetActiveCamera();
            cubeAxesActor.SetCamera(camera);
            this.reset();
        }
        renderer.setBackground(ColorList.lightBackground);
    }
    
    public double[] getCubeAxesBounds() {
        return cubeAxesActor.GetBounds();
    }
    /**
     * 
     * @param bounds (xmin,xmax, ymin,ymax, zmin,zmax)
     */
    public void setCubeAxesBounds(double[] bounds) {
        cubeAxesActor.SetBounds(bounds);
    }
    public void setDrawXGridline(boolean draw) {
        cubeAxesActor.SetDrawXGridlines(draw ? 1:0);
    }
    public void setDrawYGridline(boolean draw) {
        cubeAxesActor.SetDrawYGridlines(draw ? 1:0);
    }
    public void setDrawZGridline(boolean draw) {
        cubeAxesActor.SetDrawZGridlines(draw ? 1:0);
    }
    public void setDrawXGridpolys(boolean draw) {
        cubeAxesActor.SetDrawXGridpolys(draw ? 1:0);
    }
    public void setDrawYGridpolys(boolean draw) {
        cubeAxesActor.SetDrawYGridpolys(draw ? 1:0);
    }
    public void setDrawZGridpolys(boolean draw) {
        cubeAxesActor.SetDrawZGridpolys(draw ? 1:0);
    }
    public void reset() {
//        double[] bounds = new double[6];
         
        
        SwingUtilities.invokeLater(()->{
            boolean needToAdd = false;
            if (renderer.getRenderer().HasViewProp(cubeAxesActor) == 1) {
                renderer.getRenderer().RemoveActor(cubeAxesActor);
//                renderer.removeActor(cubeAxesActor);
                
                needToAdd = true;
//                try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                } catch (InterruptedException e) {
//                }
//                renderer.render();
            }
            
//            renderer.getRenderer().ComputeVisiblePropBounds(bounds);
            final double[] b = renderer.getRenderer().ComputeVisiblePropBounds();
            cubeAxesActor.SetBounds(b);
            if (needToAdd) {
                //renderer.addActor(cubeAxesActor);
                renderer.getRenderer().AddActor(cubeAxesActor);
                renderer.getRenderer().Render();
            }
            
            cubeAxesActor.SetXLabelFormat("%g");
            cubeAxesActor.SetYLabelFormat("%g");
            cubeAxesActor.SetZLabelFormat("%g");
        });
        
//        logger.info("Renderer Bounds: {}", Arrays.toString(bounds));
        
    }   
    
    
    public void setCompassEnabled(boolean enabled) {
        widget.SetEnabled(enabled ? 1:0);
    }
    
    private void init() {
     
        // >>>>> Axes Actor (compass)
        axesActor = new vtkAxesActor();
        axesActor.SetXAxisLabelText("E");
        axesActor.SetYAxisLabelText("N");
        axesActor.SetShaftTypeToCylinder();
        axesActor.SetCylinderRadius(0.1);
        axesActor.SetConeRadius(0.7);
        
//        cubeAxesActor.GetTitleTextProperty(0).SetColor(0.96, 0.21, 0.49);
//        cubeAxesActor.GetLabelTextProperty(0).SetColor(0.96, 0.21, 0.49);
//        cubeAxesActor.GetTitleTextProperty(1).SetColor(0.49, 0.96, 0.21);
//        cubeAxesActor.GetLabelTextProperty(1).SetColor(0.49, 0.96, 0.21);
//        cubeAxesActor.GetTitleTextProperty(2).SetColor(0.21, 0.49, 0.96);
//        cubeAxesActor.GetLabelTextProperty(2).SetColor(0.21, 0.49, 0.96);
        
        axesActor.GetXAxisShaftProperty().SetColor(ColorList.red);
        axesActor.GetXAxisTipProperty().SetColor(ColorList.red);
        axesActor.GetYAxisShaftProperty().SetColor(ColorList.yellow);
        axesActor.GetYAxisTipProperty().SetColor(ColorList.yellow);
        axesActor.GetZAxisShaftProperty().SetColor(ColorList.blue);
        axesActor.GetZAxisTipProperty().SetColor(ColorList.blue);
        
//        axesActor.GetXAxisShaftProperty().SetColor(0.96, 0.21, 0.49);
//        axesActor.GetXAxisTipProperty().SetColor(0.96, 0.21, 0.49);
//        axesActor.GetYAxisShaftProperty().SetColor(0.639, 0.721, 0.423);
//        axesActor.GetYAxisTipProperty().SetColor(0.639, 0.721, 0.423);
//        axesActor.GetZAxisShaftProperty().SetColor(0.21, 0.49, 0.96);
//        axesActor.GetZAxisTipProperty().SetColor(0.21, 0.49, 0.96);
        
//        axesActor.GetXAxisCaptionActor2D().GetProperty().SetColor(1.0, 0.4, 0.4);
//        axesActor.GetYAxisCaptionActor2D().GetProperty().SetColor(0.0, 0.85, 0.0);
//        axesActor.GetZAxisCaptionActor2D().GetProperty().SetColor(0.0, 0.78, 1.0);
        widget = new vtk.vtkOrientationMarkerWidget();
        double[] rgba = new double[4];
        vtkNamedColors colors = new vtk.vtkNamedColors();
        colors.GetColor("Carrot", rgba);
        widget.SetOutlineColor(rgba[0], rgba[1], rgba[2]);
        widget.SetOrientationMarker(axesActor);
        
        // > never executed so fix me
        if (renderer != null) { // TODO: fix me
            vtkGenericRenderWindowInteractor interactor = renderer.getRenderWindowInteractor();
            widget.SetInteractor(interactor);
            widget.SetEnabled(1);
            widget.SetViewport(0.0, 0.0, 0.1, 0.1);
            widget.InteractiveOn();
        }
        

        
        
        // >>>>> Cube Axes Actor (grid lines & xyz axis)
        cubeAxesActor = new vtkCubeAxesActor();
        // > Flips z value so increase downward
//        cubeAxesActor.SetAxisBaseForZ(0, 0, -1);
//        cubeAxesActor.SetScale(1, 1, -1);
        
        
        cubeAxesActor.SetXTitle("East");
        cubeAxesActor.SetXUnits("ft");
        cubeAxesActor.SetYTitle("North");
        cubeAxesActor.SetYUnits("ft");
        cubeAxesActor.SetZTitle("Z");
        cubeAxesActor.SetZUnits("ft");
        // > Controls where axis are plotted
         cubeAxesActor.SetFlyModeToOuterEdges();
//        axesActor.SetFlyModeToClosestTriad();
        // axesActor.SetFlyModeToFurthestTriad();
        // axesActor.SetFlyModeToStaticTriad();
//         axesActor.SetFlyModeToStaticEdges();
        
         // Green
//         0.639, 0.721, 0.423
//         0.49, 0.96, 0.21
         cubeAxesActor.GetTitleTextProperty(0).SetColor(ColorList.red);
         cubeAxesActor.GetLabelTextProperty(0).SetColor(ColorList.red);
         cubeAxesActor.GetTitleTextProperty(1).SetColor(ColorList.yellow);
         cubeAxesActor.GetLabelTextProperty(1).SetColor(ColorList.yellow);
         cubeAxesActor.GetTitleTextProperty(2).SetColor(ColorList.blue);
         cubeAxesActor.GetLabelTextProperty(2).SetColor(ColorList.blue);
         
//         cubeAxesActor.GetTitleTextProperty(0).SetColor(0.96, 0.21, 0.49);
//         cubeAxesActor.GetLabelTextProperty(0).SetColor(0.96, 0.21, 0.49);
//         cubeAxesActor.GetTitleTextProperty(1).SetColor(0.639, 0.721, 0.423);
//         cubeAxesActor.GetLabelTextProperty(1).SetColor(0.639, 0.721, 0.423);
//         cubeAxesActor.GetTitleTextProperty(2).SetColor(0.21, 0.49, 0.96);
//         cubeAxesActor.GetLabelTextProperty(2).SetColor(0.21, 0.49, 0.96);
         
         
         {
             Color color = Color.darkGray;
             double[] c = ColorList.toDouble(color);
             cubeAxesActor.GetXAxesGridlinesProperty().SetColor(c);
             cubeAxesActor.GetYAxesGridlinesProperty().SetColor(c);
             cubeAxesActor.GetZAxesGridlinesProperty().SetColor(c);
             cubeAxesActor.GetXAxesLinesProperty().SetColor(c);
             cubeAxesActor.GetYAxesLinesProperty().SetColor(c);
             cubeAxesActor.GetZAxesLinesProperty().SetColor(c);
         }
         
//        cubeAxesActor.GetTitleTextProperty(0).SetColor(1.0, 0.4, 0.4);
//        cubeAxesActor.GetLabelTextProperty(0).SetColor(1.0, 0.4, 0.4);
//
//        cubeAxesActor.GetTitleTextProperty(1).SetColor(0.0, 0.85, 0.0);
//        cubeAxesActor.GetLabelTextProperty(1).SetColor(0.0, 0.85, 0.0);
//
//        cubeAxesActor.GetTitleTextProperty(2).SetColor(0.0, 0.78, 1.0);
//        cubeAxesActor.GetLabelTextProperty(2).SetColor(0.0, 0.78, 1.0);
        
//      axesActor.DrawXGridlinesOn();
//      axesActor.DrawYGridlinesOn();
//      axesActor.DrawZGridlinesOn();
        // enum 
        // GridVisibility { VTK_GRID_LINES_ALL = 0, VTK_GRID_LINES_CLOSEST = 1, VTK_GRID_LINES_FURTHEST = 2 }
        cubeAxesActor.SetGridLineLocation(2);

//      axesActor.XAxisMinorTickVisibilityOff();
//      axesActor.YAxisMinorTickVisibilityOff();
//      axesActor.ZAxisMinorTickVisibilityOff();
        
        cubeAxesActor.XAxisMinorTickVisibilityOn();
        cubeAxesActor.YAxisMinorTickVisibilityOn();
        cubeAxesActor.ZAxisMinorTickVisibilityOn();
        
        double opacity = 0.9;
        double ambient = 0.4;
        cubeAxesActor.GetXAxesGridpolysProperty().SetOpacity(opacity);
        cubeAxesActor.GetXAxesGridpolysProperty().SetAmbient(ambient);
        cubeAxesActor.GetYAxesGridpolysProperty().SetOpacity(opacity);
        cubeAxesActor.GetYAxesGridpolysProperty().SetAmbient(ambient);
        cubeAxesActor.GetZAxesGridpolysProperty().SetOpacity(opacity);
        cubeAxesActor.GetZAxesGridpolysProperty().SetAmbient(ambient);
        
        
        cubeAxesActor.SetXLabelFormat("%g");
        cubeAxesActor.SetYLabelFormat("%g");
        cubeAxesActor.SetZLabelFormat("%g");
    }

    
    /**
     * GridVisibility { VTK_GRID_LINES_ALL = 0, VTK_GRID_LINES_CLOSEST = 1, 
     * VTK_GRID_LINES_FURTHEST = 2 }
     * @param location
     */
    public void setGridLineLocation(int location) {
        cubeAxesActor.SetGridLineLocation(location);
    }
    /**
     * FlyMode {
     * VTK_FLY_OUTER_EDGES = 0, VTK_FLY_CLOSEST_TRIAD = 1, VTK_FLY_FURTHEST_TRIAD = 2, 
     * VTK_FLY_STATIC_TRIAD = 3,
     * VTK_FLY_STATIC_EDGES = 4
     * }
     * @param mode
     */
    public void setFlyMode(int mode) {
        cubeAxesActor.SetFlyMode(mode);
    }
    
//    public void controlCamera(char key, boolean alt) {
//        double degree = 1.;
//        
//        double azimuth = 0.5;
//        double elevation = 0.5;
//        double dolly = 0.1;
//        double zoom = 0.1;
//        double roll = 0.1;
//        double yaw = 0.1;
//        double pitch = 0.1;
//        vtkCamera camera = renderer.getActiveCamera();
//       
//        if (alt) {
//            azimuth *= 0.4;
//            elevation *= 0.4;
//            degree /= 10.;
//            dolly /= 10.;
//            zoom /= 10.;
//            roll *= 0.1;
//            yaw *= 0.1;
//            pitch *= 0.1;
//        }
//        
//        switch(key) {
//        // > Rotate around the focal point
//        case 's':
//        case 'S':
//            camera.Azimuth(-azimuth);
//            break;
//        case 'f':
//        case 'F':
//            camera.Azimuth(azimuth);
//            break;
//        case 'e':
//        case 'E':
//            camera.Elevation(elevation);
//            break;
//        case 'd':
//        case 'D':
//            camera.Elevation(-elevation);
//            break;
//        case 'w':
//        case 'W':
//            camera.Roll(-roll);
//            break;
//        case 'r':
//        case 'R':
//            camera.Roll(roll);
//            break;
//        // >
//        // > Move camera forward, backward, left and right
//        // >
//        case 't':
//        case 'T':
//            camera.Dolly(1. + dolly);
//            break;
//        case 'g':
//        case 'G':
//            camera.Dolly(1. - dolly);
//            break;
//        case ',':
//        case '<':
//            // TODO: move to left
//            break;
//        case '.':
//        case '>':
//            // TODO: move to right
//            break;
//        // >      
//        // > Look up, down, left and right
//        // >
//        case 'j':
//        case 'J':
//            camera.Yaw(yaw);
//            break;
//        case 'l':
//        case 'L':
//            camera.Yaw(-yaw);
//            break;
//        case 'i':
//        case 'I':
//            camera.Pitch(-pitch);
//            break;
//        case 'k':
//        case 'K':
//            camera.Pitch(pitch);
//            break;
//            
//        // > Zoom
//        case 'q':
//        case 'Q':
//            camera.Zoom(1. + zoom);
//            break;
//        case 'a':
//        case 'A':
//            camera.Zoom(1. - zoom);
//            break;
//            
//        // > View up
//        case 'z':
//        case 'Z':
//            camera.SetViewUp(0, 0, 1);
//            break;
//            
//        default:
//            break;
//        }
//        
//        renderer.render();
//    }
    
    
    
    
    
//    public void updateAxesActor() {
//        final double[] bounds = mainGrid.getUnstructuredGrid().GetBounds();
//        final int length = bounds.length;
//        double[] newBounds = new double[length];
//        
//        final double alpha = 0.5;
//        
//        // x-bound
//        double xspan = Math.abs(bounds[1] - bounds[0]);
//        {
//            double span = bounds[1] - bounds[0];
//            newBounds[0] = bounds[0] - alpha*span;
//            newBounds[1] = bounds[1] + alpha*span;
//        }
//        // y-bound
//        double yspan = Math.abs(bounds[2] - bounds[3]);
//        {
//            double span = bounds[3] - bounds[2];
//            newBounds[2] = bounds[2] - alpha * span;
//            newBounds[3] = bounds[3] + alpha * span;
//        }
//        
//        // z-bound
//        {
////          double beta = 0.75;
//            double beta = 0.50;
//            double span = Math.abs(bounds[5] - bounds[4]);
//            span = Math.max(span, beta * xspan);
//            span = Math.max(span, beta * yspan);
//            
//            double midz = 0.5*(bounds[4] + bounds[5]);
////          newBounds[4] = bounds[4] - alpha * span;
////          newBounds[5] = bounds[5] + alpha * span;
//            newBounds[4] = midz - 0.5 * span;
//            newBounds[5] = midz + 0.5 * span;  
//            
//        }
//        
//        
//        
//        axesActor.SetBounds(newBounds);
//        axesActor.SetCamera(renderer.getRenderer().GetActiveCamera());
//        
//        
//    }

    
    public void showAxesActor() {
        widget.SetEnabled(1);
//        renderer.addActor(axesActor);
    }
    public void hideAxesActor() {
        widget.SetEnabled(0);
//        renderer.removeActor(axesActor);
    }
    
    public void showCubeAxesActor(boolean show) {
        if (show) {
            renderer.addActor(cubeAxesActor);
        } else {
            renderer.removeActor(cubeAxesActor);
        }
    }
    
    public void showCubeAxesActor() {
        renderer.addActor(cubeAxesActor);
    }
    
    public void hideCubeAxesActor() {
        renderer.removeActor(cubeAxesActor);
    }
    
    
    public void setGridlineLocationAll() {
        cubeAxesActor.SetGridLineLocation(0);
    }
    public void setGridlineLocationBack() {
        cubeAxesActor.SetGridLineLocation(2);
    }
    public void setGridlineLocationFront() {
        cubeAxesActor.SetGridLineLocation(1);
    }
    
}
