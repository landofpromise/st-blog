package test;
/**
 * 
 */


import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import socns.utils.PreviewTextUtils;

/**
 * @author langhsu
 *
 */
public class HtmlCutTest extends TestCase {
	
	@Test
	public void testCut() {
		String text = "<p>简单的教育题材却让我数次揪心流泪。</p><br/><p>感叹即使真心对待，认真付出也难以让结果与之相符。</p>";
		String cuted = PreviewTextUtils.truncateText(text, 20);
		System.out.println(cuted);
		System.out.println(cuted.length());
		
		System.out.println(StringUtils.abbreviate("1234567", 5));
	}

}
