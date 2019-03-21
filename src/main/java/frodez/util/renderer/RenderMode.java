package frodez.util.renderer;

import frodez.util.renderer.reverter.CSSReverter;
import frodez.util.renderer.reverter.Reverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RenderMode {

	CSSREVERTER(CSSReverter.class);

	private Class<? extends Reverter> reverter;

}
