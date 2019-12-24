package org.allnix.vtk.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

import org.allnix.gui.Builder;
import org.allnix.vtk.VtkExternalFrame;
import org.allnix.vtk.VtkFrame;
import org.allnix.vtk.VtkLoader;
import org.allnix.vtk.VtkRenderWindowPanelRenderer;
import org.allnix.vtk.VtkRenderer;
import org.allnix.vtk.VtkUnstructuredGrid;

import vtk.vtkRenderWindowPanel;

public class VtkUnstructuredGridExample {
    /**
     * Run
     * <pre>
     * ./gradlew -PmainClass=org.allnix.vtk.example.VtkUnstructuredGridExample runApp
     * </pre>
     * @param args
     * @throws InterruptedException
     */
    static public void main(String[] args) throws InterruptedException {
        VtkLoader.loadAllNativeLibraries();

        VtkExternalFrame vframe = new VtkExternalFrame();
        VtkRenderWindowPanelRenderer renderer = vframe.getVtkRenderer();
        
        
        // > Value to show
        String name = "Temperature";
        
        // > The grid
        VtkUnstructuredGrid grid = new VtkUnstructuredGrid();
        grid.init();
        
       
        
        Builder.buildVtkUnstructuredGrid7Cell(grid);
        
        // > Value range to color from blue - red
        double[] range = grid.getRange(name);
        grid.setActiveScalars("Temperature");
        grid.setLookupTableRange(range);
        
        // > render
//        vframe.pack();
        renderer.addActor(grid.getActor());
        
        
        renderer.resetCamera();
        
        vframe.setVisible(true);
        
//        renderer.render();
//        TimeUnit.MILLISECONDS.sleep(500); // it is a windows thing
        renderer.render();
    }
}