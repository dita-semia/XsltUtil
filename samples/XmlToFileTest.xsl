<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl	= "http://www.w3.org/1999/XSL/Transform"
	xmlns:xs	= "http://www.w3.org/2001/XMLSchema"
	xmlns:util 	= "http://www.dita-semia.org/xslt-util"
	extension-element-prefixes="util"
	exclude-result-prefixes="xs"
	version="2.0">
	
	
	<xsl:variable name="RootNamespace"  as="xs:string?"/>
	<xsl:variable name="Schema" 		as="xs:string?"/>
	
	<xsl:variable name="Daten"			select="'Daten'"/>
	
	<xsl:variable name="RootName" 		select="''"/>
	
	<xsl:template match="/">
		
		<!-- Schreibe  -->
		<util:xml-to-file href="{resolve-uri('Test1.xml')}" select="."/>
		
		<xsl:variable name="name" select="'test'"/>
		
		<xsl:variable name="file" select="'C:/Testteamserver/DITA-SEMIA/XsltUtil/samples/Test4.xml'"/>
		
		<!-- schreib Datei hier in den lokalen Projektordner-->
		<util:xml-to-file  href="C:/Testteamserver/DITA-SEMIA/XsltUtil/samples/Test2.xml">
			<new><root>
				<xsl:text>Test 2 mit Dateiname </xsl:text>
<!--				<xsl:text>&#x0A;</xsl:text>-->
				<xsl:value-of select="$name"/>
			</root></new>
		</util:xml-to-file>	
	
		<!-- schreib Datei hier in den lokalen Projektordner-->
		<util:xml-to-file  href="C:/Testteamserver/DITA-SEMIA/XsltUtil/samples/Test3.xml">
			<xsl:text>Test 3 mit Dateiname</xsl:text>
		</util:xml-to-file>
		
		
		<!-- schreib Datei hier in den lokalen Projektordner-->
		<util:xml-to-file  href="{resolve-uri($file)}" >
		<xelement>text einfach so
			<xsl:text>Text mit xsl:test</xsl:text>
			<!-- hier weiter das ist immer noch leer -->
			<xsl:variable name="Root" as="xs:string?">
				<xsl:choose>
					<xsl:when test="$RootName">						
						<xsl:text>hallihallo</xsl:text>	
					</xsl:when>
					<xsl:when test="exists($Daten)">
						<xsl:text>daten gibt es auch</xsl:text>
					</xsl:when>
				</xsl:choose>			
			</xsl:variable>
			
			<xsl:value-of select="$Root"/>
			
<!--  		<xsl:apply-templates select="$Root" mode="XmlIndent"/> -->
		</xelement> <!-- TODO: wie bekomme ich hier den Zeilenumbruch hin? So?--> 
		<xsl:text>&#x0A;</xsl:text>
		<noxheinelement>
				testtest
			</noxheinelement>
		</util:xml-to-file>
		
		
		
	
		
		
		
	</xsl:template>
	
</xsl:stylesheet>