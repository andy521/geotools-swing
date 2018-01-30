package org.geotools.geotools_swing.plugins;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geotools_swing.MapPane;
import org.geotools.geotools_swing.utils.RangeHashMap;
import org.sam.swing.utils.Pair;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 标尺插件
 * 
 * @author sam
 *
 */
public class ScalebarPlugin extends PluginBase {

	/**
	 * 标尺的表示范围 double 真实的地理空间跨度，单位米 Pair.Integer 表示的比例尺
	 */
	private static RangeHashMap<Double, Pair<Integer, String>> rulers = new RangeHashMap<>();

	/**
	 * 地理信息对象生成工具
	 */
	private static GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

	/**
	 * 标尺的字体
	 */
	private static final Font font = new Font("宋体" , Font.PLAIN , 12);

	/**
	 * 计算地图横跨范围开始点的坐标
	 */
	private Point pointBegin;

	/**
	 * 计算地图横跨范围结束点的坐标
	 */
	private Point pointEnd;

	/**
	 * 初始化默认标尺表示范围
	 */
	static {
		rulers.put(0.0, new Pair<Integer, String>(1, "1m"));
		rulers.put(1.01, new Pair<Integer, String>(2, "2m"));
		rulers.put(2.01, new Pair<Integer, String>(5, "5m"));
		rulers.put(5.01, new Pair<Integer, String>(10, "10m"));
		rulers.put(10.01, new Pair<Integer, String>(20, "20m"));
		rulers.put(20.01, new Pair<Integer, String>(50, "50m"));
		rulers.put(50.01, new Pair<Integer, String>(100, "100m"));
		rulers.put(100.01, new Pair<Integer, String>(200, "200m"));
		rulers.put(200.01, new Pair<Integer, String>(500, "500m"));
		rulers.put(500.01, new Pair<Integer, String>(1000, "1km"));
		rulers.put(1000.01, new Pair<Integer, String>(2000, "2km"));
		rulers.put(2000.01, new Pair<Integer, String>(5000, "5km"));
		rulers.put(5000.01, new Pair<Integer, String>(10000, "10km"));
		rulers.put(10000.01, new Pair<Integer, String>(20000, "20km"));
		rulers.put(20000.01, new Pair<Integer, String>(50000, "50km"));
		rulers.put(50000.01, new Pair<Integer, String>(100000, "100km"));
		rulers.put(100000.01, new Pair<Integer, String>(200000, "200km"));
		rulers.put(200000.01, new Pair<Integer, String>(500000, "500km"));
		rulers.put(500000.01, new Pair<Integer, String>(1000000, "1000km"));
		rulers.put(1000000.01, new Pair<Integer, String>(2000000, "2000km"));
		rulers.put(2000000.01, new Pair<Integer, String>(5000000, "5000km"));
		rulers.put(5000000.01, new Pair<Integer, String>(10000000, "10000km"));
		rulers.put(10000000.01, new Pair<Integer, String>(20000000, "20000km"));
	}

	/**
	 * 标尺插件
	 * 
	 * @param title
	 * @param group
	 * @param map
	 */
	public ScalebarPlugin(String title, String group, MapPane map) {
		super(title, group, map);
		pointBegin = gf.createPoint(new Coordinate());
		pointEnd = gf.createPoint(new Coordinate());
	}

	/**
	 * {@inheritDoc} 将标尺直接画到图片上
	 */
	@Override
	public void beforePaint(Graphics2D g2d) {

		MapPane mapPane = this.getMapPane();
		ReferencedEnvelope bounds = mapPane.getMapContent().getViewport().getBounds();
		Coordinate begin = pointBegin.getCoordinate();
		begin.setOrdinate(Coordinate.X, bounds.getMinX());
		begin.setOrdinate(Coordinate.Y, bounds.getMinY());

		Coordinate end = pointEnd.getCoordinate();
		end.setOrdinate(Coordinate.X, bounds.getMaxX());
		end.setOrdinate(Coordinate.Y, bounds.getMinY());

		// 转换成了米
		double distance = pointEnd.distance(pointBegin) * 100000;
		Rectangle location = mapPane.getVisibleRectangle();
		Pair<Integer, String> ruler = rulers.get(distance);
		int pwidth = ruler.getKey();
		double x = pwidth * location.getWidth() / distance; //真正的标尺代表的宽度
		if (x < 1)
			x = 1;

		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int sWidth = fontMetrics.stringWidth(ruler.getValue());
		int iX = location.width - (int)x - 5;
		int iY = location.height - 20;
		
		g2d.setColor(Color.WHITE);
		g2d.drawString(ruler.getValue(), iX + ((int)x - sWidth) / 2, iY - 2);
		g2d.drawLine(iX, iY, location.width - 5, iY);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPaint(Graphics2D g2d, int dx, int dy) {
	}

}
