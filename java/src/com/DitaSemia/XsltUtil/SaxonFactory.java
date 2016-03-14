package com.DitaSemia.XsltUtil;


import net.sf.saxon.style.StyleElement;

import com.saxonica.xsltextn.ExtensionElementFactory;

public class SaxonFactory implements ExtensionElementFactory {

	public static final String NAMESPACE_URI = "http://www.gdv-dl.de/xslt-util";

	@Override
	public Class<? extends StyleElement> getExtensionClass(String localname) {
		if 		(localname.equals("xml-to-file")) 	return XmlToFile.class;
		else return null;
	}

}
