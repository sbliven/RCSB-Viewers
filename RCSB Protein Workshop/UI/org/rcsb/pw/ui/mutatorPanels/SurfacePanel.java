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
 * Created on 2008/12/22
 *
 */ 
package org.rcsb.pw.ui.mutatorPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color4f;

import org.rcsb.mbt.model.Structure;
import org.rcsb.mbt.model.Surface;
import org.rcsb.mbt.model.attributes.ColorBrewer;
import org.rcsb.mbt.model.attributes.SurfaceColorUpdater;
import org.rcsb.pw.controllers.app.ProteinWorkshop;
import org.rcsb.uiApp.controllers.app.AppBase;
import org.rcsb.uiApp.controllers.doc.SurfaceThread;
import org.rcsb.uiApp.controllers.update.IUpdateListener;
import org.rcsb.uiApp.controllers.update.UpdateEvent;
import org.rcsb.uiApp.ui.dialogs.ColorChooserDialog;
import org.rcsb.uiApp.ui.dialogs.ColorPaletteChooserDialog;


public class SurfacePanel extends JPanel implements IUpdateListener
{
	private static int TRANSPARENCY_MIN = 0;
	private static int TRANSPARENCY_MAX = 100;
	private static int TRANSPARENCY_INIT = 0;
	private static Color DEFAULT_COLOR = new Color(0.1f, 0.8f, 1.0f);
	private static String[] surfaceOptions = {"Chain", "Chain type", "Single color", "Hydrophobicity"};
	private static final long serialVersionUID = -7205000642717901355L;

	private final JLabel colorLabel = new JLabel("Color by");
//	private final ColorPane colorPanel = new ColorPane(Color.CYAN);

	private final JSlider transparencySlider = new JSlider(JSlider.HORIZONTAL,
			TRANSPARENCY_MIN, TRANSPARENCY_MAX, TRANSPARENCY_INIT);
	private final JComboBox surfaceColorType = new JComboBox(surfaceOptions);

	public SurfacePanel() {
		super(false);
		super.setLayout(new BorderLayout());
		super.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Surface"),
				BorderFactory.createEmptyBorder(-1,1,1,1)));

		transparencySlider.setMajorTickSpacing(50);
		transparencySlider.setMinorTickSpacing(10);
		transparencySlider.setPaintTicks(true);
		//Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("Off") );
		labelTable.put( new Integer( 50 ), new JLabel("Transparent") );
		labelTable.put( new Integer( 100 ), new JLabel("Opaque") );
		transparencySlider.setLabelTable( labelTable );
		transparencySlider.setPaintLabels(true);
		super.add(transparencySlider, BorderLayout.PAGE_START);

		JPanel firstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		firstPanel.add(colorLabel);

		super.add(firstPanel, BorderLayout.PAGE_END);

		this.transparencySlider.addChangeListener(new TransparencySliderListener());   
		this.transparencySlider.setToolTipText("Change transparency of the surface");

		surfaceColorType.addActionListener(new SurfaceTypeListener());
		firstPanel.add(surfaceColorType);

		this.reset();

		AppBase.sgetUpdateController().registerListener(this);
	}

	public void reset() {
        // can set currently selected color panel here
	}

	/* (non-Javadoc)
	 * @see edu.sdsc.mbt.views_controller.IViewUpdateListener#handleModelChangedEvent(edu.sdsc.mbt.views_controller.ViewUpdateEvent)
	 */
	public void handleUpdateEvent(UpdateEvent evt)
	{
		if (evt.action == UpdateEvent.Action.VIEW_RESET)
			reset();
	}

	private class TransparencySliderListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();

			if (!source.getValueIsAdjusting()) {
				Structure structure = AppBase.sgetModel().getStructures().get(0);

				// lazy initialization of the surface
				boolean newSurface = false;
				if (structure.getStructureMap().getSurfaceCount() == 0) {
					SurfaceThread thread = new SurfaceThread();	
					thread.createSurface();
					newSurface = true;
				}

				for (Surface s: structure.getStructureMap().getSurfaces()) {
					float transparency = ((int)source.getValue()) * 0.01f;
					System.out.println("Setting transparency: " + transparency);
					Color4f[] colors = s.getColors();
					if (colors != null && colors.length > 0) {
						float currentTranspacency = colors[0].getW();
						SurfaceColorUpdater.setSurfaceTransparency(s, transparency);

						if (currentTranspacency > 0.05f && transparency <= 0.05) {
							ProteinWorkshop.sgetGlGeometryViewer().surfaceRemoved(structure);
						} else if (newSurface || currentTranspacency <= 0.05f && transparency > 0.05f) { 
							ProteinWorkshop.sgetGlGeometryViewer().surfaceAdded(structure);
						} else if (currentTranspacency > 0.05f && transparency > 0.05f) {
							ProteinWorkshop.sgetGlGeometryViewer().surfaceRemoved(structure);
							ProteinWorkshop.sgetGlGeometryViewer().surfaceAdded(structure);
						} 
					}
				}
			}
		}
	}

	private class SurfaceTypeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			final JComboBox source = (JComboBox)e.getSource();
			source.hidePopup();
			
			// lazy initialization of the surface
			Structure structure = AppBase.sgetModel().getStructures().get(0);
			if (structure.getStructureMap().getSurfaceCount() == 0) {
				SurfaceThread thread = new SurfaceThread();	
				thread.createSurface();
			}
			
			Color newColor = null;
			ColorBrewer newPalette = null;
			
			if (source.getSelectedIndex() < 2) {
				ColorPaletteChooserDialog paletteChooser = new ColorPaletteChooserDialog(AppBase.sgetActiveFrame());
				paletteChooser.showDialog();
				if (paletteChooser.wasOKPressed()) {
					newPalette = paletteChooser.getColorPalette();
				}
				if (newPalette == null) {
					return;
				}
			} else if (source.getSelectedIndex() == 2) {
				ColorChooserDialog colorDialog = new ColorChooserDialog(AppBase.sgetActiveFrame());
				colorDialog.setColor(DEFAULT_COLOR);
				colorDialog.showDialog();
				if(colorDialog.wasOKPressed()) {
					newColor = colorDialog.getColor();
				}       
				if (newColor == null) {
					return;
				}
			}

			int surfaceCount = structure.getStructureMap().getSurfaceCount();
			if (surfaceCount == 0) {
				return;
			}
			
			// create a list of unique entity ids
			List<Integer> entitySet = null;
			if (source.getSelectedIndex() == 1) {
				entitySet = new ArrayList<Integer>();
				for (Surface s: structure.getStructureMap().getSurfaces()) {
					if (! entitySet.contains(s.getChain().getEntityId())) {
						entitySet.add(s.getChain().getEntityId());
					}
				}
			}

			int index = 0;
			for (Surface s: structure.getStructureMap().getSurfaces()) {
				if (source.getSelectedIndex() == 0) {
					// color by chain
					SurfaceColorUpdater.setPaletteColor(s, newPalette, surfaceCount, index);
					index++;
				} else if (source.getSelectedIndex() == 1) {
					// color by chain type (entity id)
					int entityId = s.getChain().getEntityId();
					SurfaceColorUpdater.setPaletteColor(s, newPalette, entitySet.size(), entitySet.indexOf(entityId));
				} else if (source.getSelectedIndex() == 2) {
					// color by a single color
					Color4f color = new Color4f(newColor);
					SurfaceColorUpdater.setSurfaceColor(s, color);
				} else if (source.getSelectedIndex() == 3) {
					// color by hydrophobicity
					SurfaceColorUpdater.setHydrophobicSurfaceColor(s);
				}
			}
			ProteinWorkshop.sgetGlGeometryViewer().surfaceRemoved(structure);
			ProteinWorkshop.sgetGlGeometryViewer().surfaceAdded(structure);
		}
	}
}