<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:naf="http://www.novocode.com/namespaces/naf">

	<!-- xsl:output method="xml" indent="yes"/ -->
	
	<xsl:template match="naf:GroupBoxLabel">
		<naf:GroupBox text="{@title}">
			<naf:Label text="{@contents}" />
		</naf:GroupBox>
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
