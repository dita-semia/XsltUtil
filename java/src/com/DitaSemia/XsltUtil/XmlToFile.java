package com.DitaSemia.XsltUtil;

import java.io.*;
import java.net.URI;

import org.apache.log4j.Logger;

import net.sf.saxon.expr.*;
import net.sf.saxon.om.AxisInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.QueryResult;
import net.sf.saxon.style.Compilation;
import net.sf.saxon.style.ComponentDeclaration;
import net.sf.saxon.style.ExtensionInstruction;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.EmptySequence;
import net.sf.saxon.value.StringValue;

public class XmlToFile extends ExtensionInstruction {

	private static final Logger logger = Logger.getLogger(XmlToFile.class.getName());
	
	Expression href;
	Expression select;

	@Override
    public void prepareAttributes() throws XPathException {
    	
    	// mandatory href attribute
    	final String hrefAtt = getAttributeValue("", "href");
        if (hrefAtt == null) {
            reportAbsence("href");
        } else {
        	href = makeAttributeValueTemplate(hrefAtt);
        }
        
    	// optional select attribute
    	final String selectAtt = getAttributeValue("", "select");
        if (selectAtt != null) {
        	select = makeAttributeValueTemplate(selectAtt);
        } else {
        	select = null;
        }

    }

	@Override
    public void validate(ComponentDeclaration decl) throws XPathException {
        super.validate(decl);
        href	= typeCheck("href", 	href);
        if (select != null) {
        	select 	= typeCheck("select",		select);
        }

		if ((select != null) && (hasChildNodes())) {
			compileError("When the select attribute is present no child nodes are allowed.");
        } else if ((select == null) && (!hasChildNodes())) {
        	compileError("When there are no child nodes the select attribute needs to be present.");
        }
    }

	@Override
    public Expression compile(Compilation exec, ComponentDeclaration decl) throws XPathException {
		if (select == null) {
			select = compileSequenceConstructor(exec, decl, iterateAxis(AxisInfo.CHILD), false);
		}
		
        return new XmlToFileInstruction(href, select);
    }

	private static class XmlToFileInstruction extends SimpleExpression {

        public static final int HREF	= 0;
        public static final int SELECT	= 1;

        private FileOutputStream fos;
        
		public XmlToFileInstruction(Expression href, Expression select) {
			Expression[] subs = {href, select};
        	setArguments(subs);
        }

    	@Override
        public int computeCardinality() {
            return StaticProperty.EXACTLY_ONE;
        }

    	@Override
        public String getExpressionType() {
            return "util:xml-to-file";
        }

    	@Override
        public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {
            final String hrefString 	= arguments[HREF].head().getStringValue();

            Writer out = null;            
            
            try
            { 
            	final URI 	uri  = new URI(hrefString);           
            	final File 	file = new File(uri.getPath());     
            
            	//logger.info("file: " + file.toURI()); 
            	
            	fos = new FileOutputStream(file);
                out = new OutputStreamWriter(fos, "UTF-8");
                	
            	
            	
               	SequenceIterator iterator = arguments[SELECT].iterate();
   				Item item = iterator.next();
   				while (item != null) {

   					//logger.info("stringvalue: " + item.getStringValue());
   					
   					if (item instanceof StringValue) {
   						   						
   						final String string = item.toString();
	   					//logger.info("string (String) " + string);	   					
	   					out.write(string);
   						
   					}
   					else if ((item instanceof NodeInfo)) {

	   					final String string = QueryResult.serialize((NodeInfo)item);	   	   				
	   	   				//logger.info("string (NodeInfo) " + string);	   	   					
	   	 				out.write(string);
   						

   					} else {
   						throw new XPathException("Can't serialize item: " + item.getClass()); 
   					}	   					
	   				item = iterator.next();
   					
   				}
            } catch (Exception e){
         	   logger.error(e, e);
            } finally {
        	   if (out != null) {
        		   try {
        			   out.close(); 
        		   } catch (IOException e) {
        			   logger.error(e);
        		   }
        	   }
            }

            //logger.info("href: " + hrefString);
            
    		return EmptySequence.getInstance();
        }
    }

	
}
