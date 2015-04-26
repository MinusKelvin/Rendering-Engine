package minusk.render.graphics.globjects;


import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Shader {
	public final String vertexSource;
	public final String fragmentSource;
	public final String geometrySource;
	public final String tessControlSource;
	public final String tessEvaluationSource;
	public final String computeSource;
	public final int id;
	private boolean useable = false;
	
	private int vertex,fragment,tessE,tessC,geometry,compute;
	
	public Shader(String vertex, String tessControl, String tessEvaluation,
			String geometry, String fragment) {
		vertexSource = vertex;
		fragmentSource = fragment;
		geometrySource = geometry;
		tessControlSource = tessControl;
		tessEvaluationSource = tessEvaluation;
		computeSource = null;
		id = load();
	}
	
	public Shader(String vertex, String tessControl, String tessEvaluation, String fragment) {
		this(vertex, tessControl, tessEvaluation, null, fragment);
	}
	
	public Shader(String vertex, String geometry, String fragment) {
		this(vertex, null, null, geometry, fragment);
	}
	
	public Shader(String vertex, String fragment) {
		this(vertex, null, fragment);
	}
	
	public Shader(String compute) {
		vertexSource = null;
		fragmentSource = null;
		geometrySource = null;
		tessControlSource = null;
		tessEvaluationSource = null;
		computeSource = compute;
		id = load();
	}
	
	public Shader(InputStream compute) {
		vertexSource = null;
		fragmentSource = null;
		geometrySource = null;
		tessControlSource = null;
		tessEvaluationSource = null;
		
		Reader r = new BufferedReader(new InputStreamReader(compute));
		int c;
		StringBuilder end = new StringBuilder();
		boolean success = false;
		try {
			while ((c = r.read()) != -1)
				end.appendCodePoint(c);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		computeSource = end.toString();
		if (success)
			id = load();
		else
			id = 0;
		try {
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Shader(InputStream vertex, InputStream fragment) {
		computeSource = null;
		geometrySource = null;
		tessControlSource = null;
		tessEvaluationSource = null;
		BufferedReader vr = new BufferedReader(new InputStreamReader(vertex));
		BufferedReader fr = new BufferedReader(new InputStreamReader(fragment));
		StringBuilder vs = new StringBuilder();
		StringBuilder fs = new StringBuilder();
		int c;
		boolean success = false;
		try {
			while ((c = vr.read()) != -1)
				vs.appendCodePoint(c);
			while ((c = fr.read()) != -1)
				fs.appendCodePoint(c);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		vertexSource = vs.toString();
		fragmentSource = fs.toString();
		if (success)
			id = load();
		else
			id = 0;
		try {
			vr.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Shader(InputStream vertex, InputStream geometry , InputStream fragment) {
		computeSource = null;
		tessControlSource = null;
		tessEvaluationSource = null;
		BufferedReader vr = new BufferedReader(new InputStreamReader(vertex));
		BufferedReader fr = new BufferedReader(new InputStreamReader(fragment));
		BufferedReader gr = new BufferedReader(new InputStreamReader(fragment));
		StringBuilder gs = new StringBuilder();
		StringBuilder vs = new StringBuilder();
		StringBuilder fs = new StringBuilder();
		int c;
		boolean success = false;
		try {
			while ((c = vr.read()) != -1)
				vs.appendCodePoint(c);
			while ((c = fr.read()) != -1)
				fs.appendCodePoint(c);
			while ((c = gr.read()) != -1)
				gs.appendCodePoint(c);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		vertexSource = vs.toString();
		fragmentSource = fs.toString();
		geometrySource = fs.toString();
		if (success)
			id = load();
		else
			id = 0;
		try {
			vr.close();
			fr.close();
			gr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Shader(InputStream vertex, InputStream tessControl,
			InputStream tessEvaluation, InputStream fragment) {
		computeSource = null;
		geometrySource = null;
		BufferedReader vr = new BufferedReader(new InputStreamReader(vertex));
		BufferedReader fr = new BufferedReader(new InputStreamReader(fragment));
		BufferedReader tr = new BufferedReader(new InputStreamReader(tessControl));
		BufferedReader er = new BufferedReader(new InputStreamReader(tessEvaluation));
		StringBuilder vs = new StringBuilder();
		StringBuilder fs = new StringBuilder();
		StringBuilder ts = new StringBuilder();
		StringBuilder es = new StringBuilder();
		int c;
		boolean success = false;
		try {
			while ((c = vr.read()) != -1)
				vs.appendCodePoint(c);
			while ((c = fr.read()) != -1)
				fs.appendCodePoint(c);
			while ((c = tr.read()) != -1)
				ts.appendCodePoint(c);
			while ((c = er.read()) != -1)
				es.appendCodePoint(c);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		vertexSource = vs.toString();
		fragmentSource = fs.toString();
		tessControlSource = ts.toString();
		tessEvaluationSource = es.toString();
		if (success)
			id = load();
		else
			id = 0;
		try {
			vr.close();
			fr.close();
			tr.close();
			er.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Shader(InputStream vertex, InputStream tessControl, InputStream tessEvaluation,
			InputStream geometry, InputStream fragment) {
		computeSource = null;
		BufferedReader vr = new BufferedReader(new InputStreamReader(vertex));
		BufferedReader fr = new BufferedReader(new InputStreamReader(fragment));
		BufferedReader tr = new BufferedReader(new InputStreamReader(tessControl));
		BufferedReader er = new BufferedReader(new InputStreamReader(tessEvaluation));
		BufferedReader gr = new BufferedReader(new InputStreamReader(geometry));
		StringBuilder vs = new StringBuilder();
		StringBuilder fs = new StringBuilder();
		StringBuilder ts = new StringBuilder();
		StringBuilder es = new StringBuilder();
		StringBuilder gs = new StringBuilder();
		int c;
		boolean success = false;
		try {
			while ((c = vr.read()) != -1)
				vs.appendCodePoint(c);
			while ((c = fr.read()) != -1)
				fs.appendCodePoint(c);
			while ((c = tr.read()) != -1)
				ts.appendCodePoint(c);
			while ((c = er.read()) != -1)
				es.appendCodePoint(c);
			while ((c = gr.read()) != -1)
				gs.appendCodePoint(c);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		vertexSource = vs.toString();
		fragmentSource = fs.toString();
		tessControlSource = vs.toString();
		tessEvaluationSource = fs.toString();
		geometrySource = fs.toString();
		if (success)
			id = load();
		else
			id = 0;
		try {
			vr.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int load() {
		if (computeSource == null) {
			vertex = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vertex, vertexSource);
			glCompileShader(vertex);
			if (glGetShaderi(vertex, GL_COMPILE_STATUS) != GL_TRUE) {
				System.err.println("Vertex Error: "+glGetShaderInfoLog(vertex, glGetShaderi(vertex, GL_INFO_LOG_LENGTH)));
				delete(vertex,fragment,tessE,tessC,geometry,compute,'v');
				return 0;
			}
			
			fragment = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fragment, fragmentSource);
			glCompileShader(fragment);
			if (glGetShaderi(fragment, GL_COMPILE_STATUS) != GL_TRUE) {
				System.err.println("Fragment Error: "+glGetShaderInfoLog(fragment, glGetShaderi(fragment, GL_INFO_LOG_LENGTH)));
				delete(vertex,fragment,tessE,tessC,geometry,compute,'f');
				return 0;
			}
			
			if (tessControlSource != null) {
				tessC = glCreateShader(GL_TESS_CONTROL_SHADER);
				glShaderSource(tessC, tessControlSource);
				glCompileShader(tessC);
				if (glGetShaderi(tessC, GL_COMPILE_STATUS) != GL_TRUE) {
					System.err.println("Tesselation Control Error: "+glGetShaderInfoLog(tessC, glGetShaderi(tessC, GL_INFO_LOG_LENGTH)));
					delete(vertex,fragment,tessE,tessC,geometry,compute,'t');
					return 0;
				}
				
				tessE = glCreateShader(GL_TESS_EVALUATION_SHADER);
				glShaderSource(tessE, tessEvaluationSource);
				glCompileShader(tessE);
				if (glGetShaderi(tessE, GL_COMPILE_STATUS) != GL_TRUE) {
					System.err.println("Tesselation Evaluation Error: "+glGetShaderInfoLog(tessE, glGetShaderi(tessE, GL_INFO_LOG_LENGTH)));
					delete(vertex,fragment,tessE,tessC,geometry,compute,'e');
					return 0;
				}
			}
			
			if (geometrySource != null) {
				geometry = glCreateShader(GL_GEOMETRY_SHADER);
				glShaderSource(geometry, geometrySource);
				glCompileShader(geometry);
				if (glGetShaderi(geometry, GL_COMPILE_STATUS) != GL_TRUE) {
					System.err.println("Geometry Error: "+glGetShaderInfoLog(geometry, glGetShaderi(geometry, GL_INFO_LOG_LENGTH)));
					delete(vertex,fragment,tessE,tessC,geometry,compute,'g');
					return 0;
				}
			}
		} else {
			compute = glCreateShader(GL_COMPUTE_SHADER);
			glShaderSource(compute, computeSource);
			glCompileShader(compute);
			if (glGetShaderi(compute, GL_COMPILE_STATUS) != GL_TRUE) {
				System.err.println("Compute Error: "+glGetShaderInfoLog(compute, glGetShaderi(compute, GL_INFO_LOG_LENGTH)));
				delete(vertex,fragment,tessE,tessC,geometry,compute,'c');
				return 0;
			}
		}
		
		int program = glCreateProgram();
		
		if (vertexSource != null)
			glAttachShader(program, vertex);
		if (tessControlSource != null)
			glAttachShader(program, tessC);
		if (tessEvaluationSource != null)
			glAttachShader(program, tessE);
		if (geometrySource != null)
			glAttachShader(program, geometry);
		if (fragmentSource != null)
			glAttachShader(program, fragment);
		if (computeSource != null)
			glAttachShader(program, compute);
		
		return program;
	}
	
	private void delete(int v, int f, int t, int e, int g, int c, char stage) {
		switch (stage) {
		case 'g': case 'd':
			if (geometrySource != null)
				glDeleteShader(g);
		case 'e':
			if (tessControlSource != null)
				glDeleteShader(e);
		case 't':
			if (tessControlSource != null)
				glDeleteShader(t);
		case 'f':
			if (fragmentSource != null)
				glDeleteShader(f);
		case 'v':
			if (vertexSource != null)
				glDeleteShader(v);
		case 'c':
			if (computeSource != null)
				glDeleteShader(c);
			break;
		}
	}
	
	public void link() {
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) != GL_TRUE) {
			System.err.println("Link Error: "+glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
			delete(vertex,fragment,tessE,tessC,geometry,compute,'d');
			glDeleteProgram(id);
			useable = false;
		}
		useable = true;
		
		delete(vertex,fragment,tessE,tessC,geometry,compute,'d');
	}
	
	public void use() {
		if (useable)
			glUseProgram(id);
		else
			System.err.println("Attempted to use shader that did not compile or link properly.");
	}
	
	@Override
	protected void finalize() {
		glDeleteProgram(id);
	}
}
