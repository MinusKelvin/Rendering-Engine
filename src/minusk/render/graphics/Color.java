package minusk.render.graphics;

import minusk.render.graphics.filters.BlendFunc;

public class Color {
	private float r,g,b,a;
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.b = b;
		this.g = g;
		this.a = a;
	}
	
	public Color(float r, float g, float b) {
		this(r,g,b,1);
	}
	
	public Color(int r, int g, int b, int a) {
		this(r/255f, g/255f, b/255f, a/255f);
	}
	
	public Color(int r, int g, int b) {
		this(r,g,b,1);
	}
	
	public Color blend(Color dst, BlendFunc blendFunc) {
		float sr=0,sg=0,sb=0,sa=0;
		switch (blendFunc.src) {
		case DST_ALPHA:
			sr = r * dst.a;
			sg = g * dst.a;
			sb = b * dst.a;
			sa = a * dst.a;
			break;
		case DST_COLOR:
			sr = r * dst.r;
			sg = g * dst.g;
			sb = b * dst.b;
			sa = a * dst.a;
			break;
		case ONE:
			sr = r;
			sg = g;
			sb = b;
			sa = a;
			break;
		case ONE_MINUS_DST_ALPHA:
			sr = r * (1-dst.a);
			sg = g * (1-dst.a);
			sb = b * (1-dst.a);
			sa = a * (1-dst.a);
			break;
		case ONE_MINUS_DST_COLOR:
			sr = r * (1-dst.r);
			sg = g * (1-dst.g);
			sb = b * (1-dst.b);
			sa = a * (1-dst.a);
			break;
		case ONE_MINUS_SRC_ALPHA:
			sr = r * (1-a);
			sg = g * (1-a);
			sb = b * (1-a);
			sa = a * (1-a);
			break;
		case ONE_MINUS_SRC_COLOR:
			sr = r * (1-r);
			sg = g * (1-g);
			sb = b * (1-b);
			sa = a * (1-a);
			break;
		case SRC_ALPHA:
			sr = r * a;
			sg = g * a;
			sb = b * a;
			sa = a * a;
			break;
		case SRC_COLOR:
			sr = r * r;
			sg = g * g;
			sb = b * b;
			sa = a * a;
			break;
		default:
			break;
		}
		float dr=0,dg=0,db=0,da=0;
		switch (blendFunc.src) {
		case DST_ALPHA:
			dr = dst.r * dst.a;
			dg = dst.g * dst.a;
			db = dst.b * dst.a;
			da = dst.a * dst.a;
			break;
		case DST_COLOR:
			dr = dst.r * dst.r;
			dg = dst.g * dst.g;
			db = dst.b * dst.b;
			da = dst.a * dst.a;
			break;
		case ONE:
			dr = dst.r;
			dg = dst.g;
			db = dst.b;
			da = dst.a;
			break;
		case ONE_MINUS_DST_ALPHA:
			dr = dst.r * (1-dst.a);
			dg = dst.g * (1-dst.a);
			db = dst.b * (1-dst.a);
			da = dst.a * (1-dst.a);
			break;
		case ONE_MINUS_DST_COLOR:
			dr = dst.r * (1-dst.r);
			dg = dst.g * (1-dst.g);
			db = dst.b * (1-dst.b);
			da = dst.a * (1-dst.a);
			break;
		case ONE_MINUS_SRC_ALPHA:
			dr = dst.r * (1-a);
			dg = dst.g * (1-a);
			db = dst.b * (1-a);
			da = dst.a * (1-a);
			break;
		case ONE_MINUS_SRC_COLOR:
			dr = dst.r * (1-r);
			dg = dst.g * (1-g);
			db = dst.b * (1-b);
			da = dst.a * (1-a);
			break;
		case SRC_ALPHA:
			dr = dst.r * a;
			dg = dst.g * a;
			db = dst.b * a;
			da = dst.a * a;
			break;
		case SRC_COLOR:
			dr = dst.r * r;
			dg = dst.g * g;
			db = dst.b * b;
			da = dst.a * a;
			break;
		default:
			break;
		}
		return new Color(dr+sr, dg+sg, sb+db, sa+da);
	}
	
	public float getR() {
		return r;
	}
	public float getG() {
		return g;
	}
	public float getB() {
		return b;
	}
	public float getA() {
		return a;
	}
	public int getIntR() {
		return (int)(r*255);
	}
	public int getIntG() {
		return (int)(g*255);
	}
	public int getIntB() {
		return (int)(b*255);
	}
	public int getIntA() {
		return (int)(a*255);
	}
	public int intValue() {
		return getIntA() << 24 | getIntB() << 16 | getIntG() << 8 | getIntR();
	}
	
	public static final Color Transparent_Black	= new Color(0, 0, 0, 0);
	public static final Color Transparent_White	= new Color(255, 255, 255, 0);
	
	// Grayscale Colors
	public static final Color Gray12			= new Color(0.125f, 0.125f, 0.125f);
	public static final Color Gray25			= new Color(0.25f, 0.25f, 0.25f);
	public static final Color Gray37			= new Color(0.375f, 0.375f, 0.375f);
	public static final Color Gray50			= new Color(0.5f, 0.5f, 0.5f);
	public static final Color Gray62			= new Color(0.625f, 0.625f, 0.625f);
	public static final Color Gray75			= new Color(0.75f, 0.75f, 0.75f);
	public static final Color Gray87			= new Color(0.875f, 0.875f, 0.875f);
	public static final Color Gray				= Gray50;
	
	// Other Colors
	
	public static final Color Amaranth			= new Color(229, 43, 80);
	public static final Color Amber		 		= new Color(255, 191, 0);
	public static final Color Amethyst			= new Color(153, 102, 204);
	public static final Color Apricot			= new Color(251, 206, 177);
	public static final Color Aquamarine		= new Color(127, 255, 212);
	public static final Color Azure				= new Color(0, 127, 255);
	public static final Color Baby_blue			= new Color(137, 207, 240);
	public static final Color Beige				= new Color(245, 245, 220);
	public static final Color Black				= new Color(0, 0, 0);
	public static final Color Blue				= new Color(0, 0, 255);
	public static final Color Blue_green		= new Color(0, 149, 182);
	public static final Color Blue_violet		= new Color(138, 43, 226);
	public static final Color Blush				= new Color(222, 93, 131);
	public static final Color Bronze			= new Color(205, 127, 50);
	public static final Color Brown				= new Color(150, 75, 0);
	public static final Color Burgundy			= new Color(128, 0, 32);
	public static final Color Byzantium			= new Color(112, 41, 99);
	public static final Color Carmine			= new Color(150, 0, 24);
	public static final Color Cerise			= new Color(222, 49, 99);
	public static final Color Cerulean			= new Color(0, 123, 167);
	public static final Color Champagne			= new Color(247, 231, 206);
	public static final Color Chartreuse_green	= new Color(127, 255, 0);
	public static final Color Chocolate			= new Color(123, 63, 0);
	public static final Color Cobalt_blue		= new Color(0, 71, 171);
	public static final Color Coffee			= new Color(111, 78, 55);
	public static final Color Copper			= new Color(184, 115, 51);
	public static final Color Coral				= new Color(248, 131, 121);
	public static final Color Crimson			= new Color(220, 20, 60);
	public static final Color Cyan				= new Color(0, 255, 255);
	public static final Color Desert_sand		= new Color(237, 201, 175);
	public static final Color Electric_blue		= new Color(125, 249, 255);
	public static final Color Emerald			= new Color(80, 200, 120);
	public static final Color Erin				= new Color(0, 255, 63);
	public static final Color Gold				= new Color(255, 215, 0);
	public static final Color Green				= new Color(0, 255, 0);
	public static final Color Harlequin			= new Color(63, 255, 0);
	public static final Color Indigo			= new Color(75, 0, 130);
	public static final Color Ivory				= new Color(255, 255, 240);
	public static final Color Jade				= new Color(0, 168, 107);
	public static final Color Jungle_green		= new Color(41, 171, 135);
	public static final Color Lavender			= new Color(181, 126, 220);
	public static final Color Lemon				= new Color(255, 247, 0);
	public static final Color Lilac				= new Color(200, 162, 200);
	public static final Color Lime				= new Color(191, 255, 0);
	public static final Color Magenta			= new Color(255, 0, 255);
	public static final Color Magenta_rose		= new Color(255, 0, 175);
	public static final Color Maroon			= new Color(128, 0, 0);
	public static final Color Mauve				= new Color(224, 176, 255);
	public static final Color Navy_blue			= new Color(0, 0, 128);
	public static final Color Ocher				= new Color(204, 119, 34);
	public static final Color Olive				= new Color(128, 128, 0);
	public static final Color Orange			= new Color(255, 165, 0);
	public static final Color Orange_red		= new Color(255, 69, 0);
	public static final Color Orchid			= new Color(218, 112, 214);
	public static final Color Peach				= new Color(255, 229, 180);
	public static final Color Pear				= new Color(209, 226, 49);
	public static final Color Periwinkle		= new Color(204, 204, 255);
	public static final Color Persian_blue		= new Color(28, 57, 187);
	public static final Color Pink				= new Color(255, 192, 203);
	public static final Color Plum				= new Color(142, 69, 133);
	public static final Color Prussian_blue		= new Color(0, 49, 83);
	public static final Color Puce				= new Color(204, 136, 153);
	public static final Color Purple			= new Color(128, 0, 128);
	public static final Color Raspberry			= new Color(227, 11, 92);
	public static final Color Red				= new Color(255, 0, 0);
	public static final Color Red_violet		= new Color(199, 21, 133);
	public static final Color Rose				= new Color(255, 0, 127);
	public static final Color Ruby				= new Color(224, 17, 95);
	public static final Color Salmon			= new Color(250, 128, 114);
	public static final Color Sangria			= new Color(146, 0, 10);
	public static final Color Sapphire			= new Color(15, 82, 186);
	public static final Color Scarlet			= new Color(255, 36, 0);
	public static final Color Silver			= new Color(192, 192, 192);
	public static final Color Slate_gray		= new Color(112, 128, 144);
	public static final Color Spring_bud		= new Color(167, 252, 0);
	public static final Color Spring_green		= new Color(0, 255, 127);
	public static final Color Tan				= new Color(210, 191, 140);
	public static final Color Taupe				= new Color(72, 60, 50);
	public static final Color Teal				= new Color(0, 128, 128);
	public static final Color Turquoise			= new Color(64, 224, 208);
	public static final Color Violet			= new Color(238, 130, 238);
	public static final Color Viridian			= new Color(64, 130, 109);
	public static final Color White				= new Color(255, 255, 255);
	public static final Color Yankees_Blue		= new Color(28, 40, 65);
	public static final Color Yellow			= new Color(255, 255, 0);
}
