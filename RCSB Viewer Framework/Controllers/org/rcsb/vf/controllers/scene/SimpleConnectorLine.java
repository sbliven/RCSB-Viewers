package org.rcsb.vf.controllers.scene;

import org.rcsb.mbt.model.StructureComponent;
import org.rcsb.mbt.model.StructureComponentRegistry.ComponentType;

// a simple line connecting two components. Not necessarily any structural significance.
public class SimpleConnectorLine extends StructureComponent {
    public double[] firstCoord = null;
    public double[] secondCoord = null;
    
    public Object firstObject = null;
    public Object secondObject = null;
    
    
	public void copy(final StructureComponent structureComponent) {
        // TODO Auto-generated method stub
        if(!(structureComponent instanceof SimpleConnectorLine)) {
            return;
        }
        
        final SimpleConnectorLine newLine = (SimpleConnectorLine)structureComponent;
        newLine.firstCoord = this.firstCoord;
        newLine.secondCoord = this.secondCoord;
        newLine.firstObject = this.firstObject;
        newLine.secondObject = this.secondObject;
    }

    
	public ComponentType getStructureComponentType() {
        return null;
    }

}