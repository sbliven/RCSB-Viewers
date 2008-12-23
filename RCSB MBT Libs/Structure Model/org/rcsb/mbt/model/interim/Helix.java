/*
 * BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence. This should
 * be distributed with the code. If you do not have a copy,
 * see:
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors. These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 * http://www.biojava.org/
 *
 * This code was contributed from the Molecular Biology Toolkit
 * (MBT) project at the University of California San Diego.
 *
 * Please reference J.L. Moreland, A.Gramada, O.V. Buzko, Qing
 * Zhang and P.E. Bourne 2005 The Molecular Biology Toolkit (MBT):
 * A Modular Platform for Developing Molecular Visualization
 * Applications. BMC Bioinformatics, 6:21.
 *
 * The MBT project was funded as part of the National Institutes
 * of Health PPG grant number 1-P01-GM63208 and its National
 * Institute of General Medical Sciences (NIGMS) division. Ongoing
 * development for the MBT project is managed by the RCSB
 * Protein Data Bank(http://www.pdb.org) and supported by funds
 * from the National Science Foundation (NSF), the National
 * Institute of General Medical Sciences (NIGMS), the Office of
 * Science, Department of Energy (DOE), the National Library of
 * Medicine (NLM), the National Cancer Institute (NCI), the
 * National Center for Research Resources (NCRR), the National
 * Institute of Biomedical Imaging and Bioengineering (NIBIB),
 * the National Institute of Neurological Disorders and Stroke
 * (NINDS), and the National Institute of Diabetes and Digestive
 * and Kidney Diseases (NIDDK).
 *
 * Created on 2007/02/08
 *
 */ 
package org.rcsb.mbt.model.interim;

import org.rcsb.mbt.model.Structure;
import org.rcsb.mbt.model.StructureComponent;
import org.rcsb.mbt.model.StructureComponentRegistry;
import org.rcsb.mbt.model.StructureComponentRegistry.ComponentType;


/**
 *  Implements a StructureComponent container for Helix conformation
 *  (secondary structure) data.
 *  <P>
 *  @author	John L. Moreland
 *  @see	org.rcsb.mbt.model.Structure
 *  @see	org.rcsb.mbt.model.interim.Conformation
 *  @see	org.rcsb.mbt.model.StructureComponent
 */
public class Helix
	extends Conformation
	implements java.lang.Cloneable
{
	//
	// Constructor
	//

	/**
	 *  Creates a new Helix object.
	 */
	public Helix( )
	{
	}

	/**
	 *  This method returns the fully qualified name of this class as a String
	 *  object. The String object is guaranteed to be a reference to the
	 *  same String object for all instances of a given sub-class.
	 */
	private static String className = null;
	public static String getClassName()
	{
		if ( Helix.className == null ) {
			Helix.className = Helix.class.getName();
		}
		return Helix.className;
	}

	/**
	 *  This method returns the fully qualified name of this class.
	 */
	
	public ComponentType getStructureComponentType( )
	{
		return ComponentType.HELIX;
	}

	//
	// StructureComponent methods
	//

	/**
	 *  Copy all of the field values from the parameter object into "this".
	 */
	
	public void copy( final StructureComponent structureComponent )
	{
		super.copy( structureComponent );
		final Helix helix = (Helix) structureComponent;
		this.righthand       = helix.righthand;
	}

	/**
	 *  Clone this object.
	 */
	
	public Object clone( )
		throws CloneNotSupportedException
	{
		return super.clone( );
	}

	//
	// Helix fields
	//

	/**
	 *  TRUE=righthanded, FALSE=lefthanded
	 */
	private boolean righthand = true;


	/**
	 *  TRUE=righthanded, FALSE=lefthanded
	 */
	public void setRighthand( final boolean righthand )
	{
		this.righthand = righthand;
	}


	/**
	 *  TRUE=righthanded, FALSE=lefthanded
	 */
	public boolean getRighthand( )
	{
		return this.righthand;
	}
}

