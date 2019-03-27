package frodez.util.renderer;

import frodez.util.renderer.reverter.CSSReverter;
import frodez.util.renderer.reverter.Reverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 转换器类型
 * @author Frodez
 * @date 2019-03-27
 */
@Getter
@AllArgsConstructor
public enum RenderMode {

	CSSREVERTER(CSSReverter.class);

	private Class<? extends Reverter> reverter;

}
