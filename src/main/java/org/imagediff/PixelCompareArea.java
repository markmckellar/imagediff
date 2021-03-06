package org.imagediff;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class PixelCompareArea {
	transient private List<PixelCompare> pixelCompareList;
	transient private List<Shape> pixelAreas;
	private Shape boundingBox;
	private PixelCompareResult pixelCompareResult;
	public PixelCompareArea() {
		this.pixelCompareList = new ArrayList<PixelCompare>();
		this.setPixelAreas(new ArrayList<Shape>());
	}

	public PixelCompareArea(PixelCompare pixelCompare,int margin) {
		this();
		this.addPixelCompare(pixelCompare,margin);
	}

	public void fillBoundingBox() {
			Area area = new Area();
			for(Shape shape:getPixelAreas()) area.add(new Area(shape));
			Shape bounds = area.getBounds();
			this.setBoundingBox(bounds);
			
			this.setPixelCompareResult(new PixelCompareResult(getPixelCompareList()));
	}
	


	public void clearPixelCompareArea() {
		this.getPixelCompareList().clear();
	}
	
	public boolean containsPixelCompare(PixelCompare pc) {
		boolean contains = false;
		for(Shape shape:getPixelAreas())
			if(shape.contains(pc.getPoint())) {
				contains = true;
				break;
			}			
		return contains;
	}

	public void mergePixelCompareArea(PixelCompareArea pixelCompareArea) {
		getPixelAreas().addAll(pixelCompareArea.getPixelAreas());
		getPixelCompareList().addAll(pixelCompareArea.getPixelCompareList());
	}

	public void  addPixelCompare(PixelCompare pc,int margin) {
		this.getPixelCompareList().add(pc);
		this.getPixelAreas().add(	
			new Rectangle2D.Double(
				pc.getPoint().getX()-(margin/2),
				pc.getPoint().getY()-(margin/2),
				margin,
				margin) );
	}

	public List<PixelCompare> getPixelCompareList() {
		return pixelCompareList;
	}

	public void setPixelCompareList(List<PixelCompare> pixelCompareList) {
		this.pixelCompareList = pixelCompareList;
	}

	public List<Shape> getPixelAreas() {
		return pixelAreas;
	}

	public void setPixelAreas(List<Shape> pixelAreas) {
		this.pixelAreas = pixelAreas;
	}

	public String getInfoString() {
		// TODO Auto-generated method stub
		return("PixelCompareArea:pixelAreas.size="+this.getPixelAreas().size()+
				":pixelCompareList.size="+this.getPixelCompareList().size());
	}

	public Shape getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Shape boundingBox) {
		this.boundingBox = boundingBox;
	}

	public PixelCompareResult getPixelCompareResult() {
		return pixelCompareResult;
	}

	public void setPixelCompareResult(PixelCompareResult pixelCompareResult) {
		this.pixelCompareResult = pixelCompareResult;
	}
}











