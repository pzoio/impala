package org.impalaframework.plugin.builder;

import java.util.LinkedList;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.SimpleBeansetPluginSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class SingleStringPluginSpecBuilder implements PluginSpecProvider {

	private ParentSpec parentSpec;

	private String pluginString;

	public SingleStringPluginSpecBuilder(ParentSpec parentSpec, String pluginString) {
		super();
		Assert.notNull(parentSpec);
		Assert.notNull(pluginString);
		this.parentSpec = parentSpec;
		this.pluginString = pluginString;
	}

	public ParentSpec getPluginSpec() {
		if (StringUtils.hasText(pluginString)) {
			String[] pluginNames = doPluginSplit();

			for (String pluginString : pluginNames) {
				int openBracketIndex = pluginString.indexOf('(');
				if (openBracketIndex < 0) {
					new SimplePluginSpec(parentSpec, pluginString);
				}
				else {
					int closeBracketIndex = pluginString.indexOf(')');
					// doPluginSplit() will check this, but just to make sure
					Assert.isTrue(closeBracketIndex > openBracketIndex);
					String pluginName = pluginString.substring(0, openBracketIndex);
					String beanSetString = pluginString.substring(openBracketIndex + 1, closeBracketIndex);
					if (StringUtils.hasText(beanSetString))
						new SimpleBeansetPluginSpec(parentSpec, pluginName.trim(), beanSetString.trim());
					else
						new SimpleBeansetPluginSpec(parentSpec, pluginName.trim());
				}
			}
		}
		return parentSpec;
	}

	String[] doPluginSplit() {

		List<Integer> indexes = new LinkedList<Integer>();

		char[] chars = pluginString.toCharArray();

		boolean inOverrides = false;

		indexes.add(-1);

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				if (inOverrides) {
					invalidChar(chars, i);
				}
				inOverrides = true;
			}
			if (chars[i] == ')') {
				if (!inOverrides) {
					invalidChar(chars, i);
				}
				inOverrides = false;
			}

			if (chars[i] == ',') {
				if (!inOverrides) {
					indexes.add(i);
				}
			}
		}

		// add the length as the last index
		indexes.add(pluginString.length());

		List<String> segments = new LinkedList<String>();
		String pluginString = this.pluginString;

		for (int i = 1; i < indexes.size(); i++) {
			segments.add(pluginString.substring(indexes.get(i - 1) + 1, indexes.get(i)));
		}

		// convert to array
		String[] pluginNames = segments.toArray(new String[segments.size()]);

		// trim
		for (int i = 0; i < pluginNames.length; i++) {
			pluginNames[i] = pluginNames[i].trim();
		}
		return pluginNames;
	}

	private void invalidChar(char[] chars, int i) {
		throw new IllegalArgumentException("Invalid plugin string " + pluginString + ". Invalid character '" + chars[i]
				+ "' at column " + (i + 1));
	}

}
