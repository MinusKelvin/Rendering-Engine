package minusk.render.graphics;


public class ColorDrawPass extends DrawPass {
	
	public ColorDrawPass(int maxVerticies) {
		super(maxVerticies, 7);
	}
	
	public ColorDrawPass() {
		this(DEFAULT_MAX_VERTICIES);
	}

	@Override
	protected void render() {
		
	}
}
